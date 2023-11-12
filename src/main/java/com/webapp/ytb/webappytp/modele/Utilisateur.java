package com.webapp.ytb.webappytp.modele;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "Utilisateur")
@Getter
@Setter
@NoArgsConstructor
@Valid
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'nom' est obligatoire.")
    private String nom;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'prenom' est obligatoire.")
    private String prenom;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'login' est obligatoire.")
    private String login;

    @Column(length = 50)
    @NotBlank(message = "Le champ 'mot de passe' est obligatoire.")
    private String mdp;

    @Lob
    @Column(nullable = true) // Permet à la colonne "photo" d'être nulle
    private byte[] photo;

    @Enumerated(EnumType.STRING)
    private UserRole role; // Champ pour indiquer le rôle de l'étudiant (ADMIN, SUPERADMIN, USER)


}
