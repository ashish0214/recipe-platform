package com.epam.recipe.platform.utils;

import com.epam.recipe.platform.dto.request.UserDTO;
import com.epam.recipe.platform.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity userDTOToUser(UserDTO userDTO);
}