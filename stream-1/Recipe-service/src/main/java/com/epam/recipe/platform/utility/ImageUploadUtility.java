package com.epam.recipe.platform.utility;

import com.epam.recipe.platform.entity.RecipeEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@Slf4j
@SuppressWarnings("ALL")
public class ImageUploadUtility {

    @Value("${app.image-directory}")
    private String imageDirectory;

    public  void uploadImage(MultipartFile file, RecipeEntity recipeEntity)  {
        String filename = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];
        Path imagePath = Paths.get(imageDirectory + filename);
        try {
            Files.write(imagePath, file.getBytes());
        } catch (IOException e) {
            log.error("Error uploading image: {}", e.getMessage(), e);
            throw new RecipeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String imageUrl = "http://localhost:8080/images/" + filename;
        recipeEntity.setImageToken(imageUrl);
    }
}
