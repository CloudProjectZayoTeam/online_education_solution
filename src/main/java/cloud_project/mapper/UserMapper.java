package cloud_project.mapper;

import cloud_project.dtos.RegisterUserDto;
import cloud_project.dtos.UpdateProfileDto;
import cloud_project.dtos.UserProfileDto;
import cloud_project.entity.Role;
import cloud_project.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    UserProfileDto toUserProfileDto(User user);

    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    User toEntity(RegisterUserDto registerUserDto);

    void updateEntity(UpdateProfileDto updateProfileDto, @MappingTarget User user);

    @Named("mapRole")
    default Role mapRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}
