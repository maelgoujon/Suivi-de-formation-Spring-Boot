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

    @Column
    private int niveauDemande;



}
