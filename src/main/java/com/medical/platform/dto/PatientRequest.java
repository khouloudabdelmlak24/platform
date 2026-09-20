package com.medical.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientRequest {

    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Le genre est obligatoire")
    private String gender;

    private String address;

    private String emergencyContact;

    private String medicalHistory;

    private String familyHistory;

    private String currentTreatments;
}