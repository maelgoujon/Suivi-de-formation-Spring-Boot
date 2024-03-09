package com.webapp.ytb.webappytp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.UtilisateurServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UtilisateurServiceImpl utilisateurService; // Injectez votre UtilisateurService ici
    
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String login = request.getParameter("username");
        
        log.debug("failure réussie pour l'utilisateur : {}", login);
        // Utilisez la méthode de service pour décrémenter le nombre d'essais
        utilisateurService.decrementerNombreEssais(login);
        
        // Obtenez le nombre d'essais restants pour afficher dans le message
        int essaisRestants = utilisateurService.getNombreEssais(login);
        
        // Construisez le message d'erreur avec le nombre d'essais restants
        String errorMessage = "Mot de passe invalide. Il vous reste " + essaisRestants + " essais.";
        
        // Stockez le message d'erreur dans la requête pour l'afficher dans la page de connexion
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", errorMessage);
        request.getSession().setAttribute("NOMBRE_ESSAIS", essaisRestants);

        // Redirigez vers la page de login avec un paramètre d'erreur
        getRedirectStrategy().sendRedirect(request, response, "/accueil?error=true");
    }
}
