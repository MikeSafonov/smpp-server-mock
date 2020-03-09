package com.github.mikesafonov.smpp.server;

import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.SubmitSm;

/**
 * Generator interface for {@link DeliverSm} from {@link SubmitSm}
 *
 * @author Mike Safonov
 */
public interface DeliverSmGenerator {

    DeliverSm generate(SubmitSm submitSm, String messageId);

}
