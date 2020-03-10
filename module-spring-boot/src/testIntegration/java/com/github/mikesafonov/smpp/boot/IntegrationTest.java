package com.github.mikesafonov.smpp.boot;

import com.github.mikesafonov.smpp.api.SenderManager;
import com.github.mikesafonov.smpp.config.SmppAutoConfiguration;
import com.github.mikesafonov.smpp.core.dto.Message;
import com.github.mikesafonov.smpp.core.dto.MessageResponse;
import com.github.mikesafonov.smpp.server.MockSmppServerHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mike Safonov
 */
@SpringBootTest(classes = {TestConfiguration.class, SmppAutoConfiguration.class})
public class IntegrationTest {
    @Autowired
    private SenderManager senderManager;
    @Autowired
    private MockSmppServerHolder holder;
    @Autowired
    private DeliveryReportConsumerImpl deliveryReportConsumer;

    @Test
    void contextLoad() {

        Message message = Message.simple("some text")
                .from("333")
                .to("1233442344")
                .build();
        MessageResponse response = senderManager.getClient().send(message);

        await().atMost(3, TimeUnit.SECONDS).pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> assertFalse(deliveryReportConsumer.getReports().isEmpty()));

        deliveryReportConsumer.getReports().forEach(deliveryReport -> {
            assertEquals(response.getSmscMessageID(), deliveryReport.getMessageId());
        });
    }

}
