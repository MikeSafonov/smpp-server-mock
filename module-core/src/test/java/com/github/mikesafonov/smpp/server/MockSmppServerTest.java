package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.SmppChannelException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Safonov
 */
class MockSmppServerTest {
    @Nested
    class Create {
        @Test
        void shouldCreateDefault() {
            MockSmppServer server = new MockSmppServer();

            assertUUID(server.getName());
            assertEquals("mockSmppServer", server.getSystemId());
            assertEquals("password", server.getPassword());
        }

        @Test
        void shouldCreateDefaultWithRandomPort() {
            MockSmppServer server = new MockSmppServer(MockSmppServer.RANDOM_PORT);

            assertUUID(server.getName());
            assertEquals("mockSmppServer", server.getSystemId());
            assertEquals("password", server.getPassword());
        }

        @Test
        void shouldCreateWithName() {
            String smppName = "smppName";
            MockSmppServer server = new MockSmppServer(smppName);

            assertEquals(smppName, server.getName());
            assertEquals("mockSmppServer", server.getSystemId());
            assertEquals("password", server.getPassword());
        }

        @Test
        void shouldCreateWithDefaultSystemId() {
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port);

            assertUUID(server.getName());
            assertEquals(port, server.getPort());
            assertEquals("mockSmppServer", server.getSystemId());
            assertEquals("password", server.getPassword());
        }

        @Test
        void shouldCreateWithNameAndDefaultSystemId() {
            int port = 3000;
            String smppName = "smppName";
            MockSmppServer server = new MockSmppServer(smppName, port);

            assertEquals(smppName, server.getName());
            assertEquals(port, server.getPort());
            assertEquals("mockSmppServer", server.getSystemId());
            assertEquals("password", server.getPassword());
        }

        @Test
        void shouldCreateWithDefaultPort() {
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServer server = new MockSmppServer(systemId, password);

            assertUUID(server.getName());
            assertEquals(systemId, server.getSystemId());
            assertEquals(password, server.getPassword());
        }

        @Test
        void shouldCreateWithNameAndDefaultPort() {
            String smppName = "smppName";
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServer server = new MockSmppServer(smppName, systemId, password);

            assertEquals(smppName, server.getName());
            assertEquals(systemId, server.getSystemId());
            assertEquals(password, server.getPassword());
        }

        @Test
        void shouldCreateWithCustomPortAndSystemId() {
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password);

            assertUUID(server.getName());
            assertEquals(port, server.getPort());
            assertEquals(systemId, server.getSystemId());
            assertEquals(password, server.getPassword());
        }

        private void assertUUID(String uuid) {
            assertDoesNotThrow(() -> UUID.fromString(uuid));
        }
    }

    @Nested
    class Start {
        @Test
        void shouldCallStartOnSmppServer() throws SmppChannelException {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);
            server.start();

            assertTrue(server.isStarted());
            verify(defaultSmppServer, times(1)).start();
        }

        @Test
        void shouldThrowRuntimeExceptionWhenException() throws SmppChannelException {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);

            doThrow(SmppChannelException.class).when(defaultSmppServer).start();

            assertThrows(RuntimeException.class, server::start);
        }

        @Test
        void shouldCallStartOnSmppServerOnce() throws SmppChannelException {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);
            server.start();
            server.start();
            server.start();

            assertTrue(server.isStarted());
            verify(defaultSmppServer, times(1)).start();
        }
    }

    @Nested
    class Stop {
        @Test
        void shouldCallStopOnSmppServer() {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);
            server.stop();

            verify(defaultSmppServer, times(1)).stop();
            assertFalse(server.isStarted());
        }
    }

    @Nested
    class Description {
        @Test
        void shouldReturnExpectedDescription() {
            MockSmppServer server = new MockSmppServer("server", 2000, "systemId", "pass");
            assertEquals("Smpp server[name: server, port: 2000, systemId: systemId]", server.getDescription());
        }
    }

    @Nested
    class ClearRequests {
        @Test
        void shouldRemoveAllRequests() {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            SmppRequestsQueue smppRequestsQueue = mock(SmppRequestsQueue.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);

            when(handler.getSessionHandler()).thenReturn(smppRequestsQueue);

            server.clearRequests();

            verify(smppRequestsQueue).clear();
        }
    }

    @Nested
    class GetHost {
        @Test
        void shouldReturnLocalhost() {
            String smppName = "smppName";
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServer server = new MockSmppServer(smppName, systemId, password);

            assertEquals("localhost", server.getHost());
        }
    }

    @Nested
    class GetCountRequests {
        @Test
        void shouldReturnExpectedCount() {
            int count = ThreadLocalRandom.current().nextInt();
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            SmppRequestsQueue smppRequestsQueue = mock(SmppRequestsQueue.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);

            when(handler.getSessionHandler()).thenReturn(smppRequestsQueue);
            when(smppRequestsQueue.countTotalMessages()).thenReturn(count);

            assertEquals(count, server.getCountRequests());
        }
    }

    @Nested
    class GetRequests {
        @Test
        void shouldReturnExpectedList() {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            SmppRequestsQueue smppRequestsQueue = mock(SmppRequestsQueue.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            List<PduRequest> pduRequests = Arrays.asList(
                    mock(PduRequest.class), mock(PduRequest.class)
            );
            BlockingDeque<PduRequest> deque = new LinkedBlockingDeque<>(pduRequests);

            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);

            when(handler.getSessionHandler()).thenReturn(smppRequestsQueue);
            when(smppRequestsQueue.getReceivedPduRequests()).thenReturn(deque);

            assertEquals(pduRequests, server.getRequests());
        }
    }

    @Nested
    class GetSubmitSmMessages {
        @Test
        void shouldReturnExpectedList() {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            SmppRequestsQueue smppRequestsQueue = mock(SmppRequestsQueue.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            List<SubmitSm> requests = Arrays.asList(
                    mock(SubmitSm.class), mock(SubmitSm.class)
            );
            BlockingDeque<SubmitSm> deque = new LinkedBlockingDeque<>(requests);

            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);

            when(handler.getSessionHandler()).thenReturn(smppRequestsQueue);
            when(smppRequestsQueue.getSubmitSms()).thenReturn(deque);

            assertEquals(requests, server.getSubmitSmMessages());
        }
    }

    @Nested
    class GetCancelSmMessages {
        @Test
        void shouldReturnExpectedList() {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            SmppRequestsQueue smppRequestsQueue = mock(SmppRequestsQueue.class);
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            List<CancelSm> requests = Arrays.asList(
                    mock(CancelSm.class), mock(CancelSm.class)
            );
            BlockingDeque<CancelSm> deque = new LinkedBlockingDeque<>(requests);

            MockSmppServer server = new MockSmppServer(port, systemId, password, handler, defaultSmppServer);

            when(handler.getSessionHandler()).thenReturn(smppRequestsQueue);
            when(smppRequestsQueue.getCancelSms()).thenReturn(deque);

            assertEquals(requests, server.getCancelSmMessages());
        }
    }
}
