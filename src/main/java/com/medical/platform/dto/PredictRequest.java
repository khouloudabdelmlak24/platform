package com.medical.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PredictRequest {
    private Integer systolicPressure;
    private Integer diastolicPressure;
    private Double bloodGlucose;
    private Double cholesterol;
    private Double weight;
    private Double height;
}