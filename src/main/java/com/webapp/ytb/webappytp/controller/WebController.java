package com.webapp.ytb.webappytp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {
    @GetMapping
    String getPeople(Model model) {
        model.addAttribute("something","this is the text");
        return "index";
    }
    
}
