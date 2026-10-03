package com.medical.platform.service;

import com.medical.platform.dto.MeasurementRequest;
import com.medical.platform.dto.MeasurementResponse;
import com.medical.platform.dto.PredictRequest;
import com.medical.platform.dto.PredictResponse;
import com.medical.platform.entity.Patient;
import com.medical.platform.entity.VitalMeasurement;
import com.medical.platform.repository.PatientRepository;
import com.medical.platform.repository.VitalMeasurementRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final VitalMeasurementRepository measurementRepository;
    private final PatientRepository patientRepository;
    private final AuditService auditService;
    private final AiPredictionService aiPredictionService;

    public List<MeasurementResponse> getMeasurementsForPatient(Long patientId, Authentication authentication) {
        checkAccess(patientId, authentication);

        return measurementRepository.findByPatientIdOrderByMeasurementDateDesc(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MeasurementResponse addMeasurement(Long patientId, MeasurementRequest request, HttpServletRequest httpRequest, Authentication authentication) {
        checkAccess(patientId, authentication);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé avec l'id : " + patientId));

        VitalMeasurement measurement = new VitalMeasurement();
        measurement.setPatient(patient);
        measurement.setSystolicPressure(request.getSystolicPressure());
        measurement.setDiastolicPressure(request.getDiastolicPressure());
        measurement.setBloodGlucose(request.getBloodGlucose());
        measurement.setCholesterol(request.getCholesterol());
        measurement.setWeight(request.getWeight());
        measurement.setHeight(request.getHeight());

        PredictResponse prediction = aiPredictionService.predict(
                PredictRequest.builder()
                        .systolicPressure(request.getSystolicPressure())
                        .diastolicPressure(request.getDiastolicPressure())
                        .bloodGlucose(request.getBloodGlucose())
                        .cholesterol(request.getCholesterol())
                        .weight(request.getWeight())
                        .height(request.getHeight())
                        .build()
        );

        if (prediction != null) {
            measurement.setRiskLevel(prediction.getRiskLevel());
            measurement.setRiskScore(prediction.getRiskScore());
            measurement.setRiskFactors(
                    prediction.getFactors() != null ? String.join(", ", prediction.getFactors()) : null
            );
        }

        measurementRepository.save(measurement);

        auditService.log(
                patient.getUser(),
                "ADD_MEASUREMENT",
                "VitalMeasurement",
                "Nouvelle mesure ajoutée pour le patient id " + patientId
                        + (prediction != null ? " (risque : " + prediction.getRiskLevel() + ")" : ""),
                httpRequest
        );

        return toResponse(measurement);
    }

    /**
     * Un DOCTOR peut consulter n'importe quel patient.
     * Un PATIENT ne peut consulter que son propre dossier.
     */
    private void checkAccess(Long patientId, Authentication authentication) {
        boolean isDoctor = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_DOCTOR"));

        if (isDoctor) {
            return;
        }

        String email = authentication.getName();
        Patient patient = patientRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé pour l'email : " + email));

        if (!patient.getId().equals(patientId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Vous n'avez pas accès aux données de ce patient"
            );
        }
    }

    private MeasurementResponse toResponse(VitalMeasurement measurement) {
        return MeasurementResponse.builder()
                .id(measurement.getId())
                .systolicPressure(measurement.getSystolicPressure())
                .diastolicPressure(measurement.getDiastolicPressure())
                .bloodGlucose(measurement.getBloodGlucose())
                .cholesterol(measurement.getCholesterol())
                .weight(measurement.getWeight())
                .height(measurement.getHeight())
                .bmi(measurement.getBmi())
                .riskLevel(measurement.getRiskLevel())
                .riskScore(measurement.getRiskScore())
                .riskFactors(measurement.getRiskFactors())
                .measurementDate(measurement.getMeasurementDate())
                .build();
    }
}