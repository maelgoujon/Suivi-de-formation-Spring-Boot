package com.webapp.ytb.webappytp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Maintenance;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;
import com.webapp.ytb.webappytp.modele.ElementsFiche.MateriauxAmenagement;
import com.webapp.ytb.webappytp.repository.MateriauxAmenagementRepository;
import com.webapp.ytb.webappytp.service.FicheServiceImpl;
import com.webapp.ytb.webappytp.service.UtilisateurServiceImpl;

@Controller
public class HomeController {

    MateriauxAmenagementRepository materiauxAmenagementRepository;

    UtilisateurServiceImpl userServ;
    FicheServiceImpl ficheServ;

    public HomeController(UtilisateurServiceImpl userServ,FicheServiceImpl ficheServ,MateriauxAmenagementRepository materiauxAmenagementRepository) {
        this.userServ = userServ;
        this.ficheServ = ficheServ;
        this.materiauxAmenagementRepository = materiauxAmenagementRepository;
    }

    // Ajouter une fiche
    @GetMapping("/ajout_fiche")
    public String ajout_fiche(Model model) {
        FicheIntervention fiche = new FicheIntervention();
        model.addAttribute("fiche", fiche);
        model.addAttribute("users", userServ.lire());
        return "fiche_a_completer";
    }

    @PostMapping("/ajouter_fiche")
    public String ajouter_fiche(@ModelAttribute FicheIntervention fiche, Model model) {
        fiche.setDateCreation(LocalDate.now());
        FicheIntervention createdFiche = ficheServ.creer(fiche);
        model.addAttribute("createdFiche", createdFiche);
        return "redirect:/fiche/" + createdFiche.getId(); // On affiche la fiche créée
    }

    // Accueils
    @GetMapping("/")
    public String home(Model model) {
        List<Utilisateur> utilisateurs = userServ.getUtilisateursByRole("USER");
        model.addAttribute("utilisateurs", utilisateurs);
        return "accueil";
    }

    @GetMapping("/accueil")
    public String redirectToAccueil(Model model) {
        List<Utilisateur> utilisateurs = userServ.getUtilisateursByRole("USER");
        model.addAttribute("utilisateurs", utilisateurs);
        return "accueil";
    }

    @GetMapping("/accueil_admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        List<Utilisateur> utilisateurs = userServ.getUtilisateursByRole("USER");
        model.addAttribute("utilisateurs", utilisateurs);
        if ("ROLE_ADMIN".equals(role)) {
            return "accueil_admin";
        }
        return "redirect:/accueil";
    }

    @GetMapping("/profil_apprenti/{id}")
    public String redirectToprofil(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = userServ.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "profil_apprenti";
    }

    @GetMapping("/modif/{id}")
    public String modif(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = userServ.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "modif";
    }

    @GetMapping("/mdpmodif/{id}")
    public String mdpmodif(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = userServ.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "mdpmodif";
    }

