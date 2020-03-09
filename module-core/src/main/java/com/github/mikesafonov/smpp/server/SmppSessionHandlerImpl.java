package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.PduAsyncResponse;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.pdu.*;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

import static com.cloudhopper.commons.util.RandomUtil.generateString;

@Slf4j
@EqualsAndHashCode
@AllArgsConstructor
public class SmppSessionHandlerImpl implements SmppSessionHandler {
    private final SmppRequestsQueue smppRequestsQueue;
    private final DeliverSmGenerator deliverSmGenerator;
    private final ResponseSessionHolder responseSessionHolder;

    @Override
    public void fireChannelUnexpectedlyClosed() {
        // nothing
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        smppRequestsQueue.getReceivedPduRequests().add(pduRequest);
        if (pduRequest instanceof SubmitSm) {
            SubmitSm submitSm = (SubmitSm) pduRequest;
            smppRequestsQueue.getSubmitSms().add(submitSm);
            SubmitSmResp response = submitSm.createResponse();
            String messageId = generateString(10);
            response.setMessageId(messageId);
            if (isRegisteredDeliveryReport(submitSm)) {
                sendDeliverySm(submitSm, messageId);
            }
            return response;
        }
        if (pduRequest instanceof CancelSm) {
            smppRequestsQueue.getCancelSms().add((CancelSm) pduRequest);
        }
        return pduRequest.createResponse();
    }

    @Override
    public void firePduRequestExpired(PduRequest pduRequest) {
        // nothing
    }

    @Override
    public void fireExpectedPduResponseReceived(PduAsyncResponse pduAsyncResponse) {
        // nothing
    }

    @Override
    public void fireUnexpectedPduResponseReceived(PduResponse pduResponse) {
        // nothing
    }

    @Override
    public void fireUnrecoverablePduException(UnrecoverablePduException e) {
        log.error(e.getMessage(), e);
    }

    @Override
    public void fireRecoverablePduException(RecoverablePduException e) {
        log.error(e.getMessage(), e);
    }

    @Override
    public void fireUnknownThrowable(Throwable t) {
        log.error(t.getMessage(), t);
    }

    @Override
    public String lookupResultMessage(int commandStatus) {
        return null;
    }

    @Override
    public String lookupTlvTagName(short tag) {
        return null;
    }


    private boolean isRegisteredDeliveryReport(SubmitSm submitSm) {
        return submitSm.getRegisteredDelivery() == SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED;
    }

    private void sendDeliverySm(SubmitSm submitSm, String messageId) {
        if(responseSessionHolder.getSessions().isEmpty()){
            return;
        }
        DeliverSm deliverSm = deliverSmGenerator.generate(submitSm, messageId);
        responseSessionHolder.getSessions().forEach(s -> CompletableFuture.runAsync(() -> {
            try {
                s.sendRequestPdu(deliverSm, 1000, true);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }));
    }
}
