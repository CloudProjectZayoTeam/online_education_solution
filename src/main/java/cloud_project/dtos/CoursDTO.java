package cloud_project.dtos;

import cloud_project.entity.TypeCours;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoursDTO {
    private Long id;

    @NotBlank(message = "Le titre ne peut pas être vide")
    private String titre;

    private String description;

    @NotNull(message = "Le type de cours est obligatoire")
    private TypeCours typeCours;

    private String fichierUrl;
}