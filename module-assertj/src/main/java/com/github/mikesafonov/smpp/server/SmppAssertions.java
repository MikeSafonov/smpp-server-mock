package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.assertj.core.api.Assertions;

public class SmppAssertions extends Assertions {
    public static SmppAssert assertThat(MockSmppServer actual) {
        return new SmppAssert(actual);
    }

    public static SubmitSmAssert assertThat(SubmitSm submitSm) {
        return new SubmitSmAssert(submitSm);
    }

    public static CancelSmAssert assertThat(CancelSm cancelSm) {
        return new CancelSmAssert(cancelSm);
    }
}
