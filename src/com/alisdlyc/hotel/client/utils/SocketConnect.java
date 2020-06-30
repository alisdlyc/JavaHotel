package com.alisdlyc.hotel.client.utils;

import com.alisdlyc.hotel.client.config.ClientConfig;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;

import static com.alisdlyc.hotel.client.utils.CommandCheckUtil.COMMAND_NOT_FOUND;
import static com.alisdlyc.hotel.client.utils.CommandCheckUtil.SUCCESS;

/**
 * @author wangz
 */
public class SocketConnect {
    private static final Socket CLIENT = new Socket();
    private static PrintStream socketPrintStream;
    private static BufferedReader socketBufferedReader;

    static {

        ClientConfig clientConfig;

        try {
            clientConfig = new ClientConfig(3000, 2000, Inet4Address.getLocalHost());


            CLIENT.setSoTimeout(clientConfig.getTimeout());
            CLIENT.connect(clientConfig.getInetSocketAddress());


            // 得到Socket输出流，并转换为打印流
            OutputStream outputStream = CLIENT.getOutputStream();
            socketPrintStream = new PrintStream(outputStream);


            // 得到Socket输入流，并转换为BufferedReader
            InputStream inputStream = CLIENT.getInputStream();
            socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            try {
                releaseResource();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


    }

    public static String input(String str) throws IOException {

        //校验一下,如果命令合法继续,否则跳过本次循环
        int check = CommandCheckUtil.check(str);

        if (check != SUCCESS) {
            System.out.println(check == COMMAND_NOT_FOUND ? "WRONG COMMAND" : "WRONG PARAMETER");
        }
        // 发送到服务器
        socketPrintStream.println(str);

        // 返回服务器的数据
        return socketBufferedReader.readLine();

    }

    public static void releaseResource() throws IOException {

        // 资源释放
        socketPrintStream.close();
        socketBufferedReader.close();
    }


}
