package com.webapp.ytb.webappytp.service;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.UserRole;
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

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.relation.Role;

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
    public void supprimerFormationAvecUtilisateurs(Long formationId) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        List<Utilisateur> utilisateurs = formation.getUtilisateurs();

        // Supprimez la référence à la formation pour chaque utilisateur
        for (Utilisateur utilisateur : utilisateurs) {
            utilisateur.getFormations().remove(formation);
            utilisateurRepository.save(utilisateur); // Sauvegardez chaque utilisateur
        }

        // Supprimez la formation
        formationRepository.delete(formation);
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
        Optional<Formation> formationOptional = formationRepository.findById(formationId);

        if (formationOptional.isPresent()) {
            Formation formation = formationOptional.get();
            formations.remove(formation);
            utilisateur.setFormations(formations);

            utilisateurRepository.save(utilisateur);
        } else {
            throw new RuntimeException("Formation non trouvée");
        }
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

        // Define cell styles
        // Style pour les en-têtes des formations
        HSSFCellStyle formationHeaderStyle = workbook.createCellStyle();
        HSSFFont formationHeaderFont = workbook.createFont();
        formationHeaderFont.setBold(true);
        formationHeaderStyle.setFont(formationHeaderFont);
        formationHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        formationHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        formationHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Style pour les données des formations
        HSSFCellStyle formationDataStyle = workbook.createCellStyle();
        formationDataStyle.setAlignment(HorizontalAlignment.CENTER);
        formationDataStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        formationDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Style pour les en-têtes des utilisateurs
        HSSFCellStyle userHeaderStyle = workbook.createCellStyle();
        HSSFFont userHeaderFont = workbook.createFont();
        userHeaderFont.setBold(true);
        userHeaderStyle.setFont(userHeaderFont);
        userHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        userHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        userHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Style pour les données des utilisateurs
        HSSFCellStyle userDataStyle = workbook.createCellStyle();
        userDataStyle.setAlignment(HorizontalAlignment.CENTER);
        userDataStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        userDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Style pour les en-têtes des fiches
        HSSFCellStyle ficheHeaderStyle = workbook.createCellStyle();
        HSSFFont ficheHeaderFont = workbook.createFont();
        ficheHeaderFont.setBold(true);
        ficheHeaderStyle.setFont(ficheHeaderFont);
        ficheHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
        ficheHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        ficheHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Style pour les données des fiches
        HSSFCellStyle ficheDataStyle = workbook.createCellStyle();
        ficheDataStyle.setAlignment(HorizontalAlignment.CENTER);
        ficheDataStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        ficheDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle dateStyle = workbook.createCellStyle();
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        dateStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));

        // Set column widths
        for (int i = 0; i <= 15; i++) {
            sheet.setColumnWidth(i, 7500);
        }

        int dataRowIndex = 0;
        HSSFRow rowFormation = sheet.createRow(dataRowIndex);
        rowFormation.createCell(0).setCellValue("Nom de la Formation");
        rowFormation.createCell(1).setCellValue("Niveau de Qualification de la Formation");
        rowFormation.createCell(2).setCellValue("Description de la Formation");

        for (int i = 0; i < 3; i++) {
            rowFormation.getCell(i).setCellStyle(formationHeaderStyle);
        }

        dataRowIndex++;

        HSSFRow dataRowFormation = sheet.createRow(dataRowIndex);
        dataRowFormation.createCell(0).setCellValue(formation.getNom());
        dataRowFormation.createCell(1).setCellValue(formation.getNiveau_qualif());
        dataRowFormation.createCell(2).setCellValue(formation.getDescription());
        for (int i = 0; i < 3; i++) {
            dataRowFormation.getCell(i).setCellStyle(formationDataStyle);
        }
        dataRowIndex = dataRowIndex + 2;

        for (Utilisateur utilisateur : utilisateurs) {

            HSSFRow rowUser = sheet.createRow(dataRowIndex);
            rowUser.createCell(0).setCellValue("Nom de l'apprenti");
            rowUser.createCell(1).setCellValue("Prenom de l'apprenti");
            rowUser.createCell(2).setCellValue("Description de l'apprenti");
            rowUser.createCell(3).setCellValue("Role de l'apprentis");

            for (int i = 0; i < 4; i++) {
                rowUser.getCell(i).setCellStyle(userHeaderStyle);
            }

            dataRowIndex++;

            HSSFRow dataRowUser = sheet.createRow(dataRowIndex);
            dataRowUser.createCell(0).setCellValue(utilisateur.getNom());
            dataRowUser.createCell(1).setCellValue(utilisateur.getPrenom());
            dataRowUser.createCell(2).setCellValue(utilisateur.getDescription());
            dataRowUser.createCell(3).setCellValue(utilisateur.getRole().toString());
            for (int i = 0; i < 4; i++) {
                dataRowUser.getCell(i).setCellStyle(userDataStyle);
            }
            dataRowIndex++;

            // Headers for Fiche Intervention Information
            HSSFRow rowFiche = sheet.createRow(dataRowIndex++);
            rowFiche.createCell(0).setCellValue("Date de Création");
            rowFiche.createCell(1).setCellValue("Date de Demande");
            rowFiche.createCell(2).setCellValue("Date d'Intervention");
            rowFiche.createCell(3).setCellValue("Degré d'Urgence");
            rowFiche.createCell(4).setCellValue("Durée d'Intervention");
            rowFiche.createCell(5).setCellValue("État de la Fiche Finie");
            rowFiche.createCell(6).setCellValue("Type de Maintenance");
            rowFiche.createCell(7).setCellValue("Nom du Demandeur");
            rowFiche.createCell(8).setCellValue("Travaux Non Réalisés");
            rowFiche.createCell(9).setCellValue("Travaux Réalisés");
            rowFiche.createCell(10).setCellValue("Description");
            rowFiche.createCell(11).setCellValue("Localisation");
            rowFiche.createCell(12).setCellValue("Nom");
            rowFiche.createCell(13).setCellValue("Prénom");
            rowFiche.createCell(14).setCellValue("Type d'Intervention");
            rowFiche.createCell(15).setCellValue("Matériaux");

            for (int i = 0; i <= 15; i++) {
                rowFiche.getCell(i).setCellStyle(ficheHeaderStyle);
            }

            // Fiche Intervention Data
            for (FicheIntervention fiche : ficheInterventions) {
                HSSFRow dataRowFiche = sheet.createRow(dataRowIndex++);
                dataRowFiche.createCell(0).setCellValue(fiche.getDateCreation().toString());
                dataRowFiche.createCell(1).setCellValue(fiche.getDateDemande().toString());
                dataRowFiche.createCell(2).setCellValue(
                        fiche.getDateIntervention() != null ? fiche.getDateIntervention().toString() : "");
                dataRowFiche.createCell(3).setCellValue(fiche.getDegreUrgence());
                dataRowFiche.createCell(4).setCellValue(fiche.getDureeIntervention());
                dataRowFiche.createCell(5).setCellValue(fiche.isEtatFicheFinie());
                dataRowFiche.createCell(6).setCellValue(fiche.getMaintenance().getNiveauMaintenanceType());
                dataRowFiche.createCell(7).setCellValue(fiche.getNomDemandeur());
                dataRowFiche.createCell(8).setCellValue(fiche.getTravauxNonRealises());
                dataRowFiche.createCell(9).setCellValue(fiche.getTravauxRealises());
                dataRowFiche.createCell(10).setCellValue(fiche.getDescription());
                dataRowFiche.createCell(11).setCellValue(fiche.getLocalisation());
                dataRowFiche.createCell(12).setCellValue(fiche.getIntervenant().getNom());
                dataRowFiche.createCell(13).setCellValue(fiche.getIntervenant().getPrenom());
                dataRowFiche.createCell(14).setCellValue(fiche.getTypeIntervention());
                dataRowFiche.createCell(15).setCellValue(String.join(", ", fiche.getMateriauxOptions()));

                for (int i = 0; i <= 15; i++) {
                    dataRowFiche.getCell(i).setCellStyle(ficheDataStyle);
                }
            }
            dataRowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public List<Formation> getFormationsByUserId(long id) {
        List<Formation> formations = formationRepository.findAll();
        List<Formation> formationsByUserId = new ArrayList<>();
        for (Formation formation : formations) {
            List<Utilisateur> utilisateurs = formation.getUtilisateurs();
            for (Utilisateur utilisateur : utilisateurs) {
                if (utilisateur.getId() == id) {
                    formationsByUserId.add(formation);
                }
            }
        }
        return formationsByUserId;
    }

}
