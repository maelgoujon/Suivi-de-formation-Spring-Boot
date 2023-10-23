package com.webapp.ytb.webappytp.service;


import java.util.List;

import com.webapp.ytb.webappytp.modele.Etudiant;

public interface EtudiantService {
    Etudiant creer(Etudiant etudiant);

    List<Etudiant> lire();

    Etudiant modifier(Long id, Etudiant etudiant);

    String supprimer(Long id);
}
