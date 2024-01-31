package com.webapp.ytb.webappytp.service;

import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Message;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.repository.MessageRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private FicheService ficheService;

    public List<Message> lire() {
        //retourner la liste des messages triés par date
        return messageRepository.findAllByOrderByTimestampAsc();
    }

    //retourner les messages d'une fiche triés par date
    public List<Message> lireParFiche(long id) {
        return messageRepository.findAllByFicheInterventionIdOrderByTimestampAsc(id);
    }

    public void envoyer(String textContent, byte[] voiceContent, Utilisateur principal, Long ficheId) {
        Message message = new Message();
        Utilisateur sender = utilisateurService.findUserByLogin(principal.getLogin());

        message.setSender(sender);
        message.setTextContent(textContent);
    
        if (voiceContent != null && voiceContent.length > 0) {
            message.setAudio(voiceContent);
        }else{
            message.setAudio(null);
        }
    
        ZonedDateTime zdtAtCet = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        message.setTimestamp(zdtAtCet.toLocalDateTime());
        FicheIntervention fiche = ficheService.lire(ficheId);
        message.setFicheIntervention(fiche);
        messageRepository.save(message);
    }

    private String saveAudioFile(MultipartFile voiceContent) {
        // Impl�mentez la logique pour enregistrer le fichier audio et renvoyez le chemin
        // Assurez-vous de g�n�rer un nom de fichier unique pour �viter les conflits
        return "chemin/vers/le/fichier/audio";
    }
}