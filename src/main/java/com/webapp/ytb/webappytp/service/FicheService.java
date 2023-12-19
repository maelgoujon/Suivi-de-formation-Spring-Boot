package com.webapp.ytb.webappytp.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.webapp.ytb.webappytp.modele.FicheIntervention;

public interface FicheService {
    FicheIntervention creer(FicheIntervention ficheIntervention);

    List<FicheIntervention> lireTout();
    FicheIntervention lire(Long id);

    FicheIntervention modifier(Long id, FicheIntervention ficheIntervention);

    String supprimer(Long id);

    public void enregistrerAudio(Long id, MultipartFile fichierAudio) throws Exception;

    public List<FicheIntervention> getFichesByUserId(Long userId);

}

