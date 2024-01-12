package com.webapp.ytb.webappytp.modele;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Formation")
@Getter
@Setter
@NoArgsConstructor
@Valid
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100)
    @NotBlank(message = "Le champ 'nom' est obligatoire.")
    private String nom;

    @Column(length = 500)
    private String description;
    
    
}
