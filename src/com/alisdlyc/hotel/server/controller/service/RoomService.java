package com.alisdlyc.hotel.server.controller.service;

import com.alisdlyc.hotel.utils.CookieStorage;

import java.net.Socket;

/**
 * @author alisdlyc
 */
public interface RoomService {
    /**
     * 添加房间
     *
     * @param cookie cookie
     * @param socket socket
     * @param number 房间号
     * @return res
     */
    String addRoom(CookieStorage cookie, Socket socket, String number);
}
