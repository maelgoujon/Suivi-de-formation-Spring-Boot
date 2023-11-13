package com.webapp.ytb.webappytp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.ytb.webappytp.modele.UserRole;
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

    @PostMapping("/enregistrer_utilisateur")
    public String enregistrerUtilisateur(@RequestParam("photo") byte[] photo,
                                        @RequestParam("nom") String nom,
                                        @RequestParam("prenom") String prenom,
                                        @RequestParam("date_naissance") String dateNaissance,
                                        @RequestParam("mot_de_passe") String motDePasse,
                                        @RequestParam("confirmer_mot_de_passe") String confirmerMotDePasse,
                                        @RequestParam("description_profil") String descriptionProfil,
                                        @RequestParam("niveau_moyen") Integer niveauMoyen,
                                        @RequestParam("role") String role) {
        
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPhoto(photo);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setMdp(motDePasse);
        utilisateur.setDescription(descriptionProfil);
        utilisateur.setNiveau(niveauMoyen);
        utilisateur.setRole(UserRole.valueOf(role)); // Assurez-vous que UserRole prend en charge cette conversion

        // Enregistrez l'utilisateur dans la base de données (vous devez implémenter cette logique)
        utilisateurService.creer(utilisateur);

        // Rediriger vers une page de confirmation ou d'accueil
        return "redirect:/accueil_admin";
    }

}
