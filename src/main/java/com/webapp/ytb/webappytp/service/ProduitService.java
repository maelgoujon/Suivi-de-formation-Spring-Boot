package com.webapp.ytb.webappytp.service;


import java.util.List;

import com.webapp.ytb.webappytp.modele.Etudiant;

public interface ProduitService {
    Etudiant creer(Etudiant produit);

    List<Etudiant> lire();

    Etudiant modifier(Long id, Etudiant produit);

    String supprimer(Long id);
}
