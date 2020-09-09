package com.alisdlyc.hotel.server;


import com.alisdlyc.hotel.server.entry.User;
import com.alisdlyc.hotel.utils.CommandProcessor;
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

    public static User usr;

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(12000);
        System.out.println("服务器准备就绪");
        System.out.println("服务器信息：" + server.getInetAddress() + " P:" + server.getLocalPort());
        usr = new User();

        while (true) {
            try {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                clientHandler.start();
            } catch (Exception e) {
                return;
            }
        }

    }

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

                do {
                    String str = socketInput.readLine();
                    if ("end".equals(str)) {
                        flag = false;
                    } else {
                        socketOutput.println(new CommandProcessor().process(str, usr));
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
