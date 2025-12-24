package ma.dentalTech.entities.Admin;

import lombok.experimental.SuperBuilder;
import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import lombok.Data;
import ma.dentalTech.entities.enums.RoleEnum;

import java.util.List;

@Data
@SuperBuilder
public class Admin extends Utilisateur {
    public static Admin createTestInstance() {
        return Admin.builder()
                .nom("admin 1")
                .login("admin")
                .motDePass("admin")
                .roles(List.of(Role.builder()
                        .idRole(1L)
                        .libelle(RoleEnum.ADMIN)
                        .build()))
                .build();
    }
}