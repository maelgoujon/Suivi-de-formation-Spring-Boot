package com.webapp.ytb.webappytp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;

@Repository
public interface MateriauxRepository extends JpaRepository<Materiaux, Long> {
        // Ajoutez des méthodes personnalisées si nécessaire
}
