package com.alisdlyc.hotel.server.controller.service.serviceImpl;

import com.alisdlyc.hotel.server.controller.service.UserService;
import com.alisdlyc.hotel.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    @Override
    public void addUser(String usr, String psw) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = JdbcUtils.getConnection();

            // 区别
            // 用 ? 代替参数
            String sql = "INSERT INTO usr(`name`,`password`) VALUES(?,?)";

            st = conn.prepareStatement(sql);

            // 手动给参数赋值
            st.setString(1, usr);
            st.setString(2, psw);

            int i = st.executeUpdate();
            if (i > 0) {
                System.out.println("OK");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("FAIL");
        } finally {
            JdbcUtils.release(conn, st, null);
        }
    }

    @Override
    public void login(String usr, String psw) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void adminDelete(String admin, String usr) {

    }

    @Override
    public void showReservations() {

    }
}
