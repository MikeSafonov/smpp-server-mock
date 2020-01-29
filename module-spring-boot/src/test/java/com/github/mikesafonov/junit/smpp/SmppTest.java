package com.github.mikesafonov.junit.smpp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mike Safonov
 */
@SpringBootTest(properties = {
        "smpp.mocks.first.systemId=root",
        "smpp.mocks.first.password=toor"
})
public class SmppTest {
    @Autowired
    private MockSmppServerHolder holder;

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
