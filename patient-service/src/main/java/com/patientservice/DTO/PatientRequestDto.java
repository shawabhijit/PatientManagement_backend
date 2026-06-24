package com.patientservice.DTO;

import com.patientservice.DTO.validators.CreatePatientValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Patient creation/update request")
public class PatientRequestDto {

    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    @Schema(
            description = "Full name of the patient",
            example = "John Doe"
    )
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    @Schema(
            description = "Patient email address",
            example = "john.doe@example.com"
    )
    private String email;

    @NotBlank(message = "Address is required.")
    @Schema(
            description = "Residential address",
            example = "123 Main Street, New York"
    )
    private String address;

    @NotBlank(message = "Date of birth is required.")
    @Schema(
            description = "Date of birth",
            example = "1995-05-20"
    )
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class , message = "Registered date is required.")
    @Schema(
            description = "Patient registration date",
            example = "2025-06-20"
    )
    private String registeredDate;
}
