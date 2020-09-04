package com.alisdlyc.hotel.client.config;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author wangz
 */
public class ClientConfig {
    final public static String STOP = "end";
    private final int timeout;
    private final InetSocketAddress inetSocketAddress;

    public ClientConfig(int timeout, int port, InetAddress address) {
        this.timeout = timeout;
        this.inetSocketAddress = new InetSocketAddress(address, port);
    }

    public int getTimeout() {
        return timeout;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

}
