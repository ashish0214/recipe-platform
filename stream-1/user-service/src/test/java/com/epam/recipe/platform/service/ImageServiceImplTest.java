package com.epam.recipe.platform.service;

import com.epam.recipe.platform.entity.UserEntity;
import com.epam.recipe.platform.exceptionhandler.exception.UserException;
import com.epam.recipe.platform.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private ImageServiceImpl imageService;

    private UserEntity userEntity;

    private Path localDir;

    @BeforeEach
    void setUp() throws IOException {
        userEntity = new UserEntity();
        localDir = Paths.get("src/test/resources/images");
        if (!Files.exists(localDir)) {
            Files.createDirectories(localDir);
        }
    }

    @Test
    void handleImage_success() throws IOException {
        MockMultipartFile file = new MockMultipartFile("data", "image.jpg", "image/jpeg", "image content".getBytes());
        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:images")).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getFile()).thenReturn(localDir.toFile());
        imageService.handleImage(file, userEntity);
        verify(resourceLoader).getResource("classpath:images");
        verify(resource).exists();
        verify(resource).getFile();
        assertNotNull(userEntity.getImageUrl());
    }

    @Test
    public void testHandleImage_ImageDirectoryNotFound() {
        Resource resource = mock(Resource.class);
        when(resource.exists()).thenReturn(false);
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "some image".getBytes()
        );
        UserException exception = assertThrows(UserException.class, () -> imageService.handleImage(file, userEntity));

        assertEquals("Image directory not found", exception.getMessage());
    }

    @Test
    void handleImage_ioException() throws IOException {
        MockMultipartFile file = new MockMultipartFile("data", "image.jpg", "image/jpeg", "image content".getBytes());

        Resource resource = mock(Resource.class);
        File imageDir = mock(File.class);
        when(resourceLoader.getResource("classpath:images")).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getFile()).thenReturn(imageDir);
        when(imageDir.getPath()).thenReturn("images");

        UserException exception = assertThrows(UserException.class, () -> imageService.handleImage(file, userEntity));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());

        verify(resourceLoader).getResource("classpath:images");
        verify(resource).exists();
        verify(resource).getFile();
    }
}