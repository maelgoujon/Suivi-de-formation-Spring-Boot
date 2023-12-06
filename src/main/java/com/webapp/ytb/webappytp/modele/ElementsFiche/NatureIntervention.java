package com.webapp.ytb.webappytp.modele.ElementsFiche;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class NatureIntervention {
    public enum NatureType {
        AMENAGEMENT(0),
        FINITIONS(1),
        INSTALLATION_SANITAIRE(2),
        INSTALLATION_ELECTRIQUE(3);

        private final int value;

        NatureType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private NatureType NatureType;

    public NatureType getMaintenanceType() {
        return NatureType;
    }

    public void setMaintenanceType(NatureType NatureType) {
        this.NatureType = NatureType;
    }

}
