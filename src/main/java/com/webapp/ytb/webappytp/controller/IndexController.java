package com.webapp.ytb.webappytp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(@RequestParam(name = "sujet", defaultValue = "Plomberie") String sujet, Model model) {
        String texte = "Format vierge " + sujet;
        model.addAttribute("texte", texte);

        return "index";
    }
}

