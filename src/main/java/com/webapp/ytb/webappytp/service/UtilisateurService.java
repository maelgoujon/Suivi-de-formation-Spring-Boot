package com.webapp.ytb.webappytp.service;


import java.util.List;

import com.webapp.ytb.webappytp.modele.Utilisateur;

public interface UtilisateurService {
    Utilisateur creer(Utilisateur utilisateur);

    List<Utilisateur> lire();

    Utilisateur modifier(Long id, Utilisateur utilisateur);

    String supprimer(Long id);

    public Utilisateur findById(Long id);

    // Ajoutez cette méthode pour modifier le mot de passe
    void modifierMotDePasse(Long id, String nouveauMotDePasse);

    public List<Utilisateur> getUtilisateursByRole(String roleStr);
}
