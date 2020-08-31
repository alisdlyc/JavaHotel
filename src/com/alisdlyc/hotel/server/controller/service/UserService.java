package com.alisdlyc.hotel.server.controller.service;

import com.alisdlyc.hotel.utils.CookieStorage;

import java.net.Socket;

/**
 * @author alisdlyc
 */
public interface UserService {
    String addUser(String usr, String psw);
    String login(CookieStorage cookie, Socket socket, String usr, String psw);
    String logout(CookieStorage cookie, Socket socket);
    String adminDelete(CookieStorage cookie, Socket socket, String admin, String usr);
    String showReservations(CookieStorage cookie, Socket socket);
    String showReservation(CookieStorage cookie, Socket socket);
}
