package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.pdu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Safonov
 */
class SmppSessionHandlerImplTest {
    private SmppRequestsQueue queue;
    private ResponseSessionHolder responseSessionHolder;
    private DeliverSmGenerator deliverSmGenerator;
    private SmppSessionHandlerImpl sessionHandler;

    @BeforeEach
    void setUp() {
        queue = new SmppRequestsQueue();
        deliverSmGenerator = mock(DeliverSmGenerator.class);
        responseSessionHolder = mock(ResponseSessionHolder.class);
        sessionHandler = new SmppSessionHandlerImpl(queue, deliverSmGenerator, responseSessionHolder);
    }

    @Nested
    class FirePduRequest {
        @Test
        void shouldAddPduRequest() throws InterruptedException {
            BindReceiver bindReceiver = new BindReceiver();

            PduResponse pduResponse = sessionHandler.firePduRequestReceived(bindReceiver);

            assertThat(pduResponse).isEqualToComparingFieldByField(bindReceiver.createResponse());
            assertEquals(1, queue.getReceivedPduRequests().size());
            assertEquals(0, queue.getSubmitSms().size());
            assertEquals(0, queue.getCancelSms().size());
            assertEquals(1, queue.countTotalMessages());
            assertEquals(bindReceiver, queue.getReceivedPduRequests().take());
        }
    }

    @Nested
    class FireSubmitSm {
        @Test
        void shouldAddSubmitSm() throws InterruptedException {
            SubmitSm submitSm = new SubmitSm();

            PduResponse pduResponse = sessionHandler.firePduRequestReceived(submitSm);

            assertEquals(1, queue.getReceivedPduRequests().size());
            assertEquals(1, queue.getSubmitSms().size());
            assertEquals(0, queue.getCancelSms().size());
            assertEquals(1, queue.countTotalMessages());
            assertEquals(submitSm, queue.getSubmitSms().take());

            SubmitSmResp resp = (SubmitSmResp) pduResponse;
            assertNotNull(resp.getMessageId());
            assertNotEquals("", resp.getMessageId());
        }
    }

    @Nested
    class FireCancelSm {
        @Test
        void shouldAddSubmitSm() throws InterruptedException {
            CancelSm cancelSm = new CancelSm();

            PduResponse pduResponse = sessionHandler.firePduRequestReceived(cancelSm);

            assertThat(pduResponse).isEqualToComparingFieldByField(cancelSm.createResponse());
            assertEquals(1, queue.getReceivedPduRequests().size());
            assertEquals(1, queue.getCancelSms().size());
            assertEquals(0, queue.getSubmitSms().size());
            assertEquals(1, queue.countTotalMessages());
            assertEquals(cancelSm, queue.getCancelSms().take());
        }
    }

    @Nested
    class Clear {
        @Test
        void shouldRemoveAllRequests() {
            BindReceiver bindReceiver = new BindReceiver();
            CancelSm cancelSm = new CancelSm();
            SubmitSm submitSm = new SubmitSm();

            sessionHandler.firePduRequestReceived(bindReceiver);
            sessionHandler.firePduRequestReceived(cancelSm);
            sessionHandler.firePduRequestReceived(submitSm);

            assertEquals(3, queue.getReceivedPduRequests().size());
            assertEquals(1, queue.getCancelSms().size());
            assertEquals(1, queue.getSubmitSms().size());
            assertEquals(3, queue.countTotalMessages());

            queue.clear();

            assertThat(queue.getCancelSms()).isEmpty();
            assertThat(queue.getReceivedPduRequests()).isEmpty();
            assertThat(queue.getSubmitSms()).isEmpty();
        }
    }

    @Nested
    class DeliveryReport {
        private SubmitSm submitSm;

        @BeforeEach
        void setUp() {
            submitSm = new SubmitSm();
            submitSm.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
        }

        @Test
        void shouldNotSendBecauseNoSession() {
            when(responseSessionHolder.getSessions()).thenReturn(Collections.emptyList());

            sessionHandler.firePduRequestReceived(submitSm);
        }

        @Test
        void shouldNotSendBecauseSubmitNotRequiresReport() {
            List<SmppServerSession> sessions = Arrays.asList(
                    mock(SmppServerSession.class),
                    mock(SmppServerSession.class)
            );
            when(responseSessionHolder.getSessions()).thenReturn(sessions);

            SubmitSm sm = new SubmitSm();

            sessionHandler.firePduRequestReceived(sm);

            sessions.forEach(Mockito::verifyNoInteractions);
        }

        @Test
        void shouldSendDeliveryReport() throws Exception {
            List<SmppServerSession> sessions = Arrays.asList(
                    mock(SmppServerSession.class),
                    mock(SmppServerSession.class)
            );
            DeliverSm expected = new DeliverSm();

            when(responseSessionHolder.getSessions()).thenReturn(sessions);
            when(deliverSmGenerator.generate(eq(submitSm), anyString())).thenReturn(expected);

            sessionHandler.firePduRequestReceived(submitSm);

            await().atMost(Duration.ofSeconds(3)).pollInterval(Duration.ofSeconds(1)).untilAsserted(() -> {
                for (SmppServerSession session : sessions) {
                    verify(session).sendRequestPdu(refEq(expected), eq(1000L), eq(true));
                }
            });
        }
    }
}
