package com.webapp.ytb.webappytp.modele;

import java.util.List;


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

    @Column(length = 50)
    private String niveau_qualif;

    @Column(length = 500)
    private String description;

    @ManyToMany(mappedBy = "formations")
    private List<Utilisateur> utilisateurs;
}
