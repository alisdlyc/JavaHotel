package com.alisdlyc.hotel.server;


import com.alisdlyc.hotel.utils.CommandProcessor;
import com.alisdlyc.hotel.utils.CookieStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author alisdlyc
 */
public class Server {

    private static CookieStorage cookieStorage;

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(12000);

        cookieStorage = new CookieStorage();

        System.out.println("服务器准备就绪");
        System.out.println("服务器信息：" + server.getInetAddress() + " P:" + server.getLocalPort());

        // 等待客户端连接
        while (true) {
            try {
                // 得到客户端
                Socket client = server.accept();
                // 客户端构建异步线程
                ClientHandler clientHandler = new ClientHandler(client);
                // 启动线程
                clientHandler.start();
            } catch (Exception e) {
                return;
            }
        }

    }

    /**
     * 客户端消息处理
     */
    private static class ClientHandler extends Thread {
        private final Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            try {
                System.out.println("新客户端连接：" + socket.getInetAddress() + " Port:" + socket.getPort());

                // 得到打印流，用于数据输出；服务器回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                socketOutput.println("server Connected, please enter command:");

                // 得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 添加客户端状态信息
                cookieStorage.loginStage.put("" + socket.getInetAddress() + socket.getPort(), null);

                do {
                    // 客户端拿到一条数据
                    String str = socketInput.readLine();
                    if ("end".equals(str)) {
                        flag = false;
                    } else {
                        socketOutput.println(new CommandProcessor().process(str, cookieStorage, socket));
                    }

                } while (flag);

                socketInput.close();
                socketOutput.close();

                socket.close();

                System.out.println("客户端已退出：" + socket.getInetAddress() + " Port:" + socket.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
