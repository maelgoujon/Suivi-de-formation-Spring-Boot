package com.webapp.ytb.webappytp.modele.ElementsFiche;

public class MateriauxAmenagement {

    private final String optionName;
    private final String emplacementFichier;

    public MateriauxAmenagement(String optionName, String emplacementFichier) {
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
