package com.alisdlyc.hotel.server.service;

import java.sql.SQLException;

/**
 * @author alisdlyc
 */
public interface RoomService {
    /**
     * 添加房间
     *
     * @param number 房间号
     * @return res
     */
    String addRoom(String number) throws SQLException;
}
