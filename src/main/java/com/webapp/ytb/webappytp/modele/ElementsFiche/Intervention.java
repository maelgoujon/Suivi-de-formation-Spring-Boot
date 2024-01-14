package com.webapp.ytb.webappytp.modele.ElementsFiche;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Intervention {
    public enum TypeIntervention {
        AMENAGEMENT, ELECTRICITE, FINITION, PLOMBERIE, SERRURERIE;
    
        @Override
        public String toString() {
            return name();
        }
    }
    

    @Column(columnDefinition = "VARCHAR")
    private String typeIntervention;

    public String getTypeIntervention() {
        return typeIntervention;
    }

    public void setTypeIntervention(String typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    @Column
    // @NotBlank(message = "Le champ 'DateIntervention' est obligatoire.")
    private LocalDate dateIntervention;

    @Column
    // @NotBlank(message = "Le champ 'DureeIntervention' est obligatoire.")
    private int dureeIntervention;

    //Niveaux

    @Column
    private int niveauTitreIntervention;

    @Column
    private int niveauDureeIntervention;

    @Column
    private int niveauTypeIntervention;

    @Column
    private int niveauDateIntervention;

    //Couleurs
    @Column
    private String couleurTitreIntervention;

    @Column
    private String couleurDureeIntervention;

    @Column
    private String couleurTypeIntervention;

    @Column
    private String couleurDateIntervention;

    //Images
    @Column
    private String imageTitreInterventionUrl;

    @Column
    private String imageDureeInterventionUrl;

    @Column
    private String imageTypeInterventionUrl;

    @Column
    private String imageDateInterventionUrl;


}
