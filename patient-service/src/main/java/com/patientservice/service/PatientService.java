package com.patientservice.service;

import com.patientservice.DTO.PatientRequestDto;
import com.patientservice.DTO.PatientResponseDto;
import com.patientservice.exception.EmailAlreadyExistsException;
import com.patientservice.exception.PatientNotFoundException;
import com.patientservice.grpc.BillingServiceGrpcClient;
import com.patientservice.kafka.KafkaProducer;
import com.patientservice.model.Patient;
import com.patientservice.model.enums.PatientStatus;
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
    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDto> getPatients () {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(this::patientEntityToDto).toList();
    }

    public List<PatientResponseDto> getPatientsByStatus (PatientStatus status) {
        List<Patient> patients = patientRepository.findByStatus(status);
        return patients.stream()
                .map(this::patientEntityToDto).toList();
    }

    public PatientResponseDto getPatientById(UUID patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with this id : " + patientId)
        );
        return patientEntityToDto(patient);
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if(patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDto.getEmail());
        }
        Patient patient = patientDtoToEntity(patientRequestDto);
        patient.setStatus(PatientStatus.ACTIVE);
        patient = patientRepository.save(patient);
        // creating a billing account for the patient (GRPC Request)
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString() , patient.getName() , patient.getEmail());

        // sending an event to kafka producer which is new patient created
        kafkaProducer.sendEvent(patient);

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

        String status = patient.getStatus().toString().equals("ACTIVE") ?
                "ACTIVE" : patient.getStatus().toString().equals("SUSPENDED") ? "SUSPENDED" : "CLOSED";

        billingServiceGrpcClient.updateBillingAccount(patientId.toString(), patient.getName(),patient.getEmail(),status);

        return patientEntityToDto(patient);
    }

    public void deletePatient(UUID patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with this id : " + patientId)
        );

        patient.setStatus(PatientStatus.INACTIVE);
        patientRepository.save(patient);
        billingServiceGrpcClient.closeBillingAccount(patientId.toString());
    }

    public PatientResponseDto patientEntityToDto (Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .email(patient.getEmail())
                .status(patient.getStatus().toString())
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
