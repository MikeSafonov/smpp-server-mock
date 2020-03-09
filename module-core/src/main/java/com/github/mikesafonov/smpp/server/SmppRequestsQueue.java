package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.SubmitSm;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Holder for all incoming SMPP requests
 *
 * @author Mike Safonov
 */
@Getter
@EqualsAndHashCode
public class SmppRequestsQueue {

    private final BlockingQueue<PduRequest> receivedPduRequests;
    private final BlockingQueue<SubmitSm> submitSms;
    private final BlockingQueue<CancelSm> cancelSms;

    public SmppRequestsQueue() {
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
}
