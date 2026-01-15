package ma.dentalTech.service.modules.auth.impl;

import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.dto.authentificationDtos.AuthRequest;
import ma.dentalTech.mvc.dto.authentificationDtos.AuthResult;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.repository.modules.auth.api.UtilisateurRepo;
import ma.dentalTech.service.modules.auth.api.AuthenticationService;

import java.util.*;
import java.util.stream.Collectors;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UtilisateurRepo utilisateurRepo;

    public AuthenticationServiceImpl(UtilisateurRepo utilisateurRepo) {
        this.utilisateurRepo = utilisateurRepo;
    }

    @Override
    public AuthResult authenticate(AuthRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        
        // Validation des champs
        if (request.login() == null || request.login().trim().isEmpty()) {
            fieldErrors.put("login", "Le login est requis");
        }
        if (request.password() == null || request.password().isEmpty()) {
            fieldErrors.put("password", "Le mot de passe est requis");
        }
        
        // Si des erreurs de validation, retourner immédiatement
        if (!fieldErrors.isEmpty()) {
            return AuthResult.failure("Veuillez corriger les erreurs", fieldErrors);
        }

        try {
            Optional<Utilisateur> userOpt = utilisateurRepo.findByLogin(request.login().trim());
            if (userOpt.isEmpty()) {
                return AuthResult.failure("Identifiant ou mot de passe incorrect");
            }

            Utilisateur user = userOpt.get();
            
            // Vérification du mot de passe
            if (!request.password().equals(user.getMotDePass()) && !checkPassword(request.password(), user.getMotDePass())) {
                return AuthResult.failure("Identifiant ou mot de passe incorrect");
            }

            // Récupérer les rôles de l'utilisateur
            List<Role> roles = utilisateurRepo.findRolesByUserId(user.getIdUser());
            List<RoleEnum> roleEnums = roles.stream()
                    .map(Role::getLibelle)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Déterminer le rôle principal (premier rôle ou ADMIN par défaut)
            RoleEnum rolePrincipal = roleEnums.isEmpty() ? RoleEnum.ADMIN : roleEnums.get(0);

            // Collecter tous les privilèges
            Set<String> privileges = roles.stream()
                    .filter(r -> r.getPrivileges() != null)
                    .flatMap(r -> r.getPrivileges().stream())
                    .collect(Collectors.toSet());

            UserPrincipal principal = new UserPrincipal(
                    user.getIdUser(),
                    user.getNom(),
                    user.getEmail(),
                    user.getLogin(),
                    rolePrincipal,
                    roleEnums,
                    privileges
            );

            return AuthResult.success(principal);
        } catch (Exception e) {
            return AuthResult.failure("Une erreur est survenue lors de l'authentification: " + e.getMessage());
        }
    }

    @Override
    public void logout(UserPrincipal principal) {
        // TODO: Implémenter la logique de déconnexion si nécessaire
        // (ex: invalider la session, logger l'événement, etc.)
    }

    // Méthode simple pour vérifier le mot de passe (support BCrypt et plain text)
    private boolean checkPassword(String plainPassword, String hashedPassword) {
        // Si le mot de passe commence par $2a$ ou $2b$, c'est du BCrypt
        if (hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$")) {
            // TODO: Utiliser BCrypt.checkpw() si disponible
            // Pour l'instant, comparaison simple
            return plainPassword.equals(hashedPassword);
        }
        // Sinon, comparaison directe (pour les mots de passe en clair dans seed.sql)
        return plainPassword.equals(hashedPassword);
    }
}

