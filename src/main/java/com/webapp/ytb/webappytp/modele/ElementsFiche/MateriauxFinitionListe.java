package com.webapp.ytb.webappytp.modele.ElementsFiche;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum MateriauxFinitionListe {
    CHAMPLAT("Champlat"),
    CHIFFONS("Chiffons"),
    COLLE_FIXATION("Colle acrylique de fixation pour plinthe"),
    COLLE_TOILE_VERRE("Colle pour toile de verre"),
    CROISILLONS_EPaisseur_2_MM("Croisillons épaisseur 2 mm"),
    ENDUIT_JOINT("Enduit à joint"),
    ENDUIT_REBOUCHAGE("Enduit de rebouchage"),
    ETAGERE_BOIS_20_60("Etagère bois 20 x 60"),
    FAIENCE_MUR_20X20("Faïence mur 20 x 20"),
    JOINT_POUDRE_CARRELAGE("Joint poudre carrelage"),
    LOT_COLORANTS_UNIVERSELS("Lot de colorants universels de peintre"),
    MORTIER_COLLE_POUDRE("Mortier colle poudre"),
    PANNEAU_BOIS("Panneau bois (OSB ou aggloméré)"),
    PAPIER_VERRE_120("Papier de verre grain 120"),
    PAPIER_VERRE_80("Papier de verre grain 80"),
    PEINTURE_ACRYLIQUE_SATINEE("Peinture acrylique satinée"),
    PEINTURE_BOISERIES_ACRYLIQUE_BRILLANT("Peinture boiseries acrylique brillant"),
    PEINTURE_IMPRESSION("Peinture impression"),
    PLANCHE_COFFRAGE("Planche de coffrage"),
    PLAQUE_PLATRE_BA13("Plaque de Plâtre BA13"),
    PLINTHE_MDF_BOIS_BRUT("Plinthe MDF ou bois brut"),
    POINTES_TETE_HOMME("Pointes tête homme"),
    PORTEMANTEAU_MURAL_2_TETES("Portemanteau mural bois 2 têtes"),
    RAIL_R48("Rail R48"),
    REVETEMENT_PEINDRE_TOILE_VERRE("Revêtement à peindre - toile de verre (largeur 1 m)"),
    SERRURE_STANDARD_ENCASTRABLE_CYLINDRE("Serrure satandard encastrable NF Cylindre européen"),
    SERRURE_STANDARD_EN_L_ENCASTRABLE("Serrure standard en L encastrable"),
    TABLETTE_BOIS("Tablette bois"),
    TASSEAU_RABOTE("Tasseau raboté"),
    VERROU_BOUTON_CYLINDRE_40("Verrou à bouton - cylindre 40 mm"),
    VIS_BOIS_30("Vis à bois 30 mm"),
    VIS_TRPF("Vis TRPF"),
    VIS_TTPC_25("Vis TTPC 25"),
    VIS_TTPC_35("Vis TTPC 35");

    private final String optionName;

    MateriauxFinitionListe(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }
}
