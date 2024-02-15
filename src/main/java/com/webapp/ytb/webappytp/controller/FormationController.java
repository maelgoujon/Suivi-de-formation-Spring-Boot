package com.webapp.ytb.webappytp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.FicheServiceImpl;
import com.webapp.ytb.webappytp.service.FormationServiceImpl;
import com.webapp.ytb.webappytp.service.UtilisateurServiceImpl;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/formation")
public class FormationController {

    private final FormationServiceImpl formationService;
    private final UtilisateurServiceImpl utilisateurService;
    private final FicheServiceImpl ficheService;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String REDIRECT_FORMATION_LISTE = "redirect:/formation/list";

    @Autowired
    public FormationController(FormationServiceImpl formationService, UtilisateurServiceImpl utilisateurService, FicheServiceImpl ficheService) {
        this.formationService = formationService;
        this.utilisateurService = utilisateurService;
        this.ficheService = ficheService;
    }

    private String determineUserRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");
    }

    @PostMapping("/ajouterFormation")
    public String ajouterFormation(@ModelAttribute Formation formation, RedirectAttributes redirectAttributes) {
        try {
            formationService.creer(formation);
            redirectAttributes.addFlashAttribute(SUCCESS, "Formation ajoutée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "Erreur lors de l'ajout de la formation: " + e.getMessage());
        }
        return REDIRECT_FORMATION_LISTE;
    }

    @PostMapping("/supprimerFormation")
    public String supprimerFormation(@RequestParam Long formationId, RedirectAttributes redirectAttributes) {
        try {
            formationService.supprimerFormationAvecUtilisateurs(formationId);
            redirectAttributes.addFlashAttribute(SUCCESS, "Formation supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR,
                    "Erreur lors de la suppression de la formation: " + e.getMessage());
        }
        return REDIRECT_FORMATION_LISTE;
    }

    @PostMapping("/ajouterUtilisateur")
    public String ajouterUtilisateurALaFormation(@RequestParam Long utilisateurId, @RequestParam Long formationId,
            RedirectAttributes redirectAttributes) {
        try {
            formationService.ajouterUtilisateurALaFormation(utilisateurId, formationId);
            redirectAttributes.addFlashAttribute(SUCCESS, "Utilisateur ajouté à la formation avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "Erreur lors de l'ajout: " + e.getMessage());
        }
        return REDIRECT_FORMATION_LISTE;
    }

    @PostMapping("/retirerUtilisateur")
    public String retirerUtilisateurDeLaFormation(@RequestParam Long utilisateurId,
            @RequestParam Long formationId, RedirectAttributes redirectAttributes) {
        try {
            formationService.retirerUtilisateurDeLaFormation(utilisateurId, formationId);
            redirectAttributes.addFlashAttribute(SUCCESS, "Utilisateur retiré de la formation avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "Erreur lors du retrait de l'utilisateur: " + e.getMessage());
        }
        return REDIRECT_FORMATION_LISTE;
    }

    @GetMapping("/list")
    public String afficherFormulaireAjoutUtilisateur(Model model) {
        String utilisateurConnecteRole = determineUserRole();
        model.addAttribute("utilisateurConnecteRole", utilisateurConnecteRole);
        List<Formation> formations = formationService.lire();
        List<Utilisateur> utilisateurs = utilisateurService.lire();
        model.addAttribute("formations", formations);
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("newFormation", new Formation());

        // Ajouter la liste des formations pour le formulaire de création d'utilisateur
        List<Formation> allFormations = formationService.lire();
        model.addAttribute("allFormations", allFormations);

        return "liste_formations";
    }

    @GetMapping("/excel/{idFormation}")
    public void generateExcelArchive(@PathVariable Long idFormation, HttpServletResponse response) throws Exception {
        // Création du fichier excel
        Formation formation = formationService.findById(idFormation);

        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String formationNameForFile = formation.getNom().replace(" ", "_");
        String headerValue = "attachment; filename=archive_formation_" + formationNameForFile + ".xls";

        response.setHeader(headerKey, headerValue);

        formationService.generatedExcel(idFormation, response);

        response.flushBuffer();
        // Suppression des éléments liés à la formation
        // supprimer les users liés à la formation (2) ainsi que les fiches liés à
        // l'user (1)

        // Retrieve all users associated with the formation
        List<Utilisateur> utilisateurs = utilisateurService.findUserByFormation(idFormation);

        // (1) Delete fiches d'intervention for each user
        for (Utilisateur utilisateur : utilisateurs) {
            List<FicheIntervention> fiches = ficheService.getFichesByUserId(utilisateur.getId());
            for (FicheIntervention fiche : fiches) {
                ficheService.supprimer(fiche.getId());
            }
        }

        // (2) Delete users associated with the formation
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getRole() == UserRole.USER) {
                utilisateurService.supprimer(utilisateur.getId());
            }
        }
    }

}
