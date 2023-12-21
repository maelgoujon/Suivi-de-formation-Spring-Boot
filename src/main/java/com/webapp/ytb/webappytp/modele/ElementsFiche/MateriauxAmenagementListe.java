package com.webapp.ytb.webappytp.modele.ElementsFiche;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum MateriauxAmenagementListe {
    BANDE_JOINT("Bande à joint", "/images/MateriauxAmenagement/bande_a_joint"),
    BANDE_ARME_JOINT("Bande armée à joint", "/images/MateriauxAmenagement/bande_arme_a_joint.png"),
    CHAMPLAT("Champlat", "/images/MateriauxAmenagement/champlat.png"),
    CHEVILLES_EXPANSION("Chevilles à expansion avec patte à vis", "/images/MateriauxAmenagement/chevilles_expansion.webp"),
    CHEVILLES_FRAPPER("Chevilles à frapper", "/images/MateriauxAmenagement/chevilles_frapper.png"),
    CHEVILLES_AUTOFORANTES("Chevilles autoforeuses - Fixation plaque de plâtre", "/images/MateriauxAmenagement/chevilles_autoforeuses.png"),
    CHIFFONS("Chiffons", "/images/MateriauxAmenagement/chiffons.png"),
    COLLE_FIXATION("Colle acrylique de fixation pour plinthe", "/images/MateriauxAmenagement/colle_fixation.png"),
    COLLE_TOILE_VERRE("Colle pour toile de verre", "/images/MateriauxAmenagement/colle_toile_verre.png"),
    CROISILLONS_EPaisseur_2_MM("Croisillons épaisseur 2 mm", "/images/MateriauxAmenagement/croisillons.png"),
    CYLINDRE_DOUBLE_ENTREE("Cylindre double entrée profil européen", "/images/MateriauxAmenagement/cylindre_double_entree.png"),
    ENDUIT_JOINT("Enduit à joint", "/images/MateriauxAmenagement/enduit_joint.png"),
    ENDUIT_REBOUCHAGE("Enduit de rebouchage", "/images/MateriauxAmenagement/enduit_rebouchage.png"),
    ENSEMBLE_PORTE_CLE_I("Ensemble de porte - Clé I", "/images/MateriauxAmenagement/ensemble_porte_cle_i.png"),
    ENSEMBLE_PORTE_CLE_L("Ensemble de porte - Clé L", "/images/MateriauxAmenagement/ensemble_porte_cle_l.png"),
    ENSEMBLE_INTERRUPT_SAVV_ENCASTRABLE("Ensemble intérupteur SA/VV - encastrable", "/images/MateriauxAmenagement/ensemble_interrupteur.png"),
    ENSEMBLE_PRISE_2P_T_ENCASTRABLE("Ensemble Prise 2P+T - encastrable", "/images/MateriauxAmenagement/ensemble_prise.png"),
    //continuer les telechargements depuis ici
    ETAGERE_BOIS_20_60("Etagère bois 20 x 60", "/images/MateriauxAmenagement/etagere_bois.png"),
    FAIENCE_MUR_20X20("Faïence mur 20 x 20", "/images/MateriauxAmenagement/faience_mur.png"),
    JOINT_POUDRE_CARRELAGE("Joint poudre carrelage", "/images/MateriauxAmenagement/joint_poudre_carrelage.png"),
    LOT_COLORANTS_UNIVERSELS("Lot de colorants universels de peintre", "/images/MateriauxAmenagement/lot_colorants.png"),
    MONTANT_M48("Montant M48", "/images/MateriauxAmenagement/montant_m48.png"),
    MORTIER_COLLE_POUDRE("Mortier colle poudre", "/images/MateriauxAmenagement/mortier_colle_poudre.png"),
    PANNEAU_BOIS("Panneau bois (OSB ou aggloméré)", "/images/MateriauxAmenagement/panneau_bois.png"),
    PAPIER_VERRE_120("Papier de verre grain 120", "/images/MateriauxAmenagement/papier_verre_120.png"),
    PAPIER_VERRE_80("Papier de verre grain 80", "/images/MateriauxAmenagement/papier_verre_80.png"),
    PEINTURE_ACRYLIQUE_SATINEE("Peinture acrylique satinée", "/images/MateriauxAmenagement/peinture_acrylique_satinee.png"),
    PEINTURE_BOISERIES_ACRYLIQUE_BRILLANT("Peinture boiseries acrylique brillant", "/images/MateriauxAmenagement/peinture_boiseries_brillant.png"),
    PEINTURE_IMPRESSION("Peinture impression", "/images/MateriauxAmenagement/peinture_impression.png"),
    PLANCHE_COFFRAGE("Planche de coffrage", "/images/MateriauxAmenagement/planche_coffrage.png"),
    PLAQUE_PLATRE_BA13("Plaque de Plâtre BA13", "/images/MateriauxAmenagement/plaque_platre_ba13.png"),
    PLINTHE_MDF_BOIS_BRUT("Plinthe MDF ou bois brut", "/images/MateriauxAmenagement/plinthe_mdf_bois_brut.png"),
    POINTES_TETE_HOMME("Pointes tête homme", "/images/MateriauxAmenagement/pointes_tete_homme.png"),
    PORTEMANTEAU_MURAL_2_TETES("Portemanteau mural bois 2 têtes", "/images/MateriauxAmenagement/portemanteau_mural.png"),
    RAIL_R48("Rail R48", "/images/MateriauxAmenagement/rail_r48.png"),
    REVETEMENT_PEINDRE_TOILE_VERRE("Revêtement à peindre - toile de verre (largeur 1 m)", "/images/MateriauxAmenagement/revetement_peindre_toile_verre.png"),
    SERRURE_STANDARD_ENCASTRABLE_CYLINDRE("Serrure satandard encastrable NF Cylindre européen", "/images/MateriauxAmenagement/serrure_encastrable_cylindre.png"),
    SERRURE_STANDARD_EN_L_ENCASTRABLE("Serrure standard en L encastrable", "/images/MateriauxAmenagement/serrure_en_l_encastrable.png"),
    TABLETTE_BOIS("Tablette bois", "/images/MateriauxAmenagement/tablette_bois.png"),
    TASSEAU_RABOTE("Tasseau raboté", "/images/MateriauxAmenagement/tasseau_rabote.png"),
    VERROU_BOUTON_CYLINDRE_40("Verrou à bouton - cylindre 40 mm", "/images/MateriauxAmenagement/verrou_bouton_cylindre_40.png"),
    VIS_BOIS_30("Vis à bois 30 mm", "/images/MateriauxAmenagement/vis_bois_30.png"),
    VIS_TRPF("Vis TRPF", "/images/MateriauxAmenagement/vis_trpf.png"),
    VIS_TTPC_25("Vis TTPC 25", "/images/MateriauxAmenagement/vis_ttpc_25.png"),
    VIS_TTPC_35("Vis TTPC 35", "/images/MateriauxAmenagement/vis_ttpc_35.png");

    private final String optionName;
    private final String emplacementFichier;

    MateriauxAmenagementListe(String optionName, String emplacementFichier) {
        this.optionName = optionName;
        this.emplacementFichier = emplacementFichier;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getEmplacementFichier() {
        return emplacementFichier;
    }
}
