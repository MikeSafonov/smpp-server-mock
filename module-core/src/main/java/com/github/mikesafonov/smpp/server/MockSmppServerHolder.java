package com.github.mikesafonov.smpp.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

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

    /**
     * @return true if all servers started
     */
    public boolean isAllStarted() {
        return servers.stream()
                .allMatch(MockSmppServer::isStarted);
    }

    /**
     * @param name server name
     * @return {@link MockSmppServer} with given name
     */
    public Optional<MockSmppServer> getByName(String name) {
        return servers.stream()
                .filter(mockSmppServer -> mockSmppServer.getName().equals(name))
                .findFirst();
    }
}
