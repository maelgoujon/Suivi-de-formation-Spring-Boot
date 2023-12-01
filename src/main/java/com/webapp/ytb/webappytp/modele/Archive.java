package com.webapp.ytb.webappytp.modele;

import java.util.Date;

public class Archive {
    private Date dateArchivage;
    private String nomApprenti;

    public Archive(Date date, String nom){
        this.dateArchivage = date;
        this.nomApprenti = nom;
    }
}
