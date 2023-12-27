package com.webapp.ytb.webappytp.modele.ElementsFiche;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MateriauxAmenagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nomImage;

    // Mettez à jour le champ pour stocker l'URL locale
    @Column(nullable = true)
    private String imageUrl;

    @Override
    public String toString() {
        return "MateriauxAmenagement{" +
                "nomImage='" + nomImage + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
