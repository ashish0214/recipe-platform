package com.epam.recipe.platform.constants;

public class RecipeDTOValidationConstants {


    // Recipe name validations
    public static final String RECIPE_NAME_NOT_NULL_MESSAGE = "Recipe name should not be null.";
    public static final String RECIPE_NAME_SIZE_MESSAGE = "Recipe name must be between 2 and 50 characters.";
    public static final String RECIPE_NAME_PATTERN_MESSAGE = "Recipe name should only contain letters, hyphen, numbers, and spaces, and should not start with a number.";

    // Recipe description validations
    public static final String RECIPE_DESCRIPTION_NOT_NULL_MESSAGE = "Recipe description should not be null.";
    public static final String RECIPE_DESCRIPTION_SIZE_MESSAGE = "Recipe description must be between 20 and 5000 characters.";

    // Recipe cuisine validations
    public static final String RECIPE_CUISINE_NOT_NULL_MESSAGE = "Recipe cuisine should not be null.";
    public static final String RECIPE_CUISINE_SIZE_MESSAGE = "Recipe cuisine must be between 2 and 20 characters.";
    public static final String RECIPE_CUISINE_PATTERN_MESSAGE = "Recipe cuisine should only contain letters, hyphen, and spaces.";

    // Recipe category validations
    public static final String RECIPE_CATEGORY_NOT_NULL_MESSAGE = "Recipe category should not be null.";
    public static final String RECIPE_CATEGORY_SIZE_MESSAGE = "Recipe category must be between 2 and 20 characters.";
    public static final String RECIPE_CATEGORY_PATTERN_MESSAGE = "Recipe category should only contain letters, hyphens, and spaces.";

    // Tags validations
    public static final String TAGS_NOT_EMPTY_MESSAGE = "Tags should not be null.";

    // Ingredients validations
    public static final String INGREDIENTS_NOT_EMPTY_MESSAGE = "Ingredients should not be Empty.";

    // Cooking time validations
    public static final String COOKING_TIME_NOT_NULL_MESSAGE = "Cooking time should not be null.";
    public static final String COOKING_TIME_MIN_MESSAGE = "Cooking time should be greater than or equal to 1.";
    public static final String COOKING_TIME_MAX_MESSAGE = "Cooking time should be less than or equal to 180.";

    // Difficulty validations
    public static final String DIFFICULTY_NOT_NULL_MESSAGE = "Difficulty should not be null.";
    public static final String DIFFICULTY_SIZE_MESSAGE = "Recipe difficulty must be between 2 and 50 characters.";
    public static final String DIFFICULTY_PATTERN_MESSAGE = "Difficulty should only contain letters.";

    // Additional fields
    public static final String TAGS_SIZE_MESSAGE = "Tag name should be between 2 and 35 characters.";
    public static final String TAGS_PATTERN_MESSAGE = "Tag name should contain only alphabets, spaces, and hyphen.";

    public static final String INGREDIENTS_SIZE_MESSAGE = "Ingredient name must be between 2 and 50 characters.";
    public static final String INGREDIENTS_PATTERN_MESSAGE = "Ingredient should contain only alphabets, space, and hyphen.";

    private RecipeDTOValidationConstants() {
        throw new AssertionError("This class should not be instantiated.");
    }
}
