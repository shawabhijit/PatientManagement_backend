package com.patientservice.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientResponseDto {
    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
}
