package com.webapp.ytb.webappytp.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller; // Import the correct Controller annotation
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.service.UtilisateurService;
import com.webapp.ytb.webappytp.service.FicheService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/fiche")
@Validated
@AllArgsConstructor
public class FicheController {
    private final FicheService ficheService;

    private final UtilisateurService utilisateurService;

    @Autowired
    public FicheController(UtilisateurService utilisateurService, FicheService ficheService) {
        this.utilisateurService = utilisateurService;
        this.ficheService = ficheService;
    }

    @PostMapping("/create/{utilisateurId}")
    public String create(@Valid @ModelAttribute("fiche") FicheIntervention fiche, @PathVariable Long utilisateurId) {
        Utilisateur utilisateur = utilisateurService.findById(utilisateurId);
        fiche.setUtilisateur(utilisateur);
        if (utilisateur != null) {
            ficheService.creer(fiche);
            return "redirect:/ajoutfiche/" + utilisateurId; // Redirect to the create form again or any other page
        } else {
            return "redirect:/ajoutfiche/" + utilisateurId;
        }
    }

    @GetMapping("/read/{id}")
    public FicheIntervention read(@PathVariable Long id) {
        return ficheService.lire(id);
    }

    @PutMapping("/update/{id}")
    public FicheIntervention update(@PathVariable Long id, @RequestBody FicheIntervention fiche) {
        return ficheService.modifier(id, fiche);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        return ficheService.supprimer(id);
    }

    @GetMapping("/creer_intervention")
    public String showCreateInterventionForm(Model model) {
        List<Utilisateur> users = utilisateurService.lire();
        model.addAttribute("fiche", new FicheIntervention());
        model.addAttribute("users", users);
        return "creer_intervention";
    }

    @PostMapping("/enregistrerAudio")
    @ResponseBody
    public ResponseEntity<String> enregistrerAudio(@RequestParam("ficheId") Long ficheId,
            @RequestPart("audioFile") MultipartFile audioFile) {
        try {
            // Vérifiez si la fiche existe
            FicheIntervention fiche = ficheService.lire(ficheId);

            if (fiche == null) {
                return ResponseEntity.badRequest().body("La fiche avec l'ID " + ficheId + " n'existe pas.");
            }

            // Enregistrez le fichier audio dans la fiche
            fiche.setEvaluation(audioFile.getBytes());
            ficheService.modifier(ficheId, fiche);

            return ResponseEntity.ok().body("Enregistrement audio réussi pour la fiche avec l'ID " + ficheId);
        } catch (IOException e) {
            // Gérez les erreurs d'entrée/sortie
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement de l'audio pour la fiche avec l'ID " + ficheId);
        }
    }
}
