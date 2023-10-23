package com.webapp.ytb.webappytp.modele;

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
@Table(name = "Etudiant")
@Getter
@Setter
@NoArgsConstructor
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    @NotBlank(message = "Le nom de l'étudiant ne peut pas être vide")
    private String nom;


    @Column(length = 50)
    private String prenom;

    @Column(length = 50)
    private String login;

    @Column(length = 50)
    private String mdp;

    @Column(length = 50)
    private String photo;

    @PrePersist
    public void validate() {
        if (nom == null || nom.trim().isEmpty()) {
            throw new ConstraintViolationException("Le nom de l'étudiant ne peut pas être vide.", null);
        }
    }
}
