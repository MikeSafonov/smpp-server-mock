package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.BindReceiver;
import com.cloudhopper.smpp.pdu.BindReceiverResp;
import com.cloudhopper.smpp.type.SmppProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

            SmppSessionConfiguration configuration =
                    new SmppSessionConfiguration(SmppBindType.RECEIVER, systemId, password);

            BindReceiver bindReceiver = new BindReceiver();
            bindReceiver.setPassword(password);
            bindReceiver.setSystemId("anothreId");
            assertThrows(SmppProcessingException.class,
                    () -> handler.sessionBindRequested(0L, configuration, bindReceiver));
        }

        @Test
        void shouldThrowExceptionWhenPasswordNotMatch() {
            String systemId = "customId";
            String password = "customPassword";
            MockSmppServerHandler handler = new MockSmppServerHandler(systemId, password);

            SmppSessionConfiguration configuration = new SmppSessionConfiguration(SmppBindType.RECEIVER, systemId,
                    password);
            BindReceiver bindReceiver = new BindReceiver();
            bindReceiver.setPassword("anotherPassword");
            bindReceiver.setSystemId(systemId);
            assertThrows(SmppProcessingException.class,
                    () -> handler.sessionBindRequested(0L, configuration, bindReceiver));
        }
    }

    @Nested
    class SessionCreated {
        private String systemId = "customId";
        private String password = "customPassword";
        private SmppRequestsQueue sessionHandler;
        private ResponseSessionHolder responseSessionHolder;
        private MockSmppServerHandler handler;
        private SmppServerSession session;
        private SmppSessionConfiguration configuration;

        @BeforeEach
        void setUp() {
            sessionHandler = mock(SmppRequestsQueue.class);
            responseSessionHolder = mock(ResponseSessionHolder.class);
            handler = new MockSmppServerHandler(systemId, password, sessionHandler,
                    responseSessionHolder);
            session = mock(SmppServerSession.class);
            configuration = mock(SmppSessionConfiguration.class);

            when(session.getConfiguration()).thenReturn(configuration);
        }

        @Test
        void shouldCreateReceiverSessionAndConfigure() {
            DeliverSmGenerator generator = new DefaultDeliverSmGenerator();

            when(configuration.getType()).thenReturn(SmppBindType.RECEIVER);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            SmppSessionHandlerImpl expectedHandler = new SmppSessionHandlerImpl(sessionHandler, generator,
                    new ResponseSessionHolder());
            ArgumentCaptor<SmppSessionHandlerImpl> captor = ArgumentCaptor.forClass(SmppSessionHandlerImpl.class);
            verify(session).serverReady(captor.capture());

            SmppSessionHandlerImpl handler = captor.getValue();
            assertThat(handler).isEqualToIgnoringGivenFields(expectedHandler, "responseSessionHolder");
        }

        @Test
        void shouldCreateTransceiverSessionAndConfigure() {
            DeliverSmGenerator generator = new DefaultDeliverSmGenerator();

            when(configuration.getType()).thenReturn(SmppBindType.TRANSCEIVER);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            SmppSessionHandlerImpl expectedHandler = new SmppSessionHandlerImpl(sessionHandler, generator,
                    new ResponseSessionHolder());
            ArgumentCaptor<SmppSessionHandlerImpl> captor = ArgumentCaptor.forClass(SmppSessionHandlerImpl.class);
            verify(session).serverReady(captor.capture());

            SmppSessionHandlerImpl handler = captor.getValue();
            assertThat(handler).isEqualToIgnoringGivenFields(expectedHandler, "responseSessionHolder");
        }

        @Test
        void shouldCreateTransmitterSessionAndConfigure() {
            DeliverSmGenerator generator = new DefaultDeliverSmGenerator();

            when(configuration.getType()).thenReturn(SmppBindType.TRANSMITTER);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            verify(session).serverReady(new SmppSessionHandlerImpl(sessionHandler, generator, responseSessionHolder));
        }

        @Test
        void shouldCreateSessionAndSave() {
            when(configuration.getType()).thenReturn(SmppBindType.TRANSMITTER);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            assertEquals(1, handler.getSessions().size());
            assertTrue(handler.getSessions().contains(session));
            verifyNoInteractions(responseSessionHolder);
        }

        @Test
        void shouldCreateSessionAndSaveAndPutToResponseHolder() {
            when(configuration.getType()).thenReturn(SmppBindType.RECEIVER);

            handler.sessionCreated(0L, session, new BindReceiverResp());

            assertEquals(1, handler.getSessions().size());
            assertTrue(handler.getSessions().contains(session));
            verify(responseSessionHolder).add(session);
        }
    }

    @Nested
    class SessionDestroyed {
        @Test
        void shouldDestroySession() {
            String systemId = "customId";
            String password = "customPassword";
            SmppRequestsQueue sessionHandler = mock(SmppRequestsQueue.class);
            ResponseSessionHolder responseSessionHolder = mock(ResponseSessionHolder.class);
            MockSmppServerHandler handler = new MockSmppServerHandler(systemId, password, sessionHandler,
                    responseSessionHolder);
            SmppServerSession session = mock(SmppServerSession.class);
            SmppSessionConfiguration configuration = mock(SmppSessionConfiguration.class);

            when(session.getConfiguration()).thenReturn(configuration);
            when(configuration.getType()).thenReturn(SmppBindType.RECEIVER);
            handler.sessionCreated(0L, session, new BindReceiverResp());

            handler.sessionDestroyed(0L, session);

            assertFalse(handler.getSessions().contains(session));
            verify(session).destroy();
            verify(responseSessionHolder).remove(session);
        }
    }
}
