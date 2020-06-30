package com.alisdlyc.hotel.utils;

import com.alisdlyc.hotel.server.controller.service.OrderService;
import com.alisdlyc.hotel.server.controller.service.RoomService;
import com.alisdlyc.hotel.server.controller.service.UserService;
import com.alisdlyc.hotel.server.controller.service.serviceImpl.OrderServiceImpl;
import com.alisdlyc.hotel.server.controller.service.serviceImpl.RoomServiceImpl;
import com.alisdlyc.hotel.server.controller.service.serviceImpl.UserServiceImpl;

import java.net.Socket;
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

    public String process(String command, CookieStorage cookie, Socket socket) {
        List<String> list = commandParse(command);

        switch (list.get(0)) {
            case CREATE_USER:
                return userService.addUser(list.get(1), list.get(2));

            case LOGIN:
                String usr = cookie.loginStage.get("" + socket.getInetAddress() + socket.getPort());
                // 若当前已经登陆
                if ( usr != null) {
                    if (usr.equals(list.get(1))){
                        return "当前账户已经登陆";
                    } else {
                        return "请先退出当前账号";
                    }
                }
                return userService.login(cookie, socket, list.get(1), list.get(2));

            case LOGOUT:
                return userService.logout(cookie, socket);

            case DELETE:
                return userService.adminDelete(cookie, socket, "admin", list.get(2));

            case ADDROOM:
                return roomService.addRoom(list.get(1));

            case RESERVE_ROOM:
                return orderService.reserveRoom(list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));

            case SHOW_RESERVATIONS:
                return userService.showReservations(cookie, socket);

            default:
                return "FAIL";
        }
    }

    private List<String> commandParse(String command) {
        String[] list = command.split(" ");
        return Arrays.asList(list);

    }

}
