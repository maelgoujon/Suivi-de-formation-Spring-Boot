package com.webapp.ytb.webappytp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Utilisateur user = utilisateurRepository.findUserByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getLogin())
                        .password(user.getMdp()) // Ici, le mot de passe est déjà encodé
                        .roles(user.getRole().toString())
                        .build();

        return userDetails;
    }
}
