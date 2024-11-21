package com.epam.recipe.platform.repository;

import com.epam.recipe.platform.entity.IngredientsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IngredientRepository extends MongoRepository<IngredientsEntity, String>
{
    Optional<IngredientsEntity> findByIngredientNameIgnoreCase(String name);
}
