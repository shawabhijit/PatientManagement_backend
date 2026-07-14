package com.pm.billingservice.DTO;

import com.pm.billingservice.model.enums.BillingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillingAccountResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String patientEmail;
    private BigDecimal outstandingBalance;
    private BillingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
