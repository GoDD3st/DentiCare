package ma.dentalTech.repository.modules.auth.api;

import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.repository.common.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UtilisateurRepo extends CrudRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByLogin(String login) throws SQLException;
    Optional<Utilisateur> findByEmail(String email) throws SQLException;
    List<Role> findRolesByUserId(Long userId) throws SQLException;
}
