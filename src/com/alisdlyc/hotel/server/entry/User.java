package com.alisdlyc.hotel.server.entry;

import com.alisdlyc.hotel.server.service.LoginService;
import com.alisdlyc.hotel.server.service.OrderService;
import com.alisdlyc.hotel.server.service.RoomService;
import com.alisdlyc.hotel.server.service.UserService;
import com.alisdlyc.hotel.utils.JdbcUtils;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User implements LoginService, OrderService, RoomService, UserService {

    private int id;
    private int authority;
    private String userName;
    public Socket socket;
    public Connection connection;
    public PreparedStatement st;
    public ResultSet rs;

    public User() {
        authority = -1;
        connection = JdbcUtils.getConnection();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getAuthority() {
        return authority;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String login(String usr, String psw) throws SQLException {
        if (authority != -1) {
            return "FAIL";
        } else {
            String sql = "SELECT * FROM usr where `name` = ? and `password` = ?";
            st = connection.prepareStatement(sql);
            st.setString(1, usr);
            st.setString(2, psw);
            rs = st.executeQuery();
            this.setId(Integer.parseInt(rs.getString("id")));
            this.setUserName(rs.getString("name"));
            this.setAuthority(Integer.parseInt(rs.getString("authority")));
            return "OK";
        }
    }

    @Override
    public String logout() {
        if (authority != -1) {
            this.authority = -1;
            return "OK";
        }
        return "FAIL";
    }

    /**
     * 客户下单
     *
     * @param peopleNumber 人数
     * @param startYear    起始年份
     * @param startMonth   起始月份
     * @param startDay     起始天
     * @param endYear      结束年份
     * @param endMonth     结束月份
     * @param endDay       结束天数
     * @return ?
     */
    @Override
    public String reserveRoom(String peopleNumber, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) throws SQLException {
        if (this.getAuthority() == -1) {
            return "FAIL";
        }
        HashMap<String, String> hotelInfo = new HashMap<>();
        String sql = """
                SELECT `roomnumber`, `capacity`
                FROM `room`
                WHERE `roomnumber` NOT IN
                \t(SELECT `roomnumber`
                \tFROM `order`
                \tWHERE (`order`.begintime BETWEEN ? AND ?)
                \t\tOR (`order`.endtime BETWEEN ? AND ?)
                \t\tOR (`order`.begintime < ? AND `order`.endtime > ?)
                \t)
                """;
        st = connection.prepareStatement(sql);
        String startTime = "" + startYear + "-" + startMonth + "-" + startDay;
        String endTime = "" + endYear + "-" + endMonth + "-" + endDay;
        st.setString(1, startTime);
        st.setString(3, startTime);
        st.setString(5, startTime);
        st.setString(2, endTime);
        st.setString(4, endTime);
        st.setString(6, endTime);

        rs = st.executeQuery();
        while (rs.next()) {
            hotelInfo.put(rs.getString("roomnumber"), rs.getString("capacity"));
        }


        // 对获取到的 hotelInfo 信息进行遍历
        List<Map.Entry<String, String>> sortHotelInfo = new ArrayList<>(hotelInfo.entrySet());
        sortHotelInfo.sort(new ValueComparator());
        int peopleCapacity = Integer.parseInt(peopleNumber);
        List<String> re = new LinkedList<>();

        // TODO 进行房间的选择规则
        for (Map.Entry<String, String> entry : sortHotelInfo) {
            int capacity = Integer.parseInt(entry.getValue());
            if (peopleCapacity > 0 && peopleCapacity >= capacity) {
                peopleCapacity -= capacity;
                re.add(entry.getKey());
            }
            if (peopleCapacity == 0) {
                break;
            }
        }

        if (peopleCapacity != 0) {
            return "FAIL";
        } else {
            int i = 0;
            for (String str : re) {
                System.out.println(str);
                sql = "INSERT INTO `order`(`usrname`, `roomnumber`, `begintime`, `endtime`) VALUES(?, ?, ?, ?)";
                st = connection.prepareStatement(sql);
                st.setString(1, userName);
                st.setString(2, str);
                st.setString(3, startTime);
                st.setString(4, endTime);
                i += st.executeUpdate();
            }
            if (i == re.size()) {
                sql = "SELECT `id` FROM `order` WHERE `roomnumber` = ? and `begintime` = ? and `endtime` = ?";
                st = connection.prepareStatement(sql);
                st.setString(1, re.get(0));
                st.setString(2, startTime);
                st.setString(3, endTime);
                rs = st.executeQuery();
                if (rs.next()) {
                    return "订单号: " + rs.getString("id") + " 预定旅客名: " + userName +
                            " 预定人数: " + peopleNumber + " 预定入住日期: " + startTime + " 预定退房日期: " + endTime + " 预定房间号: " + re;
                }
            } else {
                return "FAIL";
            }
        }
        return "FAIL";

    }

    /**
     * 添加房间
     *
     * @param number 房间号
     * @return res
     */
    @Override
    public String addRoom(String number) throws SQLException {
        if (this.getAuthority() < 1) {
            return "FAIL";
        }
        String primRoomSql = "SELECT * FROM room WHERE `roomnumber` = ?";
        st = connection.prepareStatement(primRoomSql);
        st.setString(1, number);
        rs = st.executeQuery();
        if (rs.next()) {
            System.out.println(rs.getString("room number") + "is already exist");
            return "FAIL";
        }

        String sql = "INSERT INTO `room`(`roomnumber`) VALUES(?)";
        st = connection.prepareStatement(sql);
        st.setString(1, number);
        int i = st.executeUpdate();
        if (i > 0) {
            return "OK";
        } else {
            return "FAIL";
        }
    }

    /**
     * @param usr 用户名
     * @param psw 密码
     */
    @Override
    public String addUser(String usr, String psw) throws SQLException {
        if (this.getAuthority() == -1) {
            return "FAIL";
        }
        String primSql = "SELECT * FROM usr where `name` = ?";
        String sql = "INSERT INTO usr(`name`,`password`,`authority`) VALUES(?,?,?)";

        st = connection.prepareStatement(primSql);
        st.setString(1, usr);
        rs = st.executeQuery();
        if (rs.next()) {
            System.out.println(rs.getString("name") + "is already exist");
            return "FAIL";
        }
        st = connection.prepareStatement(sql);
        st.setString(1, usr);
        st.setString(2, psw);
        if (usr.contains("admin")) {
            st.setInt(3, 1);
        } else {
            st.setInt(3, 0);
        }

        int i = st.executeUpdate();
        if (i > 0) {
            return "OK";
        }
        return "FAIL";
    }

    @Override
    public String adminDelete(String admin, String usr) throws SQLException {
        if (this.getAuthority() != 2) {
            return "FAIL";
        }
        String sql = "DELETE FROM usr WHERE `name` = ? ";
        st = connection.prepareStatement(sql);
        st.setString(1, userName);

        int i = st.executeUpdate();
        if (i > 0) {
            return "OK";
        }
        return "FAIL";
    }

    @Override
    public String showReservations() throws SQLException {
        if (this.getAuthority() >= 1) {
            String sql = "SELECT * FROM `order`";
            st = connection.prepareStatement(sql);
            rs = st.executeQuery();
            StringBuilder re = getStringBuilder(rs);
            return re.toString();
        }
        return "FAIL";
    }

    @Override
    public String showReservation() throws SQLException {
        if (this.getAuthority() != 0) {
            return "FAIL";
        }

        String sql = "SELECT * FROM `order` WHERE `usrname` = ?";
        st = connection.prepareStatement(sql);
        st.setString(1, this.userName);
        rs = st.executeQuery();
        StringBuilder re = getStringBuilder(rs);
        return re.toString();
    }

    private StringBuilder getStringBuilder(ResultSet rs) throws SQLException {
        StringBuilder re = new StringBuilder();
        while (rs.next()) {
            re.append(rs.getString("id"));
            re.append(" ");

            re.append(rs.getString("usrname"));
            re.append(" ");

            re.append(rs.getString("roomnumber"));
            re.append(" ");

            re.append(rs.getString("begintime"));
            re.append(" ");

            re.append(rs.getString("endtime"));
            re.append("\n");

        }
        return re;
    }
    private static class ValueComparator implements Comparator<Map.Entry<String,String>> {
        @Override
        public int compare(Map.Entry<String,String> m, Map.Entry<String,String> n) {
            return Integer.parseInt(n.getValue()) - Integer.parseInt(m.getValue());
        }

    }
}