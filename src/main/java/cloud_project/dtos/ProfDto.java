package cloud_project.dtos;

import cloud_project.entity.Prof;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Arejdal
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfDto {
    private int id;
    private String name;
    private String email;
    private String motDePass;
    public static ProfDto  profEntityToDto(Prof prof){
        return (prof==null)? null: ProfDto.builder()
                .id(prof.getId())
                .name(prof.getName())
                .email(prof.getEmail())
                .motDePass(prof.getMotDePass()).build();
    }

    public static Prof  profDtoToEntity(ProfDto prof){
        return (prof==null)? null: Prof.builder()
                .id(prof.getId())
                .name(prof.getName())
                .email(prof.getEmail())
                .motDePass(prof.getMotDePass()).build();
    }

}
