package com.webapp.ytb.webappytp.repository;

import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.Utilisateur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
}
