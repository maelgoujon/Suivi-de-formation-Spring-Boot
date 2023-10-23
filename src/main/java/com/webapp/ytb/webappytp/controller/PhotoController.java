package com.webapp.ytb.webappytp.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.ytb.webappytp.modele.Etudiant;
import com.webapp.ytb.webappytp.repository.EtudiantRepository;

@Controller
public class PhotoController {
    private final EtudiantRepository etudiantRepository;

    @Autowired
    public PhotoController(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    @PostMapping("/upload-photo/{etudiantId}")
    public String uploadPhoto(@PathVariable Long etudiantId, @RequestParam("file") MultipartFile file) {
        try {
            Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(etudiantId);
            if (optionalEtudiant.isPresent()) {
                Etudiant etudiant = optionalEtudiant.get();
                etudiant.setPhoto(file.getBytes());
                etudiantRepository.save(etudiant);
            }   
        } catch (IOException e) {
            System.out.println("Erreur dans l envoie de la photo");
        }
        return "redirect:/etu/read";
    }

    @GetMapping("/photo/{etudiantId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long etudiantId) {
        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(etudiantId);
        if (optionalEtudiant.isPresent()) {
            Etudiant etudiant = optionalEtudiant.get();
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(etudiant.getPhoto());
        }
        return ResponseEntity.notFound().build();
    }
}
