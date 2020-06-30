package com.alisdlyc.hotel.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private static HashMap<String, String> map = null;

    public static void main(String[] args) throws IOException {

        map = new HashMap<>(10);

        ServerSocket server = new ServerSocket(2000);

        System.out.println("服务器准备就绪～");
        System.out.println("服务器信息：" + server.getInetAddress() + " P:" + server.getLocalPort());

        // 等待客户端连接
        for (; ; ) {
            // 得到客户端
            Socket client = server.accept();
            // 客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            // 启动线程
            clientHandler.start();
        }

    }

    /**
     * 客户端消息处理
     */
    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接：" + socket.getInetAddress() + " P:" + socket.getPort());

            try {
                // 得到打印流，用于数据输出；服务器回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                socketOutput.println("Server Connected, please enter command:");
                // 得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 存入用户标识和权限信息
                map.put("" + socket.getInetAddress() + socket.getPort(), null);

                do {
                    // 客户端拿到一条数据
                    String str = socketInput.readLine();
                    if ("qwq".equalsIgnoreCase(str)) {
                        flag = false;
                        // 回送
                        socketOutput.println("qwq");
                    } else {
                        // 对客户端发送的信息进行响应
                        // TODO 将指令和对应的sql操作封装为 hashmap, 获取指令时, 寻找对应的操作, 并回送数据

                        socketOutput.println("回送：" + str.length());
                    }

                } while (flag);

                socketInput.close();
                socketOutput.close();

            } catch (Exception e) {
                System.out.println("连接异常断开");
            } finally {
                // 连接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("客户端已退出：" + socket.getInetAddress() + " P:" + socket.getPort());

        }
    }
}
