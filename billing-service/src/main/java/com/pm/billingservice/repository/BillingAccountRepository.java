package com.pm.billingservice.repository;

import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.model.enums.BillingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillingAccountRepository extends JpaRepository<BillingAccount , UUID> {
    Optional<BillingAccount> findByPatientId(UUID patientId);
    List<BillingAccount> findByStatus(BillingStatus status);
    boolean existsByPatientId(UUID patientId);
}
