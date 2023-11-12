package com.webapp.ytb.webappytp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "accueil";
    }
    


    @GetMapping("/accueil")
    public String redirectToAccueil() {
        return "accueil";
    }

    @GetMapping("/select_fiche")
    public String select_fiche() {
        return "select_fiche";
    }
    
    @GetMapping("/ajout_apprenti")
    public String ajout_apprenti() {
        return "ajout_apprenti";
    }


    @GetMapping("/fiche")
    public String fiche(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "index";
    }

    @GetMapping("/accueil_admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        if ("ROLE_ADMIN".equals(role)) {
            return "accueil_admin";
        }
        return "redirect:/accueil";
    }

    @GetMapping("/menu")
    public String menu(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
        model.addAttribute("username", username);
        model.addAttribute("role", role);
        return "menu";
    }
}
