package ma.dentalTech.mvc.dto.authentificationDtos;

import ma.dentalTech.entities.enums.RoleEnum;
import java.util.List;
import java.util.Set;

public record UserPrincipal(
    Long id,
    String nom,
    String email,
    String login,
    RoleEnum rolePrincipal,
    List<RoleEnum> roles,
    Set<String> privileges
) {
    public UserPrincipal {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        if (nom == null) nom = "";
        if (email == null) email = "";
        if (login == null) login = "";
    }
}

