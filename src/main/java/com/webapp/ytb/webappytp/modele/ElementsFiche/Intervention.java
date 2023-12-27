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
    private LocalDate DateIntervention;

    @Column
    private int NiveauDateIntervention;

    @Column
    // @NotBlank(message = "Le champ 'DureeIntervention' est obligatoire.")
    private int DureeIntervention;

    @Column
    private int NiveauDureeIntervention;

}
