package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.entity.UserEntity;
import com.epam.recipe.platform.exceptionhandler.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl {

    private final ResourceLoader resourceLoader;

    public void handleImage(MultipartFile file, UserEntity userEntity) {
        String filename = UUID.randomUUID() + "." + Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        try {
            Resource resource = resourceLoader.getResource("classpath:images");
            if (!resource.exists()) {
                log.error("Error occurred: Image directory not found");
                throw new UserException("Image directory not found", HttpStatus.NOT_FOUND);
            }
            File imageDir = resource.getFile();
            Path imagePath = Paths.get(imageDir.getPath(), filename);
            Files.write(imagePath, file.getBytes());
            userEntity.setImageUrl(imagePath.toString());
        } catch (IOException ex) {
            log.error("Error occurred while saving image file: {}", ex.getMessage());
            throw new UserException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
