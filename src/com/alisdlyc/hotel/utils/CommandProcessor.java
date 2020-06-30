package com.alisdlyc.hotel.utils;

import com.alisdlyc.hotel.Server.controller.service.OrderService;
import com.alisdlyc.hotel.Server.controller.service.RoomService;
import com.alisdlyc.hotel.Server.controller.service.UserService;
import com.alisdlyc.hotel.Server.controller.service.serviceImpl.OrderServiceImpl;
import com.alisdlyc.hotel.Server.controller.service.serviceImpl.RoomServiceImpl;
import com.alisdlyc.hotel.Server.controller.service.serviceImpl.UserServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * 命令处理器,分发命令
 *
 * @author wangz
 */
public class CommandProcessor {
    final static String CREATE_USER = "create";
    final static String LOGIN = "login";
    final static String LOGOUT = "logout";
    final static String DELETE = "adminDelete";
    final static String ADDROOM = "addroom";
    final static String RESERVE_ROOM = "reserve_room";
    final static String SHOW_RESERVATIONS = "show_reservations";

    final static UserService userService = new UserServiceImpl();
    final static RoomService roomService = new RoomServiceImpl();
    final static OrderService orderService = new OrderServiceImpl();

    public void process(String command) {
        List<String> list = commandParse(command);
        switch (list.get(0)) {
            case CREATE_USER:
                userService.addUser(list.get(1), list.get(2));
                break;

            case LOGIN:
                userService.login(list.get(1), list.get(2));
                break;

            case LOGOUT:
                userService.logout();
                break;

            case DELETE:
                userService.adminDelete("admin", list.get(2));
                break;

            case ADDROOM:
                roomService.addRoom(list.get(1));
                break;

            case RESERVE_ROOM:
                orderService.reserveRoom(list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
                break;

            case SHOW_RESERVATIONS:
                userService.showReservations();
                break;

            default:
                break;
        }
    }

    private List<String> commandParse(String command) {
        String[] list = command.split(" ");
        return Arrays.asList(list);

    }

}
