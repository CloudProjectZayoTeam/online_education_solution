package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "UpdateProfileDto", description = "DTO for updating user profile")
public class UpdateProfileDto {

    @Schema(description = "Name of the user", example = "Zouhir El Amraoui")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    private String name;

    @Schema(description = "Email address of the user", example = "zouhir@gmail.com")
    @Email(message = "Email should be valid")
    private String email;

    // Champs pour Professeur
    @Schema(description = "department", example = "MPI!")
    private String department;

    @Schema(description = "specialization", example = "Maths!")
    private String specialization;


    // Champs pour Étudiant
    @Schema(description = "major", example = "Maths!")
    private String major;

    @Schema(description = "yearOfStudy", example = "2!")
    private Integer yearOfStudy;

    @Schema(description = "Phone number of the user", example = "0123456789")
    @Pattern(regexp = "^[0-9]{10}$", message = "The phone number must be ten digits!")
    private String mobileNumber;
}
