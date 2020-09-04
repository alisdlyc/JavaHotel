package com.alisdlyc.hotel.server.controller.service;

import com.alisdlyc.hotel.utils.CookieStorage;

import java.net.Socket;

/**
 * @author alisdlyc
 */
public interface OrderService {
    /**
     * 客户下单
     *
     * @param cookie       cookie
     * @param socket       socket
     * @param peopleNumber 人数
     * @param startYear    起始年份
     * @param startMonth   起始月份
     * @param startDay     起始天
     * @param endYear      结束年份
     * @param endMonth     结束月份
     * @param endDay       结束天数
     * @return ?
     */
    String reserveRoom(CookieStorage cookie, Socket socket, String peopleNumber, String startYear, String startMonth,
                       String startDay, String endYear, String endMonth, String endDay);
}
