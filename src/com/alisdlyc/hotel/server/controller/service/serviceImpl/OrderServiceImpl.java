package com.alisdlyc.hotel.server.controller.service.serviceImpl;

import com.alisdlyc.hotel.server.controller.service.OrderService;
import com.alisdlyc.hotel.utils.CookieStorage;
import com.alisdlyc.hotel.utils.JdbcUtils;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OrderServiceImpl implements OrderService {

    @Override
    public String reserveRoom(CookieStorage cookie, Socket socket, String peopleNumber, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) {
        if (cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()) == null) {
            return "请先登录";
        }

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        HashMap<String, String> hotelInfo = new HashMap<>();

        try {
            conn = JdbcUtils.getConnection();
            String sql = "SELECT `roomnumber`, `capacity`\n" +
                    "FROM `room`\n" +
                    "WHERE `roomnumber` NOT IN\n" +
                    "\t(SELECT `roomnumber`\n" +
                    "\tFROM `order`\n" +
                    "\tWHERE (`order`.begintime BETWEEN ? AND ?)\n" +
                    "\t\tOR (`order`.endtime BETWEEN ? AND ?)\n" +
                    "\t\tOR (`order`.begintime < ? AND `order`.endtime > ?)\n" +
                    "\t)\n";
            st = conn.prepareStatement(sql);
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

            List<Map.Entry<String, String>> sortHotelInfo = new ArrayList<>();
            sortHotelInfo.addAll(hotelInfo.entrySet());
            Collections.sort(sortHotelInfo, new ValueComparator());

            int peopleCapacity = Integer.parseInt(peopleNumber);
            List<String> re = new LinkedList<>();

            System.out.println(sortHotelInfo);
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
                System.out.println("马上马上啦");
                int i = 0;
                for (String str : re) {
                    System.out.println(str);
                    sql = "INSERT INTO `order`(`usrname`, `roomnumber`, `begintime`, `endtime`) VALUES(?, ?, ?, ?)";
                    st = conn.prepareStatement(sql);
                    st.setString(1, cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()));
                    st.setString(2, str);
                    st.setString(3, startTime);
                    st.setString(4, endTime);
                    i += st.executeUpdate();
                }
                if (i == re.size()) {
                    sql = "SELECT `id` FROM `order` WHERE `roomnumber` = ? and `begintime` = ? and `endtime` = ?";
                    st = conn.prepareStatement(sql);
                    st.setString(1, re.get(0));
                    st.setString(2, startTime);
                    st.setString(3, endTime);
                    rs = st.executeQuery();
                    if (rs.next()) {
                        return "订单号: " + rs.getString("id") + " 预定旅客名: " + cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()) +
                                " 预定人数: " + peopleNumber + " 预定入住日期: " + startTime + " 预定退房日期: " + endTime + " 预定房间号: " + re;
                    }
                } else {
                    return "FAIL";
                }
            }
            return "FAIL";
        } catch (SQLException e) {
            e.printStackTrace();
            return "FAIL -- Exception";
        } finally {
            JdbcUtils.release(conn, st, rs);
        }
    }
    private static class ValueComparator implements Comparator<Map.Entry<String,String>> {
        @Override
        public int compare(Map.Entry<String,String> m, Map.Entry<String,String> n)
        {
            return Integer.parseInt(n.getValue()) - Integer.parseInt(m.getValue());
        }

    }
}

