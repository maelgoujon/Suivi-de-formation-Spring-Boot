package com.webapp.ytb.webappytp.service;

import com.webapp.ytb.webappytp.modele.Formation;
import com.webapp.ytb.webappytp.repository.FormationRepository;
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
}
