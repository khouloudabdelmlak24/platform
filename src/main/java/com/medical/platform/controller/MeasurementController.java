package com.medical.platform.controller;

import com.medical.platform.dto.MeasurementRequest;
import com.medical.platform.dto.MeasurementResponse;
import com.medical.platform.service.MeasurementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping
    public ResponseEntity<List<MeasurementResponse>> getMeasurements(@PathVariable Long patientId) {
        return ResponseEntity.ok(measurementService.getMeasurementsForPatient(patientId));
    }

    @PostMapping
    public ResponseEntity<MeasurementResponse> addMeasurement(
            @PathVariable Long patientId,
            @Valid @RequestBody MeasurementRequest request
    ) {
        return ResponseEntity.ok(measurementService.addMeasurement(patientId, request));
    }
}