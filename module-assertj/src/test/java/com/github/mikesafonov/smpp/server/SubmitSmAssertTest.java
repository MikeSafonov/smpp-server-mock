package com.github.mikesafonov.smpp.server;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.SmppInvalidArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mike Safonov
 */
class SubmitSmAssertTest {

    String destAddress = "destination";
    String sourceAddress = "source";
    byte esmClass = (byte) 1;
    String text = "messageText";
    SubmitSmAssert submitSmAssert = SmppAssertions.assertThat(submitSm());

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

    private SubmitSm submitSmWithReport() {
        SubmitSm submitSm = submitSm();
        submitSm.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
        return submitSm;
    }

    @Test
    void shouldSuccessAssert() {
        submitSmAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass(esmClass)
                .doesNotHaveDeliveryReport()
                .hasSource(sourceAddress);

    }

    @Test
    void shouldSuccessAssertWithDeliveryReport() {
        new SubmitSmAssert(submitSmWithReport())
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass(esmClass)
                .hasDeliveryReport()
                .hasSource(sourceAddress);

    }

    @Test
    void shouldFailOnText() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest(destAddress)
                .hasText("text2")
                .hasEsmClass(esmClass)
                .doesNotHaveDeliveryReport()
                .hasSource(sourceAddress));
        assertEquals("Expected text <text2> but was <messageText>", assertionError.getMessage());
    }

    @Test
    void shouldFailOnSourceAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass(esmClass)
                .doesNotHaveDeliveryReport()
                .hasSource("otherSource"));
        assertEquals("Expected source address <otherSource> but was <source>", assertionError.getMessage());
    }

    @Test
    void shouldFailOnDestAddress() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest("otherDest")
                .hasText(text)
                .hasEsmClass(esmClass)
                .doesNotHaveDeliveryReport()
                .hasSource(sourceAddress));
        assertEquals("Expected dest address <otherDest> but was <destination>", assertionError.getMessage());
    }

    @Test
    void shouldFailOnEsmClass() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass((byte) 2)
                .doesNotHaveDeliveryReport()
                .hasSource(sourceAddress));
        assertEquals("Expected esm class <2> but was <1>", assertionError.getMessage());
    }

    @Test
    void shouldFailOnNotDeliveryReport() {
        SubmitSm submitSm = submitSm();
        submitSm.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
        SubmitSmAssert smAssert = new SubmitSmAssert(submitSm);
        AssertionError assertionError = assertThrows(AssertionError.class, () -> smAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass(esmClass)
                .hasSource(sourceAddress)
                .doesNotHaveDeliveryReport());
        assertEquals("Not expected registered delivery <1>", assertionError.getMessage());
    }

    @Test
    void shouldFailOnDeliveryReport() {
        AssertionError assertionError = assertThrows(AssertionError.class, () -> submitSmAssert
                .hasDest(destAddress)
                .hasText(text)
                .hasEsmClass(esmClass)
                .hasDeliveryReport()
                .hasSource(sourceAddress));
        assertEquals("Expected registered delivery <1> but was <0>", assertionError.getMessage());
    }
}
