package com.epam.recipe.platform.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "recipes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class RecipeEntity {
    @Id
    private String recipeId;

    @Field(name = "recipe_name")
    private String recipeName;

    @Field(name = "image_token")
    private String imageToken;

    @Field(name = "recipe_description")
    private String recipeDescription;

    @Field(name = "cuisine")
    private String cuisine;

    @Field(name = "category")
    private String category;

    @Field(name = "tags")
    private List<String> tags;

    @Field(name = "ingredients")
    private List<String> ingredients;

    @Field(name = "cooking_time_minutes")
    private Integer cookingTime;

    @Field(name = "difficulty")
    private String difficulty;

    @Field(name = "instructions")
    private String dietaryRestrictions;

    @Field(name = "reviews")
    private List<String> reviews;

    @Field(name = "ratings")
    private Double ratings;

    @Field(name = "count_of_ratings")
    private Integer countOfRatings;
}
