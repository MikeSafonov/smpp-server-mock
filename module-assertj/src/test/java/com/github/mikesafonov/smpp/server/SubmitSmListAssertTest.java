package com.github.mikesafonov.smpp.server;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.SmppInvalidArgumentException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mike Safonov
 */
class SubmitSmListAssertTest {

    String destAddress = "destination";
    String sourceAddress = "source";
    byte esmClass = (byte) 1;
    String text = "messageText";
    SubmitSmListAssert submitSmAssert = new SubmitSmListAssert(submitSmList());

    private List<SubmitSm> submitSmList() {
        return Arrays.asList(
                submitSm(),
                submitSm()
        );
    }

    private SubmitSm submitSm() {
        try {
            SubmitSm submitSm = new SubmitSm();
            submitSm.setEsmClass(esmClass);
            submitSm.setShortMessage(CharsetUtil.encode(text, CharsetUtil.CHARSET_GSM));
            submitSm.setDestAddress(new Address((byte) 0, (byte) 0, destAddress));
            submitSm.setSourceAddress(new Address((byte) 0, (byte) 0, sourceAddress));
            return submitSm;
        } catch (SmppInvalidArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldSuccessAssert() {
        submitSmAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass(esmClass)
                .hasSource(sourceAddress);

    }

    @Test
    void shouldFailOnText() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest(destAddress)
                .hasText("text2")
                .hasEsmClass(esmClass)
                .hasSource(sourceAddress));
        assertEquals("Expected at least one message with text <text2> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnSourceAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass(esmClass)
                .hasSource("otherSource"));
        assertEquals("Expected at least one message with source address <otherSource> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnDestAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest("otherDest")
                .hasText(text)
                .hasEsmClass(esmClass)
                .hasSource(sourceAddress));
        assertEquals("Expected at least one message with dest address <otherDest> but no one find", assertionError.getMessage());
    }

    @Test
    void shouldFailOnEsmClass() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass((byte) 2)
                .hasSource(sourceAddress));
        assertEquals("Expected at least one message with esm class <2> but no one find", assertionError.getMessage());
    }
}
