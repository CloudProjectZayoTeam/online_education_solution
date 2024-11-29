package cloud_project.dtos;

import cloud_project.utils.PasswordsMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(
        name = "NewPasswordDto",
        description = "DTO for setting new password functionality"
)
@PasswordsMatch
public class NewPasswordDto {

    @Schema(description = "New password for the user", example = "newStrongPassword123")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}",
            message = "Password must contain at least one digit, one lower case letter, one upper case letter, and one special character")
    private String password;

    @Schema(description = "Confirm new password", example = "newStrongPassword123")
    @NotEmpty(message = "Confirm Password cannot be empty")
    private String confirmPassword;

    @Schema(description = "Email address of the user", example = "zouhir@gmail.com")
    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
}
