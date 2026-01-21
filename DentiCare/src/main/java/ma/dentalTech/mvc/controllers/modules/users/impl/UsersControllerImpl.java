package ma.dentalTech.mvc.controllers.modules.users.impl;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.mvc.controllers.modules.users.api.UsersController;
import ma.dentalTech.service.modules.auth.api.UtilisateurService;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsersControllerImpl implements UsersController {

    private final UtilisateurService userService;

    public UsersControllerImpl() {
        this.userService = ApplicationContext.getBean(UtilisateurService.class);
    }

    @Override
    public List<Utilisateur> getAllUsers() {
        try {
            return userService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération des utilisateurs: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }

    @Override
    public Optional<Utilisateur> getUserById(Long id) {
        try {
            return userService.findByID(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la récupération de l'utilisateur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    @Override
    public Utilisateur createUser(Utilisateur user) {
        try {
            return userService.create(user);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la création de l'utilisateur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utilisateur updateUser(Long id, Utilisateur user) {
        try {
            return userService.update(id, user);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            userService.deleteByID(id);
            JOptionPane.showMessageDialog(null,
                    "Utilisateur supprimé avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la suppression de l'utilisateur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<Utilisateur> searchUsers(String searchTerm) {
        try {
            List<Utilisateur> allUsers = userService.findAll();
            String lowerSearchTerm = searchTerm.toLowerCase();
            return allUsers.stream()
                    .filter(u -> {
                        return (u.getNom() != null && u.getNom().toLowerCase().contains(lowerSearchTerm)) ||
                               (u.getEmail() != null && u.getEmail().toLowerCase().contains(lowerSearchTerm)) ||
                               (u.getLogin() != null && u.getLogin().toLowerCase().contains(lowerSearchTerm));
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la recherche: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
}
