package cloud_project.dtos;

import cloud_project.utils.PasswordsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordsMatch
public class ChangePasswordDto {

    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 50, message = "New password must be between 8 and 50 characters")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}",
            message = "Password must contain at least one digit, one lower case letter, one upper case letter, and one special character")
    private String newPassword;

    @NotBlank(message = "Confirm new password is required")
    private String confirmNewPassword;
}
