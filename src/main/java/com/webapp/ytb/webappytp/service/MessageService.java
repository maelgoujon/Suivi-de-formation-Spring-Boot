package com.webapp.ytb.webappytp.service;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.expression.Objects;
import org.springframework.beans.factory.annotation.Autowired;

import com.webapp.ytb.webappytp.modele.FicheIntervention;
import com.webapp.ytb.webappytp.modele.Message;
import com.webapp.ytb.webappytp.modele.Utilisateur;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.repository.MessageRepository;

import jakarta.transaction.Transactional;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private FicheService ficheService;

    public List<Message> lire() {
        // retourner la liste des messages triés par date
        return messageRepository.findAllByOrderByTimestampAsc();
    }

    // retourner les messages d'une fiche triés par date
    public List<Message> lireParFiche(long id) {
        return messageRepository.findAllByFicheInterventionIdOrderByTimestampAsc(id);
    }

    // enregistrer une images dans le repertoire /images/messages
    public String saveImage(MultipartFile imageFile) throws IOException {
        String userChosenFileName = imageFile.getOriginalFilename();
        if (userChosenFileName != null) {
            // replace spaces with underscores
            userChosenFileName = userChosenFileName.replaceAll("\\s+", "_");
            String fileName = userChosenFileName;
            return saveImageLocally(imageFile, fileName);
        } else {
            userChosenFileName = UUID.randomUUID().toString();
            String fileName = userChosenFileName;
            return saveImageLocally(imageFile, fileName);
        }
    }

    private String saveImageLocally(MultipartFile imageFile, String fileName) throws IOException {
        // Obtenez le dossier de destination en fonction du type d'intervention
        String localPath = "images/messages/";

        // Assurez-vous que le dossier de destination existe
        Path uploadPath = Paths.get(localPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Concaténez le chemin local et le nom du fichier pour obtenir l'URL
        String imageUrl = "/images/messages" + "/" + fileName;

        // Enregistrez le fichier localement
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return imageUrl;
    }

    public void envoyer(String textContent, byte[] voiceContent, MultipartFile image, Utilisateur principal,
            Long ficheId) {
        Message message = new Message();
        Utilisateur sender = utilisateurService.findUserByLogin(principal.getLogin());

        message.setSender(sender);
        message.setTextContent(textContent);

        if (voiceContent != null && voiceContent.length > 0) {
            message.setAudio(voiceContent);
        } else {
            message.setAudio(null);
        }

        if (image != null && !image.isEmpty()) {
            try {
                String path = saveImage(image);
                message.setImageUrl(path);
            } catch (IOException e) {
                logger.error("Erreur lors de l'enregistrement de l'image", e);
            }
        } else {
            message.setImageUrl(null);
        }

        ZonedDateTime zdtAtCet = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        message.setTimestamp(zdtAtCet.toLocalDateTime());
        FicheIntervention fiche = ficheService.lire(ficheId);
        message.setFicheIntervention(fiche);
        messageRepository.save(message);
    }

}