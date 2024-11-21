package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.dto.RecipeRequestDTO;
import com.epam.recipe.platform.dto.RecipeResponseDTO;
import com.epam.recipe.platform.entity.RecipeEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.RecipeRepository;
import com.epam.recipe.platform.utility.ImageUploadUtility;
import com.mongodb.client.*;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ImageUploadUtility imageUploadUtility;

    @Mock
    private MongoClient mongoClient;

    @Mock
    private MongoConverter mongoConverter;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Document> mongoCollection;


    @Mock
    private AggregateIterable<Document> aggregateIterable;

    @InjectMocks
    private RecipeServiceImpl recipeService;




    private RecipeEntity recipeEntity;

    @BeforeEach
    void setUp() {
        recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Tomato Soup");
        recipeEntity.setRecipeId("1");
        recipeEntity.setRecipeDescription("Delicious Tomato Soup");
        recipeEntity.setCuisine("Italian");
        recipeEntity.setCategory("Soups");
        recipeEntity.setTags(Arrays.asList("vegetarian", "healthy"));
        recipeEntity.setIngredients(Arrays.asList("tomatoes", "onions", "garlic"));
        recipeEntity.setCookingTime(30);
        recipeEntity.setDifficulty("Medium");
        recipeEntity.setDietaryRestrictions("none");
    }

    @Test
    void testValidRecipeUpdate()  {
        RecipeRequestDTO inputDTO = new RecipeRequestDTO();
        inputDTO.setRecipeName("Updated Soup Recipe");
        inputDTO.setRecipeDescription("Updated description");
        inputDTO.setCuisine("French");
        inputDTO.setCategory("Appetizers");
        inputDTO.setTags(Arrays.asList("vegetarian", "appetizer"));
        inputDTO.setIngredients(Arrays.asList("mushrooms", "spinach"));
        inputDTO.setCookingTime(25);
        inputDTO.setDifficulty("Easy");
        inputDTO.setDietaryRestrictions("none");

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);  // Stubbing used by the method under test
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipeEntity)); // Stubbing used by the method under test
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity); // Stubbing used by the method under test


        RecipeResponseDTO result = recipeService.updateRecipe("1", inputDTO, file);
        // Asserting the result
        assertNotNull(result);
        assertEquals("Updated Soup Recipe", result.getRecipeName());
        assertEquals("Updated description", result.getRecipeDescription());
        assertEquals("French", result.getCuisine());
        assertEquals("Appetizers", result.getCategory());
        assertEquals(Arrays.asList("vegetarian", "appetizer"), result.getTags());
        assertEquals(Arrays.asList("mushrooms", "spinach"), result.getIngredients());
        assertEquals(25, result.getCookingTime());
        assertEquals("Easy", result.getDifficulty());
        assertEquals("none", result.getDietaryRestrictions());
        assertEquals(recipeEntity.getRecipeId(), result.getRecipeId());
        assertEquals(recipeEntity.getReviews(), result.getReviews());
        assertEquals(recipeEntity.getRatings(), result.getRatings());
        assertEquals(recipeEntity.getCountOfRatings(), result.getCountOfRatings());

        // Verifying interactions
        verify(recipeRepository, times(1)).findById("1");
        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
        verify(imageUploadUtility, times(1)).uploadImage(any(MultipartFile.class), any(RecipeEntity.class));
    }

    @Test
     void testRecipeNotFound() {
        RecipeRequestDTO inputDTO = new RecipeRequestDTO();
        inputDTO.setRecipeName("Updated Soup Recipe");
        inputDTO.setRecipeDescription("Updated description");
        inputDTO.setCuisine("French");
        inputDTO.setCategory("Appetizers");
        inputDTO.setTags(Arrays.asList("vegetarian", "appetizer"));
        inputDTO.setIngredients(Arrays.asList("mushrooms", "spinach"));
        inputDTO.setCookingTime(25);
        inputDTO.setDifficulty("Easy");
        inputDTO.setDietaryRestrictions("none");

        MultipartFile file = mock(MultipartFile.class);

        when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> recipeService.updateRecipe("1",inputDTO, file));
    }

    @Test
     void testNullFile() {
        RecipeRequestDTO inputDTO = new RecipeRequestDTO();
        inputDTO.setRecipeName("Updated Soup Recipe");
        inputDTO.setRecipeDescription("Updated description");
        inputDTO.setCuisine("French");
        inputDTO.setCategory("Appetizers");
        inputDTO.setTags(Arrays.asList("vegetarian", "appetizer"));
        inputDTO.setIngredients(Arrays.asList("mushrooms", "spinach"));
        inputDTO.setCookingTime(25);
        inputDTO.setDifficulty("Easy");
        inputDTO.setDietaryRestrictions("none");

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity);

        RecipeResponseDTO result = recipeService.updateRecipe("1",inputDTO, null);

        assertNotNull(result);
        assertEquals("Updated Soup Recipe", result.getRecipeName());
        assertEquals("Updated description", result.getRecipeDescription());
        assertEquals("French", result.getCuisine());
        assertEquals("Appetizers", result.getCategory());
        assertEquals(Arrays.asList("vegetarian", "appetizer"), result.getTags());
        assertEquals(Arrays.asList("mushrooms", "spinach"), result.getIngredients());
        assertEquals(25, result.getCookingTime());
        assertEquals("Easy", result.getDifficulty());
        assertEquals("none", result.getDietaryRestrictions());
        assertEquals(recipeEntity.getRecipeId(), result.getRecipeId());
        assertEquals(recipeEntity.getImageToken(), result.getImageToken());
        assertEquals(recipeEntity.getReviews(), result.getReviews());
        assertEquals(recipeEntity.getRatings(), result.getRatings());
        assertEquals(recipeEntity.getCountOfRatings(), result.getCountOfRatings());
    }

    @Test
     void testEmptyFile() {
        RecipeRequestDTO inputDTO = new RecipeRequestDTO();
        inputDTO.setRecipeName("Updated Soup Recipe");
        inputDTO.setRecipeDescription("Updated description");
        inputDTO.setCuisine("French");
        inputDTO.setCategory("Appetizers");
        inputDTO.setTags(Arrays.asList("vegetarian", "appetizer"));
        inputDTO.setIngredients(Arrays.asList("mushrooms", "spinach"));
        inputDTO.setCookingTime(25);
        inputDTO.setDifficulty("Easy");
        inputDTO.setDietaryRestrictions("none");

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity);

        RecipeResponseDTO result = recipeService.updateRecipe("1",inputDTO, file);

        assertNotNull(result);
        assertEquals("Updated Soup Recipe", result.getRecipeName());
        assertEquals("Updated description", result.getRecipeDescription());
        assertEquals("French", result.getCuisine());
        assertEquals("Appetizers", result.getCategory());
        assertEquals(Arrays.asList("vegetarian", "appetizer"), result.getTags());
        assertEquals(Arrays.asList("mushrooms", "spinach"), result.getIngredients());
        assertEquals(25, result.getCookingTime());
        assertEquals("Easy", result.getDifficulty());
        assertEquals("none", result.getDietaryRestrictions());
        assertEquals(recipeEntity.getRecipeId(), result.getRecipeId());
        assertEquals(recipeEntity.getImageToken(), result.getImageToken());
        assertEquals(recipeEntity.getReviews(), result.getReviews());
        assertEquals(recipeEntity.getRatings(), result.getRatings());
        assertEquals(recipeEntity.getCountOfRatings(), result.getCountOfRatings());
    }


    @Test
     void testNullRecipeDTO() {
        MultipartFile file = mock(MultipartFile.class);

        assertThrows(NotFoundException.class, () -> recipeService.updateRecipe(null,null, file));
    }


    @Test
    void testGetRecipeById()  {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeId("1");
        when(recipeRepository.findById("1")).thenReturn(Optional.of(recipeEntity));
        RecipeResponseDTO result = recipeService.getById("1");
        assertEquals("1", result.getRecipeId());
    }
    @Test
    void testGetRecipeById_failure() {
        when(recipeRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RecipeException.class, () -> recipeService.getById("1"));
    }
    @Test
    void testSearchRecipesFound() {
        when(mongoClient.getDatabase("recipe-management")).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection("recipes")).thenReturn(mongoCollection);
        String searchText = "test recipe";

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName("Test Recipe");

        Document document = new Document("recipeName", "test recipe")
                .append("cuisine", "American")
                .append("recipeDescription", "Delicious chicken recipe");
        List<Document> documents = List.of(document);

        when(mongoCollection.aggregate(anyList())).thenReturn(aggregateIterable);
        doAnswer(invocation -> {
            Consumer<Document> consumer = invocation.getArgument(0);
            documents.forEach(consumer);
            return null;
        }).when(aggregateIterable).forEach(any());

        when(mongoConverter.read(RecipeEntity.class, document)).thenReturn(recipeEntity);

        List<RecipeResponseDTO> result = recipeService.search(searchText);

        assertEquals(1, result.size());
        verify(mongoCollection).aggregate(anyList());
    }

    @Test
    void testSearchNoRecipesFound() {
        when(mongoClient.getDatabase("recipe-management")).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection("recipes")).thenReturn(mongoCollection);
        String searchText = "no recipe";

        List<Document> documents = new ArrayList<>();

        when(mongoCollection.aggregate(anyList())).thenReturn(aggregateIterable);
        doAnswer(invocation -> {
            Consumer<Document> consumer = invocation.getArgument(0);
            documents.forEach(consumer);
            return null;
        }).when(aggregateIterable).forEach(any());

        RecipeException thrown = assertThrows(RecipeException.class, () -> {
            recipeService.search(searchText);
        });

        assertEquals("Recipe not found", thrown.getMessage());
        verify(mongoCollection).aggregate(anyList());
    }

    @Test
    void testDeleteRecipe_ValidId_ReturnsTrue() throws Exception {
        String recipeId = "abc123";

        when(recipeRepository.existsById(recipeId)).thenReturn(true);

        boolean result = recipeService.deleteRecipe(recipeId);

        assertTrue(result);
        verify(recipeRepository, times(1)).deleteById(recipeId);
    }

    @Test
    void testDeleteRecipe_InvalidId_ReturnsFalse() throws Exception {
        String recipeId = "invalid-id";

        when(recipeRepository.existsById(recipeId)).thenReturn(false);

        boolean result = recipeService.deleteRecipe(recipeId);

        assertFalse(result);
        verify(recipeRepository, times(0)).deleteById(recipeId);
    }

    @Test
    void testAddRecipe_NullFile() {
        // Prepare input DTO
        RecipeRequestDTO inputDTO = new RecipeRequestDTO();
        inputDTO.setRecipeName("New Recipe");
        inputDTO.setRecipeDescription("Delicious new recipe");
        inputDTO.setCuisine("Mexican");
        inputDTO.setCategory("Main Course");
        inputDTO.setTags(Arrays.asList("spicy", "healthy"));
        inputDTO.setIngredients(Arrays.asList("chicken", "peppers", "onions"));
        inputDTO.setCookingTime(40);
        inputDTO.setDifficulty("Medium");
        inputDTO.setDietaryRestrictions("none");

        // Mocking behavior for repository save method
        when(recipeRepository.save(any(RecipeEntity.class))).thenAnswer(invocation -> {
            RecipeEntity savedEntity = invocation.getArgument(0);
            savedEntity.setRecipeId(UUID.randomUUID().toString()); // Simulating ID generation
            return savedEntity;
        });

        // Call the service method with null file
        RecipeResponseDTO result = recipeService.addRecipe(inputDTO, null);

        // Assert the result
        assertNotNull(result);
        assertEquals("New Recipe", result.getRecipeName());
        assertEquals("Delicious new recipe", result.getRecipeDescription());
        assertEquals("Mexican", result.getCuisine());
        assertEquals("Main Course", result.getCategory());
        assertEquals(Arrays.asList("spicy", "healthy"), result.getTags());
        assertEquals(Arrays.asList("chicken", "peppers", "onions"), result.getIngredients());
        assertEquals(40, result.getCookingTime());
        assertEquals("Medium", result.getDifficulty());
        assertEquals("none", result.getDietaryRestrictions());
        assertNotNull(result.getRecipeId()); // ID should not be null
        assertNull(result.getImageToken()); // Image token should be null when file is null

        // Verify repository save method invocation
        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
        // Verify image upload utility was not invoked
        verify(imageUploadUtility, times(0)).uploadImage(any(MultipartFile.class), any(RecipeEntity.class));
    }
    @Test
    void testGetAllRecipes() {
        List<RecipeEntity> mockRecipeEntities = Arrays.asList(recipeEntity);

        when(recipeRepository.findAll()).thenReturn(mockRecipeEntities);

        List<RecipeResponseDTO> result = recipeService.getAllRecipes();

        assertNotNull(result);
        assertEquals(1, result.size());
        RecipeResponseDTO responseDTO = result.get(0);
        assertEquals("Tomato Soup", responseDTO.getRecipeName());
        assertEquals("Delicious Tomato Soup", responseDTO.getRecipeDescription());
        assertEquals("Italian", responseDTO.getCuisine());
        assertEquals("Soups", responseDTO.getCategory());
        assertEquals(Arrays.asList("vegetarian", "healthy"), responseDTO.getTags());
        assertEquals(Arrays.asList("tomatoes", "onions", "garlic"), responseDTO.getIngredients());
        assertEquals(30, responseDTO.getCookingTime());
        assertEquals("Medium", responseDTO.getDifficulty());
        assertEquals("none", responseDTO.getDietaryRestrictions());
        assertEquals("1", responseDTO.getRecipeId());
        assertNull(responseDTO.getImageToken());

        verify(recipeRepository, times(1)).findAll();
    }

}
