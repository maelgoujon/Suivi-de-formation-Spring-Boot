package com.webapp.ytb.webappytp.service;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.repository.FicheRepository;
import com.webapp.ytb.webappytp.repository.FormationRepository;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final FicheRepository ficheRepository;
    private final FormationRepository formationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Utilisateur creer(Utilisateur utilisateur) {
        // Encodez le mot de passe avant de sauvegarder l'utilisateur
        utilisateur.setMdp(passwordEncoder.encode(utilisateur.getMdp()));
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> lire() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur modifier(Long id, Utilisateur utilisateur) {
        return utilisateurRepository.findById(id)
                .map(p -> {
                    p.setPrenom(utilisateur.getPrenom());
                    p.setNom(utilisateur.getNom());
                    p.setLogin(utilisateur.getLogin());
                    p.setMdp(passwordEncoder.encode(utilisateur.getMdp()));
                    p.setPhotoBase64(utilisateur.getPhotoBase64());
                    p.setRole(utilisateur.getRole());
                    p.setDescription(utilisateur.getDescription());
                    return utilisateurRepository.save(p);
                }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    public String supprimer(Long id) {
        utilisateurRepository.deleteById(id);
        return "Utilisateur supprimé";
    }

    @Override
    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    @Override
    public void modifierMotDePasse(Long id, String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
        // Encodez le nouveau mot de passe avant de le sauvegarder
        utilisateur.setMdp(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> getUtilisateursByRole(String roleStr) {
        UserRole role = UserRole.valueOf(roleStr); // Convertit la chaîne en énumération
        return utilisateurRepository.findByRole(role);
    }

    @Override
    public Utilisateur findUserByLogin(String login) {
        return utilisateurRepository.findUserByLogin(login);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // retourne la liste des utilisateurs d'une formation
    @Override
    public List<Utilisateur> findUserByFormation(Long formationId) {
        return formationRepository.findById(formationId).get().getUtilisateurs();
    }

    @Override
    public void generatedExcelForUser(Long userId, HttpServletResponse response) throws Exception {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<FicheIntervention> ficheInterventions = ficheRepository.findByUtilisateurId(userId);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Archive");

        // Define cell styles for user and fiche data
        HSSFCellStyle userDataStyle = workbook.createCellStyle();
        userDataStyle.setAlignment(HorizontalAlignment.CENTER);
        userDataStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        userDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle ficheDataStyle = workbook.createCellStyle();
        ficheDataStyle.setAlignment(HorizontalAlignment.CENTER);
        ficheDataStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        ficheDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle dateStyle = workbook.createCellStyle();
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        dateStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));

        // Set column widths
        for (int i = 0; i <= 15; i++) {
            sheet.setColumnWidth(i, 7500);
        }

        int dataRowIndex = 0;

        // User Information Section
        HSSFRow rowUser = sheet.createRow(dataRowIndex);
        rowUser.createCell(0).setCellValue("Nom de l'apprenti");
        rowUser.createCell(1).setCellValue("Prenom de l'apprenti");
        rowUser.createCell(2).setCellValue("Description de l'apprenti");
        rowUser.createCell(3).setCellValue("Role de l'apprentis");

        for (int i = 0; i < 4; i++) {
            rowUser.getCell(i).setCellStyle(headerStyle);
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
            rowFiche.getCell(i).setCellStyle(headerStyle);
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

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

}
