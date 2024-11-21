package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.dto.RecipeResponseDTO;
import com.epam.recipe.platform.entity.RecipeEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.RecipeRepository;
import com.epam.recipe.platform.service.RecipeService;
import com.epam.recipe.platform.utility.ImageUploadUtility;
import com.epam.recipe.platform.utility.RecipeMapper;
import com.mongodb.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.epam.recipe.platform.dto.RecipeRequestDTO;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {


    private final RecipeRepository recipeRepository;

    private final ImageUploadUtility imageUploadUtility;

    private final MongoClient mongoClient;

    private final MongoConverter mongoConverter;

    @Override
    public RecipeResponseDTO addRecipe(RecipeRequestDTO recipeRequestDTO, MultipartFile file) {
        RecipeEntity recipeEntity = RecipeMapper.INSTANCE.recipeRequestDtoToEntity(recipeRequestDTO);

        imageUploadUtility.uploadImage(file, recipeEntity);

        recipeEntity.setReviews(new ArrayList<>());
        recipeEntity.setCountOfRatings(0);
        recipeEntity.setRatings(0.0);

        recipeRepository.save(recipeEntity);
        return RecipeMapper.INSTANCE.entityToRecipeResponseDTO(recipeEntity);
    }

    @Override
    public RecipeResponseDTO updateRecipe(String recipeId, RecipeRequestDTO recipeRequestDTO, MultipartFile file) {
        log.info("Updating recipe with ID: {}", recipeId);

        RecipeEntity recipeEntity = recipeRepository.findById(recipeId).orElseThrow(() -> {
            String errorMessage = "Recipe not found for ID: " + recipeId;
            log.error(errorMessage);
            return new NotFoundException(errorMessage);
        });

        if (file != null && !file.isEmpty()) {
            imageUploadUtility.uploadImage(file, recipeEntity);
        }

        updateEntity(recipeEntity, recipeRequestDTO);
        RecipeEntity updatedEntity = recipeRepository.save(recipeEntity);

        log.info("Recipe updated successfully with ID: {}", updatedEntity.getRecipeId());

        return RecipeMapper.INSTANCE.entityToRecipeResponseDTO(updatedEntity);
    }

    @Override
    public RecipeResponseDTO getById(String id) {
        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(id);
        log.info("Fetching recipe with ID: {}", id);
        return recipeEntity.map(RecipeMapper.INSTANCE::entityToRecipeResponseDTO).orElseThrow(() -> new RecipeException("Recipe not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<RecipeResponseDTO> search(String text)  {

        Instant start = Instant.now();
        log.info("Searching for recipe by text: {}", text);
        final List<RecipeEntity> recipeEntities = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("recipe-management");
        MongoCollection<Document> collection = database.getCollection("recipes");

        AggregateIterable<Document> aggregateIterable = performTextSearchQuery(text, collection);

        aggregateIterable.forEach(document -> recipeEntities.add(mongoConverter.read(RecipeEntity.class, document)));

        long timeTakenInMilliSec = Duration.between(start, Instant.now()).toMillis();
        log.info("Time taken to search for recipe by text: {} is {} milliseconds", text, timeTakenInMilliSec);

        Optional.of(recipeEntities).filter(List::isEmpty).ifPresentOrElse(list -> {
            log.error("No recipe found with text: {}", text);
            throw new RecipeException("Recipe not found",HttpStatus.NOT_FOUND);
        }, () -> log.info("Found recipes for text: {}", text));


        return recipeEntities.stream().map(RecipeMapper.INSTANCE::entityToRecipeResponseDTO).toList();

    }

    private static AggregateIterable<Document> performTextSearchQuery(String text, MongoCollection<Document> collection) {
        return collection.aggregate(List.of(new Document("$search", new Document("index", "default").append("text", new Document("query", text).append("path", Arrays.asList("cuisine", "recipe_description", "recipe_name", "category", "tags", "ingredients")).append("fuzzy", new Document("maxEdits", 2L).append("prefixLength", 3L).append("maxExpansions", 20L))))));
    }

    @Override
    public List<RecipeResponseDTO> getAllRecipes() {
    log.info("Fetching all recipes");
        List<RecipeEntity> recipeEntities = recipeRepository.findAll();
        return recipeEntities.stream().
                map(RecipeMapper.INSTANCE::entityToRecipeResponseDTO).
                toList();
    }



    @Override
    public boolean deleteRecipe(String recipeId) {
        log.info("Deleting recipe with id: {}", recipeId);
        boolean recipeExists = recipeRepository.existsById(recipeId);
        if (recipeExists) {
            recipeRepository.deleteById(recipeId);
        }
        return recipeExists;
    }


    private void updateEntity(RecipeEntity recipeEntity, RecipeRequestDTO recipeRequestDTO) {
        recipeEntity.setRecipeName(recipeRequestDTO.getRecipeName());
        recipeEntity.setRecipeDescription(recipeRequestDTO.getRecipeDescription());
        recipeEntity.setCuisine(recipeRequestDTO.getCuisine());
        recipeEntity.setCategory(recipeRequestDTO.getCategory());
        recipeEntity.setTags(recipeRequestDTO.getTags());
        recipeEntity.setIngredients(recipeRequestDTO.getIngredients());
        recipeEntity.setCookingTime(recipeRequestDTO.getCookingTime());
        recipeEntity.setDifficulty(recipeRequestDTO.getDifficulty());
        recipeEntity.setDietaryRestrictions(recipeRequestDTO.getDietaryRestrictions());
    }

}
