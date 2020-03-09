package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.joda.time.DateTime;


/**
 * Default implementation of {@link DeliverSmGenerator}. Generates delivered {@link DeliverSm}
 *
 * @author Mike Safonov
 */
@EqualsAndHashCode
public class DefaultDeliverSmGenerator implements DeliverSmGenerator {

    @Override
    @SneakyThrows
    public DeliverSm generate(SubmitSm submitSm, String messageId) {
        DeliverSm deliverSm = new DeliverSm();
        deliverSm.setSourceAddress(submitSm.getSourceAddress());
        deliverSm.setDestAddress(submitSm.getDestAddress());
        deliverSm.setDataCoding(SmppConstants.DATA_CODING_DEFAULT);
        deliverSm.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_NOT_REQUESTED);
        //TODO: calculate real messages count
        int messagesCount = 1;
        DeliveryReceipt receipt = new DeliveryReceipt(messageId, messagesCount, messagesCount,
                DateTime.now(), DateTime.now(),
                SmppConstants.STATE_DELIVERED, 0, null);
        deliverSm.setShortMessage(receipt.toShortMessage().getBytes());
        return deliverSm;
    }
}
