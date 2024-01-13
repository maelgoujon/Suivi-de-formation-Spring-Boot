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
public class ImagesTitres {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TypeImage {
        INTERVENANT,
        DEMANDE,
        INTERVENTION,
        TRAVAUX_REALISES,
        TRAVAUX_NON_REALISES,
        MATERIAUX_UTILISES
        
    }

    @Column(unique = true)
    private String nomImage;

    @Column
    private String imageUrl;

    @Column
    private TypeImage typeImage;

    public void setType(String type) {
        this.typeImage = TypeImage.valueOf(type);
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }
}
