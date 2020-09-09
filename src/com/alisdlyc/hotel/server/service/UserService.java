package com.alisdlyc.hotel.server.service;

import java.sql.SQLException;

/**
 * @author alisdlyc
 */

public interface UserService {
    /**
     * @param usr 用户名
     * @param psw 密码
     * */
    String addUser(String usr, String psw) throws SQLException;


/*    String login(CookieStorage cookie, Socket socket, String usr, String psw);

    String logout(CookieStorage cookie, Socket socket);*/

    String adminDelete(String admin, String usr) throws SQLException;

    String showReservations() throws SQLException;

    String showReservation() throws SQLException;
}
