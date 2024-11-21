package com.epam.recipe.platform.service;

import com.epam.recipe.platform.dto.CuisineDTO;

import java.util.List;

public interface CuisineService {
    CuisineDTO getCuisineByName(String name) ;
    List<CuisineDTO> getCuisines();
}
