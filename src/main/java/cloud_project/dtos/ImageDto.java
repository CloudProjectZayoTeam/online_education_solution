package cloud_project.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ImageDto", description = "DTO for displaying user Image information")
public class ImageDto {

    @Schema(description = "Name of user Image", example = "image.png")
    private String fileName;

    @Schema(description = "Type of the user Image", example = "png or jpeg etc ...")
    private String fileType;

    @Schema(description = "Bytes of the user image")
    private byte[] data;
}
