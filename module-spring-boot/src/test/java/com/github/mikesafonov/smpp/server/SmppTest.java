package com.github.mikesafonov.smpp.server;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mike Safonov
 */

public class SmppTest {
    @Nested
    @SpringBootTest(properties = {
            "smpp.mocks.first.systemId=root",
            "smpp.mocks.first.password=toor"
    })
    class SingleServer extends BaseSmppTest {
        @Test
        void shouldRunExpectedSmppServer() {
            List<MockSmppServer> servers = holder.getServers();

            assertEquals(1, servers.size());
            MockSmppServer server = servers.get(0);
            assertEquals("root", server.getSystemId());
            assertEquals("toor", server.getPassword());
            assertTrue(server.isStarted());
        }
    }

    @Nested
    @SpringBootTest(properties = {
            "smpp.mocks.first.systemId=root",
            "smpp.mocks.first.password=toor",
            "smpp.mocks.second.systemId=root2",
            "smpp.mocks.second.password=toor2"
    })
    class TwoServers extends BaseSmppTest {
        @Test
        void shouldRunExpectedSmppServer() {
            List<MockSmppServer> servers = holder.getServers();

            assertEquals(2, servers.size());
            MockSmppServer firstServer = servers.get(0);
            assertEquals("root", firstServer.getSystemId());
            assertEquals("toor", firstServer.getPassword());
            assertEquals("first", firstServer.getName());
            assertTrue(firstServer.isStarted());

            MockSmppServer secondServer = servers.get(1);
            assertEquals("root2", secondServer.getSystemId());
            assertEquals("toor2", secondServer.getPassword());
            assertEquals("second", secondServer.getName());
            assertTrue(secondServer.isStarted());
        }
    }

}
