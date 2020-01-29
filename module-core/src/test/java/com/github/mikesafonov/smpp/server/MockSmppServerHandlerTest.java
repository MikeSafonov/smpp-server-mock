package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.BindReceiver;
import com.cloudhopper.smpp.pdu.BindReceiverResp;
import com.cloudhopper.smpp.type.SmppProcessingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Mike Safonov
 */
class MockSmppServerHandlerTest {
    @Nested
    class BindRequested {
        @Test
        void shouldThrowExceptionWhenSystemIdNotMatch() {
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServerHandler handler = new MockSmppServerHandler(systemId, password);

            SmppSessionConfiguration configuration = new SmppSessionConfiguration(SmppBindType.RECEIVER, "anothreId", password);

            assertThrows(SmppProcessingException.class, () -> handler.sessionBindRequested(0L, configuration, new BindReceiver()));
        }

        @Test
        void shouldThrowExceptionWhenPasswordNotMatch() {
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServerHandler handler = new MockSmppServerHandler(systemId, password);

            SmppSessionConfiguration configuration = new SmppSessionConfiguration(SmppBindType.RECEIVER, systemId,
                    "anotherPassword");

            assertThrows(SmppProcessingException.class, () -> handler.sessionBindRequested(0L, configuration, new BindReceiver()));
        }
    }

    @Nested
    class SessionCreated {
        @Test
        void shouldCreateSessionAndConfigure() {
            String systemId = "customId";
            String password = "customPassword";
            QueueSmppSessionHandler sessionHandler = mock(QueueSmppSessionHandler.class);
            MockSmppServerHandler handler = new MockSmppServerHandler(systemId, password, sessionHandler);
            SmppServerSession session = mock(SmppServerSession.class);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            verify(session).serverReady(sessionHandler);
        }

        @Test
        void shouldCreateSessionAndSave() {
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServerHandler handler = new MockSmppServerHandler(systemId, password);
            SmppServerSession session = mock(SmppServerSession.class);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            assertEquals(1, handler.getSessions().size());
            assertTrue(handler.getSessions().contains(session));
        }
    }

    @Nested
    class SessionDestroyed {
        @Test
        void shouldDestroySession() {
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServerHandler handler = new MockSmppServerHandler(systemId, password);
            SmppServerSession session = mock(SmppServerSession.class);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            handler.sessionDestroyed(0L, session);

            assertFalse(handler.getSessions().contains(session));
        }
    }
}
