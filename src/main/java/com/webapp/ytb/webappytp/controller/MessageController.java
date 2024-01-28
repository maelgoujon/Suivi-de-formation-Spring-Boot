package com.webapp.ytb.webappytp.controller;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Message;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/test")
    public String getTestPage(Model model, Principal principal) {
        //renvoyer la liste des messages tri�s par date
        List<Message> messages = messageService.lire();
        model.addAttribute("messages", messages);
        return "test";
    }

    @GetMapping("/chat")
    public String getChatPage(Model model, Principal principal) {
        List<Message> messages = messageService.lire();
        model.addAttribute("messages", messages);
        return "chat";
    }

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<String> enregistrerAudio(@RequestParam(value = "message", required = false) String message,
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
            Principal principal) {
        messageService.envoyer(message, audioFile, principal);

        return ResponseEntity.ok().body("Enregistrement audio r�ussi pour la fiche avec l'ID ");
    }

}