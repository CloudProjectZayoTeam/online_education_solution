package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(
        name = "ForgotPasswordDto",
        description = "DTO for forgot password functionality"
)
public class ForgotPasswordDto {

    @Schema(description = "Email address of the user", example = "zouhir@gmail.com")
    @NotBlank(message = "Email cannot be blank")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
}
