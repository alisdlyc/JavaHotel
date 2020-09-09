package com.alisdlyc.hotel.server.service;

import java.sql.SQLException;

/**
 * @author alisdlyc
 */
public interface OrderService {
    /**
     * 客户下单
     *
     * @param peopleNumber 人数
     * @param startYear    起始年份
     * @param startMonth   起始月份
     * @param startDay     起始天
     * @param endYear      结束年份
     * @param endMonth     结束月份
     * @param endDay       结束天数
     * @return ?
     */
    String reserveRoom(String peopleNumber, String startYear, String startMonth,
                       String startDay, String endYear, String endMonth, String endDay) throws SQLException;
}
