package com.github.mikesafonov.smpp.assertj;

import com.cloudhopper.smpp.pdu.CancelSm;
import org.assertj.core.api.AbstractAssert;

import java.util.List;

public class CancelSmListAssert extends AbstractAssert<CancelSmListAssert, List<CancelSm>> {
    public CancelSmListAssert(List<CancelSm> cancelSm) {
        super(cancelSm, CancelSmListAssert.class);
    }

    public CancelSmListAssert hasSize(int size) {
        isNotNull();
        int actualSize = actual.size();
        if (actualSize != size) {
            failWithMessage("Expected size to be <%s> but actual <%s>",
                    size, actualSize);
        }
        return this;
    }

    public CancelSmListAssert isEmpty() {
        isNotNull();
        if (!actual.isEmpty()) {
            failWithMessage(MessageUtils.cancelSmShouldBeEmpty(actual));
        }
        return this;
    }

    public CancelSmListAssert containsId(String smscId) {
        isNotNull();
        if (!containByMessageId(smscId)) {
            failWithMessage("Expected at least one message with id <%s> but no one find",
                    smscId);
        }
        return this;
    }

    public CancelSmListAssert containsSource(String source) {
        isNotNull();
        if (!containBySourceAddress(source)) {
            failWithMessage("Expected at least one message with source address <%s> but no one find",
                    source);
        }
        return this;
    }

    public CancelSmListAssert containsDest(String dest) {
        isNotNull();
        if (!containByDestAddress(dest)) {
            failWithMessage("Expected at least one message with dest address <%s> but no one find",
                    dest);
        }
        return this;
    }

    private boolean containByMessageId(String messageId) {
        return actual.stream()
                .anyMatch(cancelSm -> cancelSm.getMessageId().equals(messageId));
    }

    private boolean containBySourceAddress(String sourceAddress) {
        return actual.stream()
                .anyMatch(cancelSm -> cancelSm.getSourceAddress().getAddress().equals(sourceAddress));
    }

    private boolean containByDestAddress(String destAddress) {
        return actual.stream()
                .anyMatch(cancelSm -> cancelSm.getDestAddress().getAddress().equals(destAddress));
    }

}
