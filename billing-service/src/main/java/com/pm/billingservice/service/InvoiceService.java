package com.pm.billingservice.service;

import com.pm.billingservice.DTO.InvoiceRequest;
import com.pm.billingservice.DTO.InvoiceResponse;
import com.pm.billingservice.exceptions.InvoiceNotExitsException;
import com.pm.billingservice.model.Invoice;
import com.pm.billingservice.model.enums.InvoiceStatus;
import com.pm.billingservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

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

    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest) {
        return null;
    }

    public void deleteInvoice(UUID id) {

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
}
