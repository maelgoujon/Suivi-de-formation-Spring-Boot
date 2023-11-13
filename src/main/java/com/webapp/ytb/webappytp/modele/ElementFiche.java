package com.webapp.ytb.webappytp.modele;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ElementFiche")
@Getter
@Setter
@NoArgsConstructor
@Valid
public class ElementFiche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    private String titre;

    @Column
    private int niveau;

    @Column(length = 250)
    private String description;


    @ManyToOne
    @JoinColumn(name = "fiche_id")
    @JsonBackReference
    private FicheIntervention ficheIntervention;

    public ElementFiche(String titre, int niveau, String description) {
        this.titre = titre;
        this.niveau = niveau;
        this.description = description;
    }
}
