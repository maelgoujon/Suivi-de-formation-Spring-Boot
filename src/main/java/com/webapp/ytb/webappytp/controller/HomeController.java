package com.webapp.ytb.webappytp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Demande;
import com.webapp.ytb.webappytp.modele.ElementsFiche.ImagesTitres;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervenant;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Maintenance;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;
import com.webapp.ytb.webappytp.repository.FicheRepository;
import com.webapp.ytb.webappytp.repository.ImagesTitresRepository;
import com.webapp.ytb.webappytp.repository.MateriauxAmenagementRepository;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;
import com.webapp.ytb.webappytp.service.FicheServiceImpl;
import com.webapp.ytb.webappytp.service.UtilisateurServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import com.webapp.ytb.webappytp.service.FormationService;

@Controller
public class HomeController {

    MateriauxAmenagementRepository materiauxAmenagementRepository;
    UtilisateurServiceImpl userServ;
    FicheServiceImpl ficheServ;
    ImagesTitresRepository imagesTitresRepository;
    FicheRepository ficheRepository;
    UtilisateurRepository utilisateurRepository;

    public HomeController(UtilisateurServiceImpl userServ, FicheServiceImpl ficheServ,
            MateriauxAmenagementRepository materiauxAmenagementRepository,
            ImagesTitresRepository imagesTitresRepository, FicheRepository ficheRepository,
            UtilisateurRepository utilisateurRepository) {
        this.userServ = userServ;
        this.ficheServ = ficheServ;
        this.materiauxAmenagementRepository = materiauxAmenagementRepository;
        this.imagesTitresRepository = imagesTitresRepository;
        this.ficheRepository = ficheRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = utilisateurRepository.findUserByLogin(authentication.getName());
        return utilisateur.getId(); // Retourne l'id de l'utilisateur actuellement connecté
    }

    // -----------------------------------------//
    // ------------Fiche Intervention-----------//
    // -----------------------------------------//

    // ajouter des icones à une categorie de la fiche
    @GetMapping("/fiche/icones/{categorie}")
    public String showIcones(@PathVariable String categorie, Model model) {
        List<ImagesTitres> imagesTitres = imagesTitresRepository.findByTypeImage(ImagesTitres.TypeImage.INTERVENANT);

        model.addAttribute("imagesTitres", imagesTitres);
        model.addAttribute("image", new ImagesTitres()); // Ajout d'une instance d'image pour le formulaire
        return "icones_ficheintervention";
    }

    @PostMapping("/fiche/icones/upload")
    public String handleFileUpload(@ModelAttribute("image") ImagesTitres image,
            @RequestParam("imageFile") MultipartFile file,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Veuillez sélectionner un fichier à uploader");
            return "redirect:/fiche/icones/upload";
        }

        try {
            // Obtenir le chemin réel du répertoire /images/icones/
            ServletContext context = request.getServletContext();
            String realPath = context.getRealPath("/images/icones/");
            Path path = Paths.get(realPath + File.separator + file.getOriginalFilename());

            // Assurez-vous que le répertoire existe
            Files.createDirectories(path.getParent());

            // Sauvegarder le fichier
            Files.write(path, file.getBytes());

            // Mettez à jour l'URL de l'image dans la base de données
            image.setImageUrl("/images/icones/" + file.getOriginalFilename());

            redirectAttributes.addFlashAttribute("message",
                    "Vous avez réussi à uploader '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Erreur lors de l'upload du fichier");
        }

