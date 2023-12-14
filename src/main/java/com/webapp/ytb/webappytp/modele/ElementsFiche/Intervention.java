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

    @Column
    //@NotBlank(message = "Le champ 'DateIntervention' est obligatoire.")
    private LocalDate DateIntervention;

    @Column
    private int NiveauDateIntervention;

    @Column
    //@NotBlank(message = "Le champ 'DureeIntervention' est obligatoire.")
    private int DureeIntervention;

    @Column
    private int NiveauDureeIntervention;

    @Column
    private String couleurIconeDuree;


}
