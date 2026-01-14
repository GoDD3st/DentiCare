package ma.dentalTech.entities.Role;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.entities.enums.RoleEnum;

import java.util.Arrays;
import java.util.List;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class Role extends BaseEntity {
    private Long idRole;
    private RoleEnum libelle;
    private List<String> privileges;

    private List<Utilisateur> utilisateurs; // Relation n--n

    public static Role createTestInstance() {
        return Role.builder()
                .libelle(RoleEnum.ADMIN)
                .privileges(Arrays.asList("FullPower"))
                .build();
    }
}