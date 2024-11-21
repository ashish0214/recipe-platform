package com.epam.recipe.platform.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ingredients")
@Getter
@Setter
@NoArgsConstructor
public class IngredientsEntity
{
    @Id
    @Field(name = "_id")
    private String ingredientId;

    @Field(name = "ingredient_name")
    private String ingredientName;
}
