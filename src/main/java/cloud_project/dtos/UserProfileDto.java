package cloud_project.dtos;

import cloud_project.entity.User;
import cloud_project.repository.UserRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "UserProfileDto", description = "DTO for displaying user profile information")
public class UserProfileDto {

    UserRepository UserRepository;

    @Schema(description = "Name of the user", example = "Zouhir El Amraoui")
    private String name;

    @Schema(description = "Email address of the user", example = "zouhir@gmail.com")
    private String email;

    @Schema(description = "Creation date of the user account", example = "2024-08-11T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date of the user profile", example = "2024-08-11T12:45:00")
    private LocalDateTime updatedAt;

    User user = UserRepository.findByEmail(email);


}
