package com.webapp.ytb.webappytp.controller;

import com.webapp.ytb.webappytp.modele.Message;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/chat")
    public String getChatPage(Model model, Principal principal) {
        List<Message> messages = messageService.lire();
        model.addAttribute("messages", messages);
        return "chat";
    }

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam(value = "textContent", required = false) String textContent,
            @RequestParam(value = "voiceContent", required = false) MultipartFile voiceContent,
            Principal principal) {

        if (textContent == null || textContent.isEmpty()) {
            System.out.println("Message vide");
        } else {
            System.out.println("Message : " + textContent);
        }
        if (voiceContent != null && !voiceContent.isEmpty()) {
            System.out.println("Fichier audio : " + voiceContent.getOriginalFilename());
        } else {
            System.out.println("Pas de fichier audio");
        }
        messageService.envoyer(textContent, voiceContent, principal);
        return "redirect:/chat";
    }

}