package com.webapp.ytb.webappytp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.service.UtilisateurService;
import com.webapp.ytb.webappytp.service.FicheService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/fiche")
@Validated
@AllArgsConstructor
public class FicheController {
    private final FicheService ficheService;


    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/create/{utilisateurId}")
    public FicheIntervention create(@Valid @RequestBody FicheIntervention fiche, @PathVariable Long utilisateurId) {
        Utilisateur utilisateur = utilisateurService.findById(utilisateurId);
        fiche.setUtilisateur(utilisateur);
        if (utilisateur != null) {
            return ficheService.creer(fiche);
        } else {
            throw new RuntimeException("Étudiant non trouvé");
        }
    }

    @GetMapping("/read")
    public List<FicheIntervention> read(){
        return ficheService.lire();
    }

    @PutMapping("/update/{id}")
    public FicheIntervention update(@PathVariable Long id, @RequestBody FicheIntervention fiche){
        return ficheService.modifier(id, fiche);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return ficheService.supprimer(id);
    }
}
