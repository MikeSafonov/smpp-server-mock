package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import org.assertj.core.api.Assertions;

public class SmppAssertions extends Assertions {
    public static MockSmppServerHolderAssert assertThat(MockSmppServerHolder actual) {
        return new MockSmppServerHolderAssert(actual);
    }

    public static MockSmppServerAssert assertThat(MockSmppServer actual) {
        return new MockSmppServerAssert(actual);
    }

    public static SubmitSmAssert assertThat(SubmitSm submitSm) {
        return new SubmitSmAssert(submitSm);
    }

    public static CancelSmAssert assertThat(CancelSm cancelSm) {
        return new CancelSmAssert(cancelSm);
    }
}
