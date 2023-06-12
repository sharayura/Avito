package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skypro.homework.dto.RegisterReqDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toUser(RegisterReqDto registerReqDto);

    @Mapping(ignore = true, target = "userDto.image")
    @Mapping(source = "username", target = "email")
    void toUserDto(@MappingTarget UserDto userDto, User user);

    @Mapping(ignore = true, target = "user.id")
    @Mapping(ignore = true, target = "user.image")
    @Mapping(ignore = true, target = "user.username")
    void toUser(@MappingTarget User user, UserDto userDto);


}
