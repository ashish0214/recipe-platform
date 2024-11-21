package com.epam.recipe.platform.utility;

import com.epam.recipe.platform.dto.RecipeRequestDTO;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
@RequiredArgsConstructor
@Slf4j
public class DTOValidationUtility {
    private final Validator validator;


    public RecipeRequestDTO validate(String recipeStr)  {
        ObjectMapper objectMapper = new ObjectMapper();
        RecipeRequestDTO recipeRequestDTO;
        try {
            recipeRequestDTO = objectMapper.readValue(recipeStr, RecipeRequestDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON: {}", e.getMessage(), e);
            throw new RecipeException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Set<ConstraintViolation<RecipeRequestDTO>> violations = validator.validate(recipeRequestDTO);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<RecipeRequestDTO> violation : violations) {
                sb.append(violation.getMessage());
            }
            throw new RecipeException(sb.toString(),HttpStatus.BAD_REQUEST);
        }
        return recipeRequestDTO;
    }
}
