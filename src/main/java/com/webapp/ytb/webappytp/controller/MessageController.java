package com.webapp.ytb.webappytp.controller;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Message;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.service.FicheService;
import com.webapp.ytb.webappytp.service.MessageService;
import com.webapp.ytb.webappytp.service.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private FicheService ficheService;

    @GetMapping("/chat/{id}")
    public String getChatPage(Model model, Principal principal, @PathVariable("id") long id) {
        // renvoyer la liste des messages triés par date, renvoyer un message vide si la
        // fiche n'existe pas
        FicheIntervention fiche = ficheService.lire(id);
        if (fiche == null) {
            Message message = new Message();
            message.setTextContent("Fiche non trouvée");
            message.setSender(utilisateurService.findUserByLogin(principal.getName()));
            List<Message> messages = new ArrayList<Message>();
            messages.add(message);
            model.addAttribute("messages", messages);
            return "chat";
        } else {
            List<Message> messages = messageService.lireParFiche(id);
            model.addAttribute("messages", messages);
            model.addAttribute("id", id);
            return "chat";
        }

    }

    @PostMapping("/chat/{id}")
    public String enregistrerMessage(@PathVariable("id") Long id,
            @RequestParam(value = "message", required = false) String message,
            @RequestPart(required = false) MultipartFile audioFile,
            @RequestPart(required = false) MultipartFile imageFile,
            Principal principal) {

        System.out.println("id : " + id);
        System.out.println("message : " + message);
        // r�cuperer l'utilisateur connect� � partir de son Principal
        Utilisateur user = utilisateurService.findUserByLogin(principal.getName());

        FicheIntervention fiche = ficheService.lire(id);
        if (audioFile != null && !audioFile.isEmpty() && fiche != null) {
            try {
                System.out.println("ICICICI1");
                messageService.envoyer(message, audioFile.getBytes(), imageFile, user, id);
            } catch (IOException e) {
                logger.error("Erreur lors de l'envoi du message audio", e);
            }
        } else if ((audioFile == null || audioFile.isEmpty()) && fiche != null) {
            System.out.println("ICICICI2");
            messageService.envoyer(message, null, imageFile, user, id);
        }

        if (audioFile != null) {
            System.out.println("audioFile : " + audioFile.getOriginalFilename());
        } else {
            System.out.println("audioFile : null");
        
        }

        if (imageFile != null) {
            System.out.println("imageFile : " + imageFile.getOriginalFilename());
        } else {
            System.out.println("imageFile : null");
        }
        return "redirect:/chat/" + id;
    }
}