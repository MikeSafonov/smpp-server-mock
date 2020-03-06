package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.PduAsyncResponse;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.pdu.*;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.cloudhopper.commons.util.RandomUtil.generateString;

/**
 * {@link SmppSessionHandler} for {@link MockSmppServer}.
 *
 * @author Mike Safonov
 */
@Slf4j
@Getter
public class QueueSmppSessionHandler implements SmppSessionHandler {

    private final BlockingQueue<PduRequest> receivedPduRequests;
    private final BlockingQueue<SubmitSm> submitSms;
    private final BlockingQueue<CancelSm> cancelSms;

    public QueueSmppSessionHandler() {
        this.receivedPduRequests = new LinkedBlockingQueue<>();
        this.submitSms = new LinkedBlockingQueue<>();
        this.cancelSms = new LinkedBlockingQueue<>();
    }

    public int countTotalMessages() {
        return receivedPduRequests.size();
    }

    public void clear(){
        receivedPduRequests.clear();
        submitSms.clear();
        cancelSms.clear();
    }

    @Override
    public void fireChannelUnexpectedlyClosed() {
        // nothing
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        receivedPduRequests.add(pduRequest);
        if (pduRequest instanceof SubmitSm) {
            SubmitSm submitSm = (SubmitSm) pduRequest;
            submitSms.add(submitSm);
            SubmitSmResp response = submitSm.createResponse();
            response.setMessageId(generateString(10));
            return response;
        }
        if (pduRequest instanceof CancelSm) {
            cancelSms.add((CancelSm) pduRequest);
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
}
