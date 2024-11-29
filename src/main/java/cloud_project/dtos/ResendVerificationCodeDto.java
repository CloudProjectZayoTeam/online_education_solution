package cloud_project.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResendVerificationCodeDto {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
