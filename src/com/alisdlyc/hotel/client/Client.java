package com.alisdlyc.hotel.client;

import com.alisdlyc.hotel.client.config.ClientConfig;
import com.alisdlyc.hotel.client.utils.CommandCheckUtil;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.alisdlyc.hotel.client.utils.CommandCheckUtil.COMMAND_NOT_FOUND;
import static com.alisdlyc.hotel.client.utils.CommandCheckUtil.SUCCESS;

/**
 * @author wangz
 */
public class Client {
    public static void main(String[] args) throws UnknownHostException {
        Socket socket = new Socket();

        ClientConfig clientConfig = new ClientConfig(3000, 12000, Inet4Address.getLocalHost());


        try (socket) {
            socket.setSoTimeout(clientConfig.getTimeout());

            socket.connect(clientConfig.getInetSocketAddress());

            System.out.println("已发起服务器连接，并进入后续流程～");
            System.out.println("客户端信息：" + socket.getLocalAddress() + " P:" + socket.getLocalPort());
            System.out.println("服务器信息：" + socket.getInetAddress() + " P:" + socket.getPort());

            // 发送接收数据
            run(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        } finally {
            // 释放资源
            System.out.println("客户端已退出～");
        }

    }

    private static void run(Socket client) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));


        // 得到Socket输出流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);


        // 得到Socket输入流，并转换为BufferedReader
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        System.out.println(socketBufferedReader.readLine());

        boolean flag = true;
        do {
            // 键盘读取一行
            String str = input.readLine();

            //校验一下,如果命令合法继续,否则跳过本次循环
            int check = CommandCheckUtil.check(str);
            if (check != SUCCESS) {
                System.out.println(check == COMMAND_NOT_FOUND ? "WRONG COMMAND" : "WRONG PARAMETER");
                continue;
            }
            // 发送到服务器
            socketPrintStream.println(str);


            // 从服务器读取一行
            String echo = socketBufferedReader.readLine();

            if (ClientConfig.STOP.equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);

        // 资源释放
        socketPrintStream.close();
        socketBufferedReader.close();

    }


}
