package com.epam.recipe.platform.service.impl;

import com.epam.recipe.platform.dto.CuisineDTO;
import com.epam.recipe.platform.entity.CuisineEntity;
import com.epam.recipe.platform.handler.exception.RecipeException;
import com.epam.recipe.platform.repository.CuisineRepository;
import com.epam.recipe.platform.service.CuisineService;
import com.epam.recipe.platform.utility.CuisineMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CuisineServiceImpl implements CuisineService {

    private final CuisineRepository cuisineRepository;

    @Override
    public CuisineDTO getCuisineByName(String name)  {
        log.info("Searching for cuisine by name: {}", name);
        CuisineEntity cuisineEntity = cuisineRepository.findByCuisineNameIgnoreCase(name).orElseThrow(() -> {
            log.error("No cuisine found with name: {}", name);
            return new RecipeException("Cuisine not found", HttpStatus.NOT_FOUND);
        });

        log.info("Found cuisine with name: {}", name);
        return CuisineMapper.INSTANCE.cuisineEntityToDTO(cuisineEntity);
    }

    @Override
    public List<CuisineDTO> getCuisines() {
        log.info("Fetching all cuisines");
        return cuisineRepository.findAll().stream().map(CuisineMapper.INSTANCE::cuisineEntityToDTO).collect(Collectors.toList());
    }

}
