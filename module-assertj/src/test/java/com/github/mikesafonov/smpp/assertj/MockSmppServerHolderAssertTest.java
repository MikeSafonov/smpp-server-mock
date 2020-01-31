package com.github.mikesafonov.smpp.assertj;

import com.github.mikesafonov.smpp.server.MockSmppServer;
import com.github.mikesafonov.smpp.server.MockSmppServerHolder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.mikesafonov.smpp.assertj.SmppAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Safonov
 */
class MockSmppServerHolderAssertTest {

    @Nested
    class Servers {
        @Test
        void shouldReturnServersAssert() {
            MockSmppServerHolder holder = mock(MockSmppServerHolder.class);

            assertNotNull(assertThat(holder).servers());

            verify(holder, times(1)).getServers();
        }
    }

    @Nested
    class AllStarted {
        @Test
        void shouldSuccessAllStarted() {
            List<MockSmppServer> servers = Arrays.asList(
                    mock(MockSmppServer.class), mock(MockSmppServer.class)
            );

            MockSmppServerHolder holder = new MockSmppServerHolder(servers);

            when(servers.get(0).isStarted()).thenReturn(true);
            when(servers.get(1).isStarted()).thenReturn(true);

            assertNotNull(assertThat(holder).allStarted());
        }

        @Test
        void shouldFailAllStarted() {
            List<MockSmppServer> servers = Arrays.asList(
                    mock(MockSmppServer.class), mock(MockSmppServer.class)
            );

            MockSmppServerHolder holder = new MockSmppServerHolder(servers);
            when(servers.get(0).isStarted()).thenReturn(false);
            when(servers.get(1).isStarted()).thenReturn(true);

            AssertionError assertionError = assertThrows(AssertionError.class, () -> assertThat(holder)
                    .allStarted());
            assertEquals("Expected all servers started", assertionError.getMessage());
        }
    }

    @Nested
    class ServerByName {
        @Test
        void shouldSuccessGetByName() {
            List<MockSmppServer> servers = Arrays.asList(
                    new MockSmppServer("one"),
                    new MockSmppServer("two")
            );

            MockSmppServerHolder holder = new MockSmppServerHolder(servers);

            assertNotNull(assertThat(holder).serverByName("one"));
        }

        @Test
        void shouldFailAllStarted() {
            List<MockSmppServer> servers = Arrays.asList(
                    new MockSmppServer("one"),
                    new MockSmppServer("two")
            );

            MockSmppServerHolder holder = new MockSmppServerHolder(servers);

            AssertionError assertionError = assertThrows(AssertionError.class, () -> assertThat(holder)
                    .serverByName("five"));
            assertEquals("Expected server with name <five> but not found", assertionError.getMessage());
        }
    }
}
