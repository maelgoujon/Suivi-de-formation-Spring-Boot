package com.webapp.ytb.webappytp.controller;

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

    //Ajouter un apprenti
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

    //Ajouter une fiche
    @GetMapping("/ajout_fiche")
    public String ajout_fiche(Model model) {
        model.addAttribute("apprenti", new Utilisateur());
        return "ajout_fiche";
    }
    @PostMapping("/ajouter_fiche")
    public String ajouter_fiche(@ModelAttribute FicheIntervention fiche) {
        ficheServ.creer(fiche);
        return "accueil";
    }

    //Accueils
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


    @GetMapping("/select_fiche")
    public String select_fiche() {
        return "select_fiche";
    }

    //Afficher la fiche no
    @GetMapping("/fiche/{numero}")
    public String fiche(@PathVariable Long numero, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(numero);
        model.addAttribute("ficheIntervention", ficheIntervention);
        return "index";
    }
    //test pour afficher une fiche sans html
    @GetMapping("/ficheIntervention/{numero}")
    public String ficheIntervention(@PathVariable Long numero, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(numero);
        model.addAttribute("ficheIntervention", ficheIntervention);
        return "ficheIntervention";
    }

    @GetMapping("/menu")
    public String menu(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "menu";
    }
}
