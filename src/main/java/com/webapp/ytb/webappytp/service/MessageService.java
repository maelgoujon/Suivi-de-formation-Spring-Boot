package com.webapp.ytb.webappytp.service;

import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.webapp.ytb.webappytp.modele.Message;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    public List<Message> lire() {
        return messageRepository.findAll();
    }

    public void envoyer(String textContent, MultipartFile voiceContent, Principal principal) {
        Message message = new Message();
        Utilisateur sender = utilisateurService.findUserByLogin(principal.getName());

        message.setSender(sender);
        message.setTextContent(textContent);
    
        if (voiceContent != null && !voiceContent.isEmpty()){
            try {
                byte[] audioBytes = voiceContent.getBytes();
                message.setAudio(audioBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            message.setAudio(null);
        }
    
        ZonedDateTime zdtAtCet = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        message.setTimestamp(zdtAtCet.toLocalDateTime());
        messageRepository.save(message);
    }

    private String saveAudioFile(MultipartFile voiceContent) {
        // Implémentez la logique pour enregistrer le fichier audio et renvoyez le chemin
        // Assurez-vous de générer un nom de fichier unique pour éviter les conflits
        return "chemin/vers/le/fichier/audio";
    }
}