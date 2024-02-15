package com.webapp.ytb.webappytp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.management.relation.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import jakarta.validation.Valid;

import java.nio.file.Files;
import java.nio.file.Path;
import com.webapp.ytb.webappytp.service.FormationService;
import com.webapp.ytb.webappytp.service.ImagesTitresServiceImpl;
import com.webapp.ytb.webappytp.service.MessageService;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    MateriauxAmenagementRepository materiauxAmenagementRepository;
    UtilisateurServiceImpl userServ;
    FicheServiceImpl ficheServ;
    ImagesTitresRepository imagesTitresRepository;
    FicheRepository ficheRepository;
    UtilisateurRepository utilisateurRepository;
    ImagesTitresServiceImpl imagesTitresServiceImpl;
    private final FormationService formationService;
    private static final String MESSAGE = "message";
    private static final String FICHE = "fiche";
    private static final String FICHE_INTERVENTION = "ficheIntervention";
    private static final String UTILISATEURS = "utilisateurs";
    private static final String ROLE_SUPERADMIN = "ROLE_SUPERADMIN";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_CIP = "ROLE_CIP";
    private static final String ROLE_EDUCSIMPLE = "ROLE_EDUCSIMPLE";
    private static final String REDIRECT_ACCUEIL_SUPERADMIN = "redirect:/accueil_superadmin";
    private static final String REDIRECT_ACCUEIL = "redirect:/accueil";
    private static final String UTILISATEUR_CONNECTE_ROLE = "utilisateurConnecteRole";
    private static final String UTILISATEUR = "utilisateur";
    private static final String FICHES = "fiches";

    public HomeController(UtilisateurServiceImpl userServ, FicheServiceImpl ficheServ,
            MateriauxAmenagementRepository materiauxAmenagementRepository,
            ImagesTitresRepository imagesTitresRepository, FicheRepository ficheRepository,
            UtilisateurRepository utilisateurRepository, FormationService formationService,
            ImagesTitresServiceImpl imagesTitresServiceImpl) {
        this.userServ = userServ;
        this.ficheServ = ficheServ;
        this.materiauxAmenagementRepository = materiauxAmenagementRepository;
        this.imagesTitresRepository = imagesTitresRepository;
        this.ficheRepository = ficheRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.formationService = formationService;
        this.imagesTitresServiceImpl = imagesTitresServiceImpl;
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
            redirectAttributes.addFlashAttribute(MESSAGE, "Veuillez sélectionner un fichier à uploader");
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

            redirectAttributes.addFlashAttribute(MESSAGE,
                    "Vous avez réussi à uploader '" + file.getOriginalFilename() + "'");
        } catch (IOException e) {
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute(MESSAGE, "Erreur lors de l'upload du fichier");
        }

        return "redirect:/fiche/icones/upload";
    }

    // Ajouter une fiche
    @GetMapping("/ajoutfiche/{id}")
    public String ajoutfiche(Model model, @PathVariable Long id) {

        FicheIntervention fiche = new FicheIntervention();
        Utilisateur user = userServ.findById(id);
        fiche.setUtilisateur(user);
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

        model.addAttribute(FICHE, fiche);
        model.addAttribute("user", userServ.findById(id));
        return "fiche_nouvelle";
    }

    // Ajouter une fiche avec id utilisateur
    @GetMapping("/niveauxFiche/{id}")
    public String ajoutficheId(Model model, @PathVariable Long id) {
        FicheIntervention ficheExistante = ficheServ.lire(id);
        // FicheIntervention fiche = new FicheIntervention();
        FicheIntervention fiche = ficheServ.lire(id);
        if (ficheExistante.getIntervenant().getImageTitreIntervenantUrl() == null) {
            System.out.println("getImageTitreIntervenantUrl est null");
        }

        // on récupère les images pour les niveaux et on mets en premiere position celle
        // qui est déjà utilisée

        // ImagesTitres Intervenant
        List<ImagesTitres> imagesTitreIntervenant = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT);
        // on récupere l'image utilisée
        String stringImageTitreIntervenant = ficheExistante.getIntervenant().getImageTitreIntervenantUrl();
        // retirer le '/' en début de string
        if (stringImageTitreIntervenant != null && stringImageTitreIntervenant.charAt(0) == '/') {
            stringImageTitreIntervenant = stringImageTitreIntervenant.substring(1);
        }
        ImagesTitres imageTitreIntervenant = imagesTitresServiceImpl.findByImageUrl(stringImageTitreIntervenant);
        // on la met en premiere position
        if (imageTitreIntervenant != null) {
            imagesTitreIntervenant.remove(imageTitreIntervenant);
            imagesTitreIntervenant.add(0, imageTitreIntervenant);
        }
        // ImagesTitres Intervenant Prenom
        List<ImagesTitres> imagesTitreIntervenantPrenom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT_PRENOM);
        // on récupere l'image utilisée
        String stringImageTitreIntervenantPrenom = ficheExistante.getIntervenant().getImageTitreIntervenantPrenomUrl();
        // retirer le '/' en début de string
        if (stringImageTitreIntervenantPrenom != null && stringImageTitreIntervenantPrenom.charAt(0) == '/') {
            stringImageTitreIntervenantPrenom = stringImageTitreIntervenantPrenom.substring(1);
        }
        ImagesTitres imageTitreIntervenantPrenom = imagesTitresServiceImpl
                .findByImageUrl(stringImageTitreIntervenantPrenom);
        // on la met en premiere position
        if (imageTitreIntervenantPrenom != null) {
            imagesTitreIntervenantPrenom.remove(imageTitreIntervenantPrenom);
            imagesTitreIntervenantPrenom.add(0, imageTitreIntervenantPrenom);
        }
        // ImagesTitres Intervenant Nom
        List<ImagesTitres> imagesTitreIntervenantNom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENANT_NOM);
        // on récupere l'image utilisée
        String stringImageTitreIntervenantNom = ficheExistante.getIntervenant().getImageTitreIntervenantNomUrl();
        // retirer le '/' en début de string
        if (stringImageTitreIntervenantNom != null && stringImageTitreIntervenantNom.charAt(0) == '/') {
            stringImageTitreIntervenantNom = stringImageTitreIntervenantNom.substring(1);
        }
        ImagesTitres imageTitreIntervenantNom = imagesTitresServiceImpl.findByImageUrl(stringImageTitreIntervenantNom);
        // on la met en premiere position
        if (imageTitreIntervenantNom != null) {
            imagesTitreIntervenantNom.remove(imageTitreIntervenantNom);
            imagesTitreIntervenantNom.add(0, imageTitreIntervenantNom);
        }

        // ImagesTitres Demande
        List<ImagesTitres> imagesTitreDemande = imagesTitresRepository.findByTypeImage(ImagesTitres.TypeImage.DEMANDE);
        // on récupere l'image utilisée
        String stringImagesTitreDemande = ficheExistante.getDemande().getImageTitreDemandeUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreDemande != null && stringImagesTitreDemande.charAt(0) == '/') {
            stringImagesTitreDemande = stringImagesTitreDemande.substring(1);
        }
        ImagesTitres imageTitreDemande = imagesTitresServiceImpl.findByImageUrl(stringImagesTitreDemande);
        // on la met en premiere position
        if (imageTitreDemande != null) {
            imagesTitreDemande.remove(imageTitreDemande);
            imagesTitreDemande.add(0, imageTitreDemande);
        }

        // ImagesTitres Demande Nom
        List<ImagesTitres> imagesTitreDemandeNom = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_NOM);
        // on récupere l'image utilisée
        String stringImagesTitreDemandeNom = ficheExistante.getDemande().getImageTitreDemandeNomUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreDemandeNom != null && stringImagesTitreDemandeNom.charAt(0) == '/') {
            stringImagesTitreDemandeNom = stringImagesTitreDemandeNom.substring(1);
        }
        ImagesTitres imageTitreDemandeNom = imagesTitresServiceImpl.findByImageUrl(stringImagesTitreDemandeNom);
        // on la met en premiere position
        if (imageTitreDemandeNom != null) {
            imagesTitreDemandeNom.remove(imageTitreDemandeNom);
            imagesTitreDemandeNom.add(0, imageTitreDemandeNom);
        }

        // ImagesTitres Demande DegreUrgence
        List<ImagesTitres> imagesTitreDemandeDegreUrgence = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DEGRE_URGENCE);
        // on récupere l'image utilisée
        String stringImagesTitreDemandeDegreUrgence = ficheExistante.getDemande().getImageTitreDemandeDegreUrgenceUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreDemandeDegreUrgence != null && stringImagesTitreDemandeDegreUrgence.charAt(0) == '/') {
            stringImagesTitreDemandeDegreUrgence = stringImagesTitreDemandeDegreUrgence.substring(1);
        }
        ImagesTitres imageTitreDemandeDegreUrgence = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreDemandeDegreUrgence);
        // on la met en premiere position
        if (imageTitreDemandeDegreUrgence != null) {
            imagesTitreDemandeDegreUrgence.remove(imageTitreDemandeDegreUrgence);
            imagesTitreDemandeDegreUrgence.add(0, imageTitreDemandeDegreUrgence);
        }

        // ImagesTitres Demande Date
        List<ImagesTitres> imagesTitreDemandeDate = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DATE);
        // on récupere l'image utilisée
        String stringImagesTitreDemandeDate = ficheExistante.getDemande().getImageTitreDemandeDateUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreDemandeDate != null && stringImagesTitreDemandeDate.charAt(0) == '/') {
            stringImagesTitreDemandeDate = stringImagesTitreDemandeDate.substring(1);
        }
        ImagesTitres imageTitreDemandeDate = imagesTitresServiceImpl.findByImageUrl(stringImagesTitreDemandeDate);
        // on la met en premiere position
        if (imageTitreDemandeDate != null) {
            imagesTitreDemandeDate.remove(imageTitreDemandeDate);
            imagesTitreDemandeDate.add(0, imageTitreDemandeDate);
        }

        // ImagesTitres Demande Localisation
        List<ImagesTitres> imagesTitreDemandeLocalisation = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_LOCALISATION);
        // on récupere l'image utilisée
        String stringImagesTitreDemandeLocalisation = ficheExistante.getDemande().getImageTitreDemandeLocalisationUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreDemandeLocalisation != null && stringImagesTitreDemandeLocalisation.charAt(0) == '/') {
            stringImagesTitreDemandeLocalisation = stringImagesTitreDemandeLocalisation.substring(1);
        }
        ImagesTitres imageTitreDemandeLocalisation = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreDemandeLocalisation);
        // on la met en premiere position
        if (imageTitreDemandeLocalisation != null) {
            imagesTitreDemandeLocalisation.remove(imageTitreDemandeLocalisation);
            imagesTitreDemandeLocalisation.add(0, imageTitreDemandeLocalisation);
        }

        // ImagesTitres Demande Description
        List<ImagesTitres> imagesTitreDemandeDescription = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.DEMANDE_DESCRIPTION);
        // on récupere l'image utilisée
        String stringImagesTitreDemandeDescription = ficheExistante.getDemande().getImageTitreDemandeDescriptionUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreDemandeDescription != null && stringImagesTitreDemandeDescription.charAt(0) == '/') {
            stringImagesTitreDemandeDescription = stringImagesTitreDemandeDescription.substring(1);
        }
        ImagesTitres imageTitreDemandeDescription = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreDemandeDescription);
        // on la met en premiere position
        if (imageTitreDemandeDescription != null) {
            imagesTitreDemandeDescription.remove(imageTitreDemandeDescription);
            imagesTitreDemandeDescription.add(0, imageTitreDemandeDescription);
        }

        // ImagesTitres Intervention
        List<ImagesTitres> imagesTitreIntervention = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION);
        // on récupere l'image utilisée
        String stringImagesTitreIntervention = ficheExistante.getIntervention().getImageTitreInterventionUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreIntervention != null && stringImagesTitreIntervention.charAt(0) == '/') {
            stringImagesTitreIntervention = stringImagesTitreIntervention.substring(1);
        }
        ImagesTitres imageTitreIntervention = imagesTitresServiceImpl.findByImageUrl(stringImagesTitreIntervention);
        // on la met en premiere position
        if (imageTitreIntervention != null) {
            imagesTitreIntervention.remove(imageTitreIntervention);
            imagesTitreIntervention.add(0, imageTitreIntervention);
        }

        // ImagesTitres Intervention Date
        List<ImagesTitres> imagesTitreInterventionDate = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_DATE);
        // on récupere l'image utilisée
        String stringImagesTitreInterventionDate = ficheExistante.getIntervention().getImageDateInterventionUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreInterventionDate != null && stringImagesTitreInterventionDate.charAt(0) == '/') {
            stringImagesTitreInterventionDate = stringImagesTitreInterventionDate.substring(1);
        }
        ImagesTitres imageTitreInterventionDate = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreInterventionDate);
        // on la met en premiere position
        if (imageTitreInterventionDate != null) {
            imagesTitreInterventionDate.remove(imageTitreInterventionDate);
            imagesTitreInterventionDate.add(0, imageTitreInterventionDate);
        }

        // ImagesTitres Intervention Duree
        List<ImagesTitres> imagesTitreInterventionDuree = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_DUREE);
        // on récupere l'image utilisée
        String stringImagesTitreInterventionDuree = ficheExistante.getIntervention().getImageDureeInterventionUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreInterventionDuree != null && stringImagesTitreInterventionDuree.charAt(0) == '/') {
            stringImagesTitreInterventionDuree = stringImagesTitreInterventionDuree.substring(1);
        }
        ImagesTitres imageTitreInterventionDuree = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreInterventionDuree);
        // on la met en premiere position
        if (imageTitreInterventionDuree != null) {
            imagesTitreInterventionDuree.remove(imageTitreInterventionDuree);
            imagesTitreInterventionDuree.add(0, imageTitreInterventionDuree);
        }

        // ImagesTitres Intervention Type
        List<ImagesTitres> imagesTitreInterventionType = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.INTERVENTION_TYPE);
        // on récupere l'image utilisée
        String stringImagesTitreInterventionType = ficheExistante.getIntervention().getImageTypeInterventionUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreInterventionType != null && stringImagesTitreInterventionType.charAt(0) == '/') {
            stringImagesTitreInterventionType = stringImagesTitreInterventionType.substring(1);
        }
        ImagesTitres imageTitreInterventionType = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreInterventionType);
        // on la met en premiere position
        if (imageTitreInterventionType != null) {
            imagesTitreInterventionType.remove(imageTitreInterventionType);
            imagesTitreInterventionType.add(0, imageTitreInterventionType);
        }

        // ImagesTitres Maintenance Type
        List<ImagesTitres> imagesTitreMaintenanceType = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.MAINTENANCE_TYPE);
        // on récupere l'image utilisée
        String stringImagesTitreMaintenanceType = ficheExistante.getMaintenance().getImageTypeMaintenanceUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreMaintenanceType != null && stringImagesTitreMaintenanceType.charAt(0) == '/') {
            stringImagesTitreMaintenanceType = stringImagesTitreMaintenanceType.substring(1);
        }
        ImagesTitres imageTitreMaintenanceType = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreMaintenanceType);
        // on la met en premiere position
        if (imageTitreMaintenanceType != null) {
            imagesTitreMaintenanceType.remove(imageTitreMaintenanceType);
            imagesTitreMaintenanceType.add(0, imageTitreMaintenanceType);
        }

        // ImagesTitres Travaux Réalisés
        List<ImagesTitres> imagesTitreTravauxRealises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.TRAVAUX_REALISES);
        // on récupere l'image utilisée
        String stringImagesTitreTravauxRealises = ficheExistante.getImageTitreTravauxRealisesUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreTravauxRealises != null && stringImagesTitreTravauxRealises.charAt(0) == '/') {
            stringImagesTitreTravauxRealises = stringImagesTitreTravauxRealises.substring(1);
        }
        ImagesTitres imageTitreTravauxRealises = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreTravauxRealises);
        // on la met en premiere position
        if (imageTitreTravauxRealises != null) {
            imagesTitreTravauxRealises.remove(imageTitreTravauxRealises);
            imagesTitreTravauxRealises.add(0, imageTitreTravauxRealises);
        }

        // ImagesTitres Travaux Non Réalisés
        List<ImagesTitres> imagesTitreTravauxNonRealises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.TRAVAUX_NON_REALISES);
        // on récupere l'image utilisée
        String stringImagesTitreTravauxNonRealises = ficheExistante.getImageTitreTravauxNonRealisesUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreTravauxNonRealises != null && stringImagesTitreTravauxNonRealises.charAt(0) == '/') {
            stringImagesTitreTravauxNonRealises = stringImagesTitreTravauxNonRealises.substring(1);
        }
        ImagesTitres imageTitreTravauxNonRealises = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreTravauxNonRealises);
        // on la met en premiere position
        if (imageTitreTravauxNonRealises != null) {
            imagesTitreTravauxNonRealises.remove(imageTitreTravauxNonRealises);
            imagesTitreTravauxNonRealises.add(0, imageTitreTravauxNonRealises);
        }

        // ImagesTitres Materiaux Utilisés
        List<ImagesTitres> imagesTitreMateriauxUtilises = imagesTitresRepository
                .findByTypeImage(ImagesTitres.TypeImage.MATERIAUX_UTILISES);
        // on récupere l'image utilisée
        String stringImagesTitreMateriauxUtilises = ficheExistante.getImageTitreMateriauxUtilisesUrl();
        // retirer le '/' en début de string
        if (stringImagesTitreMateriauxUtilises != null && stringImagesTitreMateriauxUtilises.charAt(0) == '/') {
            stringImagesTitreMateriauxUtilises = stringImagesTitreMateriauxUtilises.substring(1);
        }
        ImagesTitres imageTitreMateriauxUtilises = imagesTitresServiceImpl
                .findByImageUrl(stringImagesTitreMateriauxUtilises);
        // on la met en premiere position
        if (imageTitreMateriauxUtilises != null) {
            imagesTitreMateriauxUtilises.remove(imageTitreMateriauxUtilises);
            imagesTitreMateriauxUtilises.add(0, imageTitreMateriauxUtilises);
        }

        // types d'intervention ElementsFiche.Intervention.TypeIntervention).values();
        model.addAttribute("typeInterventionList", Intervention.TypeIntervention.values());

        // types de maintenance Maintenance.MaintenanceType.values();
        model.addAttribute("maintenanceList", Maintenance.MaintenanceType.values());

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

        fiche.setId(id);
        fiche.setDemande(ficheExistante.getDemande());
        fiche.setIntervenant(ficheExistante.getIntervenant());
        fiche.setIntervention(ficheExistante.getIntervention());
        fiche.setMaintenance(ficheExistante.getMaintenance());
        fiche.setUtilisateur(ficheExistante.getUtilisateur());

        model.addAttribute(FICHE, fiche);
        model.addAttribute("user", ficheServ.lire(id).getIntervenant());
        return "fiche_a_completer";
    }

    @PostMapping("/modifFicheParFormateur/{id}")
    public String modifFicheParFormateur(@ModelAttribute @Valid FicheIntervention fiche, Model model,
            @PathVariable Long id) {
        FicheIntervention ficheOrigine = ficheRepository.findById(id).get();

        Demande demande = ficheOrigine.getDemande();
        Intervenant intervenant = ficheOrigine.getIntervenant();
        Intervention intervention = ficheOrigine.getIntervention();
        Maintenance maintenance;

        if (fiche.getMaintenance().getMaintenanceType() != null) {
            maintenance = ficheOrigine.getMaintenance();
        } else {
            maintenance = new Maintenance();
        }
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
        if (fiche.getMaintenance().getMaintenanceType() != null) {
            maintenance.setMaintenanceType(fiche.getMaintenance().getMaintenanceType());
        }
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
        ficheServ.modifier(fiche.getId(), fiche);
        //FicheIntervention createdFiche = ficheServ.creer(ficheOrigine);
        //model.addAttribute("createdFiche", createdFiche);
        return "redirect:/redirectByRole";
    }

    @PostMapping("/ajouter_fiche")
    public String ajouterFiche(@ModelAttribute FicheIntervention fiche, Model model) {
        fiche.setDateCreation(LocalDate.now());
        fiche.setMateriauxOptions(new ArrayList<>());
        Demande demande = new Demande();
        Intervenant intervenant = new Intervenant();
        Intervention intervention = new Intervention();
        Maintenance maintenance = new Maintenance();
        // demande
        demande.setNomDemandeur(
                fiche.getDemande().getNomDemandeur() != null ? fiche.getDemande().getNomDemandeur() : "");
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
        model.addAttribute(FICHE_INTERVENTION, ficheIntervention);
        model.addAttribute("materiauxAmenagementList", materiauxAmenagementList);
        model.addAttribute("color", "#8fabd9");
        return "fiche_complete";
    }

    // modifier la fiche no
    @GetMapping("/fiche/modifier/{id}")
    public String showFicheDetails(@PathVariable long id, Model model) {

        // Obtenir id de l'utilisateur connecté
        Long currentUserId = getCurrentUserId();

        FicheIntervention ficheIntervention = ficheServ.lire(id);

        // Vérifier si l'utilisateur connecté est le propriétaire de la fiche
        if (ficheIntervention.getUtilisateur().getId() != currentUserId) {
            return "redirect:/redirectByRole";
        }

        // Utiliser directement le type d'intervention de la fiche
        String typeInterventionStr = ficheIntervention.getIntervention().getTypeIntervention();
        if (typeInterventionStr != null) {
            List<Materiaux> materiauxAmenagementList = materiauxAmenagementRepository.findByTypeIntervention(
                    Intervention.TypeIntervention.valueOf(typeInterventionStr));

            model.addAttribute("materiauxAmenagementList", materiauxAmenagementList);
        }

        // envoyer la liste des type d'intervention
        model.addAttribute("typeInterventionList", Intervention.TypeIntervention.values());

        // envoyer la liste des maintenance
        model.addAttribute("maintenanceList", Maintenance.MaintenanceType.values());

        model.addAttribute(FICHE_INTERVENTION, ficheIntervention);
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
        updateDemande(ficheIntervention.getDemande(), newNomDemandeur, newDateDemande, newLocalisation, newDescription,
                newDegreUrgence);
        updateIntervention(ficheIntervention.getIntervention(), newDateIntervention, newDureeIntervention,
                newNatureType);
        updateMaintenance(ficheIntervention.getMaintenance(), newMaintenanceType);
        updateFicheDetails(ficheIntervention, newTravauxRealises, newTravauxNonRealises, nouvelleInterventionValue);
        updateMateriauxOptions(ficheIntervention, newMateriau0, newMateriau1, newMateriau2, newMateriau3, newMateriau4,
                newMateriau5);

        ficheServ.modifier(id, ficheIntervention);
        return "redirect:/chat/" + id;
    }

    private void updateDemande(Demande demande, String newNomDemandeur, LocalDate newDateDemande,
            String newLocalisation, String newDescription, Integer newDegreUrgence) {
        if (newNomDemandeur != null) {
            demande.setNomDemandeur(newNomDemandeur);
        }
        if (newDateDemande != null) {
            demande.setDateDemande(newDateDemande);
        }
        if (newLocalisation != null) {
            demande.setLocalisation(newLocalisation);
        }
        if (newDescription != null) {
            demande.setDescription(newDescription);
        }
        if (newDegreUrgence != null) {
            demande.setDegreUrgence(newDegreUrgence);
        }
    }

    private void updateIntervention(Intervention intervention, LocalDate newDateIntervention,
            Integer newDureeIntervention, String newNatureType) {
        if (newDateIntervention != null) {
            intervention.setDateIntervention(newDateIntervention);
        }
        if (newDureeIntervention != null) {
            intervention.setDureeIntervention(newDureeIntervention);
        }
        if (newNatureType != null) {
            intervention.setTypeIntervention(newNatureType);
        }
    }

    private void updateMaintenance(Maintenance maintenance, Maintenance.MaintenanceType newMaintenanceType) {
        if (newMaintenanceType != null) {
            maintenance.setMaintenanceType(newMaintenanceType);
        }
    }

    private void updateFicheDetails(FicheIntervention ficheIntervention, String newTravauxRealises,
            String newTravauxNonRealises, boolean nouvelleInterventionValue) {
        ficheIntervention.setTravauxRealises(newTravauxRealises);
        ficheIntervention.setTravauxNonRealises(newTravauxNonRealises);
        ficheIntervention.setNouvelleIntervention(nouvelleInterventionValue);
    }

    private void updateMateriauxOptions(FicheIntervention ficheIntervention, String newMateriau0, String newMateriau1,
            String newMateriau2, String newMateriau3, String newMateriau4, String newMateriau5) {
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
        updateDemande(ficheIntervention.getDemande(), newNomDemandeur, newDateDemande, newLocalisation, newDescription,
                newDegreUrgence);
        updateIntervention(ficheIntervention.getIntervention(), newDateIntervention, newDureeIntervention,
                newNatureType);
        updateMaintenance(ficheIntervention.getMaintenance(), newMaintenanceType);
        updateFicheDetails(ficheIntervention, newTravauxRealises, newTravauxNonRealises, nouvelleInterventionValue);
        updateMateriauxOptions(ficheIntervention, newMateriau0, newMateriau1, newMateriau2, newMateriau3, newMateriau4,
                newMateriau5);

        ficheServ.modifier(id, ficheIntervention);
        return "redirect:/fiche/modifier/" + id;
    }

    // -----------------------------------------//
    // ------------Accueils---------------------//
    // -----------------------------------------//
    @GetMapping("/")
    public String home(Model model) {
        List<Utilisateur> utilisateurs = userServ.getUtilisateursByRole("USER");
        model.addAttribute(UTILISATEURS, utilisateurs);
        return "accueil";
    }

    @GetMapping("/redirectByRole")
    public String redirectByRole() {
        String utilisateurConnecteRole = determineUserRole();

        if (ROLE_SUPERADMIN.equals(utilisateurConnecteRole)) {
            return REDIRECT_ACCUEIL_SUPERADMIN;
        } else if (ROLE_ADMIN.equals(utilisateurConnecteRole) || ROLE_CIP.equals(utilisateurConnecteRole)
                || ROLE_EDUCSIMPLE.equals(utilisateurConnecteRole)) {
            return "redirect:/accueil_admin";
        } else {

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
        model.addAttribute(UTILISATEURS, utilisateurs);
        return "accueil";
    }

    @GetMapping("/accueil_admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // On récupere les utilisateurs qui sont dans les memes formations que nous
        List<Formation> formations = userServ.findUserByLogin(userDetails.getUsername()).getFormations();
        Set<Utilisateur> utilisateurs = new HashSet<>();
        for (Formation formation : formations) {
            for (Utilisateur utilisateur : formation.getUtilisateurs()) {
                if (utilisateur.getRole().equals(UserRole.USER)) {
                    utilisateurs.add(utilisateur);
                }
            }
        }
        model.addAttribute(UTILISATEURS, utilisateurs);
        return "accueil_admin";
        
    }

    @GetMapping("/accueil_superadmin")
    public String superadmin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Utilisateur> utilisateurs = userServ.getAllUtilisateurs();
        model.addAttribute(UTILISATEURS, utilisateurs);
        return "/accueil_superadmin";
        
    }
    @GetMapping("/profil_apprenti/{id}")
    public String redirectToprofil(@PathVariable Long id, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        // Placer le rôle dans le modèle
        model.addAttribute(UTILISATEUR_CONNECTE_ROLE, role);
        Utilisateur utilisateur = userServ.findById(id);
        model.addAttribute(UTILISATEUR, utilisateur);
        return "profil_apprenti";
    }

    // Page de modification de profil par le superadmin
    @GetMapping("/modif/{id}")
    public String modif(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Vérifiez si l'utilisateur connecté a le rôle de superadmin
        if (isUserSuperAdmin(userDetails)) {
            Utilisateur utilisateur = userServ.findById(id);
            model.addAttribute(UTILISATEUR, utilisateur);

            // Récupérez les rôles définis dans l'enum UserRole
            UserRole[] roles = UserRole.values();
            model.addAttribute("roles", roles);
            List<Formation> allFormations = formationService.lire();
            model.addAttribute("allFormations", allFormations);
            // Vérifiez si le rôle du compte sélectionné est USER

            return "/modif";
        } else {
            // Si l'utilisateur connecté n'est pas superadmin -> accueil
            return REDIRECT_ACCUEIL;
        }
    }

    private boolean isUserSuperAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(ROLE_SUPERADMIN));
    }

    @GetMapping("/mdpmodif/{id}")
    public String mdpmodif(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = userServ.findById(id);
        model.addAttribute(UTILISATEUR, utilisateur);
        return "mdpmodif";
    }

    // deconnection
    @PostMapping("/log_out")
    public String logOut() {
        // Effectuez ici toutes les opérations de déconnexion nécessaires
        return "log_out";
    }

    @GetMapping("/select_fiche")
    public String selectFiche(Model model) {
        // List<FicheIntervention> fiches = ficheServ.lireTout(); // Ajout de la liste
        // des fiches

        List<FicheIntervention> fiches = ficheServ.getFichesByUserId(getCurrentUserId());
        Utilisateur userrr = userServ.findById(getCurrentUserId());
        model.addAttribute("userrr", userrr);
        model.addAttribute(FICHE, new FicheIntervention());
        model.addAttribute(FICHES, fiches);
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

            model.addAttribute(UTILISATEUR, utilisateur);
            model.addAttribute(FICHES, fiches);
            return "suivi_progression";
        } else {
            return REDIRECT_ACCUEIL;
        }
    }

    @GetMapping("/suivi_progression")
    public String suiviProgression(Model model) {
        Utilisateur utilisateur = userServ.findById(getCurrentUserId());
        List<FicheIntervention> fiches = ficheServ.getFichesByUserId(getCurrentUserId());

        model.addAttribute(UTILISATEUR, utilisateur);
        model.addAttribute(FICHES, fiches);
        return "suivi_progression";
    }

    @GetMapping("/record/{ficheId}")
    public String recordFiche(@PathVariable Long ficheId, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(ficheId);
        model.addAttribute(FICHE_INTERVENTION, ficheIntervention);
        return "record";
    }

    @GetMapping("/recordaffichage/{ficheId}")
    public String recordaffichage(@PathVariable Long ficheId, Model model) {
        FicheIntervention ficheIntervention = ficheServ.lire(ficheId);
        model.addAttribute(FICHE_INTERVENTION, ficheIntervention);
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
        // Récupérer le rôle de l'utilisateur connecté
        String utilisateurConnecteRole = determineUserRole();

        // List<FicheIntervention> fiches = ficheServ.getFichesByUserId(id); // Ajout de
        // la liste des fiches
        Utilisateur user = userServ.findById(id);
        List<FicheIntervention> fiches = ficheServ.getFichesByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute(FICHE, new FicheIntervention());
        model.addAttribute(FICHES, fiches); // Ajout de la liste des fiches
        model.addAttribute(UTILISATEUR_CONNECTE_ROLE, utilisateurConnecteRole);
        return "liste_fiche";
    }

    @GetMapping("/fiche/liste_fiche")
    public String showCreateInterventionForm(Model model) {

        String utilisateurConnecteRole = determineUserRole();

        if (ROLE_SUPERADMIN.equals(utilisateurConnecteRole)) {
            return REDIRECT_ACCUEIL_SUPERADMIN;
        } else if (ROLE_ADMIN.equals(utilisateurConnecteRole) || ROLE_CIP.equals(utilisateurConnecteRole)
                || ROLE_EDUCSIMPLE.equals(utilisateurConnecteRole)) {
            List<Utilisateur> users = userServ.lire();
            List<FicheIntervention> fiches = ficheServ.lireTout(); // Ajout de la liste des fiches
            model.addAttribute(FICHE, new FicheIntervention());
            model.addAttribute("users", users);
            model.addAttribute(FICHES, fiches); // Ajout de la liste des fiches
            model.addAttribute(UTILISATEUR_CONNECTE_ROLE, utilisateurConnecteRole);
            return "liste_fiche";
        } else {
            return REDIRECT_ACCUEIL;
        }

    }

}