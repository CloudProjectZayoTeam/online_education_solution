package cloud_project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerifyUpdateDto {

    @NotBlank(message = "Verification code is required")
    private String verificationCode;
}
