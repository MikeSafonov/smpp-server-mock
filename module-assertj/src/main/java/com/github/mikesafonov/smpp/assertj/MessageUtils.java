package com.github.mikesafonov.smpp.assertj;

import com.cloudhopper.smpp.pdu.CancelSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * @author Mike Safonov
 */
@UtilityClass
public class MessageUtils {

    public static String cancelSmShouldBeEmpty(List<CancelSm> cancelSms) {
        StringBuilder builder = new StringBuilder();
        builder.append("Expected to be empty\nActual messages:");
        cancelSms.forEach(cancelSm -> {
            builder.append("\n");
            builder.append(cancelSmToString(cancelSm));
        });
        return builder.toString();
    }

    public static String submitSmShouldBeEmpty(List<SubmitSm> submitSms) {
        StringBuilder builder = new StringBuilder();
        builder.append("Expected to be empty\nActual messages:");
        submitSms.forEach(submitSm -> {
            builder.append("\n");
            builder.append(submitSmsToString(submitSm));
        });
        return builder.toString();
    }

    private static String cancelSmToString(CancelSm cancelSm) {
        return String.format("id: %s source: %s dest: %s", cancelSm.getMessageId(),
                cancelSm.getSourceAddress().getAddress(),
                cancelSm.getDestAddress().getAddress());
    }

    private static String submitSmsToString(SubmitSm submitSm) {
        return String.format("source: %s dest: %s text: %s",
                submitSm.getSourceAddress().getAddress(),
                submitSm.getDestAddress().getAddress(),
                new String(submitSm.getShortMessage())
        );
    }
}
