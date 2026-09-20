package com.medical.platform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "lifestyle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lifestyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    private boolean smoking;

    @Enumerated(EnumType.STRING)
    private ActivityLevel physicalActivity;

    private String diet;

    private String alcoholConsumption;

    private Double sleepDuration;
}