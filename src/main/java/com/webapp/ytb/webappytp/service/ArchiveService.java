package com.webapp.ytb.webappytp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.webapp.ytb.webappytp.modele.Archive;

public class ArchiveService {
    
    public Archive[] getArchives(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Archive[] archives = {new Archive(determinerDate("01/02/2023"), "Dupont"),
                              new Archive(determinerDate("01/11/2023"), "Doe"),
                              new Archive(determinerDate("01/01/2023"), "Smith")};
        return archives;
    }

    private Date determinerDate(String dateString) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        } catch (ParseException e) {
            // Handle parsing exception as needed
            e.printStackTrace();
            return null;
        }
    }
}
