package com.webapp.ytb.webappytp.controller;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Session;
import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Demande;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervenant;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Maintenance;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;
import com.webapp.ytb.webappytp.repository.FicheRepository;
import com.webapp.ytb.webappytp.service.SessionService;
import com.webapp.ytb.webappytp.service.UtilisateurService;
import com.webapp.ytb.webappytp.service.FicheServiceImpl;
import com.webapp.ytb.webappytp.service.FormationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private FormationService formationService;

    @Autowired
    private FicheServiceImpl ficheService;

    @Autowired
    private UtilisateurService userService;

    @GetMapping("/session/creer")
    public String afficherFormulaireCreationSession(Model model) {
        Demande demande = new Demande();

        // Ajouter au modèle un attribut avec tous les types d'intervention
        List<String> typesIntervention = Arrays.asList(Intervention.TypeIntervention.values())
                .stream()
                .map(Enum::toString)
                .collect(Collectors.toList());

        model.addAttribute("typesIntervention", typesIntervention);
        model.addAttribute("session", new Session());

        // Récupérer l'ID de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.findUserByLogin(username).getId();

        // Récupérer les formations de l'utilisateur connecté
        model.addAttribute("allFormations", formationService.getFormationsByUserId(userId));
        model.addAttribute("demande", demande);

        return "session";
    }

    @PostMapping("/session/creer")
    public String creerSession(Session session, @RequestParam("typeIntervention") String typeIntervention) {
        Session session1 = new Session();
        session1.setDemande(session.getDemande());
        session1.setFormation(session.getFormation());
        session1.setDate(session.getDate());

        sessionService.creer(session1);

        // ajouter une fiche avec les données de la session pour chaque apprenti de la
        // formation
        List<Utilisateur> utilisateurs = session.getFormation().getUtilisateurs();
        // on ne garde que les apprentis dans la liste
        List<Utilisateur> apprentis = new ArrayList<>();
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getRole().equals(UserRole.USER)) {
                apprentis.add(utilisateur);
            }
        }
        for (Utilisateur apprenti : apprentis) {
            FicheIntervention fiche = new FicheIntervention();
            Intervention intervention = new Intervention();

            Intervenant intervenant = new Intervenant();

            Maintenance maintenance = new Maintenance();

            Demande demande = new Demande();
            demande.setDateDemande(session.getDate());
            demande.setNomDemandeur(session.getDemande().getNomDemandeur());
            demande.setDateDemande(session.getDate());
            demande.setLocalisation(session.getDemande().getLocalisation());
            demande.setDescription(session.getDemande().getDescription());
            demande.setDegreUrgence(session.getDemande().getDegreUrgence());

            List<String> materiaux = new ArrayList<>();
            materiaux.add("");
            materiaux.add("");
            materiaux.add("");
            materiaux.add("");
            materiaux.add("");
            materiaux.add("");
            fiche.setMateriauxOptions(materiaux);
            // Niveaux de la fiche
            // on mets les memes niveaux que la derniere fiche de l'apprenti
            List<FicheIntervention> fiches = ficheService.getFichesByUserId(apprenti.getId());
            // recuperer la fiche avec date creation la plus recente
            //

            if (fiches.isEmpty()) {
                //niveaux

                fiche.setNiveauIntervenant(0);
                fiche.setNiveauTravauxRealises(0);
                fiche.setNiveauTravauxNonRealises(0);
                fiche.setNiveauMateriauxUtilises(0);
                fiche.setNiveauNatureIntervention(0);
                intervention.setNiveauDateIntervention(0);
                intervention.setNiveauDureeIntervention(0);
                intervention.setNiveauTypeIntervention(0);
                intervention.setNiveauTitreIntervention(0);
                maintenance.setNiveauMaintenanceType(0);
                intervenant.setNiveauTitreIntervenant(0);
                intervenant.setNiveauTitreIntervenantNom(0);
                intervenant.setNiveauTitreIntervenantPrenom(0);
                demande.setNiveauDateDemande(0);
                demande.setNiveauDegreUrgence(0);
                demande.setNiveauDescription(0);
                demande.setNiveauLocalisation(0);
                demande.setNiveauNomDemandeur(0);
                demande.setNiveauTitreDemande(0);
                demande.setNiveauTitreDemandeDate(0);
                demande.setNiveauTitreDemandeDegreUrgence(0);
                demande.setNiveauTitreDemandeDescription(0);
                demande.setNiveauTitreDemandeLocalisation(0);
                demande.setNiveauTitreDemandeNom(0);
                demande.setNiveauTitreDemandeDate(0);
                
                //images
                fiche.setImageTitreMateriauxUtilisesUrl("/images/check.png");
                fiche.setImageTitreTravauxNonRealisesUrl("/images/check.png");
                fiche.setImageTitreTravauxRealisesUrl("/images/check.png");
                intervention.setImageDateInterventionUrl("/images/check.png");
                intervention.setImageDureeInterventionUrl("/images/check.png");
                intervention.setImageTypeInterventionUrl("/images/check.png");
                intervention.setImageTitreInterventionUrl("/images/check.png");
                maintenance.setImageTypeMaintenanceUrl("/images/check.png");
                intervenant.setImageTitreIntervenantUrl("/images/check.png");
                intervenant.setImageTitreIntervenantNomUrl("/images/check.png");
                intervenant.setImageTitreIntervenantPrenomUrl("/images/check.png");

                intervention.setTypeIntervention(typeIntervention);
                fiche.setDateCreation(LocalDate.now());


            } else {
                FicheIntervention ficheRecente = fiches.get(0);
                for (FicheIntervention ficheIntervention : fiches) {
                    if (ficheIntervention.getDateCreation() != null &&
                            (ficheRecente.getDateCreation() == null ||
                                    ficheIntervention.getDateCreation().isAfter(ficheRecente.getDateCreation()))) {
                        ficheRecente = ficheIntervention;
                    }
                }
                fiche.setNiveauIntervenant(ficheRecente.getNiveauIntervenant());
                fiche.setNiveauTravauxRealises(ficheRecente.getNiveauTravauxRealises());
                fiche.setNiveauTravauxNonRealises(ficheRecente.getNiveauTravauxNonRealises());
                fiche.setNiveauMateriauxUtilises(ficheRecente.getNiveauMateriauxUtilises());
                fiche.setNiveauNatureIntervention(ficheRecente.getNiveauNatureIntervention());
                fiche.setImageTitreMateriauxUtilisesUrl(ficheRecente.getImageTitreMateriauxUtilisesUrl());
                fiche.setImageTitreTravauxNonRealisesUrl(ficheRecente.getImageTitreTravauxNonRealisesUrl());
                fiche.setImageTitreTravauxRealisesUrl(ficheRecente.getImageTitreTravauxRealisesUrl());
                intervention.setTypeIntervention(typeIntervention);
                intervention.setNiveauDateIntervention(ficheRecente.getIntervention().getNiveauDateIntervention());
                intervention.setNiveauDureeIntervention(ficheRecente.getIntervention().getNiveauDureeIntervention());
                intervention.setNiveauTypeIntervention(ficheRecente.getIntervention().getNiveauTypeIntervention());
                intervention.setNiveauTitreIntervention(ficheRecente.getIntervention().getNiveauTitreIntervention());
                maintenance.setNiveauMaintenanceType(ficheRecente.getMaintenance().getNiveauMaintenanceType());
                maintenance.setImageTypeMaintenanceUrl(ficheRecente.getMaintenance().getImageTypeMaintenanceUrl());
                intervenant.setNiveauTitreIntervenant(ficheRecente.getIntervenant().getNiveauTitreIntervenant());
                intervenant.setNiveauTitreIntervenantNom(ficheRecente.getIntervenant().getNiveauTitreIntervenantNom());
                intervenant.setNiveauTitreIntervenantPrenom(
                        ficheRecente.getIntervenant().getNiveauTitreIntervenantPrenom());
                intervenant.setImageTitreIntervenantUrl(ficheRecente.getIntervenant().getImageTitreIntervenantUrl());
                intervenant
                        .setImageTitreIntervenantNomUrl(ficheRecente.getIntervenant().getImageTitreIntervenantNomUrl());
                intervenant.setImageTitreIntervenantPrenomUrl(
                        ficheRecente.getIntervenant().getImageTitreIntervenantPrenomUrl());
                intervention.setImageDateInterventionUrl(ficheRecente.getIntervention().getImageDateInterventionUrl());
                intervention
                        .setImageDureeInterventionUrl(ficheRecente.getIntervention().getImageDureeInterventionUrl());
                intervention.setImageTypeInterventionUrl(ficheRecente.getIntervention().getImageTypeInterventionUrl());
                intervention
                        .setImageTitreInterventionUrl(ficheRecente.getIntervention().getImageTitreInterventionUrl());
                demande.setNiveauDateDemande(ficheRecente.getDemande().getNiveauDateDemande());
                demande.setNiveauDegreUrgence(ficheRecente.getDemande().getNiveauDegreUrgence());
                demande.setNiveauDescription(ficheRecente.getDemande().getNiveauDescription());
                demande.setNiveauLocalisation(ficheRecente.getDemande().getNiveauLocalisation());
                demande.setNiveauNomDemandeur(ficheRecente.getDemande().getNiveauNomDemandeur());
                demande.setNiveauTitreDemande(ficheRecente.getDemande().getNiveauTitreDemande());
                demande.setNiveauTitreDemandeDate(ficheRecente.getDemande().getNiveauTitreDemandeDate());
                demande.setNiveauTitreDemandeDegreUrgence(
                        ficheRecente.getDemande().getNiveauTitreDemandeDegreUrgence());
                demande.setNiveauTitreDemandeDescription(ficheRecente.getDemande().getNiveauTitreDemandeDescription());
                demande.setNiveauTitreDemandeLocalisation(
                        ficheRecente.getDemande().getNiveauTitreDemandeLocalisation());
                demande.setNiveauTitreDemandeNom(ficheRecente.getDemande().getNiveauTitreDemandeNom());
                demande.setNiveauTitreDemandeDate(ficheRecente.getDemande().getNiveauTitreDemandeDate());
                //couleurs
                fiche.setImageTitreMateriauxUtilisesUrl(ficheRecente.getImageTitreMateriauxUtilisesUrl());
                fiche.setImageTitreTravauxNonRealisesUrl(ficheRecente.getImageTitreTravauxNonRealisesUrl());
                fiche.setImageTitreTravauxRealisesUrl(ficheRecente.getImageTitreTravauxRealisesUrl());
                intervention.setImageDateInterventionUrl(ficheRecente.getIntervention().getImageDateInterventionUrl());
                intervention
                        .setImageDureeInterventionUrl(ficheRecente.getIntervention().getImageDureeInterventionUrl());
                intervention.setImageTypeInterventionUrl(ficheRecente.getIntervention().getImageTypeInterventionUrl());
                intervention
                        .setImageTitreInterventionUrl(ficheRecente.getIntervention().getImageTitreInterventionUrl());
                maintenance.setImageTypeMaintenanceUrl(ficheRecente.getMaintenance().getImageTypeMaintenanceUrl());
                intervenant.setImageTitreIntervenantUrl(ficheRecente.getIntervenant().getImageTitreIntervenantUrl());
                intervenant
                        .setImageTitreIntervenantNomUrl(ficheRecente.getIntervenant().getImageTitreIntervenantNomUrl());
                intervenant.setImageTitreIntervenantPrenomUrl(

                        ficheRecente.getIntervenant().getImageTitreIntervenantPrenomUrl());
                demande.setImageTitreDemandeDateUrl(ficheRecente.getDemande().getImageTitreDemandeDateUrl());
                demande.setImageTitreDemandeDegreUrgenceUrl(
                        ficheRecente.getDemande().getImageTitreDemandeDegreUrgenceUrl());
                demande.setImageTitreDemandeDescriptionUrl(
                        ficheRecente.getDemande().getImageTitreDemandeDescriptionUrl());
                demande.setImageTitreDemandeLocalisationUrl(
                        ficheRecente.getDemande().getImageTitreDemandeLocalisationUrl());
                demande.setImageTitreDemandeNomUrl(ficheRecente.getDemande().getImageTitreDemandeNomUrl());
                demande.setImageTitreDemandeUrl(ficheRecente.getDemande().getImageTitreDemandeUrl());
                demande.setImageTitreDemandeDateUrl(ficheRecente.getDemande().getImageTitreDemandeDateUrl());
                


            }
            fiche.setIntervention(intervention);
            fiche.setMaintenance(maintenance);
            fiche.setIntervenant(intervenant);
            fiche.setDateCreation(LocalDate.now());
            fiche.setUtilisateur(apprenti);
            fiche.setDemande(demande);
            ficheService.creer(fiche);

        }
        return "redirect:/accueil_admin";
    }
}