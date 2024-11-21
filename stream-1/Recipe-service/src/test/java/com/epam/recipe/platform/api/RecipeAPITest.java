package com.epam.recipe.platform.api;

import com.epam.recipe.platform.dto.*;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.service.CuisineService;
import com.epam.recipe.platform.service.IngredientService;
import com.epam.recipe.platform.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epam.recipe.platform.service.RecipeService;
import com.epam.recipe.platform.utility.DTOValidationUtility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeAPI.class)
class RecipeAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CuisineService cuisineService;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private IngredientService ingredientService;

    @MockBean
    private TagService tagService;

    @MockBean
    private DTOValidationUtility dtoValidationUtility;

    @Test
    void testGetRecipeById_ValidId_ReturnsRecipe() throws Exception {
        RecipeResponseDTO mockResponse = new RecipeResponseDTO();
        mockResponse.setRecipeName("Test Recipe");

        when(recipeService.getById(anyString())).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/{recipeId}", "abc123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").value("Test Recipe"));
    }

    @Test
    void testGetRecipeById_InvalidId_ReturnsBadRequest() throws Exception {
        when(recipeService.getById(anyString())).thenThrow(new RecipeException("Recipe not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get("/recipes/{recipeId}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createRecipe_success() throws Exception {
        RecipeRequestDTO recipeDTO = RecipeRequestDTO.builder()
                .recipeName("Test Recipe")
                .imageToken("imageToken")
                .recipeDescription("Description Should be more than 20 characters As it must be clear and perfect")
                .cuisine("Italian")
                .category("Main Course")
                .tags(List.of("tag1", "tag2"))
                .ingredients(List.of("ingredient1", "ingredient2"))
                .cookingTime(30)
                .difficulty("Easy")
                .dietaryRestrictions("None")
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("image", "test.jpg", "text/plain", "test image".getBytes());
        RecipeResponseDTO recipeResponseDTO = RecipeResponseDTO.builder()
                .recipeId("1")
                .recipeName("Test Recipe")
                .imageToken("imageToken")
                .recipeDescription("Description Should be more than 20 characters As it must be clear and perfect")
                .cuisine("Italian")
                .category("Main Course")
                .tags(List.of("tag1", "tag2"))
                .ingredients(List.of("ingredient1", "ingredient2"))
                .cookingTime(30)
                .difficulty("Easy")
                .dietaryRestrictions("None")
                .ratings(5.0)
                .build();

        when(recipeService.addRecipe(any(RecipeRequestDTO.class), any(MultipartFile.class))).thenReturn(recipeResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/recipes")
                        .file(multipartFile)
                        .param("recipe", objectMapper.writeValueAsString(recipeDTO))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void createRecipe_missingImageFile() throws Exception {
        RecipeRequestDTO recipeDTO = RecipeRequestDTO.builder()
                .recipeName("Test Recipe")
                .imageToken("imageToken")
                .recipeDescription("Description Should be more than 20 characters As it must be clear and perfect")
                .cuisine("Italian")
                .category("Main Course")
                .tags(List.of("tag1", "tag2"))
                .ingredients(List.of("ingredient1", "ingredient2"))
                .cookingTime(30)
                .difficulty("Easy")
                .dietaryRestrictions("None")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/recipes")
                        .param("recipe", objectMapper.writeValueAsString(recipeDTO))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUpdateRecipe_ValidRequest_ReturnsUpdatedRecipe() throws Exception {
        RecipeResponseDTO mockResponse = new RecipeResponseDTO();
        mockResponse.setRecipeName("Updated Recipe");

        String requestBody = objectMapper.writeValueAsString(new RecipeRequestDTO());

        MockMultipartFile mockImageFile = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[10]);

        when(dtoValidationUtility.validate(anyString())).thenReturn(new RecipeRequestDTO());
        when(recipeService.updateRecipe(anyString(), any(RecipeRequestDTO.class), any())).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/{recipeId}", "abc123")
                        .content(requestBody)
                        .param("recipe", requestBody)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mockImageFile.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").value("Updated Recipe"));
    }

    @Test
    void testUpdateRecipe_InvalidRequest_ReturnsBadRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new RecipeRequestDTO());

        when(dtoValidationUtility.validate(anyString())).thenThrow(new RecipeException("Invalid recipe request", HttpStatus.BAD_REQUEST));

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/{recipeId}", "invalid-id")
                        .content(requestBody)
                        .param("recipe", requestBody)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testSearchRecipe_RecipeFound() throws Exception {

        String text = "chicken";
        RecipeResponseDTO recipeResponseDTO = new RecipeResponseDTO();
        List<RecipeResponseDTO> responseDTOList = List.of(recipeResponseDTO);
        when(recipeService.search(anyString())).thenReturn(responseDTOList);
        mockMvc.perform(get("/recipes/search/{text}", text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").isNotEmpty());
    }

    @Test
    void testSearchRecipe_RecipeNotFound() throws Exception {
        String text = "unknown";
        when(recipeService.search(anyString())).thenThrow(new RecipeException("Recipe not found", HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/recipes/search/{text}", text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCuisineByName_Success() throws Exception {
        String cuisineName = "Italian";
        CuisineDTO mockDTO = new CuisineDTO();
        mockDTO.setId("1");
        mockDTO.setCuisineName(cuisineName);

        when(cuisineService.getCuisineByName(cuisineName)).thenReturn(mockDTO);

        mockMvc.perform(get("/recipes/cuisines/{cuisineName}", cuisineName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.cuisineName", is(cuisineName)));

        verify(cuisineService, times(1)).getCuisineByName(cuisineName);
    }

    @Test
    void testGetCuisineByName_NotFound() throws Exception {
        String cuisineName = "Indian";

        when(cuisineService.getCuisineByName(cuisineName)).thenThrow(new RecipeException("Cuisine not found",HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/recipes/cuisines/{cuisineName}", cuisineName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cuisineService, times(1)).getCuisineByName(cuisineName);
    }

    @Test
    void testGetCuisineByName_InvalidName() throws Exception {
        mockMvc.perform(get("/recipes/cuisines/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(cuisineService, never()).getCuisineByName(anyString());
    }

    @Test
    void testGetCuisines_Success() throws Exception {
        CuisineDTO cuisine1 = new CuisineDTO();
        cuisine1.setId("1");
        cuisine1.setCuisineName("Italian");

        CuisineDTO cuisine2 = new CuisineDTO();
        cuisine2.setId("2");
        cuisine2.setCuisineName("Chinese");

        List<CuisineDTO> mockDTOs = Arrays.asList(cuisine1, cuisine2);

        when(cuisineService.getCuisines()).thenReturn(mockDTOs);

        mockMvc.perform(get("/recipes/cuisines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].cuisineName", is("Italian")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].cuisineName", is("Chinese")));

        verify(cuisineService, times(1)).getCuisines();
    }
    @Test
    void testGetIngredientByName() throws Exception {
        IngredientsDTO ingredient = new IngredientsDTO();
        ingredient.setIngredientName("Tomato");
        given(ingredientService.getIngredientByName(anyString())).willReturn(ingredient);

        mockMvc.perform(get("/recipes/ingredients/Tomato"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ingredientName").value("Tomato"));
    }

    @Test
    void testGetIngredientByName_NotFound() throws Exception {
        given(ingredientService.getIngredientByName(anyString())).willThrow(new RecipeException("Ingredient not found",HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/recipes/ingredients/Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetIngredientByName_Invalid() throws Exception {
        mockMvc.perform(get("/recipes/ingredients/12Tomato"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetIngredients() throws Exception {
        IngredientsDTO ingredient = new IngredientsDTO();
        ingredient.setIngredientName("Tomato");
        List<IngredientsDTO> ingredients = Collections.singletonList(ingredient);
        given(ingredientService.getIngredients()).willReturn(ingredients);

        mockMvc.perform(get("/recipes/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].ingredientName").value("Tomato"));
    }

    @Test
    void testGetTagByName() throws Exception {
        String tagName = "Italian";
        TagsDTO tagDTO = new TagsDTO();
        tagDTO.setTagName(tagName);
        when(tagService.getTagByName(tagName)).thenReturn(tagDTO);

        mockMvc.perform(get("/recipes/tags/{name}", tagName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagName", is(tagName)));
    }

    @Test
    void testGetTagByNameNotFound() throws Exception {
        String tagName = "NonExistentTag";
        when(tagService.getTagByName(tagName)).thenThrow(new RecipeException("Tag not found",HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/recipes/tags/{name}", tagName))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTags() throws Exception {
        TagsDTO tagDTO1 = new TagsDTO();
        tagDTO1.setTagName("Italian");
        TagsDTO tagDTO2 = new TagsDTO();
        tagDTO2.setTagName("Mexican");
        when(tagService.getTags()).thenReturn(Arrays.asList(tagDTO1, tagDTO2));

        mockMvc.perform(get("/recipes/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tagName", is("Italian")))
                .andExpect(jsonPath("$[1].tagName", is("Mexican")));
    }

    @Test
    void testGetTagsWhenNoTags() throws Exception {
        when(tagService.getTags()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/recipes/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
    @Test
    void testUpdateRecipe_NoImageFile_ReturnsUpdatedRecipe() throws Exception {
        RecipeResponseDTO mockResponse = new RecipeResponseDTO();
        mockResponse.setRecipeName("Updated Recipe");

        String requestBody = objectMapper.writeValueAsString(new RecipeRequestDTO());

        when(dtoValidationUtility.validate(anyString())).thenReturn(new RecipeRequestDTO());
        when(recipeService.updateRecipe(anyString(), any(RecipeRequestDTO.class), isNull())).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/{recipeId}", "abc123")
                        .content(requestBody)
                        .param("recipe", requestBody)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").value("Updated Recipe"));
    }


    @Test
    void testUpdateRecipe_ValidationException_ReturnsBadRequest() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new RecipeRequestDTO());

        when(dtoValidationUtility.validate(anyString())).thenThrow(new RecipeException("Invalid recipe request",HttpStatus.BAD_REQUEST));

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/{recipeId}", "abc123")
                        .content(requestBody)
                        .param("recipe", requestBody)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUpdateRecipe_RecipeNotFound_ReturnsNotFound() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new RecipeRequestDTO());

        when(dtoValidationUtility.validate(anyString())).thenReturn(new RecipeRequestDTO());
        when(recipeService.updateRecipe(anyString(), any(RecipeRequestDTO.class), any())).thenThrow(new RecipeException("Recipe not found",HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.put("/recipes/{recipeId}", "1")
                        .content(requestBody)
                        .param("recipe", requestBody)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDeleteRecipe_ValidId_ReturnsNoContent() throws Exception {
        String recipeId = "abc123";

        when(recipeService.deleteRecipe(recipeId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testDeleteRecipe_InvalidId_ReturnsBadRequest() throws Exception {
        String recipeId = "invalid-id";

        when(recipeService.deleteRecipe(recipeId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testDeleteRecipe_NotFound_ReturnsNotFound() throws Exception {
        String recipeId = "unknown";

        when(recipeService.deleteRecipe(recipeId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void testGetAllRecipes_Success() throws Exception {
        RecipeResponseDTO recipe1 = new RecipeResponseDTO();
        recipe1.setRecipeId("1");
        recipe1.setRecipeName("Recipe 1");

        RecipeResponseDTO recipe2 = new RecipeResponseDTO();
        recipe2.setRecipeId("2");
        recipe2.setRecipeName("Recipe 2");

        List<RecipeResponseDTO> mockRecipes = Arrays.asList(recipe1, recipe2);

        when(recipeService.getAllRecipes()).thenReturn(mockRecipes);

        mockMvc.perform(get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].recipeId", is("1")))
                .andExpect(jsonPath("$[0].recipeName", is("Recipe 1")))
                .andExpect(jsonPath("$[1].recipeId", is("2")))
                .andExpect(jsonPath("$[1].recipeName", is("Recipe 2")));

        verify(recipeService, times(1)).getAllRecipes();
    }


}

