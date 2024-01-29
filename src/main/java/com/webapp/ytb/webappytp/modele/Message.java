package com.webapp.ytb.webappytp.modele;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne // Spécifie la relation many-to-one
    @JoinColumn(name = "utilisateur_id") // Colonne qui contiendra la clé étrangère vers Utilisateur
    private Utilisateur sender;

    @Column
    private String textContent;

    // fichier audio enregistré
    @Lob
    @Column(nullable = true)
    private byte[] audio;


    @Column
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ficheIntervention_id")
    private FicheIntervention ficheIntervention;


    public String getAudioBase64() {
        return Base64.getEncoder().encodeToString(this.audio);
    }

}