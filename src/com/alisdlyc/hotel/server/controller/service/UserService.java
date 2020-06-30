package com.alisdlyc.hotel.server.controller.service;

public interface UserService {
    void addUser(String usr, String psw);
    void login(String usr, String psw);
    void logout();
    void adminDelete(String admin, String usr);
    void showReservations();
}
