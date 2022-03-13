package com.linguatool.other.mapper;

import com.linguatool.linguatool.other.persistence.dto.UserDto;
import com.linguatool.linguatool.other.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.convert.DefaultTypeMapper;

import java.time.LocalDateTime;

@Mapper(config = MapperConfiguration.class, uses = DefaultTypeMapper.class)
public interface UserMapper {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "username", source = "dto.username")
    @Mapping(target = "registeredDatetime", source = "registeredDatetime")
    UserDto entityToDto(UserEntity source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "registeredDatetime", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "username", source = "dto.username")
    @Mapping(target = "password", source = "password")
    void updateUser(@MappingTarget UserEntity entity, String password, UserDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "username", source = "dto.username")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "registeredDatetime", source = "registeredDatetime")
    UserEntity dtoToEntity(UserDto dto, String password, LocalDateTime registeredDatetime);
}
