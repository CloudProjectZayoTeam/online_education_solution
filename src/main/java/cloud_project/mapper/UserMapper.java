package cloud_project.mapper;

import cloud_project.dtos.RegisterUserDto;
import cloud_project.dtos.UpdateProfileDto;
import cloud_project.dtos.UserProfileDto;
import cloud_project.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterUserDto registerUserDto);

    void updateEntity(UpdateProfileDto updateProfileDto, @MappingTarget User user);

    UserProfileDto toUserProfileDto(User user);
}
