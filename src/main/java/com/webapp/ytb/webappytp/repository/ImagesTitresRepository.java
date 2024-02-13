package com.webapp.ytb.webappytp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.ytb.webappytp.modele.ElementsFiche.ImagesTitres;

@Repository
public interface ImagesTitresRepository extends JpaRepository<ImagesTitres, Long> {

        List<ImagesTitres> findByTypeImage(ImagesTitres.TypeImage typeImage);

        ImagesTitres findByNomImage(String nomImage);

}
