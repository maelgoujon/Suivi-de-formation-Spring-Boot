package com.webapp.ytb.webappytp.service;

import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.repository.FormationRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FormationServiceImpl implements FormationService {
    
    private final FormationRepository formationRepository;

    @Autowired
    public FormationServiceImpl(FormationRepository formationRepository) {
        this.formationRepository = formationRepository;
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
    public void generatedExcel(HttpServletResponse response) throws Exception {

        List<Formation> formation = formationRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Formation Archive");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("nom");
        row.createCell(2).setCellValue("description");

        int dataRowIndex = 1;

        for (Formation formations : formation) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(formations.getId());
            dataRow.createCell(1).setCellValue(formations.getNom());
            dataRow.createCell(2).setCellValue(formations.getDescription());
            dataRowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
