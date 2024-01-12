package com.webapp.ytb.webappytp.modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.webapp.ytb.webappytp.modele.ElementsFiche.Demande;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervenant;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Maintenance;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
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


    @Embedded
    private Intervenant intervenant;

    @Embedded
    private Demande demande;

    @Embedded
    private Intervention intervention;

    @Column(nullable = true)
    private LocalDate dateCreation;

    @Embedded
    private Maintenance maintenance;

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

    @Column(name = "materiaux")
    private List<String> materiauxOptions;

    @Lob
    @Column(nullable = true)
    private byte[] evaluation;

    // Niveaux
    @Column
    private int niveauIntervenant;

    @Column
    private int niveauTravauxRealises;

    @Column
    private int niveauMateriauxUtilises;

    @Column
    private int niveauNatureIntervention;

    //Couleurs
    @Column
    private String couleurTravauxRealises;

    @Column
    private String couleurTravauxNonRealises;

    @Column
    private String couleurMateriauxUtilises;

    // getters
    public String getNomDemandeur() {
        return demande.getNomDemandeur();
    }

    public LocalDate getDateDemande() {
        return demande.getDateDemande();
    }

    public String getLocalisation() {
        return demande.getLocalisation();
    }

    public String getDescription() {
        return demande.getDescription();
    }

    public int getDegreUrgence() {
        return demande.getDegreUrgence();
    }

    public String getTypeIntervention() {
        return intervention.getTypeIntervention();
    }

    public LocalDate getDateIntervention() {
        return this.intervention.getDateIntervention();
    }

    public int getDureeIntervention() {
        return intervention.getDureeIntervention();
    }

    public int getNiveauDateIntervention() {
        return intervention.getNiveauDateIntervention();
    }

    public int getNiveauDureeIntervention() {
        return intervention.getNiveauDureeIntervention();
    }

    public Maintenance.MaintenanceType getMaintenanceType() {
        return maintenance.getMaintenanceType();
    }

    public int getNiveauMaintenanceType() {
        return maintenance.getNiveauMaintenanceType();
    }



    public int getNiveauNatureIntervention() {
        return this.niveauNatureIntervention;
    }

    public FicheIntervention(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.materiauxOptions = new ArrayList<>();
        this.demande = new Demande();
        this.intervention = new Intervention();
        this.intervention.setNiveauDureeIntervention(-1);
        this.maintenance = new Maintenance();
    }

}