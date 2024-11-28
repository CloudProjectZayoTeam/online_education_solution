package cloud_project.dtos;

import cloud_project.entity.Etudiant;
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
public class EtudiantDto {
    private int id;
    private String name;
    private String email;
    private String motDePass;

    public static EtudiantDto  etudiantEntityToDto(Etudiant etudiant){
        return (etudiant==null)? null: EtudiantDto.builder()
                .id(etudiant.getId())
                .name(etudiant.getName())
                .email(etudiant.getEmail())
                .motDePass(etudiant.getMotDePass()).build();
    }
    public static Etudiant  etudiantEntityToDto(EtudiantDto etudiant){
        return (etudiant==null)? null: Etudiant.builder()
                .id(etudiant.getId())
                .name(etudiant.getName())
                .email(etudiant.getEmail())
                .motDePass(etudiant.getMotDePass()).build();
    }
}
