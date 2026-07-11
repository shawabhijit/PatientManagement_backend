package com.pm.billingservice.kafka;

import billing.events.BillingAccountCreateEvent;
import billing.events.InvoiceCancelledEvent;
import billing.events.InvoiceCreateEvent;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.model.Invoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String , byte[]> kafkaTemplate;

    public void sendBillingEvent(BillingAccount billingAccount) {
        BillingAccountCreateEvent event = BillingAccountCreateEvent.newBuilder()
                .setBillingAccountId(billingAccount.getId().toString())
                .setPatientId(billingAccount.getPatientId().toString())
                .setPatientName(billingAccount.getPatientName())
                .setPatientEmail(billingAccount.getPatientEmail())
                .setOutstandingBalance(billingAccount.getOutstandingBalance().doubleValue())
                .setStatus(billingAccount.getStatus().toString())
                .setCreatedAt(billingAccount.getCreatedAt().toString())
                .setUpdatedAt(billingAccount.getUpdatedAt().toString())
                .build();

        try {
            kafkaTemplate.send("billing" , event.toByteArray());
            log.info("Billing Account info sent to Kafka topic");
        }
        catch (Exception e) {
            log.error("Error while sending Billing event to Kafka topic {}", e.getMessage());
        }
    }

    public void sendInvoiceEvent(Invoice invoice) {
        InvoiceCreateEvent event = InvoiceCreateEvent.newBuilder()
                .setInvoiceId(invoice.getId().toString())
                .setBillingAccountId(invoice.getBillingAccount().getId().toString())
                .setInvoiceNumber(invoice.getInvoiceNumber())
                .setConsultationFee(invoice.getConsultationFee().doubleValue())
                .setMedicineFee(invoice.getMedicineFee().doubleValue())
                .setLabFee(invoice.getLabFee().doubleValue())
                .setDiscount(invoice.getDiscount().doubleValue())
                .setTax(invoice.getTax().doubleValue())
                .setTotalAmount(invoice.getTotalAmount().doubleValue())
                .setStatus(invoice.getStatus().toString())
                .setIssueDate(invoice.getIssueDate().toString())
                .setDueDate(invoice.getDueDate().toString())
                .build();

        try {
            kafkaTemplate.send("Invoice" , event.toByteArray());
        }
        catch (Exception e) {
            log.error("Error while sending Invoice event to Kafka topic {}", e.getMessage());
        }
    }

    public void sendInvoiceCancelEvent(Invoice invoice) {
        InvoiceCancelledEvent event = InvoiceCancelledEvent.newBuilder()
                .setInvoiceId(invoice.getId().toString())
                .setPatientId(invoice.getBillingAccount().getPatientId().toString())
                .setInvoiceNumber(invoice.getInvoiceNumber())
                .setCancelledAt(LocalDateTime.now().toString())
                .build();

        try {
            kafkaTemplate.send("Invoice-cancel" , event.toByteArray());
        }
        catch (Exception e) {
            log.error("Error while sending Invoice Cancel event to Kafka topic {}", e.getMessage());
        }
    }
}
