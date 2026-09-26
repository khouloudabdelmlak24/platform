package com.medical.platform.service;

import com.medical.platform.dto.PatientRequest;
import com.medical.platform.dto.PatientResponse;
import com.medical.platform.entity.Patient;
import com.medical.platform.entity.User;
import com.medical.platform.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé avec l'id : " + id));
        return toResponse(patient);
    }

    public PatientResponse getPatientByEmail(String email) {
        Patient patient = patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé pour l'email : " + email));
        return toResponse(patient);
    }

    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé avec l'id : " + id));

        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setFamilyHistory(request.getFamilyHistory());
        patient.setCurrentTreatments(request.getCurrentTreatments());

        patientRepository.save(patient);
        return toResponse(patient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient non trouvé avec l'id : " + id);
        }
        patientRepository.deleteById(id);
    }

    private PatientResponse toResponse(Patient patient) {
        User user = patient.getUser();
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .emergencyContact(patient.getEmergencyContact())
                .medicalHistory(patient.getMedicalHistory())
                .familyHistory(patient.getFamilyHistory())
                .currentTreatments(patient.getCurrentTreatments())
                .build();
    }
}