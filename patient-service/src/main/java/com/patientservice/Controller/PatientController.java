package com.patientservice.Controller;

import com.patientservice.DTO.PatientRequestDto;
import com.patientservice.DTO.PatientResponseDto;
import com.patientservice.DTO.validators.CreatePatientValidationGroup;
import com.patientservice.model.enums.PatientStatus;
import com.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Tag(name = "Patient" , description = "API for managing Patients.")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @GetMapping(params = "status")
    @Operation(summary = "Get Patients by their status, ex. ACTIVE/INACTIVE")
    public ResponseEntity<List<PatientResponseDto>> getPatientsByStatus(
            @RequestParam PatientStatus status
    ) {
        return ResponseEntity.ok().body(patientService.getPatientsByStatus(status));
    }

    @Operation(summary = "Get patient by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatient(@PathVariable UUID id) {
        return ResponseEntity.ok().body(patientService.getPatientById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create Patient",
            description = "Creates a new patient record in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PatientResponseDto> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Patient",
            description = "Updates an existing patient's information using the provided patient ID. "
                    + "Only valid patient records can be updated. "
                    + "The registration date is not required during updates."
    )
    public ResponseEntity<PatientResponseDto> updatePatient(
            @Parameter(
                    description = "Unique identifier of the patient",
                    example = "7b8c9d12-45ab-4ef1-9876-123456789abc",
                    required = true
            )
            @PathVariable UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.ok().body(patientService.updatePatient(id, patientRequestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Updating the patient status to INACTIVE")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().body("Patient is Deleted.");
    }
}
