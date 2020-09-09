package com.alisdlyc.hotel.utils;
import com.alisdlyc.hotel.server.entry.User;

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
    final static String DELETE = "delete_admin";
    final static String ADDROOM = "addroom";
    final static String RESERVE_ROOM = "reserve_room";
    final static String SHOW_RESERVATIONS = "show_reservations";
    final static String SHOW_RESERVATION = "show_reservation";

    public String process(String command, User usr) {
        List<String> list = commandParse(command);

        try {
            // 若当前已经登陆
            return switch (list.get(0)) {
                case CREATE_USER -> usr.addUser(list.get(1), list.get(2));
                case LOGIN -> usr.login(list.get(1), list.get(2));
                case LOGOUT -> usr.logout();
                case DELETE -> usr.adminDelete("admin", list.get(2));
                case ADDROOM -> usr.addRoom(list.get(1));
                case RESERVE_ROOM -> usr.reserveRoom(list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
                case SHOW_RESERVATIONS -> usr.showReservations();
                case SHOW_RESERVATION -> usr.showReservation();
                default -> "FAIL";
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "FAIL";
    }

    private List<String> commandParse(String command) {
        String[] list = command.split(" ");
        return Arrays.asList(list);
    }

}
