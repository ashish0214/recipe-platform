package com.epam.recipe.platform.utility;

import com.epam.recipe.platform.dto.CuisineDTO;
import com.epam.recipe.platform.entity.CuisineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CuisineMapper {

    CuisineMapper INSTANCE = Mappers.getMapper(CuisineMapper.class);

    CuisineDTO cuisineEntityToDTO(CuisineEntity cuisineEntity);
}
