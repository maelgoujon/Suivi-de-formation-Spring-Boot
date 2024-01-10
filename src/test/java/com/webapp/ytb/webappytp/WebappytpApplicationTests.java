package com.webapp.ytb.webappytp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Demande;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;
import com.webapp.ytb.webappytp.modele.FicheIntervention;

@SpringBootTest
class WebappytpApplicationTests {

	// ---------------------------//
	// ------Tests UserRole-------//
	// ---------------------------//

	// Valeurs possibles : ADMIN SUPERADMIN USER
	@Test
    public void UserRoleValues() {
        UserRole[] roles = UserRole.values();
        assertEquals(3, roles.length, "UserRole doit avoir exactement trois valeurs");
        assertTrue(Arrays.asList(roles).contains(UserRole.ADMIN), "UserRole doit contenir ADMIN");
        assertTrue(Arrays.asList(roles).contains(UserRole.SUPERADMIN), "UserRole doit contenir SUPERADMIN");
        assertTrue(Arrays.asList(roles).contains(UserRole.USER), "UserRole doit contenir USER");
    }

	// ---------------------------//
	// ----Tests Utilisateurs-----//
	// ---------------------------//

	// Valeurs possibles : ADMIN SUPERADMIN USER
	@Test
	public void creerUtilisateurRole() {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setRole(UserRole.ADMIN);
		assertEquals("ADMIN", utilisateur.getRole().toString(), "Le rôle doit être ADMIN");
		utilisateur.setRole(UserRole.SUPERADMIN);
		assertEquals("SUPERADMIN", utilisateur.getRole().toString(), "Le rôle doit être SUPERADMIN");
		utilisateur.setRole(UserRole.USER);
		assertEquals("USER", utilisateur.getRole().toString(), "Le rôle doit être USER");
	}

	// Assignation des roles utilisateurs
	@Test
	public void creerUtilisateurAdmin() {
		Utilisateur admin = new Utilisateur();
		admin.setRole(UserRole.ADMIN);
		assertEquals("ADMIN", admin.getRole().toString(), "Le rôle doit être ADMIN");
	}
	@Test
	public void creerUtilisateurUser() {
		Utilisateur user = new Utilisateur();
		user.setRole(UserRole.USER);
		assertEquals("USER", user.getRole().toString(), "Le rôle doit être USER");
	}
	@Test
	public void creerUtilisateurSuperAdmin() {
		Utilisateur superAdmin = new Utilisateur();
		superAdmin.setRole(UserRole.SUPERADMIN);
		assertEquals("SUPERADMIN", superAdmin.getRole().toString(), "Le rôle doit être SUPERADMIN");
	}

	// Utilisateur a bien les bonnes valeurs
	@Test
	public void creerUtilisateur() {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNom("nom");
		utilisateur.setPrenom("prenom");
		utilisateur.setLogin("login");
		utilisateur.setMdp("mdp");
		utilisateur.setDescription("description");
		utilisateur.setPhotoBase64("photoBase64");
		utilisateur.setNiveau(1);
		utilisateur.setRole(UserRole.USER);
		assertEquals("nom", utilisateur.getNom(), "Le nom doit être 'nom'");
		assertEquals("prenom", utilisateur.getPrenom(), "Le prenom doit être 'prenom'");
		assertEquals("login", utilisateur.getLogin(), "Le login doit être 'login'");
		assertEquals("mdp", utilisateur.getMdp(), "Le mdp doit être 'mdp'");
		assertEquals("description", utilisateur.getDescription(), "La description doit être 'description'");
		assertEquals("photoBase64", utilisateur.getPhotoBase64(), "La photoBase64 doit être 'photoBase64'");
		assertEquals(1, utilisateur.getNiveau(), "Le niveau doit être 1");
		assertEquals("USER", utilisateur.getRole().toString(), "Le rôle doit être USER");
	}

	// ---------------------------//
	// -Tests Fiches Intervention-//
	// ---------------------------//

	//Fiche est vide à l'initialisation
	@Test
	public void creerFicheInterventionVide() {
		FicheIntervention ficheVide = new FicheIntervention();
		assertEquals(null, ficheVide.getDateCreation());
		assertEquals(null, ficheVide.getDemande());
		assertEquals(false, ficheVide.isEtatFicheFinie()); //l'etat finie de la fiche est false par défaut
		assertEquals(null, ficheVide.getIntervention());
		assertEquals(null, ficheVide.getMaintenance());
		assertEquals(null, ficheVide.getTravauxNonRealises());
		assertEquals(null, ficheVide.getTravauxRealises());
		assertEquals(null, ficheVide.getUtilisateur());
	}

