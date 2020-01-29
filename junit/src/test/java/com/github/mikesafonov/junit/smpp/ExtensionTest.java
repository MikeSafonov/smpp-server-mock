package com.github.mikesafonov.junit.smpp;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;


public class ExtensionTest {

    @Nested
    @ExtendWith(SmppExtension.class)
    class DefaultServer {
        @SmppServer
        MockSmppServer mockSmppServer;

        @Test
        void shouldRunDefault() {
            assertEquals(MockSmppServer.DEFAULT_PORT, mockSmppServer.getPort());
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, mockSmppServer.getSystemId());
            assertTrue(mockSmppServer.isStarted());
        }
    }

    @Nested
    @ExtendWith(SmppExtension.class)
    class RandomPort {
        @SmppServer(port = SmppServer.RANDOM_PORT)
        MockSmppServer mockSmppServer;

        @Test
        void shouldRunDefault() {
            assertNotEquals(MockSmppServer.DEFAULT_PORT, mockSmppServer.getPort());
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, mockSmppServer.getSystemId());
            assertTrue(mockSmppServer.isStarted());
        }
    }

    @Nested
    @ExtendWith(SmppExtension.class)
    class WithConfigured {
        @SmppServer(port = SmppServer.RANDOM_PORT, systemId = "customSystemId", password = "anotherPassword")
        MockSmppServer mockSmppServer;

        @Test
        void shouldRunDefault() {
            assertNotEquals(MockSmppServer.DEFAULT_PORT, mockSmppServer.getPort());
            assertEquals("customSystemId", mockSmppServer.getSystemId());
            assertTrue(mockSmppServer.isStarted());
        }
    }

    @Nested
    @ExtendWith(SmppExtension.class)
    class WithCustomName {
        @SmppServer(name = "customName", port = SmppServer.RANDOM_PORT)
        MockSmppServer mockSmppServer;

        @Test
        void shouldRunDefault() {
            assertNotEquals(MockSmppServer.DEFAULT_PORT, mockSmppServer.getPort());
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, mockSmppServer.getSystemId());
            assertEquals("customName", mockSmppServer.getName());
            assertTrue(mockSmppServer.isStarted());
        }
    }

    @Nested
    @ExtendWith(SmppExtension.class)
    class RunTwo {
        @SmppServer(port = SmppServer.RANDOM_PORT)
        MockSmppServer serverOne;
        @SmppServer(port = SmppServer.RANDOM_PORT)
        MockSmppServer serverTwo;

        @Test
        void shouldRunDefault() {
            assertNotEquals(MockSmppServer.DEFAULT_PORT, serverOne);
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, serverOne.getSystemId());
            assertTrue(serverOne.isStarted());

            assertNotEquals(MockSmppServer.DEFAULT_PORT, serverTwo);
            assertEquals(MockSmppServer.DEFAULT_SYSTEM_ID, serverTwo.getSystemId());
            assertTrue(serverTwo.isStarted());
        }
    }
}
