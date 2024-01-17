package com.webapp.ytb.webappytp.modele;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "Utilisateur")
@Getter
@Setter
@NoArgsConstructor
@Valid
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "text", nullable = true)
    private String photoBase64;

    @Enumerated(EnumType.STRING)
    private UserRole role; // (ADMIN, SUPERADMIN, USER)

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FicheIntervention> ficheInterventions;

    @Min(0)
    @Max(3)
    @Column
    private Integer niveau;

    @ManyToMany
    @JoinTable(name = "utilisateur_formation", joinColumns = @JoinColumn(name = "utilisateur_id"), inverseJoinColumns = @JoinColumn(name = "formation_id"))
    private List<Formation> formations;
}
