package com.webapp.ytb.webappytp.controller;
import java.util.List;
import org.springframework.stereotype.Controller;  // Import the correct Controller annotation
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.service.UtilisateurService;
import com.webapp.ytb.webappytp.service.FicheService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@Controller  // Use @Controller instead of @RestController
@RequestMapping("/fiche")
@Validated
@AllArgsConstructor
public class FicheController {
    private final FicheService ficheService;

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/create/{utilisateurId}")
    public String create(@Valid @ModelAttribute("fiche") FicheIntervention fiche, @PathVariable Long utilisateurId) {
        Utilisateur utilisateur = utilisateurService.findById(utilisateurId);
        fiche.setUtilisateur(utilisateur);
        if (utilisateur != null) {
            ficheService.creer(fiche);
            return "redirect:/fiche/create";  // Redirect to the create form again or any other page
        } else {
            return "redirect:/accueil";
        }
    }

    @GetMapping("/read/{id}")
    public FicheIntervention read(@PathVariable Long id){
        return ficheService.lire(id);
    }

    @PutMapping("/update/{id}")
    public FicheIntervention update(@PathVariable Long id, @RequestBody FicheIntervention fiche){
        return ficheService.modifier(id, fiche);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return ficheService.supprimer(id);
    }

    @GetMapping("/create")
    public String showCreateInterventionForm(Model model) {
        List<Utilisateur> users = utilisateurService.lire();
        model.addAttribute("fiche", new FicheIntervention());
        model.addAttribute("users", users);
        return "creer_intervention";
    }
}
