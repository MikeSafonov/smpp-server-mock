package com.github.mikesafonov.smpp.server;

import com.cloudhopper.commons.charset.Charset;
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.assertj.core.api.AbstractAssert;

/**
 * Assert for {@link SubmitSm}
 *
 * @author Mike Safonov
 */
public class SubmitSmAssert extends AbstractAssert<SubmitSmAssert, SubmitSm> {
    public SubmitSmAssert(SubmitSm submitSm) {
        super(submitSm, SubmitSmAssert.class);
    }

    public SubmitSmAssert hasEsmClass(byte esmClass) {
        isNotNull();
        byte actualEsmClass = actual.getEsmClass();
        if (actualEsmClass != esmClass) {
            failWithMessage("Expected esm class <%s> but was <%s>",
                    esmClass, actualEsmClass);
        }
        return this;
    }

    public SubmitSmAssert hasSource(String source) {
        isNotNull();
        String address = actual.getSourceAddress().getAddress();
        if (!source.equals(address)) {
            failWithMessage("Expected source address <%s> but was <%s>", source, address);
        }
        return this;
    }

    public SubmitSmAssert hasDest(String dest) {
        isNotNull();
        String address = actual.getDestAddress().getAddress();
        if (!dest.equals(address)) {
            failWithMessage("Expected dest address <%s> but was <%s>", dest, address);
        }
        return this;
    }

    public SubmitSmAssert hasText(String text) {
        return hasTextWithCharset(text, CharsetUtil.CHARSET_GSM);

    }

    public SubmitSmAssert hasTextWithCharset(String text, Charset charset) {
        isNotNull();
        String messageText = SubmitSmCharsetUtil.toText(actual, charset);
        if (!text.equals(messageText)) {
            failWithMessage("Expected text <%s> but was <%s>", text, messageText);
        }
        return this;
    }

    public SubmitSmAssert hasDeliveryReport() {
        isNotNull();
        byte registeredDelivery = actual.getRegisteredDelivery();
        if (registeredDelivery != SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED) {
            failWithMessage("Expected registered delivery <%s> but was <%s>",
                    SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED, registeredDelivery);
        }
        return this;
    }

    public SubmitSmAssert doesNotHaveDeliveryReport() {
        isNotNull();
        if (actual.getRegisteredDelivery() == SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED) {
            failWithMessage("Not expected registered delivery <%s>",
                    SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
        }
        return this;
    }
}
