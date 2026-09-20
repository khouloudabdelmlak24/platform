package com.medical.platform.repository;

import com.medical.platform.entity.VitalMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VitalMeasurementRepository extends JpaRepository<VitalMeasurement, Long> {

    List<VitalMeasurement> findByPatientIdOrderByMeasurementDateDesc(Long patientId);
}