package ru.tbcarus.jrfinal.model.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.tbcarus.jrfinal.model.Role;
import ru.tbcarus.jrfinal.model.User;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {Set.class, Role.class})
public interface UserRegisterMapper {

    @Mapping(target = "roles", expression = "java(Set.of(Role.USER))")
    @Mapping(target = "email", expression = "java(dto.getEmail() != null ? dto.getEmail().toLowerCase() : null)")
    User toUser(UserRegisterDto dto);

    UserRegisterDto toUserRegisterDto(User user);
}