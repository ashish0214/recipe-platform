package com.epam.recipe.platform.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "cuisines")
@Getter
@Setter
@NoArgsConstructor

public class CuisineEntity {
    @Id
    @Field(name = "_id")
    private String id;

    @Field(name = "cuisine_name")
    private String cuisineName;
}
