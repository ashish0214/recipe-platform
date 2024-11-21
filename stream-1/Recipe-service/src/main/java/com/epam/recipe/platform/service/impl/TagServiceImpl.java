package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.dto.TagsDTO;
import com.epam.recipe.platform.entity.TagsEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.TagRepository;
import com.epam.recipe.platform.service.TagService;
import com.epam.recipe.platform.utility.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagsDTO getTagByName(String name)  {
        log.info("Searching for tag by name: {}", name);
        TagsEntity tagsEntity = tagRepository.findByTagNameIgnoreCase(name).orElseThrow(() -> {
            log.error("No tag found with name: {}", name);
            return new RecipeException("Tag not found", HttpStatus.NOT_FOUND);
        });

        log.info("Found tag with name: {}", name);
        return TagMapper.INSTANCE.tagEntityToDTO(tagsEntity);
    }

    @Override
    public List<TagsDTO> getTags() {
        log.info("Fetching all tags");
        return tagRepository.findAll().stream().map(TagMapper.INSTANCE::tagEntityToDTO).collect(Collectors.toList());
    }

}
