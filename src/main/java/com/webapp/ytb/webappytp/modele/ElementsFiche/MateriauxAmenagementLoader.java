package com.webapp.ytb.webappytp.modele.ElementsFiche;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MateriauxAmenagementLoader {

    public static List<MateriauxAmenagement> loadElements() {
        List<MateriauxAmenagement> elements = new ArrayList<>();

        // Chemin vers le répertoire des images
        String directoryPath = "src/main/resources/static/images/MateriauxAmenagement/";

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName().replaceFirst("[.][^.]+$", ""); // Retire l'extension
                    String emplacementFichier = "/images/MateriauxAmenagement/" + file.getName();
                    MateriauxAmenagement element = new MateriauxAmenagement(fileName, emplacementFichier);
                    elements.add(element);
                }
            }
        }

        return elements;
    }
}
