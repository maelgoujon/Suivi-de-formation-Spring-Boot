package com.webapp.ytb.webappytp.modele.ElementsFiche;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public enum MateriauxPlomberieListe {
    BONDE_GRILLE("Bonde à grille pour lave-mains"),
    BOUCHON_LAITON("Bouchon laiton à visser F 1/2"),
    CHEVILLES_EXPANSION("Chevilles à expansion avec patte à vis"),
    CHEVILLES_FRAPPER("Chevilles à frapper"),
    CHEVILLES_AUTOFORANTES("Chevilles autoforeuses - Fixation plaque de plâtre"),
    CHIFFONS("Chiffons"),
    COLLE_FIXATION("Colle acrylique de fixation pour plinthe"),
    COLLE_PVC("Colle PVC"),
    COLLIER_PVC_40("Collier PVC Ø 40"),
    COLLIER_PVC_32("Colliers PVC Ø 32"),
    COLLIER_PVC_100("Colliers PVC Ø 100"),
    COLLIER_ATLAS_DOUBLE("Colliers type Atlas double Ø12"),
    COLLIER_ATLAS_SIMPLE("Colliers type Atlas Simple Ø12"),
    COUDE_CUIVRE_90("Coude cuivre 90° à souder FF Ø 12"),
    COUDE_PVC_100("Coude PVC 87°30° FF Ø 100"),
    COUDE_PVC_32("Coude PVC 87°30° FF Ø 32"),
    COUDE_PVC_40("Coude PVC 87°30° FF Ø 40"),
    FAIENCE_MUR_20X20("Faïence mur 20 x 20"),
    MORTIER_COLLE("Mortier colle poudre"),
    JOINT_POUDRE_CARRELAGE("Joint poudre carrelage"),
    ECROU_LAITON("Ecrou laiton à collet battu 12x17 Ø 12"),
    JOINTS_ETANCHEITE("Joints d'étanchéité suivant montages"),
    KIT_ROBINET("Kit robinet d'arrêt WC équerre + flexible + joint"),
    LAVE_MAINS("Lave-mains"),
    LOT_CHEVILLES_CLIPS("Lot de 2 chevilles clips pour fixation WC 6x70"),
    LOT_FIXATIONS_CREUSE("Lot de 2 fixations pour lave-mains parois creuse + cheville"),
    LOT_FIXATIONS_PLEINE("Lot de 2 fixations pour lave-mains parois pleine + cheville"),
    MANCHON_CUIVRE("Manchon cuivre à souder FF Ø 12"),
    MANCHON_DILATATION_PVC("Manchon de dilatation PVC H Ø 100"),
    MANCHON_MALE_12_12x17("Manchon mâle 243 CGU Ø 12 - M 12x17"),
    MANCHON_MALE_12_15x21("Manchon mâle 243 CGU Ø 12 - M 15x21"),
    MANCHON_PVC_100("Manchon PVC Ø 100"),
    MANCHON_PVC_32("Manchon PVC Ø 32"),
    MANCHON_PVC_40("Manchon PVC Ø 40"),
    MELANGEUR_LAVE_MAINS("Mélangeur pour lave-mains + Fléxibles de raccordement"),
    PACK_WC("Pack WC à poser sortie horizontale"),
    PANNEAU_BOIS("Panneau bois (OSB ou aggloméré)"),
    PAPIER_VERRE_120("Papier de verre grain 120"),
    PAPIER_VERRE_80("Papier de verre grain 80"),
    PATES_A_VIS("Pates à vis"),
    PIPE_COUDEE_WC("Pipe coudée WC 90° 110 mm"),
    PIPE_DROITE_WC("Pipe droite WC 110 mm"),
    PLANCHE_COFFRAGE("Planche de coffrage"),
    REDUCTION_PVC_40_32("Réduction PVC Ø 40/32"),
    ROBINET_PUISAGE("Robinet de puisage de lave-linge + platine de fixation"),
    ROBINET_SIMPLE_LAVE_MAINS("Robinet simple pour lave-mains + fléxible de raccordement"),
    ROSACES_CONIQUES("Rosaces coniques H19"),
    SIPHON_LAVABO("Siphon lavabo/lave-mains à visser sortie Ø 32"),
    SYSTEME_VIDAGE_PVC("Système de vidage PVC pour machine à laver"),
    TAMPON_REDUC_PVC("Tampon de réduction simple PVC Ø 100/40"),
    TAMPON_VISITE_BOUCHON_100("Tampon de visite PVC avec bouchon M/F Ø 100"),
    TAMPON_VISITE_BOUCHON_32("Tampon de visite PVC avec bouchon M/F Ø 32"),
    TE_EGAL_CUIVRE("Té égal cuivre à souder FFF Ø 12"),
    TE_PIED_BICHE_100("Té pied de biche 87°30 FF PVC Ø 100"),
    TE_PIED_BICHE_32("Té pied de biche 87°30 FF PVC Ø 32"),
    TE_PIED_BICHE_40("Té pied de biche 87°30 FF PVC Ø 40"),
    TUBE_CUIVRE_12("Tube cuivre Ø 12"),
    TUBE_PVC_100("Tube PVC Ø 100"),
    TUBE_PVC_32("Tube PVC Ø 32"),
    TUBE_PVC_40("Tube PVC Ø 40"),
    VANNE_ARRET_MF("Vanne d'arrêt MF 1/4 de tour - 12x17"),
    VERROU_BOUTON_CYLINDRE_40("Verrou à bouton - cylindre 40 mm"),
    VIS_BOIS_30("Vis à bois 30 mm"),
    VIS_TRPF("Vis TRPF"),
    VIS_TTPC_25("Vis TTPC 25"),
    VIS_TTPC_35("Vis TTPC 35");

    private final String optionName;

    MateriauxPlomberieListe(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {
        return optionName;
    }
}
