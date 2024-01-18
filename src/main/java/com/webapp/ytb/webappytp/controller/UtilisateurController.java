package com.webapp.ytb.webappytp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.FicheService;
import com.webapp.ytb.webappytp.service.FormationService;
import com.webapp.ytb.webappytp.service.UtilisateurService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/utilisateur")
@AllArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final FicheService ficheService;
    private final FormationService formationService;

    // Page de création de nouvel utilisateur
    @GetMapping("/nouveau")
    public String afficherFormulaireCreationUtilisateur(Model model) {
        // Récupérez les rôles définis dans l'enum UserRole
        UserRole[] roles = UserRole.values();

        // Récupérez la liste des formations depuis la base de données en utilisant le
        // service
        List<Formation> allFormations = formationService.lire();

        model.addAttribute("roles", roles);
        model.addAttribute("allFormations", allFormations);
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
        UserRole[] roles = UserRole.values();
        model.addAttribute("roles", roles);
        return "modif";
    }

    @PostMapping("/modifier/{id}")
    public String modifierUtilisateur(@PathVariable Long id, @ModelAttribute Utilisateur utilisateur,
            RedirectAttributes redirectAttributes, Model model) {
        utilisateurService.modifier(id, utilisateur);
        // Récupérez les rôles définis dans l'enum UserRole
        UserRole[] roles = UserRole.values();
        model.addAttribute("roles", roles);

        List<Formation> allFormations = formationService.lire();
        model.addAttribute("allFormations", allFormations);
        redirectAttributes.addFlashAttribute("success", true);
        // Ajoutez également les modèles aux attributs flash pour les rendre disponibles
        // après la redirection
        redirectAttributes.addFlashAttribute("roles", roles);
        redirectAttributes.addFlashAttribute("allFormations", allFormations);
        return "redirect:/modif/" + id;
    }

    @PostMapping("/modifmdp/{id}")
    public String modifierMotDePasse(@PathVariable Long id, @ModelAttribute Utilisateur utilisateur,
            RedirectAttributes redirectAttributes) {
        try {
            // Vous pouvez maintenant utiliser utilisateur.getMdp() pour obtenir le mot de
            // passe sélectionné
            utilisateurService.modifierMotDePasse(id, utilisateur.getMdp());
            redirectAttributes.addFlashAttribute("success", true);
            return "redirect:/utilisateur/modifmdp/" + id + "?success=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/utilisateur/modifmdp/" + id + "?error=true";
        }
    }

    @GetMapping("/modifmdp/{id}")
    public String afficherFormulaireModificationMotDePasse(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = utilisateurService.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "mdpmodif";
    }

    // ToDo: Fix the delete method
    @DeleteMapping("/supprimer/{id}")
    public String supprimerUtilisateur(@PathVariable Long id, Model model) {
        utilisateurService.supprimer(id);
        model.addAttribute("success", true);
        return "redirect:/utilisateur/nouveau";
    }

    @GetMapping("/tousLesUtilisateurs")
    public String afficherTousLesUtilisateurs(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.lire();
        model.addAttribute("utilisateurs", utilisateurs);
        return "tousLesUtilisateurs"; // Assurez-vous que le fichier tousLesUtilisateurs.html existe
    }

    @GetMapping("/excel/{userId}")
    public void generateUserExcelArchive(@PathVariable Long userId, HttpServletResponse response) throws Exception {
        Utilisateur utilisateur = utilisateurService.findById(userId);
        if (utilisateur == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String userNameForFile = utilisateur.getNom().replace(" ", "_");
        String userFirstNameForFile = utilisateur.getPrenom().replace(" ", "_");
        String headerValue = "attachment; filename=archive_apprenti_" + userNameForFile + userFirstNameForFile + ".xls";
        response.setHeader(headerKey, headerValue);

        utilisateurService.generatedExcelForUser(userId, response);

        response.flushBuffer();

        // Delete fiches d'intervention for the user
        List<FicheIntervention> fiches = ficheService.getFichesByUserId(userId);
        for (FicheIntervention fiche : fiches) {
            ficheService.supprimer(fiche.getId());
        }

        // Delete the user
        utilisateurService.supprimer(userId);
    }
}
