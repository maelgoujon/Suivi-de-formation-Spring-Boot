package com.webapp.ytb.webappytp.modele;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "FicheIntervention")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Valid
public class FicheIntervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int numero;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'nomDemandeur' ne peut pas être vide")
    private String nomDemandeur;

    @Column
    @NotNull(message = "Le champ 'dateDemande' ne peut pas être vide")
    private Date dateDemande;

    @Column
    @NotNull(message = "Le champ 'dateIntervention' ne peut pas être vide")
    private Date dateIntervention;

    @Column
    @NotNull(message = "Le champ 'dateCreation' ne peut pas être vide")
    private Date dateCreation;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'dureeIntervention' ne peut pas être vide")
    private String dureeIntervention;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'localisation' ne peut pas être vide")
    private String localisation;

    @Column(length = 250)
    @NotBlank(message = "Le champ 'descriptionDemande' ne peut pas être vide")
    private String descriptionDemande;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'degreUrgence' ne peut pas être vide")
    private String degreUrgence;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'typeIntervention' ne peut pas être vide")
    private String typeIntervention;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'natureIntervention' ne peut pas être vide")
    private String natureIntervention;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'couleurIntervention' ne peut pas être vide")
    private String couleurIntervention;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'etatFiche' ne peut pas être vide")
    private String etatFiche;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;


    public FicheIntervention(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    

}

