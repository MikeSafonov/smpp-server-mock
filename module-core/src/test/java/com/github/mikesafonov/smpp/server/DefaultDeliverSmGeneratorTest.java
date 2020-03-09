package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.cloudhopper.smpp.util.DeliveryReceiptException;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;

import static com.cloudhopper.commons.util.RandomUtil.generateString;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Safonov
 */
public class DefaultDeliverSmGeneratorTest {
    private DefaultDeliverSmGenerator deliverSmGenerator = new DefaultDeliverSmGenerator();

    @Test
    void shouldGenerateExpectedDeliverySm() throws DeliveryReceiptException {

        String messageId = generateString(10);
        SubmitSm submitSm = new SubmitSm();
        submitSm.setDestAddress(new Address((byte)0, (byte)0, "123"));
        submitSm.setSourceAddress(new Address((byte)0, (byte)0, "321"));
        DeliverSm deliverSm = deliverSmGenerator.generate(submitSm, messageId);

        DeliveryReceipt receipt = DeliveryReceipt.parseShortMessage(new String(deliverSm.getShortMessage()),
                DateTimeZone.UTC);

        assertEquals(submitSm.getDestAddress(), deliverSm.getDestAddress());
        assertEquals(submitSm.getSourceAddress(), deliverSm.getSourceAddress());
        assertEquals(SmppConstants.DATA_CODING_DEFAULT, deliverSm.getDataCoding());
        assertEquals(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_NOT_REQUESTED, deliverSm.getRegisteredDelivery());
        assertEquals(messageId, receipt.getMessageId());
        assertEquals(1, receipt.getSubmitCount());
        assertEquals(1, receipt.getDeliveredCount());
        assertEquals(SmppConstants.STATE_DELIVERED, receipt.getState());
    }
}