    // deconnection
    @GetMapping("/log_out")
    public String log_out() {
        return "log_out";
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

    @GetMapping("/suivi_progression/{userId}")
    public String suiviProgression(@PathVariable Long userId, Model model) {
        Utilisateur utilisateur = userServ.findById(userId);
        List<FicheIntervention> fiches = ficheServ.getFichesByUserId(userId);

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("fiches", fiches);
        return "suivi_progression";
    }

    @GetMapping("/presentation")
    public String presentation() {
        return "presentation";
    }

    @GetMapping("/record/{ficheId}")
    public String record(@PathVariable Long ficheId, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(ficheId);
        model.addAttribute("ficheIntervention", ficheIntervention);
        return "record";
    }

    @GetMapping("/mdp_oublie")
    public String mdp_oublie() {
        return "mdp_oublie";
    }

    @GetMapping("/mdp_oublieformateur")
    public String mdp_oublieformateur() {
        return "mdp_oublieformateur";
    }

    @GetMapping("/recordaffichage/{ficheId}")
    public String recordaffichage(@PathVariable Long ficheId, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(ficheId);
        model.addAttribute("ficheIntervention", ficheIntervention);
        return "recordaffichage";
    }

    @GetMapping("/fiche/evaluation/{id}")
    public ResponseEntity<byte[]> getAudioEvaluation(@PathVariable Long id) {
        try {
            FicheIntervention fiche = ficheServ.lire(id);

            if (fiche == null || fiche.getEvaluation() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] audioData = fiche.getEvaluation();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mp3"))
                    .body(audioData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fiche/modifier/{id}")
    public String showFicheDetails(@PathVariable long id, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(id);
        model.addAttribute("ficheIntervention", ficheIntervention);
        return "fiche_modifier";
    }

    @PostMapping("/updateFiche/{id}")
    public String updateFiche(@PathVariable long id,
            @RequestParam String newNomDemandeur,
            @RequestParam LocalDate newDateDemande,
            @RequestParam String newLocalisation,
            @RequestParam String newDescription,
            @RequestParam int newDegreUrgence,
            @RequestParam LocalDate newDateIntervention,
            @RequestParam int newDureeIntervention,
            @RequestParam(required = false) Maintenance.MaintenanceType newMaintenanceType,
            @RequestParam(required = false) Intervention.TypeIntervention newNatureType,
            @RequestParam String newTravauxRealises,
            @RequestParam String newTravauxNonRealises,
            @RequestParam Optional<Boolean> newNouvelleIntervention,
            @RequestParam(required = false) String newMateriau0,
            @RequestParam(required = false) String newMateriau1,
            @RequestParam(required = false) String newMateriau2,
            @RequestParam(required = false) String newMateriau3,
            @RequestParam(required = false) String newMateriau4,
            @RequestParam(required = false) String newMateriau5

    ) {

        boolean nouvelleInterventionValue = newNouvelleIntervention.orElse(false);

        FicheIntervention ficheIntervention = ficheServ.lire(id);
        ficheIntervention.getDemande().setNomDemandeur(newNomDemandeur);
        ficheIntervention.getDemande().setDateDemande(newDateDemande);
        ficheIntervention.getDemande().setLocalisation(newLocalisation);
        ficheIntervention.getDemande().setDescription(newDescription);
        ficheIntervention.getDemande().setDegreUrgence(newDegreUrgence);
        ficheIntervention.getIntervention().setDateIntervention(newDateIntervention);
        ficheIntervention.getIntervention().setDureeIntervention(newDureeIntervention);
        if (newMaintenanceType != null) {
            ficheIntervention.getMaintenance().setMaintenanceType(newMaintenanceType);
        }
        if (newNatureType != null) {
            ficheIntervention.getIntervention().setTypeIntervention(newNatureType);
        }
        ficheIntervention.setTravauxRealises(newTravauxRealises);
        ficheIntervention.setTravauxNonRealises(newTravauxNonRealises);
        ficheIntervention.setNouvelleIntervention(nouvelleInterventionValue);

        ArrayList<String> materiauxOptions = new ArrayList<>();

        if (newMateriau0 != null) {
            materiauxOptions.add(newMateriau0);
        }
        if (newMateriau1 != null) {
            materiauxOptions.add(newMateriau1);
        }
        if (newMateriau2 != null) {
            materiauxOptions.add(newMateriau2);
        }
        if (newMateriau3 != null) {
            materiauxOptions.add(newMateriau3);
        }
        if (newMateriau4 != null) {
            materiauxOptions.add(newMateriau4);
        }
        if (newMateriau5 != null) {
            materiauxOptions.add(newMateriau5);
        }

        if (materiauxOptions.size() > 0) {
            ficheIntervention.setMateriauxOptions(materiauxOptions);
        }

        ficheServ.modifier(id, ficheIntervention);
        return "redirect:/fiche/" + id;
    }

    @GetMapping("/fiche/liste_fiche")
    public String showCreateInterventionForm(Model model) {
        List<Utilisateur> users = userServ.lire();
        List<FicheIntervention> fiches = ficheServ.lireTout(); // Ajout de la liste des fiches
        model.addAttribute("fiche", new FicheIntervention());
        model.addAttribute("users", users);
        model.addAttribute("fiches", fiches); // Ajout de la liste des fiches
        return "liste_fiche";
    }

    @GetMapping("/fiche/modifier2/{id}")
    public String showFicheDetails2(@PathVariable long id, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(id);
        List<Materiaux> materiauxAmenagementList = materiauxAmenagementRepository.findAll();
        model.addAttribute("ficheIntervention", ficheIntervention);
        model.addAttribute("materiauxAmenagementList", materiauxAmenagementList);
        return "fiche_modifier2";
    }


}