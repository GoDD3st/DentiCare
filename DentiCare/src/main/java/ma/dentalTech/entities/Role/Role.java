package ma.dentalTech.entities.Role;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.entities.enums.RoleEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {
    private Long idRole;
    private RoleEnum libelle;
    private List<String> privileges;

    private List<Utilisateur> utilisateurs; // Relation n--n
}