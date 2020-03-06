package com.github.mikesafonov.smpp.server;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Safonov
 */
class MockSmppServerHolderTest {
    @Nested
    class StartAll {
        @Test
        void shouldStartAll() {
            List<MockSmppServer> servers = new ArrayList<>();
            MockSmppServer one = mock(MockSmppServer.class);
            MockSmppServer two = mock(MockSmppServer.class);
            servers.add(one);
            servers.add(two);

            new MockSmppServerHolder(servers).startAll();

            verify(one, times(1)).start();
            verify(two, times(1)).start();
        }
    }

    @Nested
    class StopAll {
        @Test
        void shouldStopAll() {
            List<MockSmppServer> servers = new ArrayList<>();
            MockSmppServer one = mock(MockSmppServer.class);
            MockSmppServer two = mock(MockSmppServer.class);
            servers.add(one);
            servers.add(two);

            new MockSmppServerHolder(servers).stopAll();

            verify(one, times(1)).stop();
            verify(two, times(1)).stop();
        }
    }

    @Nested
    class ClearAll {
        @Test
        void shouldClearAll() {
            List<MockSmppServer> servers = new ArrayList<>();
            MockSmppServer one = mock(MockSmppServer.class);
            MockSmppServer two = mock(MockSmppServer.class);
            servers.add(one);
            servers.add(two);

            new MockSmppServerHolder(servers).clearAll();

            verify(one, times(1)).clearRequests();
            verify(two, times(1)).clearRequests();
        }
    }

    @Nested
    class IsAllStarted {
        @Test
        void shouldReturnTrueBecauseAllStarted() {
            List<MockSmppServer> servers = new ArrayList<>();
            MockSmppServer one = mock(MockSmppServer.class);
            MockSmppServer two = mock(MockSmppServer.class);
            servers.add(one);
            servers.add(two);

            when(one.isStarted()).thenReturn(true);
            when(two.isStarted()).thenReturn(true);

            assertTrue(new MockSmppServerHolder(servers).isAllStarted());
        }

        @Test
        void shouldReturnFalseBecauseOneNotStarted() {
            List<MockSmppServer> servers = new ArrayList<>();
            MockSmppServer one = mock(MockSmppServer.class);
            MockSmppServer two = mock(MockSmppServer.class);
            servers.add(one);
            servers.add(two);

            when(one.isStarted()).thenReturn(false);
            when(two.isStarted()).thenReturn(true);

            assertFalse(new MockSmppServerHolder(servers).isAllStarted());
        }
    }

    @Nested
    class GetByName {
        @Test
        void shouldReturnExpectedServer(){
            List<MockSmppServer> servers = new ArrayList<>();
            MockSmppServer one = new MockSmppServer("one");
            MockSmppServer two = new MockSmppServer("two");
            servers.add(one);
            servers.add(two);

            Optional<MockSmppServer> server = new MockSmppServerHolder(servers).getByName("one");
            assertEquals(one, server.get());
        }

        @Test
        void shouldReturnEmptyBecauseNameNotMatch(){
            List<MockSmppServer> servers = new ArrayList<>();
            MockSmppServer one = new MockSmppServer("one");
            MockSmppServer two = new MockSmppServer("two");
            servers.add(one);
            servers.add(two);

            Optional<MockSmppServer> server = new MockSmppServerHolder(servers).getByName("test");
            assertFalse(server.isPresent());
        }
    }
}
