package com.webapp.ytb.webappytp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webapp.ytb.webappytp.modele.Etudiant;
import com.webapp.ytb.webappytp.repository.ProduitRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProduitServiceImpl implements ProduitService{

    private final ProduitRepository produitRepository; 

    @Override
    public Etudiant creer(Etudiant produit) {
        return produitRepository.save(produit);
    }

    @Override
    public List<Etudiant> lire() {
        return produitRepository.findAll();
    }

    @Override
    public Etudiant modifier(Long id, Etudiant produit) {
        return produitRepository.findById(id)
        .map(p-> {
            p.setPrenom(produit.getPrenom());
            p.setNom(produit.getNom());
            p.setLogin(produit.getLogin());
            p.setMdp(produit.getMdp());
            p.setPhoto(produit.getPhoto());
            return produitRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Etudiant non trouvé"));
    }

    @Override
    public String supprimer(Long id) {
        produitRepository.deleteById(id);
        return "Produit supprimé";
    }
    
}
