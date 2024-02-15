package com.webapp.ytb.webappytp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.ytb.webappytp.modele.ElementsFiche.ImagesTitres;
import com.webapp.ytb.webappytp.repository.ImagesTitresRepository;

@Service
public class ImagesTitresServiceImpl implements ImagesTitresService {

    @Autowired
    private ImagesTitresRepository imagesTitresRepository;

    @Autowired
    public ImagesTitresServiceImpl(ImagesTitresRepository imagesTitresRepository) {
        this.imagesTitresRepository = imagesTitresRepository;
    }

    @Override
    public ImagesTitres findByImageUrl(String ImageUrl) {
        for (ImagesTitres imagesTitres : imagesTitresRepository.findAll()) {
            if (imagesTitres.getImageUrl().equals(ImageUrl)) {
                return imagesTitres;
            }
        }

        return null;

    }
}