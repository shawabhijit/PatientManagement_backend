package com.patientservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDto {
    private String id;
    private String name;
    private String email;
    private String status;
    private String address;
    private String dateOfBirth;
}
