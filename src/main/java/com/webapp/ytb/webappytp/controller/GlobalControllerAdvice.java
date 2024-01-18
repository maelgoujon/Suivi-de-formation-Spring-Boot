package com.webapp.ytb.webappytp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @ModelAttribute("currentUserName")
    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Retourne le nom de l'utilisateur actuellement connecté
    }

    @ModelAttribute("currentUserId")
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = utilisateurRepository.findUserByLogin(authentication.getName());
        return utilisateur.getId(); // Retourne l'id de l'utilisateur actuellement connecté
    }

    @ModelAttribute("currentUserRole")
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = utilisateurRepository.findUserByLogin(authentication.getName());
        return utilisateur.getRole().name(); // Retourne le rôle de l'utilisateur actuellement connecté
    }
}
