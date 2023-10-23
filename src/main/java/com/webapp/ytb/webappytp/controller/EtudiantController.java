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

import com.webapp.ytb.webappytp.modele.Etudiant;
import com.webapp.ytb.webappytp.service.EtudiantService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/etu")
@Validated
@AllArgsConstructor
public class EtudiantController {
    private final EtudiantService etudiantService;


    @PostMapping("/create")
    public Etudiant create(@Valid @RequestBody Etudiant etudiant) {
        return etudiantService.creer(etudiant);
    }

    @GetMapping("/read")
    public List<Etudiant> read(){
        return etudiantService.lire();
    }

    @PutMapping("/update/{id}")
    public Etudiant update(@PathVariable Long id, @RequestBody Etudiant etudiant){
        return etudiantService.modifier(id, etudiant);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return etudiantService.supprimer(id);
    }
}
