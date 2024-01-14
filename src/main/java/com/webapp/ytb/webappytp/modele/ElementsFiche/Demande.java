package com.webapp.ytb.webappytp.modele.ElementsFiche;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Demande {

    @Column(length = 50)
    @NotBlank(message = "Le champ 'NomDemandeur' est obligatoire.")
    private String NomDemandeur;

    @Column
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    //@NotBlank(message = "Le champ 'DateDemande' est obligatoire.")
    private LocalDate DateDemande;

    @Column
    @NotBlank(message = "Le champ 'Localisation' est obligatoire.")
    private String Localisation;

    @Column
    @NotBlank(message = "Le champ 'Description' est obligatoire.")
    private String Description;

    @Column
    @NotBlank(message = "Le champ 'Degré d'urgence' est obligatoire.")
    private int DegreUrgence;

    //Niveaux
    @Column
    private int niveauTitreDemande;

    @Column
    private int niveauNomDemandeur;

    @Column
    private int niveauDateDemande;

    @Column
    private int niveauLocalisation;

    @Column
    private int niveauDescription;

    @Column
    private int niveauDegreUrgence;

    @Column
    private int niveauTitreDemandeNom;

    @Column
    private int niveauTitreDemandeDate;

    @Column
    private int niveauTitreDemandeLocalisation;

    @Column
    private int niveauTitreDemandeDescription;

    @Column
    private int niveauTitreDemandeDegreUrgence;

    //Couleurs
    @Column
    private String couleurTitreDemande;

    @Column
    private String couleurNomDemandeur;

    @Column
    private String couleurDateDemande;

    @Column
    private String couleurLocalisation;

    @Column
    private String couleurDescription;

    @Column
    private String couleurDegreUrgence;

    //Images
    @Column
    private String imageTitreDemandeUrl;

    @Column
    private String imageTitreDemandeNomUrl;

    @Column
    private String imageTitreDemandeDateUrl;

    @Column
    private String imageTitreDemandeLocalisationUrl;

    @Column
    private String imageTitreDemandeDescriptionUrl;

    @Column
    private String imageTitreDemandeDegreUrgenceUrl;


}
