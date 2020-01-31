package com.github.mikesafonov.smpp.assertj;

import com.cloudhopper.commons.charset.Charset;
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.pdu.SubmitSm;
import lombok.experimental.UtilityClass;

/**
 * @author Mike Safonov
 */
@UtilityClass
class SubmitSmCharsetUtil {

    static String toText(SubmitSm submitSm, Charset charset) {
        byte[] bytes = submitSm.getShortMessage();
        return CharsetUtil.decode(bytes, charset);
    }
}
