package com.epam.recipe.platform.api;

import com.epam.recipe.platform.dto.*;
import com.epam.recipe.platform.service.CuisineService;
import com.epam.recipe.platform.service.IngredientService;
import com.epam.recipe.platform.service.RecipeService;
import com.epam.recipe.platform.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import com.epam.recipe.platform.utility.DTOValidationUtility;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.epam.recipe.platform.constants.RecipeDTOValidationConstants.*;

@RestController
@RequestMapping("recipes")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RecipeAPI {
    private final DTOValidationUtility dtoValidationUtility;
    private final RecipeService recipeService;
    private final CuisineService cuisineService;
    private final IngredientService ingredientService;
    private final TagService tagService;

    @Operation(summary = "Get Recipe by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved recipe"), @ApiResponse(responseCode = "400", description = "Invalid Recipe ID supplied"), @ApiResponse(responseCode = "404", description = "Recipe not found with provided ID")})
    @GetMapping("{recipeId}")
    public ResponseEntity<RecipeResponseDTO> getRecipeById( @Pattern(regexp = "^[a-z0-9]+$", message = "Invalid Recipe ID supplied") @PathVariable String recipeId) {
        log.info("Get Recipe by ID: {}", recipeId);
        RecipeResponseDTO recipeResponseDTO = recipeService.getById(recipeId);
        return ResponseEntity.ok(recipeResponseDTO);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecipeResponseDTO> createRecipe(@RequestParam("recipe") String recipeStr, @RequestParam("image") MultipartFile imageFile) {
        log.info("Creating recipe with recipeStr: {}", recipeStr);
        RecipeRequestDTO recipeRequestDTO = dtoValidationUtility.validate(recipeStr);
        return ResponseEntity.status(201).body(recipeService.addRecipe(recipeRequestDTO, imageFile));
    }

    @PutMapping(path = "{recipeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecipeResponseDTO> updateRecipe(@PathVariable @Pattern(regexp = "^[a-z0-9]+$", message = "Invalid Recipe ID supplied") String recipeId, @RequestParam("recipe") String recipeStr, @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        log.info("Entering updateRecipe method for recipeId: {}", recipeId);
        RecipeRequestDTO recipeRequestDTO = dtoValidationUtility.validate(recipeStr);
        log.info("Successfully updated recipe with recipeId: {}", recipeId);
        return ResponseEntity.ok(recipeService.updateRecipe(recipeId, recipeRequestDTO, imageFile));
    }

    @GetMapping("search/{text}")
    @Operation(summary = "Search for a recipe by text in the recipe name, description, ingredients, tags", responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved list of recipes"), @ApiResponse(responseCode = "404", description = "The recipe you were trying to reach is not found")})
    public ResponseEntity<List<RecipeResponseDTO>> searchRecipe(@Pattern(regexp = "^(?! +$)[a-zA-Z]+([ -][a-zA-Z]+)*$", message = "Text must be alphanumeric and should not start with a number") @PathVariable String text)  {
        log.info("Searching for recipe by text: {}", text);
        return ResponseEntity.ok(recipeService.search(text));
    }


    @Operation(summary = "Get a cuisine by name")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved cuisine"), @ApiResponse(responseCode = "400", description = "Invalid cuisine name provided"), @ApiResponse(responseCode = "404", description = "cuisine not found")})
    @GetMapping("cuisines/{name}")
    public ResponseEntity<CuisineDTO> getCuisineByName(@Pattern(regexp = "^(?! +$)[a-zA-Z]+([ -][a-zA-Z]+)*$?$", message = RECIPE_CUISINE_PATTERN_MESSAGE) @Size(min = 2, max = 20, message = RECIPE_CUISINE_SIZE_MESSAGE) @PathVariable String name) {
        return ResponseEntity.ok(cuisineService.getCuisineByName(name));
    }

    @DeleteMapping("{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable String recipeId) {
        log.info("Deleting recipe with id: {}", recipeId);
        if (recipeId.matches("[a-zA-Z0-9]+")) {
            if (recipeService.deleteRecipe(recipeId)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Recipe not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Invalid recipeId", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get all cuisines")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved cuisines"), @ApiResponse(responseCode = "404", description = "Cuisines not found")})
    @GetMapping("cuisines")
    public ResponseEntity<List<CuisineDTO>> getCuisines() {
        return ResponseEntity.ok(cuisineService.getCuisines());
    }


    @GetMapping("ingredients/{name}")
    @Operation(summary = "Get an ingredient by its name", responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved ingredient"), @ApiResponse(responseCode = "404", description = "The ingredient you were trying to reach is not found"), @ApiResponse(responseCode = "400", description = "Invalid ingredient name")})
    public ResponseEntity<IngredientsDTO> getIngredientByName(@Parameter(description = "Name of the ingredient to get", required = true) @PathVariable @Size(min = 3, max = 30, message = INGREDIENTS_SIZE_MESSAGE) @Pattern(regexp = "^(?! +$)[a-zA-Z]+([ -][a-zA-Z]+)*$", message = INGREDIENTS_PATTERN_MESSAGE) String name) {
        log.info("Searching for ingredient by name: {}", name);
        return ResponseEntity.ok(ingredientService.getIngredientByName(name));
    }

    @GetMapping("ingredients")
    @Operation(summary = "Get all ingredients", responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved list of ingredients")})
    public ResponseEntity<List<IngredientsDTO>> getIngredients() {
        return ResponseEntity.ok(ingredientService.getIngredients());
    }

    @Operation(summary = "Get a tag by name")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved tag"), @ApiResponse(responseCode = "400", description = "Invalid tag name provided"), @ApiResponse(responseCode = "404", description = "Tag not found")})
    @GetMapping("tags/{name}")
    public ResponseEntity<TagsDTO> getTagByName(@PathVariable @Pattern(regexp = "^(?! +$)[a-zA-Z]+([ -][a-zA-Z]+)*$", message = TAGS_PATTERN_MESSAGE) @Size(min = 2, max = 35, message = TAGS_SIZE_MESSAGE) String name) {
        log.info("Getting tag by name: {}", name);
        return ResponseEntity.ok(tagService.getTagByName(name));
    }

    @Operation(summary = "Get all tags")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved tags")})
    @GetMapping("tags")
    public ResponseEntity<List<TagsDTO>> getTags() {
        log.info("Getting all tags");
        return ResponseEntity.ok(tagService.getTags());
    }
    @GetMapping
    public ResponseEntity<List<RecipeResponseDTO>> getAllRecipe() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

}