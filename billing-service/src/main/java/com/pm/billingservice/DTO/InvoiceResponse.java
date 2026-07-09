package com.pm.billingservice.DTO;

import com.pm.billingservice.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceResponse {
    private UUID billingAccountId;
    private String invoiceNumber;
    private BigDecimal consultationFee;
    private BigDecimal medicineFee;
    private BigDecimal labFee;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
}
