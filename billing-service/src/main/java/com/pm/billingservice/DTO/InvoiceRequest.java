package com.pm.billingservice.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceRequest {
    @NotNull(message = "Patient ID is required.")
    private UUID patientId;

    @NotNull(message = "Consultation Fee is required.")
    @PositiveOrZero
    private BigDecimal consultationFee;

    @NotNull(message = "Medicine Fee is required.")
    private BigDecimal medicineFee;

    @NotNull(message = "Lab Fee is required.")
    private BigDecimal labFee;

    @NotNull(message = "Due Date is required.")
    private LocalDateTime dueDate;
}
