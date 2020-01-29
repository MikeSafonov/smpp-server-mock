package com.github.mikesafonov.junit.smpp;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;

/**
 * @author Mike Safonov
 */
public class Utils {
    static int checkPortOrGetFree(int port) {
        if (port == MockSmppServer.RANDOM_PORT) {
            return findRandomOpenPortOnAllLocalInterfaces();
        } else {
            return port;
        }
    }

    static String randomUuidName() {
        return UUID.randomUUID().toString();
    }

    synchronized static Integer findRandomOpenPortOnAllLocalInterfaces() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Unable to find port", e);
        }
    }
}
