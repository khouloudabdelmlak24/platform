package com.medical.platform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PredictResponse {
    private String riskLevel;
    private Integer riskScore;
    private List<String> factors;
    private String model;
}