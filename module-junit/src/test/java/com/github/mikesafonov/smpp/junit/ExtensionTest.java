package com.github.mikesafonov.smpp.junit;

import com.github.mikesafonov.smpp.server.MockSmppServer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ExtensionTest {

    @Nested
    @ExtendWith(MockSmppExtension.class)
    class RandomPort {
        @SmppServer
        MockSmppServer mockSmppServer;

        @Test
        void shouldRunDefault() {
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, mockSmppServer.getSystemId());
            assertTrue(mockSmppServer.isStarted());
        }
    }

    @Nested
    @ExtendWith(MockSmppExtension.class)
    class WithConfigured {
        @SmppServer(systemId = "customSystemId", password = "anotherPassword")
        MockSmppServer mockSmppServer;

        @Test
        void shouldRunDefault() {
            assertEquals("customSystemId", mockSmppServer.getSystemId());
            assertTrue(mockSmppServer.isStarted());
        }
    }

    @Nested
    @ExtendWith(MockSmppExtension.class)
    class WithCustomName {
        @SmppServer(name = "customName")
        MockSmppServer mockSmppServer;

        @Test
        void shouldRunDefault() {
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, mockSmppServer.getSystemId());
            assertEquals("customName", mockSmppServer.getName());
            assertTrue(mockSmppServer.isStarted());
        }
    }

    @Nested
    @ExtendWith(MockSmppExtension.class)
    class RunTwo {
        @SmppServer
        MockSmppServer serverOne;
        @SmppServer
        MockSmppServer serverTwo;

        @Test
        void shouldRunDefault() {
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, serverOne.getSystemId());
            assertTrue(serverOne.isStarted());

            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, serverTwo.getSystemId());
            assertTrue(serverTwo.isStarted());
        }
    }
}