	// ---------------------------//
	// ------Tests Materiaux------//
	// ---------------------------//

	//Création d'un matériaux
	@Test
	public void creerMateriaux() {
		Materiaux materiaux = new Materiaux();
		materiaux.setNomImage("nomImage");
		materiaux.setImageUrl("imageUrl");
		materiaux.setType("type");
		assertEquals("nomImage", materiaux.getNomImage(), "Le nom de l'image doit être 'nomImage'");
		assertEquals("imageUrl", materiaux.getImageUrl(), "L'url de l'image doit être 'imageUrl'");
		assertEquals("type", materiaux.getTypeIntervention().toString(), "Le type d'intervention doit être 'type'");
	}

	// ---------------------------//
	// -------Tests Demande-------//
	// ---------------------------//

	//Création d'une demande
	@Test
	public void creerDemande() {
		Demande demande = new Demande();
		demande.setNomDemandeur("nomDemandeur");
		demande.setDateDemande(LocalDate.of(2020, 12, 31));
		demande.setLocalisation("localisation");
		demande.setDescription("description");
		demande.setDegreUrgence(1);
		assertEquals("nomDemandeur", demande.getNomDemandeur(), "Le nom du demandeur doit être 'nomDemandeur'");
		assertEquals(LocalDate.of(2020, 12, 31), demande.getDateDemande(), "La date de la demande doit être 'dateDemande'");
		assertEquals("localisation", demande.getLocalisation(), "La localisation doit être 'localisation'");
		assertEquals("description", demande.getDescription(), "La description doit être 'description'");
		assertEquals(1, demande.getDegreUrgence(), "Le degré d'urgence doit être 1");
	}

	// ---------------------------//
	// ----Tests Intervention-----//
	// ---------------------------//

	//Création d'une intervention
	@Test
	public void creerIntervention() {
		Intervention intervention = new Intervention();
		intervention.setTypeIntervention("typeIntervention");
		intervention.setDateIntervention(LocalDate.of(2020, 12, 31));
		intervention.setNiveauDateIntervention(1);
		intervention.setDureeIntervention(1);
		intervention.setNiveauDureeIntervention(1);
		intervention.setNiveauIntervention(1);
		assertEquals("typeIntervention", intervention.getTypeIntervention(), "Le type d'intervention doit être 'typeIntervention'");
		assertEquals(LocalDate.of(2020, 12, 31), intervention.getDateIntervention(), "La date de l'intervention doit être 'dateIntervention'");
		assertEquals(1, intervention.getNiveauDateIntervention(), "Le niveau de la date de l'intervention doit être 1");
		assertEquals(1, intervention.getDureeIntervention(), "La durée de l'intervention doit être 1");
		assertEquals(1, intervention.getNiveauDureeIntervention(), "Le niveau de la durée de l'intervention doit être 1");
		assertEquals(1, intervention.getNiveauIntervention(), "Le niveau de l'intervention doit être 1");
	}

	//Valeurs possibles : AMENAGEMENT, ELECTRICITE, FINITION, PLOMBERIE, SERRURERIE;
	@Test
	public void InterventionValues(){
		Intervention.TypeIntervention[] typeIntervention = Intervention.TypeIntervention.values();
		assertEquals(5, typeIntervention.length, "TypeIntervention doit avoir exactement cinq valeurs");
		assertTrue(Arrays.asList(typeIntervention).contains(Intervention.TypeIntervention.AMENAGEMENT), "TypeIntervention doit contenir AMENAGEMENT");
		assertTrue(Arrays.asList(typeIntervention).contains(Intervention.TypeIntervention.ELECTRICITE), "TypeIntervention doit contenir ELECTRICITE");
		assertTrue(Arrays.asList(typeIntervention).contains(Intervention.TypeIntervention.FINITION), "TypeIntervention doit contenir FINITION");
		assertTrue(Arrays.asList(typeIntervention).contains(Intervention.TypeIntervention.PLOMBERIE), "TypeIntervention doit contenir PLOMBERIE");
		assertTrue(Arrays.asList(typeIntervention).contains(Intervention.TypeIntervention.SERRURERIE), "TypeIntervention doit contenir SERRURERIE");
	}




}
