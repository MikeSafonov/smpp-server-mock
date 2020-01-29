package com.github.mikesafonov.smpp.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Safonov
 */
class MockSmppServerConfigurationTest {
    @Test
    void shouldSetPortAndSystemId() {
        int port = 3333;
        String systemId = "systemIdOne";
        MockSmppServerConfiguration configuration = new MockSmppServerConfiguration(port, systemId);

        assertEquals(port, configuration.getPort());
        assertEquals(systemId, configuration.getSystemId());
    }
}
