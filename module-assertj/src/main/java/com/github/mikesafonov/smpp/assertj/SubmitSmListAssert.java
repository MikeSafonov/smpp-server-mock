package com.github.mikesafonov.smpp.assertj;

import com.cloudhopper.commons.charset.Charset;
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.assertj.core.api.AbstractAssert;

import java.util.List;

/**
 * @author Mike Safonov
 */
public class SubmitSmListAssert extends AbstractAssert<SubmitSmListAssert, List<SubmitSm>> {
    public SubmitSmListAssert(List<SubmitSm> submitSms) {
        super(submitSms, SubmitSmListAssert.class);
    }

    public SubmitSmListAssert containsEsmClass(byte esmClass) {
        isNotNull();
        if (!containByEsmClass(esmClass)) {
            failWithMessage("Expected at least one message with esm class <%s> but no one find",
                    esmClass);
        }
        return this;
    }

    public SubmitSmListAssert containsSource(String source) {
        isNotNull();
        if (!containBySourceAddress(source)) {
            failWithMessage("Expected at least one message with source address <%s> but no one find",
                    source);
        }
        return this;
    }

    public SubmitSmListAssert containsDest(String dest) {
        isNotNull();
        if (!containByDestAddress(dest)) {
            failWithMessage("Expected at least one message with dest address <%s> but no one find",
                    dest);
        }
        return this;
    }

    public SubmitSmListAssert containsText(String text) {
        return containsTextWithCharset(text, CharsetUtil.CHARSET_GSM);

    }

    public SubmitSmListAssert containsTextWithCharset(String text, Charset charset) {
        isNotNull();
        if (!containByText(text, charset)) {
            failWithMessage("Expected at least one message with text <%s> but no one find", text);
        }
        return this;
    }

    private boolean containByEsmClass(byte esmClass) {
        return actual.stream()
                .anyMatch(submitSm -> submitSm.getEsmClass() == esmClass);
    }

    private boolean containBySourceAddress(String sourceAddress) {
        return actual.stream()
                .anyMatch(submitSm -> submitSm.getSourceAddress().getAddress().equals(sourceAddress));
    }

    private boolean containByDestAddress(String destAddress) {
        return actual.stream()
                .anyMatch(submitSm -> submitSm.getDestAddress().getAddress().equals(destAddress));
    }


    private boolean containByText(String text, Charset charset) {
        return actual.stream()
                .anyMatch(submitSm -> {
                    String messageText = SubmitSmCharsetUtil.toText(submitSm, charset);
                    return text.equals(messageText);
                });
    }
}
