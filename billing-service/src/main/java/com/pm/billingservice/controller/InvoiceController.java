package com.pm.billingservice.controller;

import com.pm.billingservice.DTO.InvoiceRequest;
import com.pm.billingservice.DTO.InvoiceResponse;
import com.pm.billingservice.model.enums.InvoiceStatus;
import com.pm.billingservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok().body(invoiceService.getAllInvoices());
    }

    @GetMapping("{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<InvoiceResponse> getInvoiceByInvoiceNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok().body(invoiceService.getInvoiceByInvoiceNumber(invoiceNumber));
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<InvoiceResponse>> getInvoiceByStatus(@RequestParam InvoiceStatus status) {
        return ResponseEntity.ok().body(invoiceService.getInvoiceByStatus(status));
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Validated @RequestBody InvoiceRequest invoiceRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(invoiceRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> deleteInvoice (@PathVariable UUID id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }
}
