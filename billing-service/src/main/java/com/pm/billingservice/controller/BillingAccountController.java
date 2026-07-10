package com.pm.billingservice.controller;

import com.pm.billingservice.DTO.BillingAccountResponse;
import com.pm.billingservice.DTO.BillingAccountUpdateRequest;
import com.pm.billingservice.service.BillingAccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/billing")
public class BillingAccountController {

    private final BillingAccountService billingAccountService;

    public BillingAccountController(BillingAccountService billingAccountService) {
        this.billingAccountService = billingAccountService;
    }

    @GetMapping("/accounts")
    @Operation(summary = "Get All Billing account.")
    public ResponseEntity<List<BillingAccountResponse>> getAllBillingAccounts() {
        return ResponseEntity.ok().body(billingAccountService.getAllBillingAccounts());
    }

    @GetMapping("/accounts/{patientId}")
    @Operation(summary = "Get Billing account by Patient ID")
    public ResponseEntity<BillingAccountResponse> getAccountByPatientID(@PathVariable UUID patientId) {
        return ResponseEntity.ok().body(billingAccountService.getAccountByPatientId(patientId));
    }

    @GetMapping(value = "/accounts" , params = "status")
    @Operation(summary = "Get By Status",description = "Get Billing Accounts by status, For example - ACTIVE/CLOSED/SUSPEND")
    public ResponseEntity<List<BillingAccountResponse>> getAccountsByStatus(@RequestParam String status) {
        return ResponseEntity.ok().body(billingAccountService.getByStatus(status));
    }
}
