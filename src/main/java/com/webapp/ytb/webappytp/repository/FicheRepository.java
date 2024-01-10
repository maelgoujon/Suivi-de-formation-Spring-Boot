package com.webapp.ytb.webappytp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.ytb.webappytp.modele.FicheIntervention;

public interface FicheRepository extends JpaRepository<FicheIntervention, Long> {
    List<FicheIntervention> findByUtilisateurId(Long userId);
    FicheIntervention findByTravauxRealisesAndUtilisateurPrenom(String travauxRealises, String prenom);
}
