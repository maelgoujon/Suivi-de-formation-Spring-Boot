package com.webapp.ytb.webappytp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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
import com.webapp.ytb.webappytp.repository.FormationRepository;
import com.webapp.ytb.webappytp.repository.ImagesTitresRepository;
import com.webapp.ytb.webappytp.repository.MateriauxRepository;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

@Component
// Créer des objets au démarrage de l'application
public class Demarrage implements ApplicationRunner {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private FicheRepository ficheRepository;

    @Autowired
    private MateriauxRepository materiauxRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private ImagesTitresRepository imagesTitresRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Création des matériaux Amenagement
        for (int i = 1; i <= 4; i++) {
            String nomMateriau = "AMENAGEMENT" + i;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl("images/accueil.png");
                materiaux.setType("AMENAGEMENT");
                materiauxRepository.save(materiaux);
            }
        }
        // Création des matériaux Electricite
        for (int i = 5; i <= 8; i++) {
            String nomMateriau = "ELECTRICITE" + i;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl("images/archive.png");
                materiaux.setType("ELECTRICITE");
                materiauxRepository.save(materiaux);
            }
        }
        // Création des matériaux FINITION
        for (int i = 9; i <= 12; i++) {
            String nomMateriau = "FINITION" + i;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl("images/finition.png");
                materiaux.setType("FINITION");
                materiauxRepository.save(materiaux);
            }
        }
        // Création des matériaux PLOMBERIE
        for (int i = 13; i <= 16; i++) {
            String nomMateriau = "PLOMBERIE" + i;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl("images/plomberie.png");
                materiaux.setType("PLOMBERIE");
                materiauxRepository.save(materiaux);
            }
        }

        // Création des matériaux SERRURERIE
        for (int i = 17; i <= 20; i++) {
            String nomMateriau = "SERRURERIE" + i;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl("images/serrurerie.png");
                materiaux.setType("SERRURERIE");
                materiauxRepository.save(materiaux);
            }
        }

        // Creation Formation 1
        Formation formation = new Formation();
        formation.setNom("Agent de maintenance en bâtiment");
        formation.setNiveau_qualif("CAP 2");
        formation.setDescription("Description Formation1");
        formationRepository.save(formation);

        // Creation Formation 2
        Formation formation2 = new Formation();
        formation2.setNom("Nom Formation");
        formation2.setNiveau_qualif("CAP 1ere Annee");
        formation2.setDescription("Description Formation2");
        formationRepository.save(formation2);

        // Creation de l´administrateur
        Utilisateur admin = utilisateurRepository.findUserByLogin("admin");
        if (admin == null) {
            admin = new Utilisateur();
            admin.setFormations(List.of(formation));
            admin.setNom("Admin");
            admin.setPrenom("Admin");
            admin.setLogin("admin");
            admin.setMdp("admin");
            admin.setRole(UserRole.ADMIN);
            admin.setPhotoBase64(photoBase64);
            admin.setDescription("Création et modification visuelles totales, laisser des traces écrite/vocal");
            utilisateurRepository.save(admin);
        }
        // Creation de l´educsimple
        Utilisateur educsimple = utilisateurRepository.findUserByLogin("educsimple");
        if (educsimple == null) {
            educsimple = new Utilisateur();
            educsimple.setFormations(List.of(formation2));
            educsimple.setNom("Educ");
            educsimple.setPrenom("Simple");
            educsimple.setLogin("educsimple");
            educsimple.setMdp("educsimple");
            educsimple.setRole(UserRole.EDUCSIMPLE);
            educsimple.setPhotoBase64(photoBase64);
            educsimple.setDescription("Utilisation des fiches, pourra laisser des traces écrite/vocal \r\n" + //
                    "");
            utilisateurRepository.save(educsimple);
        }
        // Creation du superadmin
        Utilisateur superadmin = utilisateurRepository.findUserByLogin("superadmin");
        if (superadmin == null) {
            superadmin = new Utilisateur();
            superadmin.setFormations(List.of(formation));
            superadmin.setNom("Super");
            superadmin.setPrenom("Admin");
            superadmin.setLogin("superadmin");
            superadmin.setMdp("superadmin");
            superadmin.setRole(UserRole.SUPERADMIN);
            superadmin.setPhotoBase64(photoBase64);
            superadmin.setDescription("Description de l'administrateur");
            utilisateurRepository.save(superadmin);
        }
        // Creation du CIP
        Utilisateur CIP = utilisateurRepository.findUserByLogin("CIP");
        if (CIP == null) {
            CIP = new Utilisateur();
            CIP.setFormations(List.of(formation));
            CIP.setNom("CIP");
            CIP.setPrenom("CIP");
            CIP.setLogin("cip");
            CIP.setMdp("cip");
            CIP.setRole(UserRole.CIP);
            CIP.setPhotoBase64(photoBase64);
            CIP.setDescription(
                    "Suivi parcours, évolution des compétences, adaptation de la situation d'examen. Pas de modifs visuelles");
            utilisateurRepository.save(CIP);
        }

        // Création d'un utilisateur michel
        Utilisateur michel = utilisateurRepository.findUserByLogin("michelmichel");
        if (michel == null) {
            michel = new Utilisateur();
            michel.setFormations(List.of(formation));
            michel.setNom("Michel");
            michel.setPrenom("Michel");
            michel.setLogin("michelmichel");
            michel.setMdp("1234");
            michel.setRole(UserRole.USER);
            michel.setNiveau(1);
            michel.setPhotoBase64(photoBase64);
            michel.setDescription("Description de Michel Michel");
            utilisateurRepository.save(michel);
        }

        // Création d'un utilisateur johnSmith
        Utilisateur johnSmith = utilisateurRepository.findUserByLogin("johnsmith");
        if (johnSmith == null) {
            johnSmith = new Utilisateur();
            johnSmith.setFormations(List.of(formation));
            johnSmith.setNom("Smith");
            johnSmith.setPrenom("John");
            johnSmith.setLogin("johnsmith");
            johnSmith.setMdp("9876");
            johnSmith.setRole(UserRole.USER);
            johnSmith.setNiveau(1);
            johnSmith.setPhotoBase64(photoBase64);
            johnSmith.setDescription("Description de John Smith");
            utilisateurRepository.save(johnSmith);
        }

        // Création d'un utilisateur janeDoe
        Utilisateur janeDoe = utilisateurRepository.findUserByLogin("janedoe");
        if (janeDoe == null) {
            janeDoe = new Utilisateur();
            janeDoe.setFormations(List.of(formation));
            janeDoe.setNom("Doe");
            janeDoe.setPrenom("Jane");
            janeDoe.setLogin("janedoe");
            janeDoe.setMdp("4321");
            janeDoe.setRole(UserRole.USER);
            janeDoe.setNiveau(1);
            janeDoe.setPhotoBase64(photoBase64);
            janeDoe.setDescription("Description de Jane Doe");
            utilisateurRepository.save(janeDoe);
        }

        // Création d'un utilisateur aliceJohnson
        Utilisateur aliceJohnson = utilisateurRepository.findUserByLogin("alicejohnson");
        if (aliceJohnson == null) {
            aliceJohnson = new Utilisateur();
            aliceJohnson.setFormations(List.of(formation));
            aliceJohnson.setNom("Johnson");
            aliceJohnson.setPrenom("Alice");
            aliceJohnson.setLogin("alicejohnson");
            aliceJohnson.setMdp("2468"); // Mot de passe à 4 chiffres
            aliceJohnson.setRole(UserRole.USER);
            aliceJohnson.setNiveau(1);
            aliceJohnson.setPhotoBase64(photoBase64);
            aliceJohnson.setDescription("Description d'Alice Johnson");
            utilisateurRepository.save(aliceJohnson);
        }

        // Création d'un utilisateur paulWilliams
        Utilisateur paulWilliams = utilisateurRepository.findUserByLogin("paulwilliams");
        if (paulWilliams == null) {
            paulWilliams = new Utilisateur();
            paulWilliams.setFormations(List.of(formation));
            paulWilliams.setNom("Williams");
            paulWilliams.setPrenom("Paul");
            paulWilliams.setLogin("paulwilliams");
            paulWilliams.setMdp("1357"); // Mot de passe à 4 chiffres
            paulWilliams.setRole(UserRole.USER);
            paulWilliams.setNiveau(1);
            paulWilliams.setPhotoBase64(photoBase64);
            paulWilliams.setDescription("Description de Paul Williams");
            utilisateurRepository.save(paulWilliams);
        }

        // Création d'un utilisateur emilyBrown
        Utilisateur emilyBrown = utilisateurRepository.findUserByLogin("emilybrown");
        if (emilyBrown == null) {
            emilyBrown = new Utilisateur();
            emilyBrown.setFormations(List.of(formation));
            emilyBrown.setNom("Brown");
            emilyBrown.setPrenom("Emily");
            emilyBrown.setLogin("emilybrown");
            emilyBrown.setMdp("9876"); // Mot de passe à 4 chiffres
            emilyBrown.setRole(UserRole.USER);
            emilyBrown.setNiveau(1);
            emilyBrown.setPhotoBase64(photoBase64);
            emilyBrown.setDescription("Description d'Emily Brown");
            utilisateurRepository.save(emilyBrown);
        }

        // Création d'un utilisateur pierreDupont
        Utilisateur pierreDupont = utilisateurRepository.findUserByLogin("pierredupont");
        if (pierreDupont == null) {
            pierreDupont = new Utilisateur();
            pierreDupont.setFormations(List.of(formation));
            pierreDupont.setNom("Dupont");
            pierreDupont.setPrenom("Pierre");
            pierreDupont.setLogin("pierredupont");
            pierreDupont.setMdp("7462"); // Mot de passe à 4 chiffres
            pierreDupont.setRole(UserRole.USER);
            pierreDupont.setNiveau(1);
            pierreDupont.setPhotoBase64(photoBase64);
            pierreDupont.setDescription("Description de Pierre Dupont");
            utilisateurRepository.save(pierreDupont);
        }

        // Creation d'une fiche d'Intervention
        FicheIntervention ficheIntervention = ficheRepository
                .findByTravauxRealisesAndUtilisateurPrenom("Travaux réalisés par Michel Michel", "Michel");
        if (ficheIntervention == null) {
            FicheIntervention nouvelleFiche = new FicheIntervention();
            Demande demande = new Demande();
            Maintenance maintenance = new Maintenance();
            Intervention intervention = new Intervention();
            Intervenant intervenant = new Intervenant();
            // Définir les attributs de l'intervenant
            intervenant.setNom("Michel");
            intervenant.setPrenom("Michel");
            intervenant.setNiveauTitreIntervenantNom(1);
            intervenant.setNiveauTitreIntervenantPrenom(1);
            intervenant.setNiveauTitreIntervenant(1);
            intervenant.setCouleurTitreIntervenant("#FF00FF");
            intervenant.setCouleurNom("#F00");
            intervenant.setCouleurPrenom("#00FFFF");
            intervenant.setImageTitreIntervenantUrl("/images/accueil.png");
            intervenant.setImageTitreIntervenantNomUrl("/images/accueil.png");
            intervenant.setImageTitreIntervenantPrenomUrl("/images/accueil.png");

            // Définir les attributs de l'intervention
            intervention.setNiveauTitreIntervention(1);
            intervention.setNiveauDateIntervention(1);
            intervention.setNiveauDureeIntervention(1);
            intervention.setNiveauTypeIntervention(1);
            intervention.setCouleurTitreIntervention("#FF00FF");
            intervention.setCouleurDateIntervention("#F00");
            intervention.setCouleurDureeIntervention("#00FFFF");
            intervention.setCouleurTypeIntervention("#F00");
            intervention.setImageTitreInterventionUrl("/images/accueil.png");
            intervention.setImageDureeInterventionUrl("/images/accueil.png");
            intervention.setImageTypeInterventionUrl("/images/accueil.png");
            intervention.setImageDateInterventionUrl("/images/accueil.png");
            intervention.setTypeIntervention(Intervention.TypeIntervention.AMENAGEMENT.toString());
            // Définir les attributs de la maintenance
            maintenance.setMaintenanceType(Maintenance.MaintenanceType.AMELIORATIVE);
            maintenance.setNiveauMaintenanceType(1);
            maintenance.setCouleurMaintenanceType("#FF00FF");
            maintenance.setImageTypeMaintenanceUrl("/images/accueil.png");
            // Définir les attributs de la demande
            demande.setNomDemandeur("Nom du demandeur");
            demande.setDateDemande(LocalDate.now());
            demande.setLocalisation("Localisation de la demande");
            demande.setDescription("Description de la demande");
            demande.setDegreUrgence(1);
            demande.setNiveauTitreDemande(1);
            demande.setNiveauNomDemandeur(1);
            demande.setNiveauDateDemande(1);
            demande.setNiveauLocalisation(1);
            demande.setNiveauDescription(1);
            demande.setNiveauDegreUrgence(1);
            demande.setCouleurTitreDemande("#FF00FF");
            demande.setCouleurNomDemandeur("#F00");
            demande.setCouleurDateDemande("#00FFFF");
            demande.setCouleurLocalisation("#FF00FF");
            demande.setCouleurDescription("#F00");
            demande.setCouleurDegreUrgence("#00FFFF");
            demande.setImageTitreDemandeUrl("/images/accueil.png");
            // Définir les attributs des travaux réalisés
            nouvelleFiche.setNiveauTravauxRealises(1);
            nouvelleFiche.setCouleurTravauxRealises("#aef800");
            nouvelleFiche.setImageTitreTravauxRealisesUrl("/images/accueil.png");
            nouvelleFiche.setTravauxRealises("Travaux réalisés par \nMichel Michel \n      Ligne 3");
            // Définir les attributs des travaux non réalisés
            nouvelleFiche.setNiveauTravauxNonRealises(1);
            nouvelleFiche.setCouleurTravauxNonRealises("#FF00FF");
            nouvelleFiche.setImageTitreTravauxNonRealisesUrl("/images/accueil.png");
            nouvelleFiche.setTravauxNonRealises("Travaux non réalisés par Michel Michel");
            // Définir les attributs des materiaux utilisés
            nouvelleFiche.setNiveauMateriauxUtilises(1);
            nouvelleFiche.setCouleurMateriauxUtilises("#00FFFF");
            nouvelleFiche.setImageTitreMateriauxUtilisesUrl("/images/accueil.png");
            // Définir les attributs de la fiche d'intervention
            nouvelleFiche.setIntervenant(intervenant);
            nouvelleFiche.setDemande(demande);
            nouvelleFiche.setMaintenance(maintenance);
            nouvelleFiche.setIntervention(intervention);
            nouvelleFiche.setDateCreation(LocalDate.now());
            nouvelleFiche.setEtatFicheFinie(false);
            nouvelleFiche.setNouvelleIntervention(true);
            nouvelleFiche.setUtilisateur(michel);

            // Ajouter les materiaux de type amenagement
            // On ajoute 4 materiaux et rien dans les deux derniers
            List<String> materiauxAmenagement = new ArrayList<>();
            materiauxAmenagement.add("AMENAGEMENT1");
            materiauxAmenagement.add("AMENAGEMENT2");
            materiauxAmenagement.add("AMENAGEMENT3");
            materiauxAmenagement.add("");
            materiauxAmenagement.add("AMENAGEMENT4");
            materiauxAmenagement.add("");
            nouvelleFiche.setMateriauxOptions(materiauxAmenagement);
            ficheRepository.save(nouvelleFiche);
        }

        // Creation des images pour la fiche d intervention
        String[] types = { "INTERVENANT", "DEMANDE", "INTERVENTION", "MATERIAUX_UTILISES","TRAVAUX_REALISES", "TRAVAUX_NON_REALISES", "INTERVENANT_PRENOM", "INTERVENANT_NOM", "DEMANDE_NOM", "DEMANDE_DATE", "DEMANDE_LOCALISATION", "DEMANDE_DESCRIPTION", "DEMANDE_DEGRE_URGENCE", "MAINTENANCE_TYPE", "INTERVENTION_DATE", "INTERVENTION_DUREE", "INTERVENTION_TYPE" };
        String[] nomImages = { "Image1", "ADMIN", "Image2", "FICHES1", "FICHES2", "FICHES3", "FICHES4" };
        String[] imageUrls = { "images/accueil.png", "images/admin.png", "images/archive.png", "images/eleve.png",
                "images/eleve.png", "images/eleve.png", "images/eleve.png" };

        for (String type : types) {
            for (int i = 0; i < nomImages.length; i++) {

                ImagesTitres imagesTitres = new ImagesTitres();
                imagesTitres.setNomImage(nomImages[i] + ' ' + type);
                imagesTitres.setImageUrl(imageUrls[i]);
                imagesTitres.setType(type);
                imagesTitresRepository.save(imagesTitres);

            }
        }

    }

    // Champs trop longs et inutiles pour être mis en haut
    // Photo de Michel Michel
    private String photoBase64 = "data:image/jpeg;base64,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";

}