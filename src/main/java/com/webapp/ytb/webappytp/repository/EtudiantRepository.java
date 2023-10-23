package com.webapp.ytb.webappytp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.ytb.webappytp.modele.Etudiant;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long>{
    
}
