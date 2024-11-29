package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(name = "ImageUploadDto", description = "DTO for Uploading user Image")
public class ImageUploadDto {

    @Schema(description = "Uploading image file", example = "file.png")
    private MultipartFile file;
}
