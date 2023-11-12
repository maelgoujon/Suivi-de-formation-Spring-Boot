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
import com.webapp.ytb.webappytp.service.UtilisateurService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/utilisateur")
@Validated
@AllArgsConstructor
public class UtilisateurController {
    private final UtilisateurService utilisateurService;


    @PostMapping("/create")
    public Utilisateur create(@Valid @RequestBody Utilisateur utilisateur) {
        return utilisateurService.creer(utilisateur);
    }

    @GetMapping("/read")
    public List<Utilisateur> read(){
        return utilisateurService.lire();
    }

    @PutMapping("/update/{id}")
    public Utilisateur update(@PathVariable Long id, @RequestBody Utilisateur utilisateur){
        return utilisateurService.modifier(id, utilisateur);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return utilisateurService.supprimer(id);
    }

}
