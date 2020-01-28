package com.github.mikesafonov.junit.smpp;

import com.cloudhopper.smpp.pdu.BindReceiver;
import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Safonov
 */
class QueueSmppSessionHandlerTest {
    @Nested
    class FirePduRequest {
        @Test
        void shouldAddPduRequest() throws InterruptedException {
            QueueSmppSessionHandler handler = new QueueSmppSessionHandler();
            BindReceiver bindReceiver = new BindReceiver();

            PduResponse pduResponse = handler.firePduRequestReceived(bindReceiver);

            assertThat(pduResponse).isEqualToComparingFieldByField(bindReceiver.createResponse());
            assertEquals(1, handler.getReceivedPduRequests().size());
            assertEquals(0, handler.getSubmitSms().size());
            assertEquals(0, handler.getCancelSms().size());
            assertEquals(bindReceiver, handler.getReceivedPduRequests().take());
        }
    }

    @Nested
    class FireSubmitSm {
        @Test
        void shouldAddSubmitSm() throws InterruptedException {
            QueueSmppSessionHandler handler = new QueueSmppSessionHandler();
            SubmitSm submitSm = new SubmitSm();

            PduResponse pduResponse = handler.firePduRequestReceived(submitSm);

            assertThat(pduResponse).isEqualToComparingFieldByField(submitSm.createResponse());
            assertEquals(1, handler.getReceivedPduRequests().size());
            assertEquals(1, handler.getSubmitSms().size());
            assertEquals(0, handler.getCancelSms().size());
            assertEquals(submitSm, handler.getSubmitSms().take());
        }
    }

    @Nested
    class FireCancelSm {
        @Test
        void shouldAddSubmitSm() throws InterruptedException {
            QueueSmppSessionHandler handler = new QueueSmppSessionHandler();
            CancelSm cancelSm = new CancelSm();

            PduResponse pduResponse = handler.firePduRequestReceived(cancelSm);

            assertThat(pduResponse).isEqualToComparingFieldByField(cancelSm.createResponse());
            assertEquals(1, handler.getReceivedPduRequests().size());
            assertEquals(1, handler.getCancelSms().size());
            assertEquals(0, handler.getSubmitSms().size());
            assertEquals(cancelSm, handler.getCancelSms().take());
        }
    }
}
