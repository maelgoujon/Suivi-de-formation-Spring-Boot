package com.webapp.ytb.webappytp.modele.ElementsFiche;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Materiaux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nomImage;

    // Mettez à jour le champ pour stocker l'URL locale
    @Column(nullable = true)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column
    private Intervention.TypeIntervention typeIntervention;

    public void setType(String type) {
        this.typeIntervention = Intervention.TypeIntervention.valueOf(type);
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }

    @Override
    public String toString() {
        return "Materiaux{" +
                "nomImage='" + nomImage + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", typeIntervention=" + typeIntervention +
                '}';
    }
}
