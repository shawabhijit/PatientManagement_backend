package com.pm.billingservice.service;

import com.pm.billingservice.DTO.InvoiceRequest;
import com.pm.billingservice.DTO.InvoiceResponse;
import com.pm.billingservice.exceptions.AccountExitsException;
import com.pm.billingservice.exceptions.InvoiceNotExitsException;
import com.pm.billingservice.kafka.KafkaProducer;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.model.Invoice;
import com.pm.billingservice.model.enums.InvoiceStatus;
import com.pm.billingservice.repository.BillingAccountRepository;
import com.pm.billingservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final BillingAccountRepository billingAccountRepository;
    private final KafkaProducer kafkaProducer;

    public List<InvoiceResponse> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public InvoiceResponse getInvoiceById(@PathVariable UUID id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new InvoiceNotExitsException("Invoice not found with this id : " + id.toString())
        );
        return entityToDto(invoice);
    }

    public InvoiceResponse getInvoiceByInvoiceNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber).orElseThrow(
                () -> new InvoiceNotExitsException("Invoice not found with this number : " + invoiceNumber)
        );
        return entityToDto(invoice);
    }

    public List<InvoiceResponse> getInvoiceByStatus(InvoiceStatus status) {
        List<Invoice> invoices = invoiceRepository.findByStatus(status);
        return invoices.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest) {
        BillingAccount account = billingAccountRepository
                .findByPatientId(invoiceRequest.getPatientId())
                .orElseThrow(
                        () -> new AccountExitsException("Billing account not found with this id : "
                                + invoiceRequest.getPatientId())
                );

        String invoiceNumber = generateInvoiceNumber();
        BigDecimal discount = new BigDecimal("0.10");
        BigDecimal tax = new BigDecimal("0.18");

        BigDecimal total = generateTotalAmount(invoiceRequest,discount,tax);

        Invoice invoice = Invoice.builder()
                .billingAccount(account)
                .invoiceNumber(invoiceNumber)
                .discount(discount)
                .tax(tax)
                .consultationFee(invoiceRequest.getConsultationFee())
                .medicineFee(invoiceRequest.getMedicineFee())
                .labFee(invoiceRequest.getLabFee())
                .totalAmount(total)
                .status(InvoiceStatus.PENDING)
                .issueDate(LocalDateTime.now())
                .dueDate(invoiceRequest.getDueDate())
                .build();

        account.setOutstandingBalance(account.getOutstandingBalance().add(total));
        billingAccountRepository.save(account);

        invoiceRepository.save(invoice);

        kafkaProducer.sendInvoiceEvent(invoice);

        return entityToDto(invoice);
    }

    @Transactional
    public void deleteInvoice(UUID id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new InvoiceNotExitsException("Invoice not found with this id : " + id.toString())
        );

        if (invoice.getStatus() != InvoiceStatus.PENDING) {
            throw new InvoiceNotExitsException("Invoice status is not PENDING");
        }

        BillingAccount account = invoice.getBillingAccount();

        account.setOutstandingBalance(
                account.getOutstandingBalance().subtract(invoice.getTotalAmount())
        );

        invoice.setStatus(InvoiceStatus.CANCELLED);
        billingAccountRepository.save(account);
        invoiceRepository.save(invoice);

        kafkaProducer.sendInvoiceCancelEvent(invoice);
    }

    private InvoiceResponse entityToDto(Invoice invoice) {
        return InvoiceResponse.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .billingAccountId(invoice.getBillingAccount().getId())
                .consultationFee(invoice.getConsultationFee())
                .discount(invoice.getDiscount())
                .dueDate(invoice.getDueDate())
                .issueDate(invoice.getIssueDate())
                .tax(invoice.getTax())
                .labFee(invoice.getLabFee())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .medicineFee(invoice.getMedicineFee())
                .build();
    }

    private String generateInvoiceNumber() {
        Long sequence = invoiceRepository.getNextInvoiceSequence();
        return "INV-"
                + LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                + "-"
                + String.format("%06d", sequence);
    }

    private BigDecimal generateTotalAmount(
            InvoiceRequest request,
            BigDecimal discount,
            BigDecimal tax
    ) {
        BigDecimal subtotal = request.getConsultationFee()
                .add(request.getMedicineFee())
                .add(request.getLabFee());

        return subtotal.subtract(discount).add(tax);
    }
}
