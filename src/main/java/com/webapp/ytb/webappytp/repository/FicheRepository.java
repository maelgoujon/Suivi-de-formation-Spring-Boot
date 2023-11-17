package com.webapp.ytb.webappytp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.ytb.webappytp.modele.FicheIntervention;

public interface FicheRepository extends JpaRepository<FicheIntervention, Long> {
    Optional<FicheIntervention> findByNumero(Long id);
}
