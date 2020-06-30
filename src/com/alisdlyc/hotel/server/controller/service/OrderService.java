package com.alisdlyc.hotel.server.controller.service;

public interface OrderService {
    String reserveRoom(String peopleNumber, String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay);
}
