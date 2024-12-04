package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "UserProfileDto", description = "DTO for displaying user profile information")
public class UserProfileDto {

    @Schema(description = "Name of the user", example = "Zouhir El Amraoui")
    private String name;

    @Schema(description = "Email address of the user", example = "zouhir@gmail.com")
    private String email;

    @Schema(description = "Role of the user", example = "ETUDIANT")
    private String role;

    @Schema(description = "Year of study (if applicable)", example = "3")
    private Integer yearOfStudy;

    @Schema(description = "Department (if applicable)", example = "Informatics")
    private String department;

    @Schema(description = "Specialization of the user", example = "Software Engineering")
    private String specialization;

    @Schema(description = "Major of the user", example = "Computer Science")
    private String major;

    @Schema(description = "Creation date of the user account", example = "2024-08-11T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date of the user profile", example = "2024-08-11T12:45:00")
    private LocalDateTime updatedAt;
}
