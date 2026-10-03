package com.medical.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vital_measurements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VitalMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    private Integer systolicPressure;

    @NotNull
    private Integer diastolicPressure;

    private Double bloodGlucose;

    private Double cholesterol;

    @Positive
    private Double weight;

    @Positive
    private Double height;

    private Double bmi;

    private String riskLevel;

    private Integer riskScore;

    @Column(columnDefinition = "TEXT")
    private String riskFactors;

    @Column(nullable = false)
    private LocalDateTime measurementDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (measurementDate == null) {
            measurementDate = LocalDateTime.now();
        }
        if (weight != null && height != null && height > 0) {
            double heightInMeters = height / 100;
            bmi = weight / (heightInMeters * heightInMeters);
        }
    }
}