package com.webapp.ytb.webappytp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.webapp.ytb.webappytp.controller.MessageController;
import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.Message;
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
import com.webapp.ytb.webappytp.repository.MessageRepository;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

@Component
// Créer des objets au démarrage de l'application
public class Demarrage implements ApplicationRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final FicheRepository ficheRepository;
    private final MateriauxRepository materiauxRepository;
    private final FormationRepository formationRepository;
    private final ImagesTitresRepository imagesTitresRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageRepository messageRepository;
    // url vers le repertoire des images
    private static final String URL_IMAGES_MATERIAUX = "images/materiaux/";
    private Formation formation;
    private Formation formation2;
    private static final String MICHEL = "Michel";
    private static final String VIOLET = "#FF00FF";
    private static final String CYAN = "#00FFFF";
    private static final String ICONE_ACCUEIL = "/images/accueil.png";
    private static final String ICONE_ELEVE = "/images/eleve.png";


    @Autowired
    public Demarrage(UtilisateurRepository utilisateurRepository, FicheRepository ficheRepository,
            MateriauxRepository materiauxRepository, FormationRepository formationRepository,
            ImagesTitresRepository imagesTitresRepository, PasswordEncoder passwordEncoder,
            MessageRepository messageRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.ficheRepository = ficheRepository;
        this.materiauxRepository = materiauxRepository;
        this.formationRepository = formationRepository;
        this.imagesTitresRepository = imagesTitresRepository;
        this.passwordEncoder = passwordEncoder; // Initialisez le PasswordEncoder
        this.messageRepository = messageRepository;
    }
    

    @Override
    public void run(ApplicationArguments args) throws Exception {

        
        // Création des materiaux
        createMateriauxElecricite();
        createMateriauxAmenagement();
        createMateriauxFinition();
        createMateriauxPlomberie();
        createMateriauxSerrurerie();

        // Création des formations
        createFormation1();
        createFormation2();

        this.formation = formationRepository.findById(1L).orElse(null);
        this.formation2 = formationRepository.findById(2L).orElse(null);

        // Création des utilisateurs
        createAdmin();
        createEducSimple();
        createEducSimple2();
        createSuperAdmin();
        createcip();
        createMichel();
        createJohnSmith();
        createJaneDoe();
        createAliceJohnson();
        createPaulWilliams();
        createEmilyBrown();
        createPierreDupont();

        // Creation d'une fiche d'Intervention
        createFicheIntervention();

        // Creation des images pour la fiche d intervention
        createImagesFicheIntervention();

        // Création des chat messages
        createChatMessages();

    }



    // Création des matériaux ELECTRICITE
    private void createMateriauxElecricite() {
        // Création des matériaux
        String[] electriciteImages = {
                "ampoule", "chauffage", "fil", "interrupteur", "planches", "vis"
        };

        for (String imageName : electriciteImages) {
            String nomMateriau = "ELECTRICITE_" + imageName;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl(URL_IMAGES_MATERIAUX + imageName + ".png");
                materiaux.setType("ELECTRICITE");
                materiauxRepository.save(materiaux);
            }
        }
    }

    // Création des materiaux
    private void createMateriauxAmenagement() {
        String[] amenagementImages = {
                "gants", "chaussures", "marteau", "metre", "pantalon", "crayon"
        };

        for (String imageName : amenagementImages) {
            String nomMateriau = "AMENAGEMENT_" + imageName;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl(URL_IMAGES_MATERIAUX + imageName + ".png");
                materiaux.setType("AMENAGEMENT");
                materiauxRepository.save(materiaux);
            }
        }
    }

    // Création des matériaux FINITION
    private void createMateriauxFinition() {
        String[] finitionImages = {
                "materiaufinition1", "materiaufinition2", "materiaufinition3",
                "materiaufinition4", "materiaufinition6"
        };

        for (String imageName : finitionImages) {
            String nomMateriau = "FINITION_" + imageName;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl(URL_IMAGES_MATERIAUX + imageName + ".jpg");
                materiaux.setType("FINITION");
                materiauxRepository.save(materiaux);
            }
        }
        // Création de "matériauxFinition5"
        Materiaux materiaux = materiauxRepository.findByNomImage("FINITION_materiaufinition5");
        if (materiaux == null) {
            materiaux = new Materiaux();
            materiaux.setNomImage("FINITION_materiaufinition5");
            materiaux.setImageUrl(URL_IMAGES_MATERIAUX + "materiaufinition5.png");
            materiaux.setType("FINITION");
            materiauxRepository.save(materiaux);
        }
    }

    // Création des matériaux PLOMBERIE
    private void createMateriauxPlomberie() {
        String[] plomberieImages = {
                "ventouse", "boitejoints", "furet", "teflon"
        };

        for (String imageName : plomberieImages) {
            String nomMateriau = "PLOMBERIE_" + imageName;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl(URL_IMAGES_MATERIAUX + imageName + ".jfif");
                materiaux.setType("PLOMBERIE");
                materiauxRepository.save(materiaux);
            }
        }
        // Créer "molettevirax"
        Materiaux materiaux = materiauxRepository.findByNomImage("PLOMBERIE_molettevirax");
        if (materiaux == null) {
            materiaux = new Materiaux();
            materiaux.setNomImage("PLOMBERIE_molettevirax");
            materiaux.setImageUrl(URL_IMAGES_MATERIAUX + "molettevirax.jpg");
            materiaux.setType("PLOMBERIE");
            materiauxRepository.save(materiaux);
        }

        //Créer "Chalumeau"
        materiaux = materiauxRepository.findByNomImage("PLOMBERIE_Chalumeau");
        if (materiaux == null) {
            materiaux = new Materiaux();
            materiaux.setNomImage("PLOMBERIE_Chalumeau");
            materiaux.setImageUrl(URL_IMAGES_MATERIAUX + "chalumeau.jpg");
            materiaux.setType("PLOMBERIE");
            materiauxRepository.save(materiaux);
        }
    }

    // Création des matériaux SERRURERIE
    private void createMateriauxSerrurerie() {
        String[] serrurerieImages = {
                "gants", "chaussures", "marteau", "metre", "pantalon", "crayon"
        };

        for (String imageName : serrurerieImages) {
            String nomMateriau = "SERRURERIE_" + imageName;
            Materiaux materiaux = materiauxRepository.findByNomImage(nomMateriau);
            if (materiaux == null) {
                materiaux = new Materiaux();
                materiaux.setNomImage(nomMateriau);
                materiaux.setImageUrl(URL_IMAGES_MATERIAUX + imageName + ".png");
                materiaux.setType("SERRURERIE");
                materiauxRepository.save(materiaux);
            }
        }
    }

    // Creation Formation 1
    private void createFormation1() {
        Formation formationcap2 = new Formation();
        formationcap2.setNom("Agent de maintenance en bâtiment");
        formationcap2.setNiveau_qualif("CAP 2");
        formationcap2.setDescription("Description Formation1");
        formationRepository.save(formationcap2);
    }

    // Creation Formation 2
    private void createFormation2() {
        Formation formationcap1 = new Formation();
        formationcap1.setNom("Formation 2");
        formationcap1.setNiveau_qualif("CAP 1ere Annee");
        formationcap1.setDescription("Description Formation2");
        formationRepository.save(formationcap1);
    }


    // Creation de l´administrateur
    private void createAdmin() {
        final String ADMIN_LOGIN = "admin";
        final String ADMIN_NAME = "Admin";
        Utilisateur admin = utilisateurRepository.findUserByLogin(ADMIN_LOGIN);
        if (admin == null) {
            admin = new Utilisateur();
            admin.setFormations(List.of(formation, formation2));
            admin.setNom(ADMIN_NAME);
            admin.setPrenom(ADMIN_NAME);
            admin.setLogin(ADMIN_LOGIN);
            admin.setMdp(passwordEncoder.encode(ADMIN_LOGIN));
            admin.setRole(UserRole.ADMIN);
            admin.setPhotoBase64(photoBase64);
            admin.setDescription("Création et modification visuelles totales, laisser des traces écrite/vocal");
            utilisateurRepository.save(admin);
        }
    }

    // Creation de l´educsimple
    private void createEducSimple() {
        final String EDUCSIMPLE_LOGIN = "educsimple";
        Utilisateur educsimple = utilisateurRepository.findUserByLogin(EDUCSIMPLE_LOGIN);
        if (educsimple == null) {
            educsimple = new Utilisateur();
            educsimple.setFormations(List.of(formation, formation2));
            educsimple.setNom("Educ");
            educsimple.setPrenom("Simple");
            educsimple.setLogin("educsimple");
            educsimple.setMdp(passwordEncoder.encode(EDUCSIMPLE_LOGIN));
            educsimple.setRole(UserRole.EDUCSIMPLE);
            educsimple.setPhotoBase64(photoBase64);
            educsimple.setDescription("Utilisation des fiches, pourra laisser des traces écrite/vocal \r\n" + //
                    "");
            utilisateurRepository.save(educsimple);
        }
    }

    // Creation de l´educsimple2
    private void createEducSimple2() {
        final String EDUCSIMPLE2_LOGIN = "educsimple2";
        Utilisateur educsimple2 = utilisateurRepository.findUserByLogin(EDUCSIMPLE2_LOGIN);
        if (educsimple2 == null) {
            educsimple2 = new Utilisateur();
            educsimple2.setFormations(List.of(formation));
            educsimple2.setNom("Educ");
            educsimple2.setPrenom("Simple2");
            educsimple2.setLogin(EDUCSIMPLE2_LOGIN);
            educsimple2.setMdp(passwordEncoder.encode(EDUCSIMPLE2_LOGIN));
            educsimple2.setRole(UserRole.EDUCSIMPLE);
            educsimple2.setPhotoBase64(photoBase64);
            educsimple2.setDescription("Utilisation des fiches, pourra laisser des traces écrite/vocal \r\n" + //
                    "");
            utilisateurRepository.save(educsimple2);
        }
    }

    // Creation du superadmin
    private void createSuperAdmin() {
        final String SUPERADMIN_LOGIN = "superadmin";
        Utilisateur superadmin = utilisateurRepository.findUserByLogin(SUPERADMIN_LOGIN);
        if (superadmin == null) {
            superadmin = new Utilisateur();
            superadmin.setFormations(List.of(formation, formation2));
            superadmin.setNom("Super");
            superadmin.setPrenom("Admin");
            superadmin.setLogin(SUPERADMIN_LOGIN);
            superadmin.setMdp(passwordEncoder.encode(SUPERADMIN_LOGIN));
            superadmin.setRole(UserRole.SUPERADMIN);
            superadmin.setPhotoBase64(photoBase64);
            superadmin.setDescription("Description de l'administrateur");
            utilisateurRepository.save(superadmin);
        }
    }

    // Creation du cip
    private void createcip() {
        final String CIP_LOGIN = "cip";
        Utilisateur cip = utilisateurRepository.findUserByLogin("cip");
        if (cip == null) {
            cip = new Utilisateur();
            cip.setFormations(List.of(formation, formation2));
            cip.setNom(CIP_LOGIN);
            cip.setPrenom(CIP_LOGIN);
            cip.setLogin("cip");
            cip.setMdp(passwordEncoder.encode(CIP_LOGIN));
            cip.setRole(UserRole.CIP);
            cip.setPhotoBase64(photoBase64);
            cip.setDescription(
                    "Suivi parcours, évolution des compétences, adaptation de la situation d'examen. Pas de modifs visuelles");
            utilisateurRepository.save(cip);
        }
    }

    // Création d'un utilisateur michel
    private void createMichel() {
        final String MICHEL_LOGIN = "michelmichel";
        Utilisateur michel = utilisateurRepository.findUserByLogin(MICHEL_LOGIN);
        if (michel == null) {
            michel = new Utilisateur();
            michel.setFormations(List.of(formation, formation2));
            michel.setNom(MICHEL);
            michel.setPrenom(MICHEL);
            michel.setLogin(MICHEL_LOGIN);
            michel.setMdp(passwordEncoder.encode("1234"));
            michel.setRole(UserRole.USER);
            michel.setNiveau(1);
            michel.setPhotoBase64(apprenti);
            michel.setDescription("Description de Michel Michel");
            utilisateurRepository.save(michel);
        }
    }

    // Création d'un utilisateur johnSmith
    private void createJohnSmith() {
        final String JOHN_SMITH_LOGIN = "johnsmith";
        Utilisateur johnSmith = utilisateurRepository.findUserByLogin(JOHN_SMITH_LOGIN);
        if (johnSmith == null) {
            johnSmith = new Utilisateur();
            johnSmith.setFormations(List.of(formation));
            johnSmith.setNom("Smith");
            johnSmith.setPrenom("John");
            johnSmith.setLogin(JOHN_SMITH_LOGIN);
            johnSmith.setMdp(passwordEncoder.encode("9876"));
            johnSmith.setRole(UserRole.USER);
            johnSmith.setNiveau(1);
            johnSmith.setPhotoBase64(apprenti);
            johnSmith.setDescription("Description de John Smith");
            utilisateurRepository.save(johnSmith);
        }
    }

    // Création d'un utilisateur janeDoe
    private void createJaneDoe() {
        Utilisateur janeDoe = utilisateurRepository.findUserByLogin("janedoe");
        if (janeDoe == null) {
            janeDoe = new Utilisateur();
            janeDoe.setFormations(List.of(formation));
            janeDoe.setNom("Doe");
            janeDoe.setPrenom("Jane");
            janeDoe.setLogin("janedoe");
            janeDoe.setMdp(passwordEncoder.encode("4321"));
            janeDoe.setRole(UserRole.USER);
            janeDoe.setNiveau(1);
            janeDoe.setPhotoBase64(apprentie);
            janeDoe.setDescription("Description de Jane Doe");
            utilisateurRepository.save(janeDoe);
        }
    }

    // Création d'un utilisateur aliceJohnson
    private void createAliceJohnson() {
        Utilisateur aliceJohnson = utilisateurRepository.findUserByLogin("alicejohnson");
        if (aliceJohnson == null) {
            aliceJohnson = new Utilisateur();
            aliceJohnson.setFormations(List.of(formation));
            aliceJohnson.setNom("Johnson");
            aliceJohnson.setPrenom("Alice");
            aliceJohnson.setLogin("alicejohnson");
            aliceJohnson.setMdp(passwordEncoder.encode("2468")); // Mot de passe à 4 chiffres
            aliceJohnson.setRole(UserRole.USER);
            aliceJohnson.setNiveau(1);
            aliceJohnson.setPhotoBase64(apprentie);
            aliceJohnson.setDescription("Description d'Alice Johnson");
            utilisateurRepository.save(aliceJohnson);
        }
    }

    // Création d'un utilisateur paulWilliams
    private void createPaulWilliams() {
        Utilisateur paulWilliams = utilisateurRepository.findUserByLogin("paulwilliams");
        if (paulWilliams == null) {
            paulWilliams = new Utilisateur();
            paulWilliams.setFormations(List.of(formation));
            paulWilliams.setNom("Williams");
            paulWilliams.setPrenom("Paul");
            paulWilliams.setLogin("paulwilliams");
            paulWilliams.setMdp(passwordEncoder.encode("1357"));
            paulWilliams.setRole(UserRole.USER);
            paulWilliams.setNiveau(1);
            paulWilliams.setPhotoBase64(apprenti);
            paulWilliams.setDescription("Description de Paul Williams");
            utilisateurRepository.save(paulWilliams);
        }
    }

    // Création d'un utilisateur emilyBrown
    private void createEmilyBrown() {
        Utilisateur emilyBrown = utilisateurRepository.findUserByLogin("emilybrown");
        if (emilyBrown == null) {
            emilyBrown = new Utilisateur();
            emilyBrown.setFormations(List.of(formation));
            emilyBrown.setNom("Brown");
            emilyBrown.setPrenom("Emily");
            emilyBrown.setLogin("emilybrown");
            emilyBrown.setMdp(passwordEncoder.encode("9876"));
            emilyBrown.setRole(UserRole.USER);
            emilyBrown.setNiveau(1);
            emilyBrown.setPhotoBase64(apprentie);
            emilyBrown.setDescription("Description d'Emily Brown");
            utilisateurRepository.save(emilyBrown);
        }
    }

    // Création d'un utilisateur pierreDupont
    private void createPierreDupont() {
        Utilisateur pierreDupont = utilisateurRepository.findUserByLogin("pierredupont");
        if (pierreDupont == null) {
            pierreDupont = new Utilisateur();
            pierreDupont.setFormations(List.of(formation));
            pierreDupont.setNom("Dupont");
            pierreDupont.setPrenom("Pierre");
            pierreDupont.setLogin("pierredupont");
            pierreDupont.setMdp(passwordEncoder.encode("7462"));
            pierreDupont.setRole(UserRole.USER);
            pierreDupont.setNiveau(1);
            pierreDupont.setPhotoBase64(apprenti);
            pierreDupont.setDescription("Description de Pierre Dupont");
            utilisateurRepository.save(pierreDupont);
        }
    }

    // Creation d'une fiche d'Intervention
    private void createFicheIntervention() {
        FicheIntervention ficheIntervention = ficheRepository
                .findByTravauxRealisesAndUtilisateurPrenom("Travaux réalisés par Michel Michel", MICHEL);
        if (ficheIntervention == null) {
            ficheIntervention = new FicheIntervention();
            Demande demande = new Demande();
            Maintenance maintenance = new Maintenance();
            Intervention intervention = new Intervention();
            Intervenant intervenant = new Intervenant();
            // Définir les attributs de l'intervenant
            intervenant.setNom(MICHEL);
            intervenant.setPrenom(MICHEL);
            intervenant.setNiveauTitreIntervenantNom(1);
            intervenant.setNiveauTitreIntervenantPrenom(1);
            intervenant.setNiveauTitreIntervenant(1);
            intervenant.setCouleurTitreIntervenant(VIOLET);
            intervenant.setCouleurNom("#F00");
            intervenant.setCouleurPrenom(CYAN);
            intervenant.setImageTitreIntervenantUrl(ICONE_ELEVE);
            intervenant.setImageTitreIntervenantNomUrl(ICONE_ACCUEIL);
            intervenant.setImageTitreIntervenantPrenomUrl(ICONE_ELEVE);

            // Définir les attributs de l'intervention
            intervention.setNiveauTitreIntervention(1);
            intervention.setNiveauDateIntervention(1);
            intervention.setNiveauDureeIntervention(1);
            intervention.setNiveauTypeIntervention(1);
            intervention.setCouleurTitreIntervention(VIOLET);
            intervention.setCouleurDateIntervention("#F00");
            intervention.setCouleurDureeIntervention(CYAN);
            intervention.setCouleurTypeIntervention("#F00");
            intervention.setImageTitreInterventionUrl(ICONE_ACCUEIL);
            intervention.setImageDureeInterventionUrl(ICONE_ACCUEIL);
            intervention.setImageTypeInterventionUrl(ICONE_ELEVE);
            intervention.setImageDateInterventionUrl(ICONE_ACCUEIL);
            intervention.setTypeIntervention(Intervention.TypeIntervention.AMENAGEMENT.toString());
            // Définir les attributs de la maintenance
            maintenance.setMaintenanceType(Maintenance.MaintenanceType.AMELIORATIVE);
            maintenance.setNiveauMaintenanceType(1);
            maintenance.setCouleurMaintenanceType(VIOLET);
            maintenance.setImageTypeMaintenanceUrl(ICONE_ACCUEIL);
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
            demande.setCouleurTitreDemande(VIOLET);
            demande.setCouleurNomDemandeur("#F00");
            demande.setCouleurDateDemande(CYAN);
            demande.setCouleurLocalisation(VIOLET);
            demande.setCouleurDescription("#F00");
            demande.setCouleurDegreUrgence(CYAN);
            demande.setImageTitreDemandeUrl(ICONE_ACCUEIL);
            demande.setImageTitreDemandeNomUrl(ICONE_ACCUEIL);
            demande.setImageTitreDemandeDateUrl(ICONE_ACCUEIL);
            demande.setImageTitreDemandeLocalisationUrl(ICONE_ACCUEIL);
            demande.setImageTitreDemandeDescriptionUrl(ICONE_ACCUEIL);
            demande.setImageTitreDemandeDegreUrgenceUrl(ICONE_ACCUEIL);
            // Définir les attributs des travaux réalisés
            ficheIntervention.setNiveauTravauxRealises(1);
            ficheIntervention.setCouleurTravauxRealises("#aef800");
            ficheIntervention.setImageTitreTravauxRealisesUrl(ICONE_ACCUEIL);
            ficheIntervention.setTravauxRealises("Travaux réalisés par \nMichel Michel \n      Ligne 3");
            // Définir les attributs des travaux non réalisés
            ficheIntervention.setNiveauTravauxNonRealises(1);
            ficheIntervention.setCouleurTravauxNonRealises(VIOLET);
            ficheIntervention.setImageTitreTravauxNonRealisesUrl(ICONE_ACCUEIL);
            ficheIntervention.setTravauxNonRealises("Travaux non réalisés par Michel Michel");
            // Définir les attributs des materiaux utilisés
            ficheIntervention.setNiveauMateriauxUtilises(1);
            ficheIntervention.setCouleurMateriauxUtilises(CYAN);
            ficheIntervention.setImageTitreMateriauxUtilisesUrl(ICONE_ACCUEIL);
            // Définir les attributs de la fiche d'intervention
            ficheIntervention.setIntervenant(intervenant);
            ficheIntervention.setDemande(demande);
            ficheIntervention.setMaintenance(maintenance);
            ficheIntervention.setIntervention(intervention);
            ficheIntervention.setDateCreation(LocalDate.now());
            ficheIntervention.setEtatFicheFinie(false);
            ficheIntervention.setNouvelleIntervention(true);
            Utilisateur michel = utilisateurRepository.findUserByLogin("michelmichel");
            ficheIntervention.setUtilisateur(michel);

            // Ajouter les materiaux de type amenagement
            // On ajoute 4 materiaux et rien dans les deux derniers
            List<String> materiauxAmenagement = new ArrayList<>();
            materiauxAmenagement.add("AMENAGEMENT_gants");
            materiauxAmenagement.add("AMENAGEMENT_chaussures");
            materiauxAmenagement.add("AMENAGEMENT_marteau");
            materiauxAmenagement.add("AMENAGEMENT_metre");
            materiauxAmenagement.add("AMENAGEMENT_pantalon");
            materiauxAmenagement.add("AMENAGEMENT_crayon");
            ficheIntervention.setMateriauxOptions(materiauxAmenagement);
            ficheRepository.save(ficheIntervention);
        }
    }

    // Creation des images pour la fiche d intervention
    private void createImagesFicheIntervention() {
        String[] types = { "INTERVENANT", "DEMANDE", "INTERVENTION", "MATERIAUX_UTILISES", "TRAVAUX_REALISES",
                "TRAVAUX_NON_REALISES", "INTERVENANT_PRENOM", "INTERVENANT_NOM", "DEMANDE_NOM", "DEMANDE_DATE",
                "DEMANDE_LOCALISATION", "DEMANDE_DESCRIPTION", "DEMANDE_DEGRE_URGENCE", "MAINTENANCE_TYPE",
                "INTERVENTION_DATE", "INTERVENTION_DUREE", "INTERVENTION_TYPE" };
        String[] nomImages = { "Image1", "ADMIN", "Image2", "FICHES1", "FICHES2", "FICHES3", "FICHES4" };
        String[] imageUrls = { "images/accueil.png", "images/admin.png", "images/archive.png", ICONE_ELEVE,
                ICONE_ELEVE, ICONE_ELEVE, ICONE_ELEVE };

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

    // Création des chat messages
    private void createChatMessages() {
        Message chatMessage = new Message();
        chatMessage.setTextContent("Bonjour \n je suis Admin Admin");
        chatMessage.setTimestamp(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(1611382800000L), ZoneId.systemDefault()));
        Utilisateur admin = utilisateurRepository.findUserByLogin("admin");
        chatMessage.setSender(admin);
        FicheIntervention ficheIntervention = ficheRepository
                .findById(1L).orElse(null);
        chatMessage.setFicheIntervention(ficheIntervention);
        chatMessage.setAudio(null);
        messageRepository.save(chatMessage);

        Message chatMessage2 = new Message();
        chatMessage2.setTextContent("message2");
        chatMessage2.setTimestamp(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(1611382800000L), ZoneId.systemDefault()));
        chatMessage2.setSender(admin);
        chatMessage2.setFicheIntervention(ficheIntervention);
        chatMessage2.setAudio(null);
        chatMessage2.setImageUrl("/images/intervenant/aa.jpg");
        messageRepository.save(chatMessage2);
    }

    // Champs trop longs et inutiles pour être mis en haut
    // Photo d'une apprentie
    private String apprentie = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAgAAAAIACAYAAAD0eNT6AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAACAASURBVHic7N13fBx3nT/+12dmu7pkyZIt27Jc5W7LcUuTS5w4veCEUHKhPOgccEe5HAfHl+MoOTgCR360QCBAEichQCA4zS2kJ47TbMndli1bXdpdbZ+Zz+8PJSEk1uzs7mil3X09Hw8/7uL5zHw+N+edec+nvD8AERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERGNC2KsG0BUyDbP3+zqF/0VCYhKh2ZUGBCVQohKCVkhgVIBUSQhSwB4FIgSAygSMNyAUi4hvRDwAICQEIAo//uVpQBQfoYqg4DQ3vnXckgKJIRECBBxQEYARAFEBUREQsYkEBBCBiAxCAG/NOBXhBIwhAwowggIQww6nI7eR155JDQKt4qIbMYAgGiUNDc3O8tD5dMNRTQZErMhZT2ErBcQdRKYLIBKAMVj3c5REAHQC4huQHYLiF7A6IUQvYDohESXLtGuGNrpnQd29o51Y4kKFQMAIhs0Nzc7SyOly6RUlkNgOaRcDog5AJxj3bZxLgqgA8ApSLQD4rRQ5AkYYr8QRtv21u3tAOQYt5EoLzEAIEpTy/wLlkDqGyDFOgDnIj+/5sdaCBBtkLJNKNgHiL1S055kzwFR5hgAEFm0GZvV3nmDLTD0K6QQlwOYNtZtKlASkHshlJ0SxuMORTy+7bVtXWPdKKJcwwCAKIm1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";
    // Photo d'un apprenti
    private String apprenti = "data:image/png;base64,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";
    // Photo de Michel Michel
    private String photoBase64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAgAAAAIACAYAAAD0eNT6AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAACAASURBVHic7N15mCVVYTf+7zm13L336WUWmGGZAWaTdQYRWTXuksQXWRQDaow/jdFHf6Lm9xryahKMhkQFFdmyoP5EEzUS4woY0bDDAAPDjCwDs/S+3O7bd6uq8/4xQAbsmenuW1Wnlu/neXz0cbqrvlNz7z3fe+pUFUBERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERERER0aEJ3ACLaRymI8es3LHOFOgISRwqlDlMQSwAsgUAvFHoA5AG0Q0BCwQZQCDlmHQKz+wKjAmASwBiAcSg1poTcLaCeEfB2up7xTJ9n7RLvu78ZckYimgcWACINJr+yvrNmixMEvOMhxfES2KgUjgSQ1Z3NTwpoCGCbEnhUKDwilHrEkdY9A5c9OKI7G1HasQAQhWD82rWHNQ1xhpDiTCi8GsBRujNpth1QdwklfqMM47beSx/aoTsQUdqwABAFQN2y1h6eMc+SnvtWJcTrAKzSnSninhJK/cSV4icwxc/7L3m4ojsQUdKxABD5ZM+1J+Ytq/lWpdR5AF4HoE13ppiqAupHQsnvqrp7a+8Hts7oDkSURCwARC1QV0COrlh7hoK8BMAfAijpzpQwVQA/VELe2Pvslp+JK+DpDkSUFCwARIsw+M8bemVD/bGSeK9QOEx3njRQAs/Cwz9arndD1/u2Pqs7D1HcsQAQLcDw9RuPV8L7EwG8E0BOd56U8gDxI6XcK/ves/XXusMQxRULANE8DF6/4Rwp1P8GcIbuLPQSvxEQn+u57OEfCgGlOwxRnLAAEB3E8A0bX6Xg/aUAztadhQ7qESh8Zsm7H/kuiwDR/LAAEM1h6Pq1pwkhPw/gVN1ZaEHuUwKf7LvskZ/rDkIUdSwARPsZu3HdCk/Jv1JQ7wDfH7ElgJ+7Snyk/z0PP6o7C1FU8QOOCMDwNWuLIis+rSA+BCCjOw/5ognga5Y0Pt156UOTusMQRQ0LAKXe8E3r3yA88RUFdbjuLBSIQQVxed+7H/5n3UGIooQFgFJr+Ka1/XCNf4BQb9edhYKngH83hfpg92WPPqc7C1EUsABQKg3fuOFtSqlrBdClOwuFakoJ9cG+yx69WXcQIt1YAChVnrvl1FxmunIloD6kOwtp9d2M8t7X/p6t47qDEOnCAkCpMXrT2pM9T3wDEEfrzkKRsAtSXNR76cO/0h2ESAepOwBRGEZuXP/Hrifv5OBP+1kOT902fP36y5XilyFKH77oKdF2fOmoTFsh92UBvFd3FoouAXzfcOw/6nrf/VO6sxCFhQWAEmvsug3LHen9q4A4RXcWioVHTMd7E580SGnBAkCJtPfrG9cahvefAFbozkKxMiig3rzk3Y/epzsIUdC4BoASZ+S6dWcahncnOPjTwvUriDsGb1z/Vt1BiILGAkCJMnTduj9QUvwngA7dWSi2ClLhX4dvWPdu3UGIgsQCQIkxcuOG9wgpvgMgqzsLxZ4BiOuGb1j3p7qDEAWFBYASYeTGDe9RSl0LvqbJPwIQXxq+cf2VuoMQBYGLACn2Rm5Y/14FfA0c/CkoAp/rveyRT+iOQeQnfmBSrI1cv/59CuA3fwqWwuVDN2y4QncMIj9xBoBia+SGDW9RUP8GwNCdhVKCMwGUICwAFEsjN64/Qyn8GFzwR2FjCaCEYAGg2Bm8fsM6KdV/QaFTdxZKKaU+1fueR/9GdwyiVrAAUKwM37S2H568F8By3Vko7dSHet/96Jd1pyBaLC6cothQt6y14clvg4M/RYL44sgN6/mQKYotFgCKjZEZ+XUAr9adg+h5QgFf5W2DKa54CoBiYfiG9R8AcLXuHERzqAioM/kAIYobFgCKvKHr1m0QUtwFIKc7C9EBDDrK2bz0PY/v1B2EaL54CoAibezmo9qEFP8KDv4Ubf2mMG8dvmZtUXcQovliAaBIcxq5rwI4SncOonlYh4xxve4QRPPFAkCRNXT9+vOEwkW6cxDNm1BvH7lx/Ud0xyCaD64BoEgaum5dn5B4FBA9urMQLVATUpzTe+nDv9IdhOhgOANAkSSl+BoHf4opC5765uRX1vNOlRRpLAAUOUPXrz9PAefpzkHUguWNrOB6AIo0ngKgSNlz7Yl502xsBbBSdxaiVimh3tl32aM3685BNBfOAFCkWGb9CnDwp4QQSlw9duO6FbpzEM2FBYAiY+gf1x3pQfyZ7hxEPmp3PfE13SGI5sICQJEhXfEFAdi6cxD5SuANQzds+EPdMYhejmsAKBJGblx/hlK4Q3cOooAMWtI4tvPShyZ1ByF6AWcAKBI8pf5WdwaiAPU3Pe8vdYcg2h9nAEi7kevXvVEJcavuHEQBc1xXvmLgj7ds1R2ECOAMAEWAJ/Bp3RmIQmAahrpSdwiiF7AAkFbDN61/g4A4RXcOonCoNw3duP5c3SmIABYA0s3FJ3RHIAqTUPicUjz9SvqxAJA2I9etPxECp+vOQRSyE0ZvWvdG3SGIWABIGyXVx3RnINJBKfGXnAUg3fgCJC1G/2njMs/xngFg6s5CpINQ4i1L3vPwD3XnoPTiDABpoRzvMnDwpxRTApfrzkDpxgJAoVMKQim8S3cOIr3UaYM3btikOwWlFwsAhW74xnWvgcCRunMQ6SaV92HdGSi9WABIA3GJ7gRE0SDexscFky4sABSqPdeemBfAW3XnIIoI01Hy3bpDUDqxAFCoTKv+ZgBF3TmIokJAXaZu+V+G7hyUPiwAFC4l3q47AlHErBieevxs3SEofVgAKDRP37QyC+C1unMQRY2QuEx3BkofFgAKTdEtngOgoDsHUfSItwxfs5anxihULAAUJt7/nGhueWTFG3SHoHRhAaDQKCFerzsDUWQJ8TbdEShd+CwACsXgdRtWSame0p2DKMIqjmP3Ln3f/bO6g1A6cAaAQiGFd5buDEQRV7CsOq8GoNCwAFA4BM7QHYEo6hTE7+nOQOnBAkAhEa/SnYAo8pRiAaDQcA0ABW7q+rVddSFHwdcb0SEpgSP7LnuE62UocJwBoMDVlDgRHPyJ5kVCcB0AhYIFgAInBE7SnYEoLpRSr9SdgdKBBYCCJ7BBdwSiGGEBoFCwAFAIxLG6ExDFyOo9157YozsEJR8LAAVKXQEJYLXuHEQxIky7cYruEJR8LAAUqOHD1q8EkNOdgyhWXKzXHYGSjwWAgnaE7gBEcSOAdbozUPKxAFCghKcO052BKG6U4AwABY8FgAIlhFimO8NiGZlu5PvOgLTbdUehBZJ2O/J9Z8DIdOuOsljHqCvONHWHoGTjC4wC5QHL43oHoEL/ubBKRyDXcyqalZ2oTTyIxtQTAJTuaDQXIWAVDke283jYbasBIWHmlqL8zLd0J1uMzODhk8sBPKM7CCUXCwAFrV93gMWw29bAKj2/fEEIWMWVsIor4Q1Moz7+EGpjD8Bz+dTWKBBGFpmOdch2nwzD7njJn1nFlbDbVqNR3q4p3eKZyj0cLAAUIBYACpRQ6IzbTYCFMJHvP2fOP5NmCbne05HtORXNmSdRn3wMzenfQikn5JTpJoQJq3QUMh3HwS4dBQjjgD+b7z8XzemnYvdvpDyxEsAvNcegBGMBoGAJdOqOsFDZns0wDnHeX0gTdtsa2G1roLwmmtNPojb1CJrTTwHKCylp2ghY+WXIdK6H3X4shMzM67cMux3ZJZtQHf51wPn8JYS3UncGSjYWAAparFbQGVY7cks2L+h3hLRgtx8Du/0YeE4FjaltaExvR7PyHKDcgJKmhDBgFVbALq2G3XEspJFf1GZyPaeiMfEo3OaUzwGD4wkxoDsDJRsLAAVKAYU4nQHID5wDIa1F/740C8h2n4hs94lQXhNOZSfq5R1ozjwJrzntY9LkkmYeVvEI2KWjYRVXQRjz+6Z/MEJayA+cjelnv+dDwnAIhdhewkDxwAJAgRKArTvDfO1bMLbGt+0JacEqHQWrdBQABac6hOb0k2hUdsKt7oHymr7tK86EtGDml8EqHA6rdATMbB+CeHq03XYMrOJKNGee8X3bwRAsABQoFgAK2uK/TodJSOQHzg1yBzBz/TBz/cjhNAAKbn0MzuwuNGaehlN5Fp6TjqsKhMzAzA/AKqyCmV8OKz9w0EV8fioMvAaTv70hJus0FAsABYoFgIIWiwKQ7ToRZmZJiHsUMDI9MDI9yHS+Ai8UArc6BKc2DKc2BLc+Aq85E2Im/0mzCCPXCzPbCzPbByPbByPThSC+4c+HkelBtutE1Mbu1bL/BYrdAlqKFxYAClrk7zYpjTzyvadrTvE/hcDG2hf/X8+ZhVsbhlsbgtuYgNucgteYhNcsQ3nRuKxNSBPSaoO0O2BY7TDsLhjZXhjZPkgzes+Byveejsbk1jjcx6H1xQ9EB8ECQKmX7z/Tl4VmQZBmHvL5mxC9lILnzMBtPF8InBkopwrPre47leBV4TlVKHcWymsuuCwIaUJIC8LIQ5o5CJmDMPOQRg7CzEGaBUi7E4bdDmkWoesb/WIII4N8/5mY2f0j3VEOJZovSkoMFgBKNTM/gEzHBt0xFkFAmiVIswTkl8/zdxSUW9/3v5TzYikQ0oQQ+z4K9hWh+Azmi5Xp2IDaxINwZvfqjnIwsVlAS/EU+elZosAIgfzAawGR/AFvHwFhZCGM7L5z83YHDLsD0iy++P+nYfAHAAiBwsDrov5vzxkAChQLAKVWpmMDrNxS3TFIEzPXH/XZH34+U6D4AqNUEjKDfN+rdccgzaK8/oMoaCwAlEr5vlc/v3iN0iwaV4AQ6cECQKmz71rwE3THoIgI/x4QRNHAAkCpUxg4BxB86dPzhERh6Wt0pyAKHT8FKVXs9mNgFY/QHYMixiwcDrvtGN0xiELFAkCpIYSJfP/ZumNQRBVafBIkUdywAFBq5Ja8EobVrjsGRZS02pBbsll3DKLQsABQKhh2B7JLNumOQRGX7d4Mw+YzeCgdWAAoFfID5754u1uiAxGSp4koPVgAKPGs4krYpaN1x6CYsNtWwyodqTsGUeBYACjZhIHCwGt1p6CYKQy8BkJyxoiSjQWAEi3XfRKMTLfuGBQzht2JbNdJumMQBYoFgBJLmgXklpymOwbFVK73NN4umhKNBYASK99/Fh/0QosmpI18/1m6YxAFhgWAEsnKL0OmY53uGBRzmY61sPIrdMcgCgQLACWPEMgPnAtA6E5CsSeQH3gN+FqiJGIBoMTJdL4CZm6p7hiUEGauD5nOjbpjEPmOBYASRRhZ5HvP0B2DEqbQfyakkdMdg8hXLACUKPneMyBNflCTv4SRQ67vdN0xiHzFAkCJYWZ7kO16he4YlFDZrhNgZvt0xyDyDQsAJUZ+4LWA4EuagiKQX/pacEEgJQU/LSkR7PbjYBUO1x2DEs7KL4fdfozuGES+YAGg2BPSQqHvTN0xKCUKA+dASFt3DKKWsQBQ7OWWnAZpt+uOQSkhzRJyS07VHYOoZSwAFGuG3Yls98m6Y1DK5Ho2wch06Y5B1BIWAIq1wsC5fGwrhU8YKPSfozsFUUtYACjWrNJRuiNQSvG1R3HHAkBERJRCLABEREQpxAJARESUQiwAREREKcQCQERElEIsAERERCnEAkBERJRCLABEREQpxAJARESUQiwAREREKcQCQERElEIsAERERCnEAkBERJRCLABEREQpxAJARESUQqbuAERx4tRHUB/fAqfyNNzGFJTXDHR/Qlow7HaYhZXIdL4CZnZJoPtrFY8PUXywABDNg/IczA7+HLWJhwClQtxvE05tFE5tFLWx+5HtOgGFgXMAYYSWYT54fIjih6cAiA5BeQ7KO29BbfzBUAe3OZKgNn4/pp75NqBcjTleiseHKJ5YAIgOYXbwF3AqO3XHeJFT2YnK4C90x3gRjw9RPLEAEB2EUx9BbeJB3TF+R23sATi1Ud0xeHyIYowFgOgg6uNbNE9rH4hCfWKL7hA8PkQxxgJAdBDNmad1Rzggp6I/G48PUXyxABAdhNcs645wQG5jSncEHh+iGONlgKSFYXfCKq6EmV8GI9MDabVDGDaE4Ety/oTuABEX/PHpXvfJef+s8hpQXgNuYxJefQzN2efQnH4anjMTYEKiA+OnLYVGCBOZzvX7btiS69cdZ16k1Qa3Hs3FZIbdpjsCj88CCGlDSBvSLAL55ch0bgSg4FSeRXX8ITSmHgcQxfUUlFQsABQCgWz3icgtOXXfh1+MWMWVkR3gzOIq3RF4fFomYBYOR6lwONy+V2N26JfPFwGi4HENAAXKyPSg/ch3oTDwmtgN/gCQ6XwFojnVLpDt3Kg7BI+Pjwy7E6UV56Ht8PMhrfi9Vyh+WAAoMM4933p7+5Hvgpkb0B1l0czsEmS7TtAd43dku0+CkenRHYPHJwBW6Ui0H3kprPwK3VEo4VgAKBDuPd94v1Dqm0LauqO0rDBwDqziSt0xXmQVV6LQf7buGC/i8fGfNIsorbwAzj3f+APdWSi5ojh3RzHn3vON90OJr+jO4SvlojL4C9TGHoC+hVoC2e6T9g1uImLdnccnKE1P4C3WKRf9WHcQSh4WAPKVc/e33iigfgAgkY9jc2qjqE/s/7jbRqD7E9Le97jb4ipkOzdGflqbxycQ01LhVWLzRQ/rDkLJwgJAvlG/uWWZZzhbAHTrzkKUMDvkrHmCOOt83jSAfJOYeTLST8nmdeDgTxSEo72C81ndIShZOANAvnD++1tvEVL9QHcOogRzpMArxCkXbdUdhJKBMwDkCyHVX+rOQJRwplK4QncISg7OAFDLmvd883VS4T915yBKAVcqdazYfPEO3UEo/jgDQC0zgPfozkCUEoYnxLt0h6BkYAGglqi7bm5TCm/UnYMoRS7UHYCSgQWAWuIKvBpAVncOohQ5Qv3mX47SHYLij08DpJYIyNN1Z4g85aJRfgL18g64tUF4zWkAgLRKMLL9yLStht22GhCJvHfS7+LxaJlryDMA/FZ3Doo3FgBqiQDW8QnmB9YoP4HZwdvgNiZ/58/c+jjc+jgaU4/BsDuR7zsLdvsaDSnDw+PhDwGs1Z2B4o+nAKglCuBU5JwUKoO3YfrZf5tzsHs5tzGB6ef+DbODtwMqiZWKx8NPAvJo3Rko/lgAqFVLdAeIosrg7aiN3r3g36uO3oXZoTv8D6QZj4e/PCi+76hlLADUqqLuAFHTKD+xqMHuBdXRu9Aob/cxkV48Hv4TQEl3Boo/FgBqlaU7QKQod9+0dYtmB38OKNeHQJrxeARE2LoTUPyxABD5qFF+Am5jouXtuI0pNMrxv9kbjwdRdLEAEPmo7uNUdX06/tPePB5E0cUCQOQjtzro47b2+rYtXXg8iKKLBYDIR55T8W9bzRnftqULjwdRdLEAEEUWH9b5UjweRH5iASDykbT8uyrSz23pwuNBFF0sAEQ+MrL9vm3LzA34ti1deDyIoosFgMhHmbbVvm3Lbov/3V55PIiiiwWAyEd22xoYdmfL2zHsDtil+A94PB5E0cUCQOQnIZHvO6vlzeQHzk3G43B5PIgii48DJvKZ3b4GuepmVEfvWtTv55ZsXvS3XcdpojpbQaNWg+s6AADDMGFnMsjlizCt8O/crPN4ENGBsQAQBSDfdyYAheoCH4KT69mMfO+ZC96fUgrT5UlUZ2eAlz0913GacJwmZiszyBVKKLW1Q4hwL6kL+3gQ0aGxABAFQQjk+8+GmV+O2cHbDnk/fMPuRH7gnEV901VKYWJsBM1G/ZA/W61Mw3Ga6OzqCbcEhHg8iGh+WACoVQ0AfDLZAdhtq2GXjkKjvB316e1wq4PwmtMAAGmVYOT6kWlbDbu0GhCLW5IzXZ6c1+D/gma9hpnyJErtrS/OW6gwjkc6qPn/gxMdAAsAtaoMoEd3iEgTEnb7MbDbj/F90/vO+S/8Frn7TgcUYZoanuYc4PFICwVM6c5A8ceKTa0a1x0gzaqzld855z/v3634d59+CpeE4PuOWsYCQC0RAtt0Z0izRq26+N9t1HxMQmFSUI/rzkDxxwJALVHAVt0Z0sx1vcX/ruP4mITCpIR6THcGij8WAGqJB/yX7gxpptTiC4BSizx3QNoZ0viV7gwUfywA1BKzUr8DULO6cxClhsI2cdIFT+qOQfHHAkAtEWddWgPErbpzEKWH+q7uBJQMLADUMs8TX9WdgSglPKnkDbpDUDKwAFDLrFMvvAPAg7pzECWdEPieOPXCZ3TnoGRgASBfeEp9VHcGooRrCCU/pTsEJQcLAPnC2nzx7QL4D905iBJL4Cti0wXbdceg5GABIN8I5f0JgBHdOYgS6AnZqH5adwhKFhYA8o3Y/I5dHnABAFd3FqIEqUjl/YF41bundQehZGEBIF9Zmy66DVAfxqLvUE9E+6kreBeIze/gnf/IdywA5Dtj08VXKyHeB2Dxt6kjoobyxPnmpnfwPhsUCBYACoR5yoXXKSXeDj62lGgxdknpnW2eeuG/6w5CycUCQIExN1/4XWnIE5XCPbqzEMWFgLhVGjhenPyOX+vOQskmdAeg5FNKCfeeb75NQH4OUKt050mSoT3PtfT7fUtX+JSEfPC4gvoLc9PF39EdhNKBMwAUOCGEMjdd/B05WztOQb1LALeDiwSJAKApIH6glDpPnrJ9HQd/ChNnAEgLdc+3VrhQZwiozUqJEwTQi33/KenOFiecAYiVCQDDChgSwH1KqF8bpvUrccL5vHcGacECQLHn3vPNb0HhAt05dGAB0EThZmPzRe/UHYOoFTwFQPGn8KzuCJQyQvA1R7HHAkDxp/hhTCFT3k7dEYhaxQJAsaeEyw9jCpUnWTop/lgAKPYMJflhTKEyXcnSSbHHAkAJ4D2jOwGlTL7B0kmxxwJAsSc2v6MMPoaYwrNXbLykojsEUatYACgRlMBW3RkoHYTia42SwdQdgMgPAmorIM7UnSNuDnUfASEEDMOEnckgly/CtKyQkkWXkiwAlAwsAJQU/FAOgFIKjtOE4zQxW5lBrlBCqa0dQqT3HmIKgq81SgSeAqBEkCwAoahWpjExPgql0vsoB4OvNUoIFgBKBsd6VHeEtGjWa5gpT+qOoY9Ve0x3BCI/sABQIohXnj8OYFB3jrSYrczAcZq6Y+iwWxx/aYrbDyUJ1wBQYgiorQqiX3eOqFDKhduchnKr8NwaPK8OeE24Xn3OnzdkBpAWpMxAGllIMwdpliCEMefPVysVlNo7gvwrRM6+xaZEycACQImhFLZC4BzdOcLmeQ249TE49XE4ziTcxgScRhnqAAP9QkkjC8Mswch0wjQ7YGS6YGa60WjUfNl+nCie/6cEYQGgxFCQWwSSvzjNaVZQmXwKM5NPY3riGTiNKSDAv7fn1uC5NTTr+99rScC021GbWoVi+yoUOlbBtAqBZYgKJfCw7gxEfmEBoMQwDHGP5yWzAFQre1EefQLT49tRqwwhyAF/fhScxiQmBh/ExOCDAARyhV4Uu9agrWcNcoUBzfmCYUhxl+4MRH5J78W8lDhKXSG9e1aPA2jXncUP9dlRTAw/hPLIY2jU47XuzM50oL13LTqWbEQm36M7jl8m5Snbu4W4wtMdhMgPLACUKN7d3/i5gojtOgClPEyNPIrxvfdidnq37ji+yJeWoWvgZLQvWQch4nvhkQB+Ijdd9DrdOYj8wlMAlCgK4i4gfgsBledicngLRnbdiUYtXt/2D2V2ejdmp3dj+NlfYsny09DRuxFCzn1lQZQpKE7/U6KwAFCiKIG7he7T4wtUqwxj9/bvoVoZ0h0lUI3aBHb/9laM7bkby9acF7t1Akoad+vOQOSn+M7HEc3BaDrx+ZBWCiO77sSTW65L/OC/v9rsCJ7aciNGnvs19C9mnDdlePV7dIcg8hPXAFDiuHd/80kAR+jOcTAKHvbs+A9MDD2oO4pW7b3rsfzot8ZhbcB2Y9NFa3SHIPJT5N91RIsQ6VkA5Tl4btt3Uz/4A8DU8CN49vFvw/Mif1thnv+nxGEBoCT6b90BDkxh1/YfoDy6TXeQyJge34FdT3wPkT4dILgAkJKHBYASRwrcpjvDgYw89ytMjfJusi9XHtv2/JqAaJLK+IXuDER+YwGgxBGnXLQVwC7dOV6uMvkUhp79pe4YkTX07O2oTD6tO8Yc1LNi0wXbdacg8hsLACVVpGYBlOdiz5P/CagIT3PrphR2//ZWKM/RneRlxM91JyAKAgsAJZJS0frQHt3936hXx3THiLxGbQJje6J1tZ0CIvVaIvILCwAlkiGNnyEiq8pcp4qRXb/SHSM2hp/9L7hOZB41rAxh3q47BFEQWAAokcQp5w8CeEx3DgCYGNoCz438ZW6R4XkNTA5v0R3jBQ8//1oiShwWAEouEY2p28mhB3RHiJ3xwfsRiQkcoX6mOwJRUPgsAEospbyfC8g/05lhdnoXarOjoe3PtDuRbVsNK7sUhlmEkK29xZXnwHVm0KzuRm16B5zGhE9JD64+O4rZ6T3Il5aFsr8D8SB4+R8lFgsAJZYxa9/h5Z0GAFtXhsrkM+HsSBgodp+CXNsa+HmHbyFNmHYHTLsD2fZjUStvx8zY3YDyfNvHgVQmn9FdAGpmxuHiDUosngKgxBJnnT8jlNL6Da4ytTP4nQgD7f2vQa7tGAT5eA8BiVzbMWjvfy0Qwr37K+UQjt1BCOBnYuMlFa0hiALEAkCJ5kn5PX17V5gtPxf4Xoo9p8DOhfdoXTs3gFL3KYHvpxrCsTsYT0Hja4coeCwAlGiGVN8DoOXOMq5Tg+c1At2HaXciVwr/IXWZtjUw7Y5A9+G6dbhOPdB9HGz3hokf6to5URhYACjRxEkXjSrgNzr23WwEP3ucbVsNHU/1FpDIFlcHvh+nMR34PuYisqMNhAAAIABJREFUgF+Kky4Kb/UmkQYsAJR4QtNUrtOcCXwfVnZp4Ps44L7zwe+7GcIxnIuC4vQ/JR4LACWehPddaLioXIVw8x/DKga+jwPu2wx+32Ecw7l2K4X8gY4dE4WJBYAST2x+xy4F3B/2fpVyw9hJ8Ps40K7D6FQhXG74O7uEuFuccqHeFYhEIWABoFQQGqZ0wygArqPvKjUvhH2HUqJeRsdrhUgHFgBKBWmIfwt9p17w316b1d2B7+NAGtU9ge9DRwGQigWA0oEFgFJBnHTRNgV1b5j7VCFMX9emd0BBxzS5h/r09uD3E/IpAAVxl9h88Y5Qd0qkCQsApYZQ8p/C3F8Y316dxgRq5eAH4perlbfBaUwGvh8VwizK/gQQ6muESCcWAEoN6RnfAhDanWXC+vY6M3Y3mtW9oewLAJrVPaiMhjOZEvIpgJpseN8Oc4dEOrEAUGqIV54/LhDe3d1CG7yUh8nBn6JWfjzQ0wEKHqrlxzC192ehnXYIuQD8uzj94nAed0gUAXwaIKWKB++fBOTbwthXqOevlYfp0btQLW9DtrgaVn4pDKsEIVp8HLBy4Dan0ajuQX16eyjT/i/df3iXOSqlOP1PqcICQKlizC77sZffOwigP+h9hX3+GgCcxiRmxu8BxkPfdTDCK1FDRnXpT8PaGVEU8BQApYo46ywHwDfD2FfYK9iTSKmQnuMk1L88/9ogSg0WAEodKeVN4eyJ40mrwppFkZ74l1B2RBQhLACUOuLkCx4N454AnAFoXRjHUEHdKzZf9HDgOyKKGBYASieBLwa9Cx1rAJImnBIl/yGEnRBFDgsApZIhZ24BEOgDXzgD0LoQLgPcbRjl7wS9E6IoYgGgVBInva8JqGuC3IeO+9gnTQjH8Iv7XgtE6cMCQKklG/g6gJmgts8ZgNYFfAynpV2/LsgdEEUZCwClljj94gkIXB/U9jkD0LqA11FcJ46/NNw7GxFFCAsApZqU+CsA5SC2zRkAPwR2DKckmn8d1MaJ4oAFgFJNnHTRKIT6QiAb51UALQtuFkX9rdj0rrGANk4UCywAlHoy414FYNDv7fIUQOsCOoZ7ZdYN/DJQoqhjAaDUExsvqUDgM35vlwWgdYGsAVDqCrHxkor/GyaKFxYAIgCyMvB1AA/5uU2uAWid78dQqAdkdemN/m6UKJ5YAIiw7yFBUqhL4eMN/FkAWufzLIojPfwxH/pDtA8LANHzxCkXPwTl4y2CPZ4CaJmvBUD8ndh88f0+bpAo1lgAiPYjzdKnATzpx7a4BqB1/q0BEE/LbNP3dR5EccYCQLQfcdKbZz2p/gQ+XICulPIhUdr5UgA8z8NlXPhH9FIsAEQvY5188c8BcWWr2+EagNYpX06jqM9Yp154hw8bIkoUFgCiOcidxqcF1C9a2ojiWrNWtVqiFPBLudPi1D/RHFgAiOYgzj/fFUpcjBZuEMQZgNa1eAyHjaa4SJx/PhdjEM2BBYDoAMTmi4Y8Jd8FYFEDSMAPskmFFhZSup5UF4tXXbjHzzxEScICQHQQ1uYLfgrgA4v5Xc4AtG7xx1B9eN9aDiI6EBYAokMwNl10LYCrFvp7vAywdYtbBCg+Z2y6+GrfwxAlDAsA0TzIUy78GKC+uZDfYQFo3SJmAG6RpzzxqSCyECUNCwDRPAghlJxtvFsBd873d7gGoHULKVFK4A7Z1XGJEFfwwBPNAwsA0TyJsy6tGVnndQK4fT4/r/y5iU26zfNmSgq402hU3yKOfkM94EREicECQLQAYuMlFWGU3iSA2w75w5wBaNl8ZgAU8F/GrPl68ap3T4cQiSgxWACIFkic9OZZYZTefPAbBSnOAPhg3xqAA88CKOCXxqz5RnHW+TPhpSJKBhYAokXYVwLa3tKo1eb8c57/98+BnqlQr1VhuObrOfgTLQ4LANEiiZPePDsxPoLKzO/OPPMeAP6Z6zTA7Mw0JidGIV55flVDJKJEYAEgatFMeRLlqYmXzFSzAPjnpfcCUChPTWK6PHmwMwNENA+m7gBESVCtzMB1HLR39kBKAcUHAfnmhTLleR6mJkbRqHOhP5EfOANA5JNGvYbxkUE0GnXOAPhIKQ/NRmPfseXgT+QbzgAQ+ch1HUyMjiCb0Z0kOaqVMqo1l1P+RD7jDACR7xRmZ8q6QyTGTHmCgz9RAFgAiAKgpAlp5HTHiD1p5ABp6Y5BlEg8BUAUAMPIoWvF21ArP47KxENcFLhAQhjIth+HfMcGSGnrjkOUSCwARAER0kSuYz0yxSNQmdyCenkH7w54SAKZwkoUuk+CYRZ1hyFKNBYAooBJs4BSzyuRLx2DytTDaMzsZBF4GQEJu3A48p0bYdqduuMQpQILAFFIjEwX2nrPhNs5jerUY6hNb+epASGRLa5CvmMjDKtddxqiVGEBIAqZYZVQ7NmEfOeG54vADnhuuu5oK80csqXVyLUdB2lkdcchSiUWACJNpJFDoetE5LuOh1MdRHV6OxqVnQm+iZCAnRtAtm017MLhELwIiUgrFgAizQQkrNxSWLml8JxZ1GaeRGN2J5q1UcT/AngBK9uDTH4l7NKRMHhpJFFksAAQRYg088h3rEe+Yz08t4pGdTeas8+iXtk151PxokhAws71wcofhkzhcEizoDsSEc2BBYCoBcIwoNxgBmZp5JAtHoVs8SgUvQaa1UG4zVE0a0NoVIcjc6pAiOcH/FwfDLsHZqYvlGv3hcFTCEStYAEgakFv31KMDg/CdZqB7kdIG3bhMACHIQdAKRdOfQTKmYDbnESzPoFmbRyeF2wOKS1Y2S6YmU6YVgeE2QUz0wMhjED3+3KmaaK7ty/UfRIlDQsAUYt6evsxMT6KRi28lfxCGLCy/QD6AWBfKYAH5cxCedNQbg3Km4XrVuE1Z+E6VXheFcoDPLcGQMF19j1ZzzAzAASkkYWQgJQ5GGYO0srDMHIQMg8hsxBG6fnpfBHa33MumVwWHZ1LtGYgSgIWACIfdHb1oDJdxsz0lLYMAhLCLAL4nzvoJe0u+sW2DhSKJd0xiBKBBYDIJ4VSG2w7g4nxESgV99X70SKEQGdXD6wM7xlA5BeuoiHykZXJoKd/KSyLD7Dxi2Xb+44pB38iX7EAEPlMComuJX0otrVD9/nyOBMAim3t6OrpgxT8qCLyG08BEAWkUGxDLpvHxPgIHCfl9/xfIGkY6OzuhWnyI4ooKHx3EQVImia6ewdQmSmjMl3m2oBDEBAotHWgUOSjgImCxgJAFIJCsQ2FfBsmJ8dQr83qjhNJViaLjq5uTvcThYQFgCgsEujo6obTaEN5agzNZrA37YkLy7bR1tHN6X6ikPEdRxQy07bQtaQfzUYd5YlxOG461weYpom2zi5YVkZ3FKJUYgEg0sSyM+juG4DTaKI8PYlmvaY7UggELNtCqa0Tls1LJYl0YgEg0sy0LXR1L4Hjepgpj6NRrUHF/jHALyWEgG1n0dbeAcmpfqJI4DuRKCJMQ6KjswfoBKqVacxWZuJ9+aAQMA0DuUIB+UKb7jRE9DIsAEQRlCuUkCvsu+f9bGUGtWoFzUYTiMHMgGGYyObzKBbaeasxoghjASCKuHyhiHxh33Xx1co0qrUqnGYTyvM0J9tHCAnTMpHN5fhNnyhGWACIYmT/mQF4QK1eQa1WRbPRgOd6CGOGQBoGLNtGJpNDLp8Dv+YTxRMLAFFcSSCbKyCbK7z4f3meh2azDqfRQNNx4DoOlOtCQWHfTQj/579fSkCIl/63lBKWacKwbJiWDcu2ISUHe6Kk4JNKiBbJvfsbVwDiL3TnSDfxOWPThZ/QnYIojlgAiBaBg3+UsAQQLQYLANECcfCPIpYAooViASBaAA7+UcYSQLQQLABE88TBPw5YAojmiwWAaB44+McJSwDRfLAAEB0CB/84YgkgOhQWAKKD4OAfZywBRAfDAkB0ABz8k4AlgOhAWACI5sDBP0lYAojmwgJA9DIc/JOIJYDo5VgAiPbDwT/JWAKI9scCQPQ8Dv5pwBJA9AIWACJw8E8XlgAigAWAiIN/KrEEELEAUKpx8E8zlgBKNxYASp3MV+7+UzU9+1FvbPywra+ti6NK4e272WigWplBo1mH6zgABEzLhG1nkC0UYZlWeGE0aDpNVCsVNOtVOI4DADBME7aVQa5QhGXboWXZMQ2s/bHZlMXSY8jkvt6sn/E1XCG80AIQacYCQMl31XO5jDH4WW+mfIk3Ptqj6vUX/+gfTpb4wDEy8AjK81AuT6A2O3vQn8tkcii0tcGywhsIw9BsNlApl1GvVw/6c9l8AW3tnRAi+I+mq7d5+Mi9/zPeC8v2ZKn0jMgXv9ko9XwOH1g7E3gIIo1YACiZbnhgqV1xrlLlqd/zxsc6VKMx54/93jKJW88OtgAopTAxNozmATLMxc5kUCy1w7IzASYLnuM0MTM9hXr14AP//izLRkd3L6QM9uPpjbe5+OluNeefCdNSKJV2m/n8j4wu84rZ9716b6BhiDRgAaDEKFyz9RVeY/qzznT5DG9stKhc55C/kzOAwfMN5M3g3gpTE2OoVQ/+zf9A4jojMN9v/AeSzRXQ3tnlc6r/UXWB/ltczDpzF4CXkBKirX1I5vI/E8Xs5xp/dtqjgQUjChELAMVa7pp7Xuk1nL9ypyZP9cbGMlDz+EB/mf8428BrlwXzVmg06pgYHW55O3EpAq0O/Pvr7OmFHdAMyE92e3jTbYs43S8EZLGtLAvFX6hs9m+aHzvtXv/TEYWDBYBix/7SAxeK+uwn3Inxtd70pIGFj/kv8cFjJP7+5GBOA0xNjqM2W/Fte1EtAn4O/C/I5vJo7+z2bXv7+/C9Hq7Z1vp6P5HL12WpdJewzGsanzznOz5EIwoNCwDFwv4r973Ziq+v26NLwGPnmX5u8kWjQ3vguq7v243KGoHFnOOfL8Mw0NO31PftAsCx33fw22l/tymyWV5RQLHCAkDRdJCV+0F4/DwTfl8OqJTC8N5d/m70ZXTNCATxjX8ufUuXw++PqaemgTXfP/T6kFbwigKKAxYAio55rtwPQiCXAypgaO9z/m7zAMIqAmEN/C/oW7rC922+/PK/oPGKAooqFgDSajEr94MQ1OWAQZ0CmJMAcvkiSm0dvl9Hr5TCdHkS1dkZtLrmYr4M00RP74Dv233TbR5+slvT7DyvKKAIYQGg0Pmxct9vGUNg99sMtPv8Bbo8MY5q1b9FgPNh2Rl0di/xrQQopTA5PoJGwKdhXi6XL6Ctw99LActNYNl3HNRC6mQHxSsKSDMWAAqF3yv3g/DPrzJw4Sp/3xLNZgPjI0O+bnM+coUi2to7fdlWeXJi3zf/kHUt6fP9lMY3nvLwR7+O5to8XlFAYWMBoMAEuXI/CG9ZIfCvZxq+b7c8OY6qj5cCzosAupb0t/xsgWazgfHRodCm/V+QyxfR1uFPgdnfebe7+I9dEWyfL8MrCigMkf9QphgJeeW+37IGsPt/mWjz+Xk8SnmYGBtFsxHu8cgXiii1OAtQnppAtRLut/99pzB6IIS/azIiNf2/ALyigILCAkCt0bhyPwg3n27g7Sv9f1vsW0Q3hersdGjfpk3TQndvf0vbGBsZhNNs+pToEASQzxdRDGARIwB88ykP74ro9P988YoC8hMLAC1YVFbuB+HMfoGfvcb/0wAvaDabqExPoV4L/jI6ISR6B5a1tI3hvbugQlikmcnmUCi1w7KCexzyuT918cuh6E//zxuvKKAWsQDQvERx5X5QHnizifUdwe6j2WygMl0OtAjEoQDsG/iDv3/B1kmF4291k/uy5RUFtAgsAHRAcVi5H4T3rRG4+pTgZgH2F2QRME0T3S1eRz82PAjH8f8UQFgD/wv+5C4PN+yI9/T/QvCKApoPFgB6ibit3A9C3hR45g8NdIZ4d90g7qnvxyLA6akJzPq4CFDHMwwmG8DKf3VQSc6ZqgXhFQV0IKn8gKf9xHzlflA+f6LEh48L5gmBB+PbjIAAunv6YbZ4Tr3pNDE+MtjywsWwv/Hv76rHPFx+P8c8gFcU0EuxAKRRwlbuB6E3C2z7fROlYB4SeEitFgE/vv2/oJVLAXUO/ABQcYA133MwVNOy+0jjFQXEApASSV65H5TPHi9x+brwZwH2t5giYGcy6Ojy91bAE2MjC7qPge6B/wV//YiHv3iI3/4PiVcUpBILQILlvvrgaV6t+tk0rNwPQocNPPH7Brps/W+TeRWBAK+jf/E+BpXpg/5cVAZ+YN+5/9XfczDBCa6F2f+KAsu6snn5q+/RHYmCof+TjXyV1pX7QfnEOoHPHB/OFQHz4TSbqM7OolmvwvFcQCkYpgk7k0UuX4RpBnvOwnGaqFZm0KjX4boOIARMacDKZpHLFVpec+CnTz7g4Qtb+e2/VbyiILlYABKAK/eDUzCBLW8xcXhBdxJaiKdngFf80MWswwbsJ15RkCwcLOKIK/dD9dplAv9xdnRmAejQ3nq7ix/F4KE/ccYrCuKPBSAuuHJfq2+cbuD8AJ4RQP771tMKl9wZsyf+xByvKIgnfqJF2P4r992xkSJcfqjp0pMFHnmziZ6s7iR0MOMNhfU/cDHMy/704RUFscECEDFcuR9dlxwpcMMreSogyi79tYubn+J7JjJ4RUGksQBEQPbv73uP16x+1JscXe1NT0uu3I+um04z8I4j+LaJom8/o/COX3GWLLIEIAulqigWfyPtwj/UP/7KW3VHSjt+kmnClfvxVCzl8JvXKBxb4I2UomT7rIVX3mZharLc8m2LKRy8okA/Djxh4cr9WBJ2BrKjE0ZXN2T3EohsFseaFdy+5G7k+XkVCRVl4KyRk7HNKUI16vAmJ+COj8EbH4Xy8eFKFBxeUaAHC0CQrnyyPZsd+ZpbLr/Jmxgtqia/NUadyOYgO7tgdHZBdnZBZHNz/txF+b34WsfWkNPRXN47uQ7fnu2f889UrQpvYhzuxDi8iXGoAB67TP4SpqlkW/vTIpu7udFo+xyuOGlWd6akYgEIgP3l+/63Kk99yBsb6eHletH2km/4XT0QubkH/Ln8f6Un8fHS0wGmo0P56+kjcOX0EfP+eVWvw5vaN0OgJifg+fioY/KfMC0l29qeFvnctY3aWV/gaQJ/sQD4JHv1lnO8ysQN7sjoYao2y+MaUfP9hj+vbQG4uuMxvDO/x7+ANG//PLsMH5w8tqVtcIYgPkQ264i2jjulkf2zxp+/+mHdeZKAA1WLMl9+8GPe1Oifu8NDHfBYTqOmlW/482EJD9/u3IJzs2O+bpcO7qf1HlwwthGOzx9hnCGIASEgOjp3yWLx083Lz7xJd5w4YwFYjFuUkd1z983O6OjbvMlxTU+Mp7n4+Q1/vgrCxfe7H8AmeyrwfRFwV70Dvz9+PCoq+HsycIYg2kR727jR1vmlxuWv/j+A4PUfC8QCsED21ff+rbd38CPe1AQH/ggI+hv+fBWEi/+/6yGckZnQsv+0uLPeifPHN2JG6Xn7cYYgmmShVDE6Oz9d/8SZV+nOEicsAPOU+ep9H3KHR670Rkb0jDAEIDoD/lzywsM3urbgnAxPBwThZ7VuvGNiI6pK6o7yIhaCaBFtHeNGR+cHGx8//Vu6s8QBC8AhZK65/3Xu2Oh3vZGhAm8wEj4dU/qtyAiFf+x8GG/MjuiOkig/rPbi0sl1aERo8J8LTxlEgYDo7Hpaljr+oPnx0x7SnSbKWAAO5BZlZHb/98+dnTvPVE5Td5rUiPI3/PkyoPDptifxkeIzuqMkwk2zy/HRyTW+L/gLA2cI9BGmqYyevu83HHk+rjiLN2GZQ/zeUSHIfunBP3KGd1/HBX7BS8KAfyDn5/fi6vbHkeWly4tSVwIfmTwWN1eX6o7iGxaC8MlSe9no6Hhn/fIz/l13lqhhAdjfLcNF+6nHHnF2P7eST+ELRtym9Fu1yZ7CzZ1b0GfwhlALMepaeOfEBvy60ak7SqB4yiAkQsDo7but6S57Pa5Yyzfj81gAnpe55v43u3v2/BtX9/sryd/w52uprOOrnVtxVmZcd5RYuK3ehfdPrMVeL6M7Sug4QxAsWSrNoLvj9c7HzrpTd5YoYAEAYP/Dvbe4O3/7v1ST5/pbxQF/bgLAH+V34a/bd6Ag+MjauVSVxJXTR+CLlcPhKX40ASwEQRCmpYzegc80PnXmX+jOolu632XX3tdjjZUfc/fuWsIV/ouTtin9Vq22Kvh6x1acYJV1R4mU+xrteN/kcdjhFHRHiTSeMvCJAIye3vub+YEz8P9urOiOo0tqC0D+6i0nNAafvdub5JT/QvAbfutMKLy38Bw+VXoa7TLds04TnoW/mj4CN1aWx3KVv26cIWiNLLWXjZ7+k+sf3bRddxYdUvmOy33lwYuazzx9szc7k8q//0JwwA9Op2zik8Wn8N7iLhgpm4LylMAttT78+dRqjHi27jiJwUKwcCKbbZq9y95U//hpP9WdJWypGwDtL9/zN+5TT31CNbkQdC4c8MO30ZrGZ9p24MyULBL8Rb0bny4fjUeaRd1REo+FYH6EaSk50P+h5uVnXa07S5hSVQByV919a/2ZHW/kU/v2YxiQnd0we5ZAdvfwHL5GG6xpfKz4DN6aG0rcG1MB+EltCT4/vQr3Ntt0x0ktVavCGxuBMzoKb2IMcLkg9UVSwhpY/vn6J8/8uO4oYUna58wBZa666yfNp3e8ltf371u4Z/QsgdHTC9nZBcho3141bdaZM/ho6Rm8JTsMK+Y3EWooie9X+3BVZSUea3KBX6R4HryJMbijI3BHR7igEACEgLVs2TX1T5z9Qd1RwpCKApD9u7t+3Hhmx++levAXErK7B+bAUhhL+gCRin/6WOsQTfx+bgjvKezCeiteU7fbmwV8ozqAm2eX8hx/LCh44+NwBnfDHRpEqmdJhYDVv/Rr9T8/5/26owQt8aNA5u9+fVvzmafOSuvgLwtFGEuXw+hfBmFbuuPQIm2yp3BRfi9enxlBv1HXHWdOg24GP6ovwTdnB3BPo113HFok1WzA3bsb7p7d6V0zIASs5cu/Vr/8rESXgEQXgMxVv/lF86knz0bKVlgDgGxrh7XqSMju3oT/K6fPsWYFr8uO4PWZUZxiT0EKPa9vTwlscUq4o96FH9d6cFejI4XvtARTgDc6jObTT8KbntKdJnxCwBpY8eX6p878kO4oQUns0JD50r1XN3ds+0DavvnL9g5YK4/gwJ8S3bKJE60pnGiXcaJdxgnmFHqMYO4tMOLZeKDRhgeabbiv2Y77G20Y9zirlAbe+BiaT26HV05ZERAC1vIVH69ffubndUcJQiKHiMxXHvhTZ/u2LyknPU+AlKUSrKOOgezq1h2FNFth1HC4UcXhZhWHPf+/DzNqKEgXHaIJWygUpIui2Pf+mFEmKp6BhhKYVBZmlIHnnCx2ujk862ax08lhp5vDc25W89+MdPPGR9Hcvi1VpwaEYSljxbI/aHzs1d/XncVviSsA2a88fG7zyW0/U/Wa7ijhMAyYh62CteoIQHA1PxEFTCk4u56F89QOpOVLlshkXDmw/NTmx067V3cWPyWqAGSv27bKefLx33oz08kfCQVg9C+FddQaCDt9T00jIr1Uo47m9ifgDu9JxTIrWSjOOiv7VuH9pw3rzuKXRN0H39377MNpGPxFNgvruA0wOrt0RyGilBJ2Bva6DfAmlqHx2KOJv4+AV5nJW3szdzWBI3Rn8UtiBkv7C7+5yx0eTPy9RY3ePmQ2vZKDPxFFguzsRmbTaTD6l+qOEjh3fGyV9de3X687h18ScQogc839H2k+8fhVSb55hTBMWEevgbFshe4oRERzcvfuQfOJrVBJvsWwlDCXrnh74xNn3KI7SqtiXwBKX39o9eyO325T1dnY/10ORBZLsDYcD5nL645CRHRQ3mwFzUcehDeT3CsFRC7fyK487PDKB04Z1J2lFbE/BVAbGrkv0YN/Ty/skzZz8CeiWJD5AuwTN0N29+iOEhhVnbUbI6N36M7RqlgXgOzf33OtO7y3pDtHUIxlK5DZcDyEYeiOQkQ0b8I0kdl4Iszlh+uOEhh3bHSN/Te/vFx3jlbE95vzTQ+uNB578qlEfvsXAtbRx8Bckdw3DxGlg7v7OTSeeAxJvCuryOSc7IoVyysf3jykO8tixHYGwNozfm8yB38Je/0rOPgTUSIYy1bAXrsxkU8gVfWq2ZiculV3jsWKZQGwv3zv59y9e5J3gklI2Os37ntcLxFRQhh9/YktAe7Y8EnW5+64VHeOxYjfv8YtyjAf/EHDmy7HsrwckJDIrNsI2cvBn4iSyR0eROPRhwGVrEu2ZaFUcXLnteEKEau/WOwG0czuu3+cxMHfXs/Bn4iSzejth712Q+JmArzKdCFj3/F3unMsVLz+FT6/pdcYemJI1ZL1oB/7mONgLDtMdwwiolC4e3ah8fijumP4SmSzjntYsRsfekNZd5b5itU3aRvlXyZt8DcPX8XBn4hSxVi6HOZhK3XH8JWq1Uy7nPlH3TkWIjYFIP/Fh0909+w6RncOP8klvbCOWq07BhFR6Kyj1kD29OqO4St3bPi83Gfvic392mNTAJzaxHdVs6k7hm9kqQ2ZtRsRt7MwRES+EGLfwue2dt1JfKOaTeGqydg8LCgeBeDa+wbcwb0rdcfwi7As2BuOB3iHPyJKM8OAvf54CNPSncQ37sT4ubjyZ7FoNbEoANly7d9Vs6E7hm+sNcdBZHO6YxARaSeyWVjHrtMdwzeq0ZCWa31Jd475iH4B+NKOTHN48ETdMfxiLF0Bo29AdwwiosgwevtgLF2mO4ZvvMmRC3DF7abuHIcS+QKQc8e+nZRb/sp8EfbqRK1jJCLyhbX6uMQ89VRVq7adUf9Hd45DiXwBaI6NvUF3Bl8ICWvdBp73JyKagzAMWOuSc7tgd3r6j3VnOJRIF4DMVx54izc1nojVIeaKwyFLbbpjEBFFlmw9OmwsAAAJIklEQVRrh7k8GfdFUeVyt/X5X23SneNgIl0A1PTM3yfhEZLCzsBcdaTuGEREkWcecTSEndEdwwcKola/QneKg4n0IgVvdGSV7gx+sI5eA2FG+lAT+cKrzMDd9Ry8iTF4tSoAQOZykJ3dMJatgCwUNSekqBOmCeuo1Wg89ojuKC1zy5NnQykBISL5TTayJ6QzX7z3Kndoz6m6c7TK6OiEdfSxvN8PJZpyHDS3P47mtq3wypNQzQagFKAUVKMBrzwFd/dzQKMBo7s7Med5KRiy2AY1MQ71fImMLadpmI9OPef9/B8f1B1lLpE9BeBOl9+lO0PLBGCuOY6DPyWaNzaK+l137hvgD3bKTik4u55F/aH7Evc4WPKZAMzVxyTjs7Na/VPdEQ4ksgVATU506s7QKmNJH2SxpDsGUSCU46Dx2KOoP3QfVH3+D+nyxsfR3L4twGSUBLLUlohnBXgz5bW6MxxIJAtA5pr7/kzVYn7tvwDMVUfpTkEUCG9qEvV7fg13765F/b6z+zl4MzM+p6KksVYdFftZAFWrmZkv3Pl63TnmEskCoGaq/4/uDK2S/PZPSaQ8NH+7HfX77oaqtnB+VqlFlwdKD1lqg+yO/yyAqtXerzvDXCJZALzpySN0Z2iVdXjs/wpEL6EaDTQeuBfOzqcAtL6o2RsfbT0UJZ51RPxnAdzZyqt0Z5hL5ApA4dqH13tTk7G+Zk529yTqEZdE3nQZ9Xt/A3dywrdtqur81w1QeslSG2Rnt+4YLVHT5c7cVb+J3MMOIlcA3Grt43G/+Y+1dIXuCES+8cZGUL//bqgaB2zSw4z7Z6pScBvNd+qO8XKRKwBevRrJqZL5ErYNuWSJ7hhEvnD37kF9y4OA6/q+bZHL+r5NSiajtxfCtnXHaIlqNs7RneHlolcAKpWlujO0whhYDojIHVaiBXN3P4fG4w8Hds2+7OoJZLuUQELCGIj10ACvVluvO8PLRWukukUZamYqvjVPAMay5bpTELXM3bsHjW2P+bHWb25CwFjK9wrNn7F0RbwXA1YrkZsajlQByI49eJFqNHXHWDSjoysxz7Om9HJHhtB4/BEEN/oD5vLD+FwAWhCZL8Boj+/94VSjIc2r7jhdd479RaoAoN64QHeEViThrlWUbl55Eo1Htxz8lr4tkl3dsI5eE9j2Kbnkknh/xhp19VbdGfYXqQLgNRurdWdoBQsAxZmq1dDY8iDgBXeffnP5CmRecSLXydCiyJ4+3RFaohw3UrcFjtT19qpWj+0IKgtFyDyn/ymmPA+Nhx+EatQD2bywM7CPWRv7b3Ckl8znIfMFeLMV3VEWRTnNlboz7C9aBaBei+0Iyg82irPmjm3wpqcC2bbR2w/r2OMgzPiu76XokD298J59WneMRfGajUhNYUSrANSqkcqzEJz+p7hyh4fg7HrW9+0Ky4Z93HrInsgtfqYYk0t6gZgWANRqbboj7C86J+K++uteVQtm+jFwUsJoi9S/K9G8qEYTzSe2+r5d2daBzCmncvAn3xnt7YCMztC1EKrRMPA3v4rMpQyROYpZr/SGIC87CpJRaueiJoql5vbHoBoN/zYoAHP54cicdApENuffdoleICRkKa5PWlXIWNZJulO8IDKjlnIaJ+vOsFiivUN3BKIF80ZH4A7t9W+DQsA+bgOsNceyEFOgZCm+n7meVz9Od4YXROZdKjz3KN0ZFotP/qPYUR6aO7b5tz0hYB+3HkZ/vG/XSvEgO+L7mSsddaTuDC+IzKI7z23E9r6gsj2+L0ZKJ+e5nf5dSiUk/m9797IjNxGFcfyU7e5MpjNESdQk5KJBjBKQAoMQ+/CKbFjAEyFWSDwAGimAEqJckDJKl+vCKigrlHG5c87p/v/2Lp1Ft/1VuXxq+dXX0q9NbXDGDguOVwBqSZ9q1/CWmRUAGdN17RKmCMOCd51wpaYk6feZdlEHkeXDUx7++KC6y4cSBjPz1wupefxEu4a3zASAMkaXuzrCZR7+8CU/PpOa5tn4Nxx/Jv3NW7OMBby34PfeW3IxcwymmQAgMV7SLmEKZv/wpKYk6exslrG6G2tZnNyfZSzgwrzee8dk5v2FmQBQN7HXrmGKwOl/cCT/9ccss//u8FCWX56K7/NZ4VnnNQCkaOahYScAxDcu7yRel6Gwh6pInqnj3+KLhxKGxSxjAVO4XX2No5k/jokAcPXH304kZ+0yJukODrRLAN5Lef5slp3//a3b0l27MUNFwHSd09XXmlNYff+ziV2zJgLApuTvtGuYbOFy6wL2UPrzcfMYYRhkcf/zGaoB2tSlmYn0haUkp9o1iBgJADWmb7VrmGxwuXUBe6bmJPnZ0+ZxhpMHEpaEXugLvc/PAEXsdAM0EQBCSW63EoeOAAD7ytMnIqU0jRGWl2S47bZfF3aN43tvl4uJzrcmAkDJ4x3tGibr/f4IsT/S30+axxjuHbs9hQ27Jzhefa05H2vXIGIkANQ4+t1RRACAdbVIffG8aYjQD9LfvTdTQcAMHK8AWOkGaCMApPGKdg1TBWZEMC6/fCk1paYx+jt3+ewPpgTHk6+Sylq7BhEjAcBrF0AREQku2xdgj5TG2b+I0O4X9ni+9+bRxAlyJgKA1y6AgAfl1Yum68PBgevz1wFzxtFEEwMbAcBpF0DAvir1n1dNI/Trm3T8BWZUYzTxPk09ABz99OsDr10AAevK+Xn7+//1xzNVA0BERHIOqx9+UX+vph4AxlQfadcA7KzXja1/Qyfd1Wvz1ALgP+n1qN4NUD0AhJq/0a4B2FWtvf+71Ypv/4EtKDWrdwNU/2fX6LcLIGBdOT9vuj4cHc1UCYB3dSWfqNegXUBJkd6iwJbUzZum67srH81UCYB3WegGqB4AasrXtWsAdtYYmy7vVm57dAGm1ZTVuwHqB4AxcocBtqTGtgAgCxNfKwE7p+Sk3g1QPQC47gIIWJfbPgEkAABbYqAboHoAqJsNXQCBLamtRwA7PnMdMG1M6t0A9QNA3NBjDNiW1gAwEACAbahxw/IaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/419Qsmb3gGaZ/wAAAABJRU5ErkJggg==";

}