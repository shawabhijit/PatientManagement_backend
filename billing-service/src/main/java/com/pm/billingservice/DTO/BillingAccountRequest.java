package com.pm.billingservice.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingAccountRequest {
    @NotBlank(message = "Patient ID is required.")
    private UUID patientId;
    @NotBlank(message = "Patient Name is required.")
    private String patientName;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email Should be Valid.")
    private String patientEmail;
}
