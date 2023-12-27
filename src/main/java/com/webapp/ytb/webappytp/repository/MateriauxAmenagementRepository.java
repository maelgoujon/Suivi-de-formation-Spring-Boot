package com.webapp.ytb.webappytp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;

@Repository
public interface MateriauxAmenagementRepository extends JpaRepository<Materiaux, Long> {
        public List<Materiaux> findByTypeIntervention(String type);
}
