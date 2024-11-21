package com.epam.recipe.platform.repository;

import com.epam.recipe.platform.entity.TagsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TagRepository extends MongoRepository<TagsEntity, String>
{
    Optional<TagsEntity> findByTagNameIgnoreCase(String name);
}
