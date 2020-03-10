package com.github.mikesafonov.smpp.boot;

import com.github.mikesafonov.smpp.core.dto.DeliveryReport;
import com.github.mikesafonov.smpp.core.reciever.DeliveryReportConsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike Safonov
 */
public class DeliveryReportConsumerImpl implements DeliveryReportConsumer {
    private List<DeliveryReport> reports = new ArrayList<>();

    @Override
    public void accept(DeliveryReport deliveryReport) {
        reports.add(deliveryReport);
    }

    public List<DeliveryReport> getReports() {
        return reports;
    }
}
