package com.epam.recipe.platform.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "tags")
@Getter
@Setter
@NoArgsConstructor
public class TagsEntity
{
    @Id
    @Field(name = "_id")
    private String id;

    @Field(name = "tags_name")
    private String tagName;
}
