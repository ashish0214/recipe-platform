package com.epam.recipe.platform.utility;

import com.epam.recipe.platform.entity.RecipeEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ImageUploadUtilityTest {

    @InjectMocks
    private ImageUploadUtility imageUploadUtility;

    @Mock
    private RecipeEntity recipeEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUploadImage_success() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "hello.png", "image/png", "some image".getBytes());
        doNothing().when(recipeEntity).setImageToken(anyString());

        imageUploadUtility.uploadImage(file, recipeEntity);

        verify(recipeEntity, times(1)).setImageToken(anyString());
    }

    @Test
    public void testUploadImage_ioException() {
        MultipartFile file = new MockMultipartFile("file", "hello.png", "image/png", "some image".getBytes()) {
            @Override
            public byte[] getBytes() throws IOException {
                throw new IOException("Error reading file");
            }
        };

        assertThrows(RecipeException.class, () -> imageUploadUtility.uploadImage(file, recipeEntity));
    }
}