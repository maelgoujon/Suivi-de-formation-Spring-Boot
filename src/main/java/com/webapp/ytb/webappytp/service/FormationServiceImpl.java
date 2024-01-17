package com.webapp.ytb.webappytp.service;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Demande;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervenant;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Maintenance;
import com.webapp.ytb.webappytp.repository.FicheRepository;
import com.webapp.ytb.webappytp.repository.FormationRepository;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FormationServiceImpl implements FormationService {

    private final FormationRepository formationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final FicheRepository ficheRepository;

    @Autowired
    public FormationServiceImpl(FormationRepository formationRepository, UtilisateurRepository utilisateurRepository,
            FicheRepository ficheRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.formationRepository = formationRepository;
        this.ficheRepository = ficheRepository;
    }

    @Override
    public Formation creer(Formation formation) {
        return formationRepository.save(formation);
    }

    @Override
    public List<Formation> lire() {
        return formationRepository.findAll();
    }

    @Override
    public Formation modifier(Long id, Formation formation) {
        return formationRepository.findById(id)
                .map(f -> {
                    f.setNom(formation.getNom());
                    f.setNiveau_qualif(formation.getNiveau_qualif());
                    f.setDescription(formation.getDescription());
                    // Autres attributs à modifier
                    return formationRepository.save(f);
                }).orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'ID : " + id));
    }

    @Override
    public String supprimer(Long id) {
        formationRepository.deleteById(id);
        return "Formation supprimée";
    }

    @Override
    public Formation findById(Long id) {
        return formationRepository.findById(id).orElse(null);
    }

    @Override
    public void ajouterUtilisateurALaFormation(Long utilisateurId, Long formationId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        // Ajouter la formation à l'utilisateur
        List<Formation> formations = utilisateur.getFormations();
        formations.add(formation);
        utilisateur.setFormations(formations);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void retirerUtilisateurDeLaFormation(Long utilisateurId, Long formationId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));


        List<Formation> formations = utilisateur.getFormations();
        Optional<Formation> formation = formationRepository.findById(formationId);
        formations.remove(formation);
        utilisateur.setFormations(formations);


        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void generatedExcel(Long formationId, HttpServletResponse response) throws Exception {

        List<Utilisateur> utilisateurs = formationRepository.findById(formationId).get().getUtilisateurs();
        List<FicheIntervention> ficheInterventions = new ArrayList<>();

        for (Utilisateur utilisateur : utilisateurs) {
            List<FicheIntervention> utilisateurFiches = ficheRepository.findByUtilisateurId(utilisateur.getId());
            ficheInterventions.addAll(utilisateurFiches);
        }

        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found with ID: " + formationId));

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Formation Archive");

        int dataRowIndex = 0;
        HSSFRow rowFormation = sheet.createRow(dataRowIndex);
        rowFormation.createCell(0).setCellValue("Nom de la Formation");
        rowFormation.createCell(1).setCellValue("Niveau de Qualification de la Formation");
        rowFormation.createCell(2).setCellValue("Description de la Formation");

        dataRowIndex++;

        HSSFRow dataRowFormation = sheet.createRow(dataRowIndex);
        dataRowFormation.createCell(0).setCellValue(formation.getNom());
        dataRowFormation.createCell(1).setCellValue(formation.getNiveau_qualif());
        dataRowFormation.createCell(2).setCellValue(formation.getDescription());
        dataRowIndex = dataRowIndex + 2;

        HSSFRow rowUser = sheet.createRow(dataRowIndex);
        rowUser.createCell(0).setCellValue("Nom de l'apprenti");
        rowUser.createCell(1).setCellValue("Prenom de l'apprenti");
        rowUser.createCell(2).setCellValue("Description de l'apprenti");
        rowUser.createCell(3).setCellValue("Role de l'apprentis");

        dataRowIndex = dataRowIndex + 2 ;

        for (Utilisateur utilisateur : utilisateurs) {
            dataRowIndex++;
            HSSFRow dataRowUser = sheet.createRow(dataRowIndex);
            dataRowUser.createCell(0).setCellValue(utilisateur.getNom());
            dataRowUser.createCell(1).setCellValue(utilisateur.getPrenom());
            dataRowUser.createCell(2).setCellValue(utilisateur.getDescription());
            dataRowUser.createCell(3).setCellValue(utilisateur.getRole().toString());
            dataRowIndex++;
            // Headers for Fiche Intervention Information
            HSSFRow rowFiche = sheet.createRow(dataRowIndex++);
            rowFiche.createCell(0).setCellValue("La Date de Création");
            rowFiche.createCell(1).setCellValue("La Date de Demande");
            rowFiche.createCell(2).setCellValue("La Date d'Intervention");
            rowFiche.createCell(3).setCellValue("Le Degré d'Urgence");
            rowFiche.createCell(4).setCellValue("La Durée d'Intervention");
            rowFiche.createCell(5).setCellValue("L'État de la Fiche Finie");
            rowFiche.createCell(6).setCellValue("Le Type de Maintenance");
            rowFiche.createCell(7).setCellValue("Le Niveau de la Date Demande");
            rowFiche.createCell(8).setCellValue("Le Niveau de la Date d'Intervention");
            rowFiche.createCell(9).setCellValue("Le Niveau du Degré d'Urgence");
            rowFiche.createCell(10).setCellValue("Le Niveau de la Description");
            rowFiche.createCell(11).setCellValue("Le Niveau de la Durée d'Intervention");
            rowFiche.createCell(12).setCellValue("Le Niveau de l'Intervenant");
            rowFiche.createCell(13).setCellValue("Le Niveau de la Localisation");
            rowFiche.createCell(14).setCellValue("Le Niveau du Type de Maintenance");
            rowFiche.createCell(15).setCellValue("Le Niveau des Matériaux Utilisés");
            rowFiche.createCell(16).setCellValue("Le Niveau de la Nature d'Intervention");
            rowFiche.createCell(17).setCellValue("Le Niveau du Nom");
            rowFiche.createCell(18).setCellValue("Le Niveau du Nom du Demandeur");
            rowFiche.createCell(19).setCellValue("Le Niveau du Prénom");
            rowFiche.createCell(20).setCellValue("Le Niveau du Titre de Demande");
            rowFiche.createCell(21).setCellValue("Le Niveau du Titre de l'Intervenant");
            rowFiche.createCell(22).setCellValue("Le Niveau du Titre de l'Intervention");
            rowFiche.createCell(23).setCellValue("Le Niveau des Travaux Non Réalisés");
            rowFiche.createCell(24).setCellValue("Le Niveau des Travaux Réalisés");
            rowFiche.createCell(25).setCellValue("Le Niveau du Type d'Intervention");
            rowFiche.createCell(26).setCellValue("La Nouvelle Intervention");
            rowFiche.createCell(27).setCellValue("L'ID");
            rowFiche.createCell(28).setCellValue("L'ID de l'Utilisateur");
            rowFiche.createCell(29).setCellValue("Le Nom du Demandeur");
            rowFiche.createCell(30).setCellValue("Les Travaux Non Réalisés");
            rowFiche.createCell(31).setCellValue("Les Travaux Réalisés");
            rowFiche.createCell(32).setCellValue("La Couleur de la Date de Demande");
            rowFiche.createCell(33).setCellValue("La Couleur de la Date d'Intervention");
            rowFiche.createCell(34).setCellValue("La Couleur du Degré d'Urgence");
            rowFiche.createCell(35).setCellValue("La Couleur de la Description");
            rowFiche.createCell(36).setCellValue("La Couleur de la Durée d'Intervention");
            rowFiche.createCell(37).setCellValue("La Couleur de la Localisation");
            rowFiche.createCell(38).setCellValue("La Couleur du Type de Maintenance");
            rowFiche.createCell(39).setCellValue("La Couleur des Matériaux Utilisés");
            rowFiche.createCell(40).setCellValue("La Couleur du Nom");
            rowFiche.createCell(41).setCellValue("La Couleur du Nom du Demandeur");
            rowFiche.createCell(42).setCellValue("La Couleur du Prénom");
            rowFiche.createCell(43).setCellValue("La Couleur du Titre de Demande");
            rowFiche.createCell(44).setCellValue("La Couleur du Titre de l'Intervenant");
            rowFiche.createCell(45).setCellValue("La Couleur du Titre de l'Intervention");
            rowFiche.createCell(46).setCellValue("La Couleur des Travaux Non Réalisés");
            rowFiche.createCell(47).setCellValue("La Couleur des Travaux Réalisés");
            rowFiche.createCell(48).setCellValue("La Couleur du Type d'Intervention");
            rowFiche.createCell(49).setCellValue("La Description");
            rowFiche.createCell(50).setCellValue("La Localisation");
            rowFiche.createCell(51).setCellValue("Le Nom");
            rowFiche.createCell(52).setCellValue("Le Prénom");
            rowFiche.createCell(53).setCellValue("Le Type d'Intervention");
            rowFiche.createCell(54).setCellValue("Les Matériaux");

            // Fiche Intervention Data
            for (FicheIntervention fiche : ficheInterventions) {
                HSSFRow dataRowFiche = sheet.createRow(dataRowIndex++);
                dataRowFiche.createCell(0).setCellValue(fiche.getDateCreation().toString());
                dataRowFiche.createCell(1).setCellValue(fiche.getDateDemande().toString());
                dataRowFiche.createCell(2).setCellValue(fiche.getDateIntervention().toString());
                dataRowFiche.createCell(3).setCellValue(fiche.getDegreUrgence());
                dataRowFiche.createCell(4).setCellValue(fiche.getDureeIntervention());
                dataRowFiche.createCell(5).setCellValue(fiche.isEtatFicheFinie());
                dataRowFiche.createCell(6).setCellValue(fiche.getMaintenance().getNiveauMaintenanceType());
                dataRowFiche.createCell(7).setCellValue(fiche.getDemande().getNiveauDateDemande());
                dataRowFiche.createCell(8).setCellValue(fiche.getNiveauDateIntervention());
                dataRowFiche.createCell(9).setCellValue(fiche.getDemande().getNiveauDegreUrgence());
                dataRowFiche.createCell(10).setCellValue(fiche.getDemande().getNiveauDescription());
                dataRowFiche.createCell(11).setCellValue(fiche.getNiveauDureeIntervention());
                dataRowFiche.createCell(12).setCellValue(fiche.getNiveauIntervenant());
                dataRowFiche.createCell(13).setCellValue(fiche.getDemande().getNiveauLocalisation());
                dataRowFiche.createCell(14).setCellValue(fiche.getNiveauMaintenanceType());
                dataRowFiche.createCell(15).setCellValue(fiche.getNiveauMateriauxUtilises());
                dataRowFiche.createCell(16).setCellValue(fiche.getNiveauNatureIntervention());
                dataRowFiche.createCell(17).setCellValue(fiche.getIntervenant().getNiveauTitreIntervenantNom());
                dataRowFiche.createCell(18).setCellValue(fiche.getDemande().getNiveauNomDemandeur());
                dataRowFiche.createCell(19).setCellValue(fiche.getIntervenant().getNiveauTitreIntervenantPrenom());
                dataRowFiche.createCell(20).setCellValue(fiche.getDemande().getNiveauTitreDemande());
                dataRowFiche.createCell(21).setCellValue(fiche.getIntervenant().getNiveauTitreIntervenant());
                dataRowFiche.createCell(22).setCellValue(fiche.getIntervention().getNiveauTitreIntervention());
                dataRowFiche.createCell(23).setCellValue(fiche.getNiveauTravauxNonRealises());
                dataRowFiche.createCell(24).setCellValue(fiche.getNiveauTravauxRealises());
                dataRowFiche.createCell(25).setCellValue(fiche.getIntervention().getNiveauTypeIntervention());
                dataRowFiche.createCell(26).setCellValue(fiche.isNouvelleIntervention());
                dataRowFiche.createCell(27).setCellValue(fiche.getId());
                dataRowFiche.createCell(28).setCellValue(utilisateur.getId());
                dataRowFiche.createCell(29).setCellValue(fiche.getNomDemandeur());
                dataRowFiche.createCell(30).setCellValue(fiche.getTravauxNonRealises());
                dataRowFiche.createCell(31).setCellValue(fiche.getTravauxRealises());
                dataRowFiche.createCell(32).setCellValue(fiche.getDemande().getCouleurDateDemande());
                dataRowFiche.createCell(33).setCellValue(fiche.getIntervention().getCouleurDateIntervention());
                dataRowFiche.createCell(34).setCellValue(fiche.getDemande().getCouleurDegreUrgence());
                dataRowFiche.createCell(35).setCellValue(fiche.getDemande().getCouleurDescription());
                dataRowFiche.createCell(36).setCellValue(fiche.getIntervention().getCouleurDureeIntervention());
                dataRowFiche.createCell(37).setCellValue(fiche.getDemande().getCouleurLocalisation());
                dataRowFiche.createCell(38).setCellValue(fiche.getMaintenance().getCouleurMaintenanceType());
                dataRowFiche.createCell(39).setCellValue(fiche.getCouleurMateriauxUtilises());
                dataRowFiche.createCell(40).setCellValue(fiche.getIntervenant().getCouleurNom());
                dataRowFiche.createCell(41).setCellValue(fiche.getDemande().getCouleurNomDemandeur());
                dataRowFiche.createCell(42).setCellValue(fiche.getIntervenant().getCouleurPrenom());
                dataRowFiche.createCell(43).setCellValue(fiche.getDemande().getCouleurTitreDemande());
                dataRowFiche.createCell(44).setCellValue(fiche.getIntervenant().getCouleurTitreIntervenant());
                dataRowFiche.createCell(45).setCellValue(fiche.getIntervention().getCouleurTitreIntervention());
                dataRowFiche.createCell(46).setCellValue(fiche.getCouleurTravauxNonRealises());
                dataRowFiche.createCell(47).setCellValue(fiche.getCouleurTravauxRealises());
                dataRowFiche.createCell(48).setCellValue(fiche.getIntervention().getCouleurTypeIntervention());
                dataRowFiche.createCell(49).setCellValue(fiche.getDescription());
                dataRowFiche.createCell(50).setCellValue(fiche.getLocalisation());
                dataRowFiche.createCell(51).setCellValue(fiche.getIntervenant().getNom());
                dataRowFiche.createCell(52).setCellValue(fiche.getIntervenant().getPrenom());
                dataRowFiche.createCell(53).setCellValue(fiche.getTypeIntervention());
                dataRowFiche.createCell(54).setCellValue(String.join(", ", fiche.getMateriauxOptions()));
            }
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
