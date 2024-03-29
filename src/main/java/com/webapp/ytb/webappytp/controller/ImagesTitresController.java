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

import javax.swing.text.html.Option;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.webapp.ytb.webappytp.modele.ElementsFiche.Intervention;
import com.webapp.ytb.webappytp.modele.ElementsFiche.Materiaux;
import com.webapp.ytb.webappytp.modele.ElementsFiche.ImagesTitres;
import com.webapp.ytb.webappytp.modele.ElementsFiche.ImagesTitres.TypeImage;
import com.webapp.ytb.webappytp.repository.ImagesTitresRepository;

@Controller
@RequestMapping("/imagestitres")
public class ImagesTitresController {

    @Autowired
    private ImagesTitresRepository imagesTitresRepository;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    @GetMapping("/ajouter")
    public String showImagePage(Model model) {
        List<ImagesTitres> allImages = imagesTitresRepository.findAll();
        model.addAttribute("maxFileSize", parseFileSize(MAX_FILE_SIZE));
        model.addAttribute("allImages", allImages);
        model.addAttribute("imageTitre", new ImagesTitres());
        return "ajout_imageTitre";
    }

    @GetMapping("/liste")
    public String showImageList(Model model) {
        List<ImagesTitres> allImages = imagesTitresRepository.findAll();
        model.addAttribute("maxFileSize", parseFileSize(MAX_FILE_SIZE));
        model.addAttribute("allImages", allImages);
        model.addAttribute("imageTitre", new ImagesTitres());
        return "liste_imagesTitres";
    }

    @PostMapping("/uploadImagesTitres")
    public String uploadImage(@ModelAttribute("imageTitre") ImagesTitres imageTitre,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes, Model model) {

        try {
            if (!imageFile.isEmpty()) {
                String userChosenFileName = StringUtils
                        .cleanPath(Objects.requireNonNull(imageTitre.getNomImage()));

                String fileExtension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
                String fileName = userChosenFileName + "." + fileExtension;

                String imageUrl = saveImageLocally(imageFile, fileName, imageTitre.getTypeImage());
                imageTitre.setImageUrl(imageUrl);

                imagesTitresRepository.save(imageTitre);
                redirectAttributes.addFlashAttribute("successMessage", "Image enregistrée avec succès");
            }
        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "La taille du fichier dépasse la limite autorisée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Une image avec ce nom existe déjà.");
        }

        return "redirect:/imagestitres/liste";
    }

    private String saveImageLocally(MultipartFile imageFile, String fileName,
            ImagesTitres.TypeImage typeImage) throws IOException {
        String localPath = "src/main/resources/static/images/" + typeImage.name().toLowerCase() + "/";

        Path uploadPath = Paths.get(localPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String imageUrl = "images/" + typeImage.name().toLowerCase() + "/" + fileName;

        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return imageUrl;
    }

    @PostMapping("/modifier/{id}")
    public String editImage(@PathVariable("id") Long id,
            @RequestParam(value = "newImageFile", required = false) MultipartFile newImageFile,
            @RequestParam(value = "newNomImage", required = false) String newNomImage,
            @RequestParam(value = "newTypeImage") ImagesTitres.TypeImage newTypeImage,
            RedirectAttributes redirectAttributes, Model model) {
        Optional<ImagesTitres> optionalImage = imagesTitresRepository.findById(id);
        if (optionalImage.isPresent()) {
            ImagesTitres existingImage = optionalImage.get();

            try {
                if (newImageFile != null && !newImageFile.isEmpty()) {
                    String contentType = newImageFile.getContentType();
                    if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                        long maxFileSize = parseFileSize(MAX_FILE_SIZE);
                        if (newImageFile.getSize() > maxFileSize) {
                            redirectAttributes.addFlashAttribute("errorMessage",
                                    "La taille du fichier dépasse la limite autorisée.");
                            return "redirect:/imagestitres/liste";
                        }

                        String imageUrl = saveImageLocally(newImageFile, existingImage.getNomImage(),
                                newTypeImage);
                        existingImage.setImageUrl(imageUrl);
                    } else {
                        redirectAttributes.addFlashAttribute("errorMessage",
                                "Le type de fichier n'est pas pris en charge.");
                        return "redirect:/imagestitres/liste";
                    }
                }

                existingImage.setTypeImage(newTypeImage);

                if (newNomImage != null && !newNomImage.isEmpty()) {
                    existingImage.setNomImage(newNomImage);
                }

                imagesTitresRepository.save(existingImage);
                redirectAttributes.addFlashAttribute("successMessage", "Image modifiée avec succès");
            } catch (FileSizeLimitExceededException ex) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "La taille du fichier dépasse la limite autorisée.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Une erreur s'est produite lors de la modification de l'image.");
            }
        }

        return "redirect:/imagestitres/liste";
    }

    @GetMapping("/modifier/{id}")
    public String showEditImagePage(@PathVariable("id") Long id, Model model) {
        Optional<ImagesTitres> optionalImage = imagesTitresRepository.findById(id);
        if (optionalImage.isPresent()) {
            ImagesTitres existingImage = optionalImage.get();
            model.addAttribute("image", existingImage);
            model.addAttribute("maxFileSize", parseFileSize(MAX_FILE_SIZE));
            model.addAttribute("id", id);
            return "modifier_imagestitres";
        } else {
            return "redirect:/imagestitres/liste";
        }
    }

    private long parseFileSize(String fileSize) {
        DataSize dataSize = DataSize.parse(fileSize);
        return dataSize.toBytes();
    }

    @GetMapping("/supprimer/{id}")
    public String deleteImage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<ImagesTitres> optionalImage = imagesTitresRepository.findById(id);
        if (optionalImage.isPresent()) {
            ImagesTitres existingImage = optionalImage.get();

            // Supprimez l'image de la base de données
            imagesTitresRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Image supprimée avec succès");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Image introuvable");
        }

        return "redirect:/imagestitres/liste";
    }

}
