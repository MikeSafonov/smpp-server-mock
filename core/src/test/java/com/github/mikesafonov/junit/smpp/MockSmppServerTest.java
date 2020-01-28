package com.github.mikesafonov.junit.smpp;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.type.SmppChannelException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

            assertEquals(2077, server.getPort());
            assertEquals("mockSmppServer", server.getSystemId());
        }

        @Test
        void shouldCreateWithDefaultSystemId() {
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port);

            assertEquals(port, server.getPort());
            assertEquals("mockSmppServer", server.getSystemId());
        }

        @Test
        void shouldCreateWithDefaultPort() {
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServer server = new MockSmppServer(systemId, password);

            assertEquals(2077, server.getPort());
            assertEquals(systemId, server.getSystemId());
        }

        @Test
        void shouldCreateWithCustomPortAndSystemId() {
            String systemId = "customId";
            String password = "customPassword";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, password);

            assertEquals(port, server.getPort());
            assertEquals(systemId, server.getSystemId());
        }
    }

    @Nested
    class Start {
        @Test
        void shouldCallStartOnSmppServer() throws SmppChannelException {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            String systemId = "customId";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, handler, defaultSmppServer);
            server.start();

            verify(defaultSmppServer, times(1)).start();
        }

        @Test
        void shouldCallStartOnSmppServerOnce() throws SmppChannelException {
            DefaultSmppServer defaultSmppServer = mock(DefaultSmppServer.class);
            MockSmppServerHandler handler = mock(MockSmppServerHandler.class);
            String systemId = "customId";
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, handler, defaultSmppServer);
            server.start();
            server.start();
            server.start();

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
            int port = 3000;
            MockSmppServer server = new MockSmppServer(port, systemId, handler, defaultSmppServer);
            server.stop();

            verify(defaultSmppServer, times(1)).stop();
        }
    }
}
