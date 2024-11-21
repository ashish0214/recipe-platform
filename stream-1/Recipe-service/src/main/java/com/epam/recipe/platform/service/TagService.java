package com.epam.recipe.platform.service;

import com.epam.recipe.platform.dto.TagsDTO;
import org.webjars.NotFoundException;

import java.util.List;

public interface TagService {
    TagsDTO getTagByName(String name) ;

    List<TagsDTO> getTags();
}
