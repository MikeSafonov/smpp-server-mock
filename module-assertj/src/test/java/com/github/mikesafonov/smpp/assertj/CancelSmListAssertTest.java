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
    CancelSmListAssert cancelSmAssert = SmppAssertions.assertThatCancel(cancelSmList());

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
                .containsDest(destAddress)
                .containsId(messageId)
                .hasSize(2)
                .containsSource(sourceAddress);
    }

    @Test
    void shouldFailOnMessageId() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .containsId("otherId")
                .containsDest(destAddress)
                .containsSource(sourceAddress));
        assertEquals("Expected at least one message with id <otherId> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnSourceAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .containsId(messageId)
                .containsDest(destAddress)
                .containsSource("otherSource"));
        assertEquals("Expected at least one message with source address <otherSource> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnDestAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .containsId(messageId)
                .containsSource(sourceAddress)
                .containsDest("otherDest"));
        assertEquals("Expected at least one message with dest address <otherDest> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnSize() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> cancelSmAssert
                .containsDest(destAddress)
                .containsId(messageId)
                .hasSize(3)
                .containsSource(sourceAddress));
        assertEquals("Expected size to be <3> but actual <2>",
                assertionError.getMessage());
    }
}
