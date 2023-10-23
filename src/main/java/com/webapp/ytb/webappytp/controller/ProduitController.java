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
import com.webapp.ytb.webappytp.service.ProduitService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/produit")
@AllArgsConstructor
public class ProduitController {
    private final ProduitService produitService;


    @PostMapping("/create")
    public Etudiant create(@Valid @RequestBody Etudiant produit) {
        return produitService.creer(produit);
    }

    @GetMapping("/read")
    public List<Etudiant> read(){
        return produitService.lire();
    }

    @PutMapping("/update/{id}")
    public Etudiant update(@PathVariable Long id, @RequestBody Etudiant produit){
        return produitService.modifier(id, produit);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return produitService.supprimer(id);
    }
}
