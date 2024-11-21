package com.epam.recipe.platform.repository;

import com.epam.recipe.platform.entity.RecipeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<RecipeEntity, String> {
}
