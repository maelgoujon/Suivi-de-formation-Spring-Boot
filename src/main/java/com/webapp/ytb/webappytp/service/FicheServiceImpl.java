package com.webapp.ytb.webappytp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.repository.FicheRepository;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class FicheServiceImpl implements FicheService {

    private final FicheRepository ficheRepository; 

    @Override
    public FicheIntervention creer(FicheIntervention fiche) {
        return ficheRepository.save(fiche);
    }

    @Override
    public List<FicheIntervention> lireTout() {
        return ficheRepository.findAll();
    }
    @Override
    @Transactional
    public FicheIntervention lire(Long id) {
        return ficheRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fiche non trouvée"));
    }

    @Override
    public FicheIntervention modifier(Long id, FicheIntervention fiche) {
        return ficheRepository.findById(id)
            .map(existingFiche -> {
                existingFiche.setDemande(fiche.getDemande());
                existingFiche.setIntervention(fiche.getIntervention());
                existingFiche.setMaintenance(fiche.getMaintenance());
                existingFiche.setEtatFicheFinie(fiche.isEtatFicheFinie());
                existingFiche.setTravauxRealises(fiche.getTravauxRealises());
                existingFiche.setTravauxNonRealises(fiche.getTravauxNonRealises());
                existingFiche.setEvaluation(fiche.getEvaluation());;
    
                return ficheRepository.save(existingFiche);
            })
            .orElseThrow(() -> new RuntimeException("Fiche non trouvée"));
    }
    

    @Override
    public String supprimer(Long id) {
        ficheRepository.deleteById(id);
        return "Fiche supprimée";
    }


    @Transactional
    @Override
    public void enregistrerAudio(Long id, MultipartFile fichierAudio) throws Exception {
        // Récupérer la fiche d'intervention par son ID
        FicheIntervention ficheIntervention = ficheRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fiche non trouvée"));
            

        try {
            // Enregistrez le fichier audio dans le champ "evaluation" de l'entité
            ficheIntervention.setEvaluation(fichierAudio.getBytes());

            // Enregistrez les modifications dans la base de données
            ficheRepository.save(ficheIntervention);

        } catch (IOException e) {
            // Gérer les erreurs d'entrée/sortie
            throw new Exception("Erreur lors de la manipulation du fichier audio.", e);
        }
    }
}
