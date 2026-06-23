package com.patientservice.Controller;

import com.patientservice.DTO.PatientRequestDto;
import com.patientservice.DTO.PatientResponseDto;
import com.patientservice.DTO.validators.CreatePatientValidationGroup;
import com.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.ok().body(patientService.createPatient(patientRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class}) @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.ok().body(patientService.updatePatient(id, patientRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok().body("Patient is Deleted.");
    }
}
