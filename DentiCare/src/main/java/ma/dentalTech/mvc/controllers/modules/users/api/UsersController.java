package ma.dentalTech.mvc.controllers.modules.users.api;

import ma.dentalTech.entities.Utilisateur.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UsersController {
    List<Utilisateur> getAllUsers();
    Optional<Utilisateur> getUserById(Long id);
    Utilisateur createUser(Utilisateur user);
    Utilisateur updateUser(Long id, Utilisateur user);
    void deleteUser(Long id);
    List<Utilisateur> searchUsers(String searchTerm);
}
