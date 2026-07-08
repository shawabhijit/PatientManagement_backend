package com.pm.billingservice.controller;

import com.pm.billingservice.DTO.BillingAccountResponse;
import com.pm.billingservice.service.BillingAccountService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BillingAccountResponse>> getAllBillingAccounts() {
        return ResponseEntity.ok().body(billingAccountService.getAllBillingAccounts());
    }

    @GetMapping("/accounts/{patientId}")
    public ResponseEntity<BillingAccountResponse> getAccountByPatientID(@PathVariable UUID patientId) {
        return ResponseEntity.ok().body(billingAccountService.getAccountByPatientId(patientId));
    }

    @GetMapping(value = "/accounts" , params = "status")
    public ResponseEntity<List<BillingAccountResponse>> getAccountsByStatus(@RequestParam String status) {
        return ResponseEntity.ok().body(billingAccountService.getByStatus(status));
    }
}
