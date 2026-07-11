package com.pm.analyticsService.kafka;

import billing.events.BillingAccountCreateEvent;
import billing.events.InvoiceCancelledEvent;
import billing.events.InvoiceCreateEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient" , groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // ... perform any business related logic to analytics here

            log.info("Received Patient Event: [PatientId={}, PatientName={} , PatientEmail={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());

        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}" , e.getMessage());
        }
    }


    @KafkaListener(topics = "billing" , groupId = "analytics-service")
    public void consumeBillingEvent(byte[] event) {
        try {
            BillingAccountCreateEvent eventBilling = BillingAccountCreateEvent.parseFrom(event);

            // ... perform any business logic here

            log.info("Received Billing Account Creation event: [BillingAccountId={}, " +
                    "PatientId={}, PatientName={} , PatientEmail={}, OutstandingBalance={}]",
                    eventBilling.getBillingAccountId(),
                    eventBilling.getPatientId(),
                    eventBilling.getPatientName(),
                    eventBilling.getPatientEmail(),
                    eventBilling.getOutstandingBalance());
        }
        catch (InvalidProtocolBufferException ex) {
            log.error("Error deserializing billing Account Creation event {}" , ex.getMessage());
        }
    }

    @KafkaListener(topics = "Invoice" , groupId = "analytics-service")
    public void consumeInvoiceEvent(byte[] event) {
        try {
            InvoiceCreateEvent eventInvoice = InvoiceCreateEvent.parseFrom(event);

            // ... perform any business logic here

            log.info("Received Invoice Account Creation event: [InvoiceID={}, " +
                            "BillingAccountID={}, ConsultationFee={} , MedicineFee={}, LabFee={}, TotalAmount={}]",
                    eventInvoice.getInvoiceId(),
                    eventInvoice.getBillingAccountId(),
                    eventInvoice.getConsultationFee(),
                    eventInvoice.getMedicineFee(),
                    eventInvoice.getLabFee(),
                    eventInvoice.getTotalAmount());
        }
        catch (InvalidProtocolBufferException ex) {
            log.error("Error deserializing Invoice creation event {}" , ex.getMessage());
        }
    }

    @KafkaListener(topics = "Invoice-cancel" , groupId = "analytics-service")
    public void consumeInvoiceCancelEvent(byte[] event) {
        try {
            InvoiceCancelledEvent eventInvoice = InvoiceCancelledEvent.parseFrom(event);

            // ... perform any business logic here

            log.info("Received Invoice Cancel event: [InvoiceID={}, " +
                            "PatientId={}, InvoiceNumber={} , CaceledAt={}]",
                    eventInvoice.getInvoiceId(),
                    eventInvoice.getPatientId(),
                    eventInvoice.getInvoiceNumber(),
                    eventInvoice.getCancelledAt());
        }
        catch (InvalidProtocolBufferException ex) {
            log.error("Error deserializing Invoice cancel event {}" , ex.getMessage());
        }
    }
}
