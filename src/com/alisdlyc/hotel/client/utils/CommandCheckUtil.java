package com.alisdlyc.hotel.client.utils;

import java.util.HashSet;

/**
 * @author wangz
 */
public class CommandCheckUtil {
    public static final int COMMAND_NOT_FOUND = 1;
    public static final int SUCCESS = 0;
    public static final int PARAMETER_ERROR = 2;
    final static String CREATE_USER = "create";
    final static String LOGIN = "login";
    final static String LOGOUT = "logout";
    final static String DELETE = "delete_admin";
    final static String ADDROOM = "addroom";
    final static String RESERVE_ROOM = "reserve_room";
    final static String SHOW_RESERVATIONS = "show_reservations";
    final static String SHOW_RESERVATION = "show_reservation";
    private static final HashSet<String> COMMAND = new HashSet<>();

    static {
        COMMAND.add(CREATE_USER);
        COMMAND.add(LOGIN);
        COMMAND.add(LOGOUT);
        COMMAND.add(DELETE);
        COMMAND.add(ADDROOM);
        COMMAND.add(RESERVE_ROOM);
        COMMAND.add(SHOW_RESERVATIONS);
        COMMAND.add(SHOW_RESERVATION);
    }

    /**
     * @param command User input commands
     * @return 0 if no error, 1 if command not found, 2 parameter error.
     */
    public static int check(String command) {
        // 输入命令为空或者发生了什么错误导致对象为 null
        if (command == null || command.isEmpty()) {
            return COMMAND_NOT_FOUND;
        }
        String[] list = command.split(" ");

        // 输入的命令虽然不为存在但是并不是真正的命令
        if (!COMMAND.contains(list[0])) {
            return COMMAND_NOT_FOUND;
        }

        return switch (list[0]) {
            case CREATE_USER, LOGIN, DELETE -> checkLengthAndEmpty(list, 3);
            case LOGOUT, SHOW_RESERVATIONS, SHOW_RESERVATION -> checkLengthAndEmpty(list, 1);
            case ADDROOM -> checkAddRoom(list, 2);
            case RESERVE_ROOM -> checkReserveRoom(list, 8);
            default -> COMMAND_NOT_FOUND;
        };
    }

    private static int checkAddRoom(String[] list, int length) {
        int res = checkLengthAndEmpty(list, length);

        if (res != SUCCESS) {
            return res;
        }
        try {
            if (Integer.parseInt(list[1]) <= 0) {
                throw new Exception("");
            }
        } catch (Exception e) {
            return PARAMETER_ERROR;
        }
        return SUCCESS;
    }

    private static int checkReserveRoom(String[] list, int length) {
        int res = checkLengthAndEmpty(list, length);

        // 长度校验未通过
        if (res != SUCCESS) {
            return res;
        } else {

            //校验数据的合法性,保证输入的日期肯定是正整数
            for (int i = 1; i < length; i++) {
                try {

                    if (Integer.parseInt(list[i]) <= 0) {
                        throw new Exception("");
                    }
                } catch (Exception e) {
                    return PARAMETER_ERROR;
                }
            }
            return SUCCESS;
        }
    }

    private static int checkLengthAndEmpty(String[] list, int length) {
        if (list.length != length) {
            return PARAMETER_ERROR;
        }
        for (String s : list) {
            if ("".equals(s)) {
                return PARAMETER_ERROR;
            }
        }
        return SUCCESS;
    }
}