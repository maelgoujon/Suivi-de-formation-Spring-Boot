package com.webapp.ytb.webappytp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webapp.ytb.webappytp.modele.Etudiant;
import com.webapp.ytb.webappytp.repository.EtudiantRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EtudiantServiceImpl implements EtudiantService{

    private final EtudiantRepository etudiantRepository; 

    @Override
    public Etudiant creer(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    @Override
    public List<Etudiant> lire() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant modifier(Long id, Etudiant etudiant) {
        return etudiantRepository.findById(id)
        .map(p-> {
            p.setPrenom(etudiant.getPrenom());
            p.setNom(etudiant.getNom());
            p.setLogin(etudiant.getLogin());
            p.setMdp(etudiant.getMdp());
            p.setPhoto(etudiant.getPhoto());
            return etudiantRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Etudiant non trouvé"));
    }

    @Override
    public String supprimer(Long id) {
        etudiantRepository.deleteById(id);
        return "Etudiant supprimé";
    }

    @Override
    public Etudiant findById(Long id) {
        return etudiantRepository.findById(id).orElse(null);
    }

}
