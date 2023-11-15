package com.webapp.ytb.webappytp.service;


import java.util.List;

import com.webapp.ytb.webappytp.modele.FicheIntervention;

public interface FicheService {
    FicheIntervention creer(FicheIntervention ficheIntervention);

    List<FicheIntervention> lireTout();
    FicheIntervention lire(Long id);

    FicheIntervention modifier(Long id, FicheIntervention ficheIntervention);

    String supprimer(Long id);
}

