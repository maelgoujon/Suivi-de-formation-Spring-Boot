package com.webapp.ytb.webappytp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.ytb.webappytp.modele.ElementsFiche.MateriauxAmenagement;

@Repository
public interface MateriauxAmenagementRepository extends JpaRepository<MateriauxAmenagement, Long> {
        // Ajoutez des méthodes personnalisées si nécessaire
}
