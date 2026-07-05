package com.pm.billingservice.DTO;

import com.pm.billingservice.model.enums.BillingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BillingAccountGrpcResponse {
    private UUID id;
    private BillingStatus status;
}
