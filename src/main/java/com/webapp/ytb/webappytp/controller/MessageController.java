package com.webapp.ytb.webappytp.controller;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Message;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.MessageService;
import com.webapp.ytb.webappytp.service.UtilisateurService;

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

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/test")
    public String getTestPage(Model model, Principal principal) {
        //renvoyer la liste des messages triés par date
        List<Message> messages = messageService.lire();
        model.addAttribute("messages", messages);
        return "test";
    }

    @GetMapping("/chat")
    public String getChatPage(Model model, Principal principal) {
        List<Message> messages = messageService.lire();
        //creer un nouveau message en l'envoyer en attribut au template
        Message message = new Message();
        model.addAttribute("message", message);
        model.addAttribute("messages", messages);

        return "chat";
    }

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<String> enregistrerAudio(@RequestParam(value = "message", required = false) String message,
            @RequestPart(value = "audioFile", required = false) MultipartFile audioFile,
            Principal principal) {

                //récuperer l'utilisateur connecté à partir de son Principal
                Utilisateur user = utilisateurService.findUserByLogin(principal.getName());
        if (audioFile != null && !audioFile.isEmpty()) {
            try {
                messageService.envoyer(message, audioFile.getBytes(), user, 1L);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            messageService.envoyer(message, null, user, 1L);
        }
        return ResponseEntity.ok().body("Enregistrement audio réussi pour la fiche avec l'ID ");
    }

}