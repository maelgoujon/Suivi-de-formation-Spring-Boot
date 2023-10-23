package com.webapp.ytb.webappytp.modele;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FicheIntervention")
@Getter
@Setter
@NoArgsConstructor
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
    private Date dateDemande;

    @Column
    private Date dateIntervention;

    @Column
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

    @PrePersist
    public void validate() {
        if (nomDemandeur == null || nomDemandeur.trim().isEmpty() ||
            dureeIntervention == null || dureeIntervention.trim().isEmpty() ||
            localisation == null || localisation.trim().isEmpty() ||
            descriptionDemande == null || descriptionDemande.trim().isEmpty() ||
            degreUrgence == null || degreUrgence.trim().isEmpty() ||
            typeIntervention == null || typeIntervention.trim().isEmpty() ||
            natureIntervention == null || natureIntervention.trim().isEmpty() ||
            couleurIntervention == null || couleurIntervention.trim().isEmpty() ||
            etatFiche == null || etatFiche.trim().isEmpty()) {
            throw new ConstraintViolationException("Un ou plusieurs champs ne peuvent pas être vides.", null);
        }
    }
}

