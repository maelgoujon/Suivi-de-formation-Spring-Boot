package com.webapp.ytb.webappytp.controller;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.util.Objects;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;
import com.webapp.ytb.webappytp.repository.MateriauxAmenagementRepository;

@Controller
@RequestMapping("/materiaux")
public class MateriauxController {

    private final MateriauxAmenagementRepository materiauxAmenagementRepository;
    private static final String MAX_FILE_SIZE_STRING = "maxFileSize";
    private static final String REDIRECT_MATERIAUX_LISTE = "redirect:/materiaux/liste";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String SUCCESS_MESSAGE = "successMessage";
    private static final String ERROR_FILE_SIZE = "La taille du fichier dépasse la limite autorisée.";

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    @Autowired
    public MateriauxController(MateriauxAmenagementRepository materiauxAmenagementRepository) {
        this.materiauxAmenagementRepository = materiauxAmenagementRepository;
    }

    @GetMapping("/ajouter")
    public String showImagePage(Model model) {
        List<Materiaux> allImages = materiauxAmenagementRepository.findAll();
        model.addAttribute(MAX_FILE_SIZE_STRING, parseFileSize(MAX_FILE_SIZE));
        model.addAttribute("allImages", allImages);
        model.addAttribute("materiau", new Materiaux());
        return "materiaux/ajout_materiau";
    }

    @GetMapping("/liste")
    public String listeMateriaux(Model model) {
        List<Materiaux> allImages = materiauxAmenagementRepository.findAll();
        model.addAttribute(MAX_FILE_SIZE_STRING, parseFileSize(MAX_FILE_SIZE));
        model.addAttribute("allImages", allImages);
        model.addAttribute("materiau", new Materiaux());
        return "materiaux/liste_materiaux";
    }

    @PostMapping("/uploadMateriaux")
    public String uploadImage(@ModelAttribute("materiau") Materiaux materiau,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes, Model model) {

        try {
            if (!imageFile.isEmpty()) {
                String userChosenFileName = StringUtils
                        .cleanPath(Objects.requireNonNull(materiau.getNomImage()));

                String fileExtension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
                String fileName = userChosenFileName + "." + fileExtension;

                String imageUrl = saveImageLocally(imageFile, fileName, materiau.getTypeIntervention());
                materiau.setImageUrl(imageUrl);

                materiauxAmenagementRepository.save(materiau);
                redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Materiau enregistré avec succès");
            }
        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, ERROR_FILE_SIZE);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Une image avec ce nom existe déjà.");
        }

        return REDIRECT_MATERIAUX_LISTE;
    }

    private String saveImageLocally(MultipartFile imageFile, String fileName,
            Intervention.TypeIntervention typeIntervention) throws IOException {
        // Obtenez le dossier de destination en fonction du type d'intervention
        String localPath = "images/" + typeIntervention.name().toLowerCase() + "/";

        // Assurez-vous que le dossier de destination existe
        Path uploadPath = Paths.get(localPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Concaténez le chemin local et le nom du fichier pour obtenir l'URL
        String imageUrl = "images/" + typeIntervention.name().toLowerCase() + "/" + fileName;

        // Enregistrez le fichier localement
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return imageUrl;
    }

    @PostMapping("/modifier/{id}")
    public String editImage(@PathVariable("id") Long id,
            @RequestParam(value = "newImageFile", required = false) MultipartFile newImageFile,
            @RequestParam(value = "newNomImage", required = false) String newNomImage, // Nouveau nom d'image
            @RequestParam(value = "newTypeIntervention") Intervention.TypeIntervention newTypeIntervention,
            RedirectAttributes redirectAttributes, Model model) {
        Optional<Materiaux> optionalImage = materiauxAmenagementRepository.findById(id);
        if (optionalImage.isPresent()) {
            Materiaux existingImage = optionalImage.get();

            try {
                if (newImageFile != null && !newImageFile.isEmpty()) {
                    // Assurez-vous que le type de contenu est correct
                    String contentType = newImageFile.getContentType();
                    if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                        // Vérifiez la taille du fichier
                        long maxFileSize = parseFileSize(MAX_FILE_SIZE);
                        if (newImageFile.getSize() > maxFileSize) {
                            redirectAttributes.addFlashAttribute(ERROR_MESSAGE,
                                    ERROR_FILE_SIZE);
                            return REDIRECT_MATERIAUX_LISTE;
                        }

                        // Enregistrez la nouvelle image localement et obtenez l'URL
                        String imageUrl = saveImageLocally(newImageFile, existingImage.getNomImage(),
                                newTypeIntervention);
                        existingImage.setImageUrl(imageUrl);
                    } else {
                        redirectAttributes.addFlashAttribute(ERROR_MESSAGE,
                                "Le type de fichier n'est pas pris en charge.");
                        return REDIRECT_MATERIAUX_LISTE;
                    }
                }

                // Mettez à jour le type d'intervention avec la nouvelle valeur
                existingImage.setTypeIntervention(newTypeIntervention);

                // Mettez à jour le nom de l'image s'il est fourni
                if (newNomImage != null && !newNomImage.isEmpty()) {
                    existingImage.setNomImage(newNomImage);
                }

                materiauxAmenagementRepository.save(existingImage);
                redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Materiau modifiée avec succès");
            } catch (FileSizeLimitExceededException ex) {
                // Interceptez l'exception et gérez-la ici
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE,
                        ERROR_FILE_SIZE);
            } catch (Exception e) {
                // Gérez les autres exceptions ici si nécessaire
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE,
                        "Une erreur s'est produite lors de la modification du Materiau.");
            }
        }

        return REDIRECT_MATERIAUX_LISTE;
    }

    private long parseFileSize(String fileSize) {
        DataSize dataSize = DataSize.parse(fileSize);
        return dataSize.toBytes();
    }

    @GetMapping("/modifier/{id}")
    public String showEditImagePage(@PathVariable("id") Long id, Model model) {
        Optional<Materiaux> optionalImage = materiauxAmenagementRepository.findById(id);
        if (optionalImage.isPresent()) {
            Materiaux existingImage = optionalImage.get();

            model.addAttribute(MAX_FILE_SIZE_STRING, parseFileSize(MAX_FILE_SIZE));
            model.addAttribute("image", existingImage);
            model.addAttribute("id", id);
            return "materiaux/modifier_materiau";
        } else {
            return REDIRECT_MATERIAUX_LISTE;
        }
    }

    @GetMapping("/supprimer/{id}")
    public String deleteImage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Materiaux> optionalImage = materiauxAmenagementRepository.findById(id);
            if (optionalImage.isPresent()) {
                Materiaux existingImage = optionalImage.get();

                // Supprimez le fichier image du système de fichiers
                deleteImageLocally(existingImage.getImageUrl());

                // Supprimez l'entité image de la base de données
                materiauxAmenagementRepository.deleteById(id);

                redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE, "Image supprimée avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE,
                    "Une erreur s'est produite lors de la suppression de l'image.");
        }

        return REDIRECT_MATERIAUX_LISTE;
    }

    private void deleteImageLocally(String imageUrl) throws IOException {
        // Obtenez le chemin du fichier à partir de l'URL de l'image
        Path imagePath = Paths.get(imageUrl);

        // Supprimez le fichier image du système de fichiers
        Files.deleteIfExists(imagePath);
    }

}