package com.alisdlyc.hotel.server.controller.service;

import com.alisdlyc.hotel.utils.CookieStorage;

import java.net.Socket;

/**
 * @author alisdlyc
 */
public interface OrderService {
    String reserveRoom(CookieStorage cookie, Socket socket, String peopleNumber, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay);
}
