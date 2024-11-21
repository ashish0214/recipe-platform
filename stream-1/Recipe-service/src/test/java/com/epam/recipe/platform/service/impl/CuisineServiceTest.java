package com.epam.recipe.platform.service.impl;


import com.epam.recipe.platform.dto.CuisineDTO;
import com.epam.recipe.platform.entity.CuisineEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.CuisineRepository;
import com.epam.recipe.platform.utility.CuisineMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuisineServiceTest {
    @Mock
    private CuisineRepository cuisineRepository;

    @Mock
    private CuisineMapper cuisineMapper;

    @InjectMocks
    private CuisineServiceImpl cuisineService;


    @Test
    void testGetCuisineByName_Success()  {

        String cuisineName = "Italian";
        CuisineEntity cuisineEntity = new CuisineEntity();
        cuisineEntity.setId("1");
        cuisineEntity.setCuisineName(cuisineName);

        CuisineDTO expectedDTO = new CuisineDTO();
        expectedDTO.setId("1");
        expectedDTO.setCuisineName(cuisineName);
        when(cuisineRepository.findByCuisineNameIgnoreCase(cuisineName)).thenReturn(Optional.of(cuisineEntity));
        CuisineDTO resultDTO = cuisineService.getCuisineByName(cuisineName);
        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getId(), resultDTO.getId());
        assertEquals(expectedDTO.getCuisineName(), resultDTO.getCuisineName());
        verify(cuisineRepository, times(1)).findByCuisineNameIgnoreCase(cuisineName);
    }

    @Test
    void testGetCuisineByName_NotFound() {
        String cuisineName = "Indian";

        when(cuisineRepository.findByCuisineNameIgnoreCase(cuisineName)).thenReturn(Optional.empty());

        RecipeException exception = assertThrows(RecipeException.class, () -> {
            cuisineService.getCuisineByName(cuisineName);
        });
        assertEquals("Cuisine not found", exception.getMessage());

        verify(cuisineRepository, times(1)).findByCuisineNameIgnoreCase(cuisineName);
    }

    @Test
    void testGetCuisines() {
        // Mock data
        CuisineEntity cuisine1 = new CuisineEntity();
        cuisine1.setId("1");
        cuisine1.setCuisineName("Italian");

        CuisineEntity cuisine2 = new CuisineEntity();
        cuisine2.setId("2");
        cuisine2.setCuisineName("Chinese");

        List<CuisineEntity> cuisineEntities = Arrays.asList(cuisine1, cuisine2);

        CuisineDTO cuisineDTO1 = new CuisineDTO();
        cuisineDTO1.setId("1");
        cuisineDTO1.setCuisineName("Italian");

        CuisineDTO cuisineDTO2 = new CuisineDTO();
        cuisineDTO2.setId("2");
        cuisineDTO2.setCuisineName("Chinese");

        List<CuisineDTO> expectedDTOs = Arrays.asList(cuisineDTO1, cuisineDTO2);

        when(cuisineRepository.findAll()).thenReturn(cuisineEntities);

        List<CuisineDTO> resultDTOs = cuisineService.getCuisines();

        assertNotNull(resultDTOs);
        assertEquals(expectedDTOs.size(), resultDTOs.size());
        assertEquals(expectedDTOs.get(0).getId(), resultDTOs.get(0).getId());
        assertEquals(expectedDTOs.get(0).getCuisineName(), resultDTOs.get(0).getCuisineName());
        assertEquals(expectedDTOs.get(1).getId(), resultDTOs.get(1).getId());
        assertEquals(expectedDTOs.get(1).getCuisineName(), resultDTOs.get(1).getCuisineName());
        verify(cuisineRepository, times(1)).findAll();
    }
}
