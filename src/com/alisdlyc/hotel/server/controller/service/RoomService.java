package com.alisdlyc.hotel.server.controller.service;

import com.alisdlyc.hotel.utils.CookieStorage;

import java.net.Socket;

public interface RoomService {
    String addRoom(CookieStorage cookie, Socket socket, String number);
}
