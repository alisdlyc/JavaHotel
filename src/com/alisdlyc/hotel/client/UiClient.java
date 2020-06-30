package com.alisdlyc.hotel.client;

import com.alisdlyc.hotel.client.ui.LoginAndRegister;

import javax.swing.*;

/**
 * @author wangz
 */
public class UiClient {
    public static void main(String[] args) {
        JFrame frame = new JFrame("登录和注册");
        frame.setContentPane(new LoginAndRegister().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
