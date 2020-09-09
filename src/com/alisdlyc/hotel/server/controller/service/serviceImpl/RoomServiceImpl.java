package com.alisdlyc.hotel.server.controller.service.serviceImpl;

import com.alisdlyc.hotel.server.controller.service.RoomService;
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
public class RoomServiceImpl implements RoomService {

    @Override
    public String addRoom(CookieStorage cookie, Socket socket, String number) {

        if (cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()) == null) {
            return "请先登录";
        }

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();

            String primRoomSql = "SELECT * FROM room WHERE `roomnumber` = ?";

            st = conn.prepareStatement(primRoomSql);
            st.setString(1, number);
            rs = st.executeQuery();
            if (rs.next()) {
                System.out.println(rs.getString("roomnumber") + "is already exist");
                return "FAIL";
            }

            String primSql = "SELECT `authority` FROM usr where `name` = ?";

            st = conn.prepareStatement(primSql);

            st.setString(1, cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort()));

            rs = st.executeQuery();

            if (rs.next()) {
                int authority = Integer.parseInt(rs.getString("authority"));

                if (authority <= 0) {
                    return "FAIL";
                }
            }

            String sql = "INSERT INTO `room`(`roomnumber`) VALUES(?)";
            st = conn.prepareStatement(sql);

            st.setString(1, number);
            int i = st.executeUpdate();
            if (i > 0) {
                return "OK";
            } else {
                return "FAIL";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "FAIL";
        } finally {
            JdbcUtils.release(conn, st, rs);
        }
    }
}
