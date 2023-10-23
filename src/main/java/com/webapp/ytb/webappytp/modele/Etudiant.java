package com.webapp.ytb.webappytp.modele;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;



@Entity
@Getter
@Setter
@NoArgsConstructor
@Valid
public class Etudiant {
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

    @Column(length = 50)
    private String photo;
}
