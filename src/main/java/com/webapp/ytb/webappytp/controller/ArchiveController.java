package com.webapp.ytb.webappytp.controller;

import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.webapp.ytb.webappytp.modele.Archive;
import com.webapp.ytb.webappytp.service.ArchiveService;

public class ArchiveController {
    
    private ArchiveService ArchiveService;

    @GetMapping("/archive")
    public String archive(Model model){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Archive[] archives = {new Archive(determinerDate("01/02/2023"), "Dupont"),
                              new Archive(determinerDate("01/11/2023"), "Doe"),
                              new Archive(determinerDate("01/01/2023"), "Smith")};
        model.addAttribute("archives", archives);

        return "archive";
    }

    private Date determinerDate(String dateString) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
}
