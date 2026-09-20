package com.medical.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MeasurementResponse {
    private Long id;
    private Integer systolicPressure;
    private Integer diastolicPressure;
    private Double bloodGlucose;
    private Double cholesterol;
    private Double weight;
    private Double height;
    private Double bmi;
    private LocalDateTime measurementDate;
}