        return "redirect:/fiche/icones/upload";
    }

    // Ajouter une fiche
    @GetMapping("/ajout_fiche")
    public String ajout_fiche(Model model) {
        FicheIntervention fiche = new FicheIntervention();
        List<ImagesTitres> imagesTitreIntervenant = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT);
        List<ImagesTitres> imagesTitreDemande = imagesTitresRepository.findByTypeImage(ImagesTitres.TypeImage.DEMANDE);
        List<ImagesTitres> imagesTitreIntervention = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION);
        List<ImagesTitres> imagesTitreTravauxRealises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.TRAVAUX_REALISES);
        List<ImagesTitres> imagesTitreTravauxNonRealises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.TRAVAUX_NON_REALISES);
        List<ImagesTitres> imagesTitreMateriauxUtilises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.MATERIAUX_UTILISES);
        List<ImagesTitres> imagesTitreIntervenantPrenom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT_PRENOM);
        List<ImagesTitres> imagesTitreIntervenantNom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT_NOM);
        List<ImagesTitres> imagesTitreDemandeNom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_NOM);
        List<ImagesTitres> imagesTitreDemandeDegreUrgence = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DEGRE_URGENCE);
        List<ImagesTitres> imagesTitreDemandeDate = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DATE);
        List<ImagesTitres> imagesTitreDemandeLocalisation = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_LOCALISATION);
        List<ImagesTitres> imagesTitreDemandeDescription = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DESCRIPTION);
        List<ImagesTitres> imagesTitreInterventionDate = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_DATE);
        List<ImagesTitres> imagesTitreInterventionDuree = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_DUREE);
        List<ImagesTitres> imagesTitreInterventionType = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_TYPE);
        List<ImagesTitres> imagesTitreMaintenanceType = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.MAINTENANCE_TYPE);

        model.addAttribute("imagesTitreIntervenant", imagesTitreIntervenant);
        model.addAttribute("imagesTitreDemande", imagesTitreDemande);
        model.addAttribute("imagesTitreIntervention", imagesTitreIntervention);
        model.addAttribute("imagesTitreTravauxRealises", imagesTitreTravauxRealises);
        model.addAttribute("imagesTitreTravauxNonRealises", imagesTitreTravauxNonRealises);
        model.addAttribute("imagesTitreMateriauxUtilises", imagesTitreMateriauxUtilises);
        model.addAttribute("imagesTitreIntervenantPrenom", imagesTitreIntervenantPrenom);
        model.addAttribute("imagesTitreIntervenantNom", imagesTitreIntervenantNom);
        model.addAttribute("imagesTitreDemandeNom", imagesTitreDemandeNom);
        model.addAttribute("imagesTitreDemandeDegreUrgence", imagesTitreDemandeDegreUrgence);
        model.addAttribute("imagesTitreDemandeDate", imagesTitreDemandeDate);
        model.addAttribute("imagesTitreDemandeLocalisation", imagesTitreDemandeLocalisation);
        model.addAttribute("imagesTitreDemandeDescription", imagesTitreDemandeDescription);
        model.addAttribute("imagesTitreInterventionDate", imagesTitreInterventionDate);
        model.addAttribute("imagesTitreInterventionDuree", imagesTitreInterventionDuree);
        model.addAttribute("imagesTitreInterventionType", imagesTitreInterventionType);
        model.addAttribute("imagesTitreMaintenanceType", imagesTitreMaintenanceType);

        model.addAttribute("fiche", fiche);
        model.addAttribute("users", userServ.getUtilisateursByRole("USER"));
        return "fiche_a_completer";
    }

    // Ajouter une fiche avec id utilisateur
    @GetMapping("/ajout_fiche/{id}")
    public String ajout_fiche_id(Model model, @PathVariable Long id) {
        FicheIntervention fiche = new FicheIntervention();
        List<ImagesTitres> imagesTitreIntervenant = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT);
        List<ImagesTitres> imagesTitreDemande = imagesTitresRepository.findByTypeImage(ImagesTitres.TypeImage.DEMANDE);
        List<ImagesTitres> imagesTitreIntervention = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION);
        List<ImagesTitres> imagesTitreTravauxRealises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.TRAVAUX_REALISES);
        List<ImagesTitres> imagesTitreTravauxNonRealises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.TRAVAUX_NON_REALISES);
        List<ImagesTitres> imagesTitreMateriauxUtilises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.MATERIAUX_UTILISES);
        List<ImagesTitres> imagesTitreIntervenantPrenom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT_PRENOM);
        List<ImagesTitres> imagesTitreIntervenantNom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT_NOM);
        List<ImagesTitres> imagesTitreDemandeNom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_NOM);
        List<ImagesTitres> imagesTitreDemandeDegreUrgence = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DEGRE_URGENCE);
        List<ImagesTitres> imagesTitreDemandeDate = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DATE);
        List<ImagesTitres> imagesTitreDemandeLocalisation = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_LOCALISATION);
        List<ImagesTitres> imagesTitreDemandeDescription = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DESCRIPTION);
        List<ImagesTitres> imagesTitreInterventionDate = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_DATE);
        List<ImagesTitres> imagesTitreInterventionDuree = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_DUREE);
        List<ImagesTitres> imagesTitreInterventionType = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_TYPE);
        List<ImagesTitres> imagesTitreMaintenanceType = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.MAINTENANCE_TYPE);

        model.addAttribute("imagesTitreIntervenant", imagesTitreIntervenant);
        model.addAttribute("imagesTitreDemande", imagesTitreDemande);
        model.addAttribute("imagesTitreIntervention", imagesTitreIntervention);
        model.addAttribute("imagesTitreTravauxRealises", imagesTitreTravauxRealises);
        model.addAttribute("imagesTitreTravauxNonRealises", imagesTitreTravauxNonRealises);
        model.addAttribute("imagesTitreMateriauxUtilises", imagesTitreMateriauxUtilises);
        model.addAttribute("imagesTitreIntervenantPrenom", imagesTitreIntervenantPrenom);
        model.addAttribute("imagesTitreIntervenantNom", imagesTitreIntervenantNom);
        model.addAttribute("imagesTitreDemandeNom", imagesTitreDemandeNom);
        model.addAttribute("imagesTitreDemandeDegreUrgence", imagesTitreDemandeDegreUrgence);
        model.addAttribute("imagesTitreDemandeDate", imagesTitreDemandeDate);
        model.addAttribute("imagesTitreDemandeLocalisation", imagesTitreDemandeLocalisation);
        model.addAttribute("imagesTitreDemandeDescription", imagesTitreDemandeDescription);
        model.addAttribute("imagesTitreInterventionDate", imagesTitreInterventionDate);
        model.addAttribute("imagesTitreInterventionDuree", imagesTitreInterventionDuree);
        model.addAttribute("imagesTitreInterventionType", imagesTitreInterventionType);
        model.addAttribute("imagesTitreMaintenanceType", imagesTitreMaintenanceType);

        model.addAttribute("fiche", fiche);
        // si l id correspond a un role admin cip ou educsimple on n'affiche pas la page
        if (userServ.findById(id).getRole().equals(UserRole.ADMIN)
                || userServ.findById(id).getRole().equals(UserRole.CIP)
                || userServ.findById(id).getRole().equals(UserRole.EDUCSIMPLE)) {
            return "redirect:/accueil_admin";
        }
        model.addAttribute("users", userServ.findById(id));
        return "fiche_a_completer";
    }

    @PostMapping("/ajouter_fiche")
    public String ajouter_fiche(@ModelAttribute FicheIntervention fiche, Model model) {
        fiche.setDateCreation(LocalDate.now());
        fiche.setMateriauxOptions(new ArrayList<>());
        Demande demande = new Demande();
        Intervenant intervenant = new Intervenant();
        Intervention intervention = new Intervention();
        Maintenance maintenance = new Maintenance();
        // demande
        demande.setNomDemandeur(fiche.getDemande().getNomDemandeur());
        demande.setDateDemande(fiche.getDemande().getDateDemande());
        demande.setLocalisation(fiche.getDemande().getLocalisation());
        demande.setDescription(fiche.getDemande().getDescription());
        demande.setDegreUrgence(fiche.getDemande().getDegreUrgence());
        demande.setCouleurTitreDemande(fiche.getDemande().getCouleurTitreDemande());
        demande.setNiveauTitreDemande(fiche.getDemande().getNiveauTitreDemande());
        demande.setImageTitreDemandeUrl(fiche.getDemande().getImageTitreDemandeUrl());
        demande.setNiveauTitreDemandeNom(fiche.getDemande().getNiveauTitreDemandeNom());
        demande.setImageTitreDemandeNomUrl(fiche.getDemande().getImageTitreDemandeNomUrl());
        demande.setCouleurNomDemandeur(fiche.getDemande().getCouleurNomDemandeur());
        demande.setCouleurDegreUrgence(fiche.getDemande().getCouleurDegreUrgence());
        demande.setCouleurDateDemande(fiche.getDemande().getCouleurDateDemande());
        demande.setCouleurLocalisation(fiche.getDemande().getCouleurLocalisation());
        demande.setCouleurDescription(fiche.getDemande().getCouleurDescription());
        demande.setNiveauNomDemandeur(fiche.getDemande().getNiveauNomDemandeur());
        demande.setNiveauDateDemande(fiche.getDemande().getNiveauDateDemande());
        demande.setNiveauLocalisation(fiche.getDemande().getNiveauLocalisation());
        demande.setNiveauDescription(fiche.getDemande().getNiveauDescription());
        demande.setNiveauDegreUrgence(fiche.getDemande().getNiveauDegreUrgence());
        demande.setNiveauTitreDemandeDegreUrgence(fiche.getDemande().getNiveauTitreDemandeDegreUrgence());
        demande.setNiveauTitreDemandeDate(fiche.getDemande().getNiveauTitreDemandeDate());
        demande.setNiveauTitreDemandeLocalisation(fiche.getDemande().getNiveauTitreDemandeLocalisation());
        demande.setNiveauTitreDemandeDescription(fiche.getDemande().getNiveauTitreDemandeDescription());
        demande.setImageTitreDemandeDegreUrgenceUrl(fiche.getDemande().getImageTitreDemandeDegreUrgenceUrl());
        demande.setImageTitreDemandeDateUrl(fiche.getDemande().getImageTitreDemandeDateUrl());
        demande.setImageTitreDemandeLocalisationUrl(fiche.getDemande().getImageTitreDemandeLocalisationUrl());
        demande.setImageTitreDemandeDescriptionUrl(fiche.getDemande().getImageTitreDemandeDescriptionUrl());
        // maintenance
        maintenance.setMaintenanceType(fiche.getMaintenance().getMaintenanceType());
        maintenance.setNiveauMaintenanceType(fiche.getMaintenance().getNiveauMaintenanceType());
        maintenance.setCouleurMaintenanceType(fiche.getMaintenance().getCouleurMaintenanceType());
        maintenance.setImageTypeMaintenanceUrl(fiche.getMaintenance().getImageTypeMaintenanceUrl());

        // intervention
        intervention.setNiveauTitreIntervention(fiche.getIntervention().getNiveauTitreIntervention());
        intervention.setCouleurTitreIntervention(fiche.getIntervention().getCouleurTitreIntervention());
        intervention.setImageTitreInterventionUrl(fiche.getIntervention().getImageTitreInterventionUrl());
        intervention.setNiveauDateIntervention(fiche.getIntervention().getNiveauDateIntervention());
        intervention.setCouleurDateIntervention(fiche.getIntervention().getCouleurDateIntervention());
        intervention.setImageDateInterventionUrl(fiche.getIntervention().getImageDateInterventionUrl());
        intervention.setNiveauDureeIntervention(fiche.getIntervention().getNiveauDureeIntervention());
        intervention.setCouleurDureeIntervention(fiche.getIntervention().getCouleurDureeIntervention());
        intervention.setImageDureeInterventionUrl(fiche.getIntervention().getImageDureeInterventionUrl());
        intervention.setNiveauTypeIntervention(fiche.getIntervention().getNiveauTypeIntervention());
        intervention.setCouleurTypeIntervention(fiche.getIntervention().getCouleurTypeIntervention());
        intervention.setImageTypeInterventionUrl(fiche.getIntervention().getImageTypeInterventionUrl());

        // maintenance
        if (fiche.getMaintenance() != null && fiche.getMaintenance().getMaintenanceType() != null) {
            maintenance.setMaintenanceType(fiche.getMaintenance().getMaintenanceType());
        }
        // intervenant
        intervenant.setCouleurNom(fiche.getIntervenant().getCouleurNom());
        intervenant.setCouleurPrenom(fiche.getIntervenant().getCouleurPrenom());
        intervenant.setCouleurTitreIntervenant(fiche.getIntervenant().getCouleurTitreIntervenant());
        intervenant.setNiveauTitreIntervenant(fiche.getIntervenant().getNiveauTitreIntervenant());
        intervenant.setImageTitreIntervenantUrl(fiche.getIntervenant().getImageTitreIntervenantUrl());
        intervenant.setNiveauTitreIntervenantNom(fiche.getIntervenant().getNiveauTitreIntervenantNom());
        intervenant.setNiveauTitreIntervenantPrenom(fiche.getIntervenant().getNiveauTitreIntervenantPrenom());
        intervenant.setImageTitreIntervenantNomUrl(fiche.getIntervenant().getImageTitreIntervenantNomUrl());
        intervenant.setImageTitreIntervenantPrenomUrl(fiche.getIntervenant().getImageTitreIntervenantPrenomUrl());
        // Travaux
        fiche.setNiveauTravauxRealises(fiche.getNiveauTravauxRealises());
        fiche.setNiveauTravauxNonRealises(fiche.getNiveauTravauxNonRealises());
        fiche.setImageTitreTravauxRealisesUrl(fiche.getImageTitreTravauxRealisesUrl());
        // Materiaux
        fiche.setNiveauMateriauxUtilises(fiche.getNiveauMateriauxUtilises());
        fiche.setImageTitreMateriauxUtilisesUrl(fiche.getImageTitreMateriauxUtilisesUrl());

        // on mets les objets dans la fiche
        fiche.setDemande(demande);
        fiche.setIntervenant(intervenant);
        fiche.setIntervention(intervention);
        fiche.setMaintenance(maintenance);
        FicheIntervention createdFiche = ficheServ
                .creer(fiche);
        model.addAttribute("createdFiche", createdFiche);
        return "redirect:/fiche/" + createdFiche.getId();
    }

    // Afficher la fiche n°
    @GetMapping("/fiche/{id}")
    public String fiche(@PathVariable Long id, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(id);
        String typeInterventionStr = ficheIntervention.getIntervention().getTypeIntervention();
        Intervention.TypeIntervention typeIntervention = null;
        if (typeInterventionStr != null) {
            typeIntervention = Intervention.TypeIntervention.valueOf(typeInterventionStr);
        }
        List<Materiaux> materiauxAmenagementList = materiauxAmenagementRepository
                .findByTypeIntervention(typeIntervention);
        model.addAttribute("ficheIntervention", ficheIntervention);
        model.addAttribute("materiauxAmenagementList", materiauxAmenagementList);
        model.addAttribute("color", "#8fabd9");
        return "fiche_complete";
    }

    // modifier la fiche no
    @GetMapping("/fiche/modifier/{id}")
    public String showFicheDetails(@PathVariable long id, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(id);

        // Utiliser directement le type d'intervention de la fiche
        String typeInterventionStr = ficheIntervention.getIntervention().getTypeIntervention();
        if (typeInterventionStr != null) {
            List<Materiaux> materiauxAmenagementList = materiauxAmenagementRepository.findByTypeIntervention(
                    Intervention.TypeIntervention.valueOf(typeInterventionStr));

            model.addAttribute("materiauxAmenagementList", materiauxAmenagementList);
        }

        model.addAttribute("ficheIntervention", ficheIntervention);
        model.addAttribute("color", "#8fabd9");

        return "fiche_modifier";
    }

    @PostMapping("/updateFiche/{id}")
    public String updateFiche(@PathVariable long id,
            @RequestParam(required = false) String newNomDemandeur,
            @RequestParam(required = false) LocalDate newDateDemande,
            @RequestParam(required = false) String newLocalisation,
            @RequestParam(required = false) String newDescription,
            @RequestParam(required = false) Integer newDegreUrgence,
            @RequestParam(required = false) LocalDate newDateIntervention,
            @RequestParam(required = false) Integer newDureeIntervention,
            @RequestParam(required = false) Maintenance.MaintenanceType newMaintenanceType,
            @RequestParam(required = false) String newNatureType,
            @RequestParam(required = false) String newTravauxRealises,
            @RequestParam(required = false) String newTravauxNonRealises,
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
        if (newNomDemandeur != null) {
            ficheIntervention.getDemande().setNomDemandeur(newNomDemandeur);
        }
        if (newDateDemande != null) {
            ficheIntervention.getDemande().setDateDemande(newDateDemande);
        }
        if (newLocalisation != null) {
            ficheIntervention.getDemande().setLocalisation(newLocalisation);
        }
        if (newDescription != null) {
            ficheIntervention.getDemande().setDescription(newDescription);
        }
        if (newDegreUrgence != null) {
            ficheIntervention.getDemande().setDegreUrgence(newDegreUrgence);
        }

        if (newDateIntervention != null) {
            ficheIntervention.getIntervention().setDateIntervention(newDateIntervention);
        }

        if (newDureeIntervention != null) {
            ficheIntervention.getIntervention().setDureeIntervention(newDureeIntervention);
        }

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

        if (!materiauxOptions.isEmpty()) {
            ficheIntervention.setMateriauxOptions(materiauxOptions);
        }

        ficheServ.modifier(id, ficheIntervention);
        return "redirect:/fiche/" + id;
    }

    @PostMapping("/fiche/updateTypeIntervention/{id}")
    public String updateFicheTypeIntervention(@PathVariable long id,
            @RequestParam(required = false) String newNomDemandeur,
            @RequestParam(required = false) LocalDate newDateDemande,
            @RequestParam(required = false) String newLocalisation,
            @RequestParam(required = false) String newDescription,
            @RequestParam(required = false) Integer newDegreUrgence,
            @RequestParam(required = false) LocalDate newDateIntervention,
            @RequestParam(required = false) Integer newDureeIntervention,
            @RequestParam(required = false) Maintenance.MaintenanceType newMaintenanceType,
            @RequestParam(required = false) String newNatureType,
            @RequestParam(required = false) String newTravauxRealises,
            @RequestParam(required = false) String newTravauxNonRealises,
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
        if (newNomDemandeur != null) {
            ficheIntervention.getDemande().setNomDemandeur(newNomDemandeur);
        }
        if (newDateDemande != null) {
            ficheIntervention.getDemande().setDateDemande(newDateDemande);
        }
        if (newLocalisation != null) {
            ficheIntervention.getDemande().setLocalisation(newLocalisation);
        }
        if (newDescription != null) {
            ficheIntervention.getDemande().setDescription(newDescription);
        }
        if (newDegreUrgence != null) {
            ficheIntervention.getDemande().setDegreUrgence(newDegreUrgence);
        }

        if (newDateIntervention != null) {
            ficheIntervention.getIntervention().setDateIntervention(newDateIntervention);
        }

        if (newDureeIntervention != null) {
            ficheIntervention.getIntervention().setDureeIntervention(newDureeIntervention);
        }

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

        if (!materiauxOptions.isEmpty()) {
            ficheIntervention.setMateriauxOptions(materiauxOptions);
        }

        ficheServ.modifier(id, ficheIntervention);
        return "redirect:/fiche/modifier/" + id;
    }

    // -----------------------------------------//
    // ------------Accueils---------------------//
    // -----------------------------------------//
    @GetMapping("/")
    public String home(Model model) {
        List<Utilisateur> utilisateurs = userServ.getUtilisateursByRole("USER");
        model.addAttribute("utilisateurs", utilisateurs);
        return "accueil";
    }

    @GetMapping("/redirectByRole")
    public String redirectByRole() {
        String utilisateurConnecteRole = determineUserRole();

        if ("ROLE_SUPERADMIN".equals(utilisateurConnecteRole)) {
            return "redirect:/accueil_superadmin";
        } else if ("ROLE_ADMIN".equals(utilisateurConnecteRole) || "ROLE_CIP".equals(utilisateurConnecteRole)
                || "ROLE_EDUCSIMPLE".equals(utilisateurConnecteRole)) {
            return "redirect:/accueil_admin";
        } else {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            // Utilisez le nom d'utilisateur pour obtenir l'ID de l'utilisateur à partir de
            // votre service d'utilisateur
            long userId = userServ.findUserByLogin(username).getId();

            return "redirect:/select_fiche";
        }
    }

    private String determineUserRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");
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

        if ("ROLE_SUPERADMIN".equals(role)) {
            return "redirect:/accueil_superadmin";
        } else if ("ROLE_ADMIN".equals(role) || "ROLE_CIP".equals(role) || "ROLE_EDUCSIMPLE".equals(role)) {
            // Ajoutez la logique ici pour gérer le cas où l'utilisateur a le rôle "ADMIN"
            List<Utilisateur> utilisateurs = userServ.getUtilisateursByRole("USER");
            model.addAttribute("utilisateurs", utilisateurs);
            return "/accueil_admin";
        } else {
            return "redirect:/accueil";
        }
    }

    @GetMapping("/accueil_superadmin")
    public String superadmin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();

        if ("ROLE_SUPERADMIN".equals(role)) {
            // Ajoutez la logique ici pour gérer le cas où l'utilisateur a le rôle
            // "SUPERADMIN"
            List<Utilisateur> utilisateurs = userServ.getAllUtilisateurs();
            model.addAttribute("utilisateurs", utilisateurs);
            return "/accueil_superadmin";
        } else {
            return "redirect:/accueil";
        }
    }

    @GetMapping("/ancienaccueil")
    public String redirectToAncienAccueil(Model model) {
        List<Utilisateur> utilisateurs = userServ.getUtilisateursByRole("USER");
        model.addAttribute("utilisateurs", utilisateurs);
        return "ancienaccueil";
    }

    @GetMapping("/profil_apprenti/{id}")
    public String redirectToprofil(@PathVariable Long id, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        // Placer le rôle dans le modèle
        model.addAttribute("utilisateurConnecteRole", role);
        Utilisateur utilisateur = userServ.findById(id);
        model.addAttribute("utilisateur", utilisateur);
        return "profil_apprenti";
    }

    @GetMapping("/profil_admin")
    public String redirectToprofiladmin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        Utilisateur utilisateur;

        // Vérifiez le rôle de l'utilisateur
        if ("ROLE_ADMIN".equals(role) || "ROLE_SUPERADMIN".equals(role) || "ROLE_CIP".equals(role)
                || "ROLE_EDUCSIMPLE".equals(role)) {
            // Si l'utilisateur a le rôle d'admin, récupérez les informations de
            // l'utilisateur connecté
            String login = userDetails.getUsername();
            utilisateur = userServ.findUserByLogin(login);

            // Ajoutez l'utilisateur à votre modèle
            model.addAttribute("utilisateur", utilisateur);

            // Redirigez vers la page de profil_admin
            return "profil_admin";
        } else {
            // Si l'utilisateur n'a pas le rôle d'admin, redirigez-le vers la page d'accueil
            return "redirect:/accueil";
        }
    }

    @GetMapping("/modif/{id}")
    public String modif(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Vérifiez si l'utilisateur connecté a le rôle de superadmin
        if (isUserSuperAdmin(userDetails)) {
            Utilisateur utilisateur = userServ.findById(id);
            model.addAttribute("utilisateur", utilisateur);

            // Récupérez les rôles définis dans l'enum UserRole
            UserRole[] roles = UserRole.values();
            model.addAttribute("roles", roles);
            List<Formation> allFormations = formationService.lire();
            model.addAttribute("allFormations", allFormations);
            // Vérifiez si le rôle du compte sélectionné est USER
            if (utilisateur.getRole() == UserRole.USER) {
                return "/modif";
            } else {
                // Si le rôle du compte sélectionné n'est pas USER, redirigez-le vers
                // modif_admin
                return "redirect:/modif_admin/" + id;
            }
        } else {
            // Si l'utilisateur connecté n'est pas superadmin, redirigez-le vers la page
            // d'accueil
            return "redirect:/accueil";
        }
    }

    @Autowired
    private FormationService formationService;

    @GetMapping("/modif_admin/{id}")
    public String modifadmin(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Récupérez les rôles définis dans l'enum UserRole
        UserRole[] roles = UserRole.values();
        model.addAttribute("roles", roles);
        List<Formation> allFormations = formationService.lire();
        model.addAttribute("allFormations", allFormations);
        // Vérifiez si l'utilisateur connecté a le rôle de superadmin
        if (isUserSuperAdmin(userDetails)) {
            Utilisateur utilisateur = userServ.findById(id);
            model.addAttribute("utilisateur", utilisateur);

            // Votre logique spécifique pour la page modif_admin
            if (utilisateur.getRole() == UserRole.USER) {
                // Si le rôle de l'utilisateur est USER, redirigez vers la page modif
                return "redirect:/modif/" + id;
            } else {
                // Sinon, affichez la page modif_admin
                return "/modif_admin";
            }
        } else {
            // Si l'utilisateur connecté n'est pas superadmin, redirigez-le vers la page
            // d'accueil
            return "redirect:/accueil";
        }
    }

    private boolean isUserSuperAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_SUPERADMIN"));
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
    public String select_fiche(Model model) {
        // List<FicheIntervention> fiches = ficheServ.lireTout(); // Ajout de la liste
        // des fiches

        List<FicheIntervention> fiches = ficheServ.getFichesByUserId(getCurrentUserId());
        Utilisateur userrr = userServ.findById(getCurrentUserId());
        model.addAttribute("userrr", userrr);
        model.addAttribute("fiche", new FicheIntervention());
        model.addAttribute("fiches", fiches);
        return "select_fiche";
    }

    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = utilisateurRepository.findUserByLogin(authentication.getName());
        return utilisateur.getRole().name(); // Retourne le rôle de l'utilisateur actuellement connecté
    }

    @GetMapping("/suivi_progression/{userId}")
    public String suiviProgression(@PathVariable Long userId, Model model) {
        if (getCurrentUserRole().equals(UserRole.CIP.name())) {

            Utilisateur utilisateur = userServ.findById(userId);
            List<FicheIntervention> fiches = ficheServ.getFichesByUserId(userId);

            model.addAttribute("utilisateur", utilisateur);
            model.addAttribute("fiches", fiches);
            return "suivi_progression";
        }else {
            return "redirect:/accueil";
        }
    }

    @GetMapping("/suivi_progression")
    public String suiviProgression(Model model) {
        Utilisateur utilisateur = userServ.findById(getCurrentUserId());
        List<FicheIntervention> fiches = ficheServ.getFichesByUserId(getCurrentUserId());

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

    @GetMapping("/fiche/liste_fiche_id/{id}")
    public String showCreateInterventionFormId(Model model, @PathVariable Long id) {

        List<FicheIntervention> fiches = ficheServ.lireTout(); // Ajout de la liste des fiches

        // List<FicheIntervention> fiches = ficheServ.getFichesByUserId(id); // Ajout de
        // la liste des fiches
        Utilisateur user = userServ.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("fiche", new FicheIntervention());
        model.addAttribute("fiches", fiches); // Ajout de la liste des fiches

        return "liste_fiche_id";
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

}