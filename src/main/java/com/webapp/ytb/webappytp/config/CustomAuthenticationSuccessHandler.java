package com.webapp.ytb.webappytp.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.webapp.ytb.webappytp.service.UtilisateurServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.debug("Authentification réussie pour l'utilisateur : {}", authentication.getName());
        // Récupération du nom d'utilisateur (login) à partir de l'authentication
        String login = authentication.getName();

        // Réinitialiser le nombre d'essais à 5
        utilisateurService.resetNombreEssais(login);

        // Redirection vers l'URL souhaitée
        getRedirectStrategy().sendRedirect(request, response, "/redirectByRole");
    }
}
