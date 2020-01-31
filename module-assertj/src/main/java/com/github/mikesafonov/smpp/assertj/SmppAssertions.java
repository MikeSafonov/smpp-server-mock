package com.github.mikesafonov.smpp.assertj;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.github.mikesafonov.smpp.server.MockSmppServer;
import com.github.mikesafonov.smpp.server.MockSmppServerHolder;
import org.assertj.core.api.Assertions;

import java.util.List;

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

    public static SubmitSmListAssert assertThatSubmit(List<SubmitSm> submitSms) {
        return new SubmitSmListAssert(submitSms);
    }

    public static CancelSmListAssert assertThatCancel(List<CancelSm> cancelSms) {
        return new CancelSmListAssert(cancelSms);
    }
}
