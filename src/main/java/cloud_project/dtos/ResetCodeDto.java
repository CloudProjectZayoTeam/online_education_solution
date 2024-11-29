package cloud_project.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "ResetCodeDto",
        description = "DTO for verifying reset code functionality"
)
public class ResetCodeDto {

    @Schema(description = "Reset code sent to the user", example = "1234")
    @NotEmpty(message = "Reset code cannot be empty")
    @Size(min = 4, max = 4, message = "Reset code must be 4 digits")
    private String code;

    @Schema(description = "Email address of the user", example = "zouhir@gmail.com")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
}

