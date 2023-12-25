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

    @Column(columnDefinition = "text", nullable = true)
    private String imageData;


    @Override
    public String toString() {
        return "MateriauxAmenagement{" +
                "nomImage='" + nomImage + '\'' +
                ", imageData='" + imageData + '\'' +
                '}';
    }


}
