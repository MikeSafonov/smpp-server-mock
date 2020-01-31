package com.github.mikesafonov.smpp.assertj;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.type.Address;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mike Safonov
 */
class CancelSmListAssertTest {
    String messageId = "messageId";
    String destAddress = "destination";
    String sourceAddress = "source";
    CancelSmListAssert cancelSmAssert = new CancelSmListAssert(cancelSmList());

    private List<CancelSm> cancelSmList() {
        return Arrays.asList(
                cancelSm(),
                cancelSm()
        );
    }

    private CancelSm cancelSm() {
        CancelSm cancelSm = new CancelSm();
        cancelSm.setMessageId(messageId);
        cancelSm.setDestAddress(new Address((byte) 0, (byte) 0, destAddress));
        cancelSm.setSourceAddress(new Address((byte) 0, (byte) 0, sourceAddress));
        return cancelSm;
    }

    @Test
    void shouldSuccessAssert() {
        cancelSmAssert
                .hasDest(destAddress)
                .hasId(messageId)
                .hasSource(sourceAddress);
    }

    @Test
    void shouldFailOnMessageId() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .hasId("otherId")
                .hasDest(destAddress)
                .hasSource(sourceAddress));
        assertEquals("Expected at least one message with id <otherId> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnSourceAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .hasId(messageId)
                .hasDest(destAddress)
                .hasSource("otherSource"));
        assertEquals("Expected at least one message with source address <otherSource> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnDestAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .hasId(messageId)
                .hasSource(sourceAddress)
                .hasDest("otherDest"));
        assertEquals("Expected at least one message with dest address <otherDest> but no one find", assertionError.getMessage());
    }
}
