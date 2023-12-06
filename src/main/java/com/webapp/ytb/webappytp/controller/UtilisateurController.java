package com.webapp.ytb.webappytp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.UtilisateurService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/utilisateur")
@AllArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    // Page de création de nouvel utilisateur
    @GetMapping("/nouveau")
    public String afficherFormulaireCreationUtilisateur(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "nouvelUtilisateur";
    }

    @PostMapping("/creer")
    public String creerUtilisateur(@ModelAttribute @Valid Utilisateur utilisateur, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            // Gérer les erreurs de validation côté serveur
            model.addAttribute("success", false);
            return "redirect:/utilisateur/nouveau?error=true";
        }

        try {
            utilisateurService.creer(utilisateur);
            model.addAttribute("success", true);
            return "redirect:/utilisateur/nouveau?success=true";
        } catch (Exception e) {
            model.addAttribute("success", false);
            return "redirect:/utilisateur/nouveau?error=true";
        }
    }

    @GetMapping("/read")
    @ResponseBody
    public List<Utilisateur> readUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.lire();
        // Ajouter des logs pour débogage
        System.out.println("Nombre d'utilisateurs dans la liste : " + utilisateurs.size());
        return utilisateurs;
    }


    

    @GetMapping("/details/{id}")
    public String afficherDetailsUtilisateur(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = utilisateurService.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "detailsUtilisateur";
    }

    @GetMapping("/profils")
    public String afficherProfils(Model model) {
        List<Utilisateur> profils = utilisateurService.lire();
        model.addAttribute("profils", profils);
        return "accueil";
    }


    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModificationUtilisateur(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = utilisateurService.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "modifierUtilisateur";
    }

    // ToDo: Fix the update method
    @PostMapping("/modifier/{id}")
    public String modifierUtilisateur(@PathVariable Long id, @ModelAttribute @Valid Utilisateur utilisateur,
            Model model) {
        utilisateurService.modifier(id, utilisateur);
        model.addAttribute("success", true);
        return "redirect:/utilisateur/modifier/" + id;
    }

    // ToDo: Fix the delete method
    @DeleteMapping("/supprimer/{id}")
    public String supprimerUtilisateur(@PathVariable Long id, Model model) {
        utilisateurService.supprimer(id);
        model.addAttribute("success", true);
        return "redirect:/utilisateur/nouveau";
    }

}
