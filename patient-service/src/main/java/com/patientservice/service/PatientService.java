package com.patientservice.service;

import com.patientservice.DTO.PatientRequestDto;
import com.patientservice.DTO.PatientResponseDto;
import com.patientservice.exception.EmailAlreadyExistsException;
import com.patientservice.exception.PatientNotFoundException;
import com.patientservice.grpc.BillingServiceGrpcClient;
import com.patientservice.model.Patient;
import com.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public List<PatientResponseDto> getPatients () {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(this::patientEntityToDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if(patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDto.getEmail());
        }
        Patient patient = patientRepository.save(patientDtoToEntity(patientRequestDto));
        // creating a billing account for the patient
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString() , patient.getName() , patient.getEmail());
        return patientEntityToDto(patient);
    }

    public PatientResponseDto updatePatient(UUID patientId,PatientRequestDto patientRequestDto) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with this id : " + patientId)
        );
        if(patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail() , patientId)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDto.getEmail());
        }

        patient.setName(patientRequestDto.getName());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));

        patient = patientRepository.save(patient);

        return patientEntityToDto(patient);
    }

    public void deletePatient(UUID patientId) {
        patientRepository.deleteById(patientId);
    }

    public PatientResponseDto patientEntityToDto (Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .build();
    }

    public Patient patientDtoToEntity (PatientRequestDto patientRequestDto) {
        return Patient.builder()
                .name(patientRequestDto.getName())
                .email(patientRequestDto.getEmail())
                .address(patientRequestDto.getAddress())
                .dateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()))
                .registeredDate(LocalDate.parse(patientRequestDto.getRegisteredDate()))
                .build();
    }
}
