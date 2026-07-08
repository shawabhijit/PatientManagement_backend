package com.pm.billingservice.service;

import com.pm.billingservice.DTO.BillingAccountGrpcResponse;
import com.pm.billingservice.DTO.BillingAccountRequest;
import com.pm.billingservice.DTO.BillingAccountResponse;
import com.pm.billingservice.exceptions.AccountExitsException;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.model.enums.BillingStatus;
import com.pm.billingservice.repository.BillingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingAccountService {

    private final BillingAccountRepository billingAccountRepository;

    public List<BillingAccountResponse> getAllBillingAccounts() {
        List<BillingAccount> billingAccounts = billingAccountRepository.findAll();
        return billingAccounts.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public BillingAccountResponse getAccountByPatientId(UUID patientId) {
        BillingAccount account = billingAccountRepository.findByPatientId(patientId)
                .orElseThrow(() -> new AccountExitsException("Account not found with patientId: " + patientId));

        return entityToDto(account);
    }

    public List<BillingAccountResponse> getByStatus(String status) {
        return billingAccountRepository
                .findByStatus(BillingStatus.valueOf(status)).stream().map(
                        this::entityToDto
                ).toList();
    }

    public BillingAccountGrpcResponse createBillingAccount(BillingAccountRequest billingAccountRequest) {
        if(billingAccountRepository.existsByPatientId(billingAccountRequest.getPatientId())) {
            throw new AccountExitsException("Billing account with this patient is already exits: " +
                    billingAccountRequest.getPatientId());
        }
        BillingAccount account = BillingAccount.builder()
                .patientId(billingAccountRequest.getPatientId())
                .patientEmail(billingAccountRequest.getPatientEmail())
                .patientName(billingAccountRequest.getPatientName())
                .status(BillingStatus.ACTIVE)
                .build();

        account = billingAccountRepository.save(account);

        return new BillingAccountGrpcResponse(
                account.getId(),
                account.getStatus()
        );
    }

    private BillingAccountResponse entityToDto(BillingAccount account) {
        return BillingAccountResponse.builder()
                .patientEmail(account.getPatientEmail())
                .patientName(account.getPatientName())
                .outstandingBalance(account.getOutstandingBalance())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
