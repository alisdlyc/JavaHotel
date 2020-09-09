package com.alisdlyc.hotel.server.service;


import java.sql.SQLException;

public interface LoginService {
    String login(String usr, String psw) throws SQLException;
    String logout();
}
