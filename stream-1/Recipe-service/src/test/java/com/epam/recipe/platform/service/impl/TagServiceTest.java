package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.dto.TagsDTO;
import com.epam.recipe.platform.entity.TagsEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    TagsEntity tagsEntity;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository);
        tagsEntity = new TagsEntity();
        tagsEntity.setId("123");
    }

    @Test
    void testGetTagByName()  {
        String tagName = "Italian";
        tagsEntity.setTagName(tagName);
        when(tagRepository.findByTagNameIgnoreCase(tagName)).thenReturn(Optional.of(tagsEntity));

        TagsDTO tag = tagService.getTagByName(tagName);

        assertEquals(tagName, tag.getTagName());
    }

    @Test
    void testGetTagByNameNotFound() {
        String tagName = "NonExistentTag";
        when(tagRepository.findByTagNameIgnoreCase(tagName)).thenReturn(Optional.empty());

        assertThrows(RecipeException.class, () -> tagService.getTagByName(tagName));
    }

    @Test
    void testGetTags() {
        TagsEntity tagEntity1 = new TagsEntity();
        tagEntity1.setTagName("Italian");
        TagsEntity tagEntity2 = new TagsEntity();
        tagEntity2.setTagName("Mexican");
        when(tagRepository.findAll()).thenReturn(Arrays.asList(tagEntity1, tagEntity2));

        List<TagsDTO> tags = tagService.getTags();

        assertEquals(2, tags.size());
        assertEquals("Italian", tags.get(0).getTagName());
        assertEquals("Mexican", tags.get(1).getTagName());
    }

    @Test
    void testGetTagsWhenNoTags() {
        when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        List<TagsDTO> tags = tagService.getTags();

        assertEquals(0, tags.size());
    }


}
