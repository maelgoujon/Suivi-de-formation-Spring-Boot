package com.webapp.ytb.webappytp.modele;

import java.time.LocalDate;

import com.webapp.ytb.webappytp.modele.ElementsFiche.Demande;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Session")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Valid
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @ManyToOne
    Formation formation;

    @Column
    private Demande demande;

}
