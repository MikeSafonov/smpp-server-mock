package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.assertj.core.api.AbstractAssert;

import java.util.List;

public class SmppAssert extends AbstractAssert<SmppAssert, MockSmppServer> {
    public SmppAssert(MockSmppServer mockSmppServer) {
        super(mockSmppServer, SmppAssert.class);
    }

    public SmppAssert receiveRequestsCount(int size) {
        isNotNull();
        checkMessagesCount(actual.getRequests(), size);
        return this;
    }

    public SubmitSmAssert hasSingleMessage() {
        return new SubmitSmAssert(checkAndGetSubmitSm());
    }

    public CancelSmAssert hasSingleCancelMessage() {
        return new CancelSmAssert(checkAndGetCancelSm());
    }

    public PduRequestListAssert requests() {
        return new PduRequestListAssert(actual.getRequests());
    }


    private void checkMessagesCount(List<? extends PduRequest> requests, int expectedCount) {
        if (requests.size() != expectedCount) {
            failWithMessage("Expected messages size to be <%s> but was <%s>", expectedCount, requests.size());
        }
    }

    private SubmitSm checkAndGetSubmitSm() {
        isNotNull();
        List<SubmitSm> submitSmMessages = actual.getSubmitSmMessages();
        checkMessagesCount(submitSmMessages, 1);
        return submitSmMessages.get(0);
    }

    private CancelSm checkAndGetCancelSm() {
        isNotNull();
        List<CancelSm> cancelSmMessages = actual.getCancelSmMessages();
        checkMessagesCount(cancelSmMessages, 1);
        return cancelSmMessages.get(0);
    }
}
