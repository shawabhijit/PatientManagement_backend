package com.pm.billingservice.DTO;

import com.pm.billingservice.model.enums.BillingStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingAccountUpdateRequest {
    @NotBlank(message = "Patient Name is required.")
    private String patientName;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email Should be Valid.")
    private String patientEmail;
    @NotBlank(message = "Status is required.")
    private BillingStatus status;
}
