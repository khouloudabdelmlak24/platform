package com.medical.platform.service;

import com.medical.platform.dto.MeasurementRequest;
import com.medical.platform.dto.MeasurementResponse;
import com.medical.platform.entity.Patient;
import com.medical.platform.entity.VitalMeasurement;
import com.medical.platform.repository.PatientRepository;
import com.medical.platform.repository.VitalMeasurementRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final VitalMeasurementRepository measurementRepository;
    private final PatientRepository patientRepository;
    private final AuditService auditService;

    public List<MeasurementResponse> getMeasurementsForPatient(Long patientId) {
        return measurementRepository.findByPatientIdOrderByMeasurementDateDesc(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MeasurementResponse addMeasurement(Long patientId, MeasurementRequest request, HttpServletRequest httpRequest) {
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

        measurementRepository.save(measurement);

        auditService.log(
                patient.getUser(),
                "ADD_MEASUREMENT",
                "VitalMeasurement",
                "Nouvelle mesure ajoutée pour le patient id " + patientId,
                httpRequest
        );

        return toResponse(measurement);
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
                .measurementDate(measurement.getMeasurementDate())
                .build();
    }
}