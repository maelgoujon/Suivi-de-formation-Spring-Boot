package com.webapp.ytb.webappytp.modele.Elements;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class Demande {

    @Column(length = 50)
    @NotBlank(message = "Le champ 'NomDemandeur' est obligatoire.")
    private String NomDemandeur;


    @Column
    @NotBlank(message = "Le champ 'DateDemande' est obligatoire.")
    private Date DateDemande;

    @Column
    @NotBlank(message = "Le champ 'Localisation' est obligatoire.")
    private String Localisation;
    
    @Column
    @NotBlank(message = "Le champ 'Description' est obligatoire.")
    private String Description;
    
    @Column
    @NotBlank(message = "Le champ 'Degré d'urgence' est obligatoire.")
    private int DegreUrgence;
    
}
