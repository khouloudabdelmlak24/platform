package com.medical.platform.repository;

import com.medical.platform.entity.Lifestyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LifestyleRepository extends JpaRepository<Lifestyle, Long> {

    Optional<Lifestyle> findByPatientId(Long patientId);
}