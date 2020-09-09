package com.alisdlyc.hotel.server.controller.service.serviceImpl;

import com.alisdlyc.hotel.server.controller.service.UserService;
import com.alisdlyc.hotel.utils.CookieStorage;
import com.alisdlyc.hotel.utils.JdbcUtils;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author alisdlyc
 */
public class UserServiceImpl implements UserService {
    @Override
    public String addUser(String usr, String psw) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            conn = JdbcUtils.getConnection();

            String primSql = "SELECT * FROM usr where `name` = ?";
            String sql = "INSERT INTO usr(`name`,`password`,`authority`) VALUES(?,?,?)";

            st = conn.prepareStatement(primSql);
            st.setString(1, usr);
            rs = st.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString("name") + "is already exist");
                return "FAIL";
            }

            st = conn.prepareStatement(sql);
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
        } catch (SQLException e) {
            return "FAIL";
        } finally {
            JdbcUtils.release(conn, st, rs);
        }
        return "FAIL";
    }

    @Override
    public String login(CookieStorage cookie, Socket socket, String usr, String psw) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();

            String sql = "SELECT * FROM usr where `name` = ? and `password` = ?";

            st = conn.prepareStatement(sql);
            st.setString(1, usr);
            st.setString(2, psw);
            rs = st.executeQuery();
            if (rs.next()) {
                cookie.loginStage.replace("" + socket.getInetAddress() + socket.getPort(), usr);
                System.out.println(rs.getString("name") + "is login");

                System.out.println("当前连接到服务器的用户列表: ");
                System.out.println(cookie.loginStage);
                return "OK";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "FAIL";
        } finally {
            JdbcUtils.release(conn, st, rs);
        }
        return "FAIL";
    }

    @Override
    public String logout(CookieStorage cookie, Socket socket) {
        if (cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()) != null) {
            cookie.loginStage.replace("" + socket.getInetAddress() + socket.getPort(), null);
            return "OK";
        } else {
            return "FAIL";
        }
    }

    /**
     * root用户删除管理员
     *  1. 判断当前用户是否有root权限
     *  2. 被删除的用户是否为管理员
     * */
    @Override
    public String adminDelete(CookieStorage cookie, Socket socket, String admin, String usr) {

        if (cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()) == null) {
            return "FAIL";
        }
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();

            String sql = "SELECT `authority` FROM usr where `name` = ?";

            st = conn.prepareStatement(sql);
            st.setString(1, cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()));

            rs = st.executeQuery();
            if (rs.next() && "2".equals(rs.getString("authority"))) {
//                sql = "DELETE FROM usr WHERE `name` = ? and `authority` = 1";
                sql = "DELETE FROM usr WHERE `name` = ? ";
                st = conn.prepareStatement(sql);
                st.setString(1, usr);

                int i = st.executeUpdate();
                if (i > 0) {
                    // 若管理员账户已经连接到Server 将其下线
                    for (String key : cookie.loginStage.keySet()) {
                        if (usr.equals(cookie.loginStage.get(key))) {
                            cookie.loginStage.replace(key, null);
                        }
                    }
                    return "OK";
                } else {
                    return "FAIL";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "FAIL";
        } finally {
            JdbcUtils.release(conn, st, rs);
        }
        return "FAIL";
    }

    @Override
    public String showReservations(CookieStorage cookie, Socket socket) {

        if (cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()) == null) {
            return "FAIL";
        }
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();

            String sql = "SELECT `authority` FROM usr where `name` = ?";

            st = conn.prepareStatement(sql);
            st.setString(1, cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()));

            rs = st.executeQuery();
            if (rs.next() && Integer.parseInt(rs.getString("authority")) >= 1) {
                sql = "SELECT * FROM `order`";
                st = conn.prepareStatement(sql);
                rs = st.executeQuery();
                StringBuilder re = getStringBuilder(rs);
                return re.toString();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "FAIL";
        } finally {
            JdbcUtils.release(conn, st, rs);
        }
        return "FAIL";
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

    @Override
    public String showReservation(CookieStorage cookie, Socket socket) {
        if (cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()) == null) {
            return "FAIL";
        }
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();

            String sql = "SELECT * FROM `order` WHERE `usrname` = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()));
            rs = st.executeQuery();
            StringBuilder re = getStringBuilder(rs);
            return re.toString();


        } catch (SQLException e) {
            e.printStackTrace();
            return "FAIL";
        } finally {
            JdbcUtils.release(conn, st, rs);
        }
    }
}
