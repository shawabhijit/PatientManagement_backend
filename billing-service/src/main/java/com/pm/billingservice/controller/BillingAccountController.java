package com.pm.billingservice.controller;

import com.pm.billingservice.DTO.BillingAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingAccountController {

    private final BillingAccountService billingAccountService;

    @GetMapping("/accounts/{patientId}")
    public ResponseEntity<BillingAccountResponse> getAccountByPatientID(@PathVariable UUID patientId) {
        return ResponseEntity.ok().body(billingAccountService.getAccoutnByPatientId(patientId));
    }
}
