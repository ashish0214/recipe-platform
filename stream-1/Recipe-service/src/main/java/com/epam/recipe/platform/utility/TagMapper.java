package com.epam.recipe.platform.utility;


import com.epam.recipe.platform.dto.TagsDTO;
import com.epam.recipe.platform.entity.TagsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper
{
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagsDTO tagEntityToDTO(TagsEntity tagsEntity);

}
