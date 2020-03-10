package com.github.mikesafonov.smpp.boot;

import com.github.mikesafonov.smpp.core.reciever.DeliveryReportConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mike Safonov
 */
@Configuration
public class TestConfiguration {
    @Bean
    public DeliveryReportConsumer deliveryReportConsumer() {
        return new DeliveryReportConsumerImpl();
    }
}
