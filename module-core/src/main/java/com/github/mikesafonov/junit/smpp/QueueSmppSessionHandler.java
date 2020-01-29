package com.github.mikesafonov.junit.smpp;

import com.cloudhopper.smpp.PduAsyncResponse;
import com.cloudhopper.smpp.SmppSessionHandler;
import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * {@link SmppSessionHandler} for {@link MockSmppServer}.
 *
 * @author Mike Safonov
 */
public class QueueSmppSessionHandler implements SmppSessionHandler {

    private final BlockingQueue<PduRequest> receivedPduRequests;
    private final BlockingQueue<SubmitSm> submitSms;
    private final BlockingQueue<CancelSm> cancelSms;

    public QueueSmppSessionHandler() {
        this.receivedPduRequests = new LinkedBlockingQueue<>();
        this.submitSms = new LinkedBlockingQueue<>();
        this.cancelSms = new LinkedBlockingQueue<>();
    }

    public BlockingQueue<PduRequest> getReceivedPduRequests() {
        return receivedPduRequests;
    }

    public BlockingQueue<SubmitSm> getSubmitSms() {
        return submitSms;
    }

    public BlockingQueue<CancelSm> getCancelSms() {
        return cancelSms;
    }

    public int countTotalMessages() {
        return receivedPduRequests.size();
    }

    @Override
    public void fireChannelUnexpectedlyClosed() {
        // nothing
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        receivedPduRequests.add(pduRequest);
        if (pduRequest instanceof SubmitSm) {
            submitSms.add((SubmitSm) pduRequest);
        }
        if (pduRequest instanceof CancelSm) {
            cancelSms.add((CancelSm) pduRequest);
        }
        return pduRequest.createResponse();
    }

    @Override
    public void firePduRequestExpired(PduRequest pduRequest) {
    }

    @Override
    public void fireExpectedPduResponseReceived(PduAsyncResponse pduAsyncResponse) {
    }

    @Override
    public void fireUnexpectedPduResponseReceived(PduResponse pduResponse) {
    }

    @Override
    public void fireUnrecoverablePduException(UnrecoverablePduException e) {
        e.printStackTrace();
    }

    @Override
    public void fireRecoverablePduException(RecoverablePduException e) {
        e.printStackTrace();
    }

    @Override
    public void fireUnknownThrowable(Throwable t) {
        t.printStackTrace();
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
