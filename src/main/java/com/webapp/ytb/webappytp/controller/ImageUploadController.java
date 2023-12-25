package com.webapp.ytb.webappytp.controller;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.webapp.ytb.webappytp.modele.ElementsFiche.MateriauxAmenagement;
import com.webapp.ytb.webappytp.repository.MateriauxAmenagementRepository;

@Controller
public class ImageUploadController {

    @Autowired
    private MateriauxAmenagementRepository materiauxAmenagementRepository;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;

    @GetMapping("/imageMatAmenagement")
    public String showImagePage(Model model) {
        List<MateriauxAmenagement> allImages = materiauxAmenagementRepository.findAll();
        model.addAttribute("maxFileSize", parseFileSize(MAX_FILE_SIZE));
        model.addAttribute("allImages", allImages);
        model.addAttribute("materiauxAmenagement", new MateriauxAmenagement());
        return "imageMatAmenagement";
    }

    @PostMapping("/uploadImageMatAmenagement")
    public String uploadImage(@ModelAttribute("materiauxAmenagement") MateriauxAmenagement materiauxAmenagement,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {

        try {
            if (!imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // Concaténer le préfixe MIME au champ imageData
                String imageDataWithPrefix = "data:" + imageFile.getContentType() + ";base64," + base64Image;
                materiauxAmenagement.setImageData(imageDataWithPrefix);

                materiauxAmenagementRepository.save(materiauxAmenagement);
                redirectAttributes.addFlashAttribute("message", "Image enregistrée avec succès");
            }
        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("error", "La taille du fichier dépasse la limite autorisée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une image avec ce nom existe déjà.");
        }

        return "redirect:/imageMatAmenagement";
    }

    @PostMapping("/modifierImage/{id}")
    public String editImage(@PathVariable("id") Long id,
            @RequestParam("newImageFile") MultipartFile newImageFile,
            Model model) {
        Optional<MateriauxAmenagement> optionalImage = materiauxAmenagementRepository.findById(id);
        if (optionalImage.isPresent()) {
            MateriauxAmenagement existingImage = optionalImage.get();

            try {
                // Assurez-vous que le type de contenu est correct
                String contentType = newImageFile.getContentType();
                if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                    // Vérifiez la taille du fichier
                    long maxFileSize = parseFileSize(MAX_FILE_SIZE);
                    if (newImageFile.getSize() > maxFileSize) {
                        // Gérez le cas où la taille du fichier dépasse la limite
                        model.addAttribute("message", "La taille du fichier dépasse la limite autorisée.");
                        model.addAttribute("image", existingImage); // Ajoutez l'image existante au modèle
                        model.addAttribute("id", id); // Ajoutez l'ID au modèle
                        return "editImagePage";
                    }

                    byte[] newImageBytes = newImageFile.getBytes();
                    String newBase64Image = Base64.getEncoder().encodeToString(newImageBytes);
                    String newImageDataWithPrefix = "data:" + contentType + ";base64," + newBase64Image;
                    existingImage.setImageData(newImageDataWithPrefix);
                } else {
                    // Gérez le cas où le type de contenu n'est pas pris en charge
                    model.addAttribute("message", "Le type de fichier n'est pas pris en charge.");
                    model.addAttribute("image", existingImage); // Ajoutez l'image existante au modèle
                    model.addAttribute("id", id); // Ajoutez l'ID au modèle
                    return "editImagePage";
                }
            } catch (FileSizeLimitExceededException ex) {
                // Interceptez l'exception et gérez-la ici
                model.addAttribute("message", "La taille du fichier dépasse la limite autorisée.");
                model.addAttribute("image", existingImage); // Ajoutez l'image existante au modèle
                model.addAttribute("id", id); // Ajoutez l'ID au modèle
                return "editImagePage";
            } catch (Exception e) {
                e.printStackTrace();
            }

            materiauxAmenagementRepository.save(existingImage);
            model.addAttribute("message", "Image modifiée avec succès");
        }

        return "redirect:/imageMatAmenagement";
    }

    private long parseFileSize(String fileSize) {
        DataSize dataSize = DataSize.parse(fileSize);
        return dataSize.toBytes();
    }

    @GetMapping("/editImage/{id}")
    public String showEditImagePage(@PathVariable("id") Long id, Model model) {
        Optional<MateriauxAmenagement> optionalImage = materiauxAmenagementRepository.findById(id);
        if (optionalImage.isPresent()) {
            MateriauxAmenagement existingImage = optionalImage.get();

            model.addAttribute("maxFileSize", parseFileSize(MAX_FILE_SIZE));
            model.addAttribute("image", existingImage);
            model.addAttribute("id", id);
            return "editImagePage";
        } else {
            return "redirect:/imageMatAmenagement";
        }
    }

}