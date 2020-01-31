package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.type.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mike Safonov
 */
class CancelSmAssertTest {

    String messageId = "messageId";
    String destAddress = "destination";
    String sourceAddress = "source";
    CancelSmAssert cancelSmAssert = new CancelSmAssert(cancelSm());

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
        assertEquals("Expected id <otherId> but was <messageId>", assertionError.getMessage());
    }

    @Test
    void shouldFailOnSourceAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .hasId(messageId)
                .hasDest(destAddress)
                .hasSource("otherSource"));
        assertEquals("Expected source address <otherSource> but was <source>", assertionError.getMessage());
    }

    @Test
    void shouldFailOnDestAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .hasId(messageId)
                .hasDest("otherDest")
                .hasSource(sourceAddress));
        assertEquals("Expected dest address <otherDest> but was <destination>", assertionError.getMessage());
    }
}
