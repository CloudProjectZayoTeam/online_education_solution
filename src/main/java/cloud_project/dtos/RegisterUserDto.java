package cloud_project.dtos;

import cloud_project.entity.Role;
import cloud_project.utils.ConditionalValidation;
import cloud_project.utils.PasswordsMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@PasswordsMatch
@Schema(
        name = "RegisterUserDto",
        description = "DTO for user registration with password confirmation"
)
public class RegisterUserDto {

    @Schema(description = "Name of the user", example = "Zouhir")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    private String name;

    @Schema(description = "Email address of the user", example = "zouhir@gmail.com")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "mobile number for the user", example = "06 06 06 06 06!")
    @NotEmpty(message = "mobile number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "The phone number must be ten digits!")
    private String mobileNumber;

    @Schema(description = "Password for the user", example = "Password123!")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}",
            message = "Password must contain at least one digit, one lower case letter, one upper case letter, and one special character")
    private String password;

    @Schema(description = "Password confirmation for the user", example = "Password123!")
    @NotEmpty(message = "Password confirmation cannot be empty")
    private String confirmPassword;


    @Setter
    @Getter
    @NotNull(message = "Le rôle ne peut pas être vide")
    private Role role;


    @ConditionalValidation(
            field = "role",
            value = "PROF",
            message = "Le département est requis pour un professeur"
    )
    private String department;

    @ConditionalValidation(
            field = "role",
            value = "PROF",
            message = "La spécialisation est requise pour un professeur"
    )
    private String specialization;


    @ConditionalValidation(
            field = "role",
            value = "ETUDIANT",
            message = "La filière est requise pour un étudiant"
    )
    private String major;

    @ConditionalValidation(
            field = "role",
            value = "ETUDIANT",
            message = "L'année d'étude est requise pour un étudiant"
    )
    private Integer yearOfStudy;


}

