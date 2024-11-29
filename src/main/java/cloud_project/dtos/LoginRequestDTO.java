package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "LoginRequest",
        description = "Schema representing the login request with email and password"
)
public class LoginRequestDTO {

    @Schema(
            description = "Email address of the user", example = "zouhir@gmail.com"
    )
    @NotBlank(message = "Email cannot be blank")
    @NotEmpty(message = "Email address cannot be null or empty")
    @Email(message = "Email address should be a valid email format")
    private String email;

    @Schema(
            description = "Password of the user", example = "Password@123"
    )
    @NotBlank(message = "The Password cannot be blank")
    @NotEmpty(message = "Password cannot be null or empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;
}
