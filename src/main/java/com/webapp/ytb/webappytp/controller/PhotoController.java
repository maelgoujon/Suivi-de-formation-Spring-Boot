package com.webapp.ytb.webappytp.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

@Controller
public class PhotoController {
    private final UtilisateurRepository utilisateurRepository;


    public PhotoController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @PostMapping("/upload-photo/{utilisateurId}")
    public String uploadPhoto(@PathVariable Long utilisateurId, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findById(utilisateurId);
            if (optionalUtilisateur.isPresent()) {
                Utilisateur utilisateur = optionalUtilisateur.get();
                utilisateur.setPhoto(file.getBytes());
                utilisateurRepository.save(utilisateur);
            }   
        } catch (IOException e) {
            System.out.println("Erreur dans l envoie de la photo");
        }
        return "redirect:/etu/read";
    }

    @GetMapping("/photo/{utilisateurId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long utilisateurId) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepository.findById(utilisateurId);
        if (optionalUtilisateur.isPresent()) {
            Utilisateur utilisateur = optionalUtilisateur.get();
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(utilisateur.getPhoto());
        }
        return ResponseEntity.notFound().build();
    }
}
