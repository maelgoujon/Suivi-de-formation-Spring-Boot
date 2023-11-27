package com.webapp.ytb.webappytp.modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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


    @Column(length = 50)
    @NotBlank(message = "Le champ 'nomDemandeur' ne peut pas être vide")
    private String nomDemandeur;

    @Column
    @NotNull(message = "Le champ 'dateDemande' ne peut pas être vide")
    private LocalDate dateDemande;

    @Column
    @NotNull(message = "Le champ 'dateIntervention' ne peut pas être vide")
    private LocalDate dateIntervention;

    @Column
    @NotNull(message = "Le champ 'dateCreation' ne peut pas être vide")
    private LocalDate dateCreation;

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
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le champ 'typeIntervention' ne peut pas être vide")
    private TypeIntervention typeIntervention;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le champ 'natureIntervention' ne peut pas être vide")
    private NatureIntervention natureIntervention;

    @Column(length = 50)
    @NotNull(message = "Le champ 'etatFiche' ne peut pas être vide")
    private boolean etatFicheFinie;

    @Column(length = 250)
    private String travauxRealises;;

    @Column(length = 250)
    private String travauxNonRealises;

    @Column
    private boolean nouvelleIntervention;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "ficheIntervention", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ElementFiche> elements;

    @Column(name = "materiaux")
    private List<String> materiauxOptions;

    @Lob
    @Column(nullable = true) // Permet à la colonne "photo" d'être nulle
    private byte[] evaluation;

    public FicheIntervention(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.elements = new ArrayList<>();
        this.materiauxOptions = new ArrayList<>();
    }

}