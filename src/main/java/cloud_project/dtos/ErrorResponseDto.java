package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema for error responses"
)
public class ErrorResponseDto {

    @Schema(
            description = "API path invoked by the client"
    )
    private String apiPath;

    @Schema(
            description = "HTTP status code representing the error"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message describing the problem"
    )
    private String errorMessage;

    @Schema(
            description = "Timestamp of when the error occurred"
    )
    private LocalDateTime errorTime;
}
