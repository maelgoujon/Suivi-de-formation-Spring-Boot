package com.webapp.ytb.webappytp.controller;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

import org.hibernate.boot.model.naming.ImplicitJoinColumnNameSource.Nature;
import org.hibernate.mapping.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Maintenance;
import com.webapp.ytb.webappytp.modele.ElementsFiche.NatureIntervention;
import com.webapp.ytb.webappytp.repository.FicheRepository;
import com.webapp.ytb.webappytp.service.FicheService;
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

    // Ajouter une fiche
    @GetMapping("/ajout_fiche")
    public String ajout_fiche(Model model) {
        FicheIntervention fiche = new FicheIntervention();
        model.addAttribute("fiche", fiche);
        model.addAttribute("users", userServ.lire()); // Replace with your actual service method
        return "fiche_a_completer";
    }

    @PostMapping("/ajouter_fiche")
    public String ajouter_fiche(@ModelAttribute FicheIntervention fiche, Model model) {
        FicheIntervention createdFiche = ficheServ.creer(fiche);
        model.addAttribute("createdFiche", createdFiche);
        return "redirect:/fiche/" + createdFiche.getId(); // On affiche la fiche créée
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

    @GetMapping("/mdp_oublie")
    public String mdp_oublie() {
        return "mdp_oublie";
    }

    @GetMapping("/recordaffichage")
    public String recordaffichage() {
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

    @GetMapping("/ficheA/{id}")
    public String showFicheDetails(@PathVariable long id, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(id);
        model.addAttribute("ficheIntervention", ficheIntervention);
        return "ficheDetails";
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
            @RequestParam Maintenance.MaintenanceType newMaintenanceType,
            @RequestParam NatureIntervention.NatureType newNatureType,
            @RequestParam String newTravauxRealises,
            @RequestParam String newTravauxNonRealises,
            @RequestParam Optional<Boolean> newNouvelleIntervention,
            @RequestParam String newMateriau0,
            @RequestParam String newMateriau1,
            @RequestParam String newMateriau2,
            @RequestParam String newMateriau3,
            @RequestParam String newMateriau4,
            @RequestParam String newMateriau5
            
            ) {

        // Utilisez newNouvelleIntervention.orElse(false) pour obtenir la valeur du paramètre
        boolean nouvelleInterventionValue = newNouvelleIntervention.orElse(false);

        FicheIntervention ficheIntervention = ficheServ.lire(id);
        ficheIntervention.getDemande().setNomDemandeur(newNomDemandeur);
        ficheIntervention.getDemande().setDateDemande(newDateDemande);
        ficheIntervention.getDemande().setLocalisation(newLocalisation);
        ficheIntervention.getDemande().setDescription(newDescription);
        ficheIntervention.getDemande().setDegreUrgence(newDegreUrgence);
        ficheIntervention.getIntervention().setDateIntervention(newDateIntervention);
        ficheIntervention.getIntervention().setDureeIntervention(newDureeIntervention);
        ficheIntervention.getMaintenance().setMaintenanceType(newMaintenanceType);
        ficheIntervention.getNatureIntervention().setNatureType(newNatureType);
        ficheIntervention.setTravauxRealises(newTravauxRealises);
        ficheIntervention.setTravauxNonRealises(newTravauxNonRealises);
        ficheIntervention.setNouvelleIntervention(nouvelleInterventionValue);


        ArrayList<String> materiauxOptions = new ArrayList<>();
        materiauxOptions = (ArrayList<String>) ficheIntervention.getMateriauxOptions();
        materiauxOptions.set(0, newMateriau0);
        materiauxOptions.set(1, newMateriau1);
        materiauxOptions.set(2, newMateriau2);
        materiauxOptions.set(3, newMateriau3);
        materiauxOptions.set(4, newMateriau4);
        materiauxOptions.set(5, newMateriau5);
        
        ficheIntervention.setMateriauxOptions(materiauxOptions);
        
        ficheServ.modifier(id, ficheIntervention);
        return "redirect:/fiche/" + id;
    }

}