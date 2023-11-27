package com.webapp.ytb.webappytp.controller;

import java.time.LocalDate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.FicheServiceImpl;
import com.webapp.ytb.webappytp.service.UtilisateurServiceImpl;

@Controller
public class HomeController {

    UtilisateurServiceImpl userServ;
    FicheServiceImpl ficheServ;

    public HomeController(UtilisateurServiceImpl userServ, FicheServiceImpl ficheServ) {
        this.userServ = userServ;
        this.ficheServ = ficheServ;
    }

    // Ajouter un apprenti
    @GetMapping("/ajout_apprenti")
    public String ajout_apprenti(Model model) {
        model.addAttribute("apprenti", new Utilisateur());
        return "ajout_apprenti";
    }

    @PostMapping("/ajouter_apprenti")
    public String ajouter_apprenti(@ModelAttribute Utilisateur user) {
        user.setRole(UserRole.USER);
        userServ.creer(user);
        return "accueil_admin";
    }

    // Ajouter une fiche
    @GetMapping("/ajout_fiche")
    public String ajout_fiche(Model model) {
        FicheIntervention fiche = new FicheIntervention();
        model.addAttribute("fiche", fiche);
        model.addAttribute("users", userServ.lire()); // Replace with your actual service method
        return "fiche_a_completer";
    }

    @PostMapping("/ajouter_fiche")
    public String ajouter_fiche(@ModelAttribute FicheIntervention fiche) {
        fiche.setDateCreation(LocalDate.now()); // Set the dateCreation to the current date
        ficheServ.creer(fiche);
        return "accueil";
    }

    // Accueils
    @GetMapping("/")
    public String home() {
        return "accueil";
    }

    @GetMapping("/accueil")
    public String redirectToAccueil() {
        return "accueil";
    }

    @GetMapping("/accueil_admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        if ("ROLE_ADMIN".equals(role)) {
            return "accueil_admin";
        }
        return "redirect:/accueil";
    }

    // connection
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // deconnection
    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/select_fiche")
    public String select_fiche() {
        return "select_fiche";
    }

    // Afficher la fiche no
    @GetMapping("/fiche/{id}")
    public String fiche(@PathVariable Long id, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(id);
        model.addAttribute("ficheIntervention", ficheIntervention);
        return "fiche_complete";
    }

    @GetMapping("/suivi_progression")
    public String suivi_progression() {
        return "suivi_progression";
    }

    @GetMapping("/presentation")
    public String presentation() {
        return "presentation";
    } 
    
    @GetMapping("/record")
    public String record() {
        return "record";
    }  

    @GetMapping("/bc")
    public String bc() {
        return "backup_accueil";
    } 

}