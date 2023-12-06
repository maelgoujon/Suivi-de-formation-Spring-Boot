package com.webapp.ytb.webappytp.modele.ElementsFiche;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Maintenance {
    
    public enum MaintenanceType {
        AMELIORATIVE(0),
        PREVENTIVE(1),
        CORRECTIVE(2);

        private final int value;

        MaintenanceType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private MaintenanceType maintenanceType;

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }
}
