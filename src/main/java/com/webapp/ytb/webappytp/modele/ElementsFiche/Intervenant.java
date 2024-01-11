package com.webapp.ytb.webappytp.modele.ElementsFiche;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Intervenant {

    @Column
    @NotBlank(message = "Le 'nom' est obligatoire.")
    private String nom;

    @Column
    @NotBlank(message = "Le champ 'prenom' est obligatoire.")
    private String prenom;

    //Niveaux
    @Column
    private int niveauTitreIntervenant;
    
    @Column
    private int niveauNom;

    @Column
    private int niveauPrenom;

    //Couleurs
    @Column
    private String couleurTitreIntervenant;

    @Column
    private String couleurNom;

    @Column
    private String couleurPrenom;
    
}
