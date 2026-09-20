package com.medical.platform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementRequest {

    @NotNull(message = "La pression systolique est obligatoire")
    private Integer systolicPressure;

    @NotNull(message = "La pression diastolique est obligatoire")
    private Integer diastolicPressure;

    private Double bloodGlucose;

    private Double cholesterol;

    @Positive(message = "Le poids doit être supérieur à 0")
    private Double weight;

    @Positive(message = "La taille doit être supérieure à 0")
    private Double height;
}