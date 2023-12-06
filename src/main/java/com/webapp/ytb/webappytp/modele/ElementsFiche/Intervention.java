package com.webapp.ytb.webappytp.modele.ElementsFiche;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Intervention {

    @Column
    //@NotBlank(message = "Le champ 'DateIntervention' est obligatoire.")
    private Date DateIntervention;

    @Column
    //@NotBlank(message = "Le champ 'DureeIntervention' est obligatoire.")
    private int DureeIntervention;
}
