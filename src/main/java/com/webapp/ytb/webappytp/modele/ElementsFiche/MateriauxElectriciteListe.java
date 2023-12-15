package com.webapp.ytb.webappytp.modele.ElementsFiche;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum MateriauxElectriciteListe {
    AMPOULE_E27("Ampoule E27"),
    BORNES_3_ENTREES("Bornes connection rapide - 3 entrées"),
    BORNES_2_ENTREES("Bornes connection rapide - 2 entrées"),
    COLLIER_ATLAS_DOUBLE("Colliers type Atlas double Ø12"),
    COLLIER_ATLAS_SIMPLE("Colliers type Atlas Simple Ø12"),
    CONDUCTEUR_H07VU_1_5_BLEU("Conducteur HO7VU 1,5² Bleu"),
    CONDUCTEUR_H07VU_1_5_NOIR("Conducteur HO7VU 1,5² Noir"),
    CONDUCTEUR_H07VU_1_5_ORANGE("Conducteur HO7VU 1,5² Orange"),
    CONDUCTEUR_H07VU_1_5_ROUGE("Conducteur HO7VU 1,5² Rouge"),
    CONDUCTEUR_H07VU_1_5_VERT_JAUNE("Conducteur HO7VU 1,5² Vert/Jaune"),
    CONDUCTEUR_H07VU_2_5_BLEU("Conducteur HO7VU 2,5² Bleu"),
    CONDUCTEUR_H07VU_2_5_ROUGE("Conducteur HO7VU 2,5² Rouge"),
    CONDUCTEUR_H07VU_2_5_VERT_JAUNE("Conducteur HO7VU 2,5² Vert/Jaune"),
    CONVECTEUR_ELECTRIQUE("Convecteur électrique"),
    ENDUIT_REBOUCHAGE("Enduit de rebouchage"),
    INTERRUPTEUR_SA_VV_ENCASTRABLE("Ensemble interrupteur SA/VV - encastrable"),
    PRISE_2P_T_ENCASTRABLE("Ensemble Prise 2P+T - encastrable"),
    FICHE_DCL_DOUILLE_E27("Fiche DCL et douille électrique E27"),
    GAINE_ICTA_20("Gaine ICTA Ø 20"),
    INTERRUPTEUR_AUTOMATIQUE("Interrupteur automatique avec détecteur de mouvement"),
    PLAQUE_PLATRE_BA13("Plaque de Plâtre BA13"),
    MONTANT_M48("Montant M48"),
    VIS_TTPC_25("Vis TTPC 25");

    private final String optionName;

    MateriauxElectriciteListe(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }
}
