package com.webapp.ytb.webappytp.service;

import com.webapp.ytb.webappytp.modele.Formation;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface FormationService {
    Formation creer(Formation formation);

    List<Formation> lire();

    Formation modifier(Long id, Formation formation);

    String supprimer(Long id);

    Formation findById(Long id);

    public void generatedExcel(Long formationId, HttpServletResponse response) throws Exception;

    public void ajouterUtilisateurALaFormation(Long utilisateurId, Long formationId);

    public void retirerUtilisateurDeLaFormation(Long utilisateurId, Long formationId);

    public void supprimerFormationAvecUtilisateurs(Long formationId);
}
