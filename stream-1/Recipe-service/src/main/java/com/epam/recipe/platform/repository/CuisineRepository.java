package com.epam.recipe.platform.repository;

import com.epam.recipe.platform.entity.CuisineEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CuisineRepository extends MongoRepository<CuisineEntity, String>{

    Optional<CuisineEntity> findByCuisineNameIgnoreCase(String name);
}
