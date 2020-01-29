package com.github.mikesafonov.junit.smpp;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Mike Safonov
 */
public class PortUtils {
    public synchronized static Integer findRandomOpenPortOnAllLocalInterfaces() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Unable to find port", e);
        }
    }
}
