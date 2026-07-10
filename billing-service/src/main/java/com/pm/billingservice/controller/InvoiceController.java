package com.pm.billingservice.controller;

import com.pm.billingservice.DTO.InvoiceRequest;
import com.pm.billingservice.DTO.InvoiceResponse;
import com.pm.billingservice.model.enums.InvoiceStatus;
import com.pm.billingservice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get All Invoices" ,
            description = "This Api helps to fetch all the Invoices from the Invoices db, it returns List of Invoice Responses.")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok().body(invoiceService.getAllInvoices());
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Invoice By ID" ,
            description = "This Api helps to fetch Invoice with the help of Invoice ID.")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/number/{invoiceNumber}")
    @Operation(summary = "Get Invoice By Invoice Number" ,
            description = "This Api helps to fetch Invoice with the help of Invoice Number.")
    public ResponseEntity<InvoiceResponse> getInvoiceByInvoiceNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok().body(invoiceService.getInvoiceByInvoiceNumber(invoiceNumber));
    }

    @GetMapping(params = "status")
    @Operation(summary = "Get Invoices By Status" ,
            description = "This Api helps to fetch Invoices with matching status like PENDING,PAID,CANCELED etc.")
    public ResponseEntity<List<InvoiceResponse>> getInvoiceByStatus(@RequestParam InvoiceStatus status) {
        return ResponseEntity.ok().body(invoiceService.getInvoiceByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Create Invoice" ,
            description = "Creates a Invoice with InvoiceRequest and return InvoiceResponse")
    @ApiResponses({
            @ApiResponse(responseCode = "201" , description = "Invoice created successfully."),
            @ApiResponse(responseCode = "400", description = "Validation failed."),
            @ApiResponse(responseCode = "500" , description = "Internal server error.")
    })
    public ResponseEntity<InvoiceResponse> createInvoice(@Validated @RequestBody InvoiceRequest invoiceRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(invoiceRequest));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Cancel Invoice",
            description = "Cancel a Invoice with the help of invoice ID")
    public ResponseEntity<?> deleteInvoice (@PathVariable UUID id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().build();
    }
}
