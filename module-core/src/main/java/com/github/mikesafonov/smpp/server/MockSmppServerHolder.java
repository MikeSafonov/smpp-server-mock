package com.github.mikesafonov.smpp.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Holder class for list of {@link MockSmppServer}
 *
 * @author Mike Safonov
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class MockSmppServerHolder {
    private final List<MockSmppServer> servers;

    /**
     * Starts all {@link MockSmppServer}
     */
    public void startAll() {
        for (MockSmppServer server : servers) {
            log.info("Starting " + server.getDescription());
            server.start();
            log.info(server.getDescription() + " started");
        }
    }

    /**
     * Stops all {@link MockSmppServer}
     */
    public void stopAll() {
        for (MockSmppServer server : servers) {
            log.info("Stopping " + server.getDescription());
            server.stop();
            log.info(server.getDescription() + " stopped");
        }
    }
}
