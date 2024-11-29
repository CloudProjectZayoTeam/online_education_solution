package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "LoginResponseDto",
        description = "DTO for user login response"
)
public class LoginResponseDto {

    @Schema(description = "Status of the login attempt", example = "Success")
    private String status;

    @Schema(description = "JWT token for the user", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String jwtToken;
}
