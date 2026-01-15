package ma.dentalTech.service.modules.auth.impl;

import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.security.Privileges;
import ma.dentalTech.service.modules.auth.api.AuthorizationService;

public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public boolean hasPermission(UserPrincipal principal, String permission) {
        if (principal == null || permission == null) return false;
        return principal.privileges().contains(permission) || 
               principal.rolePrincipal() == RoleEnum.ADMIN;
    }

    @Override
    public boolean hasRole(UserPrincipal principal, String role) {
        if (principal == null || role == null) return false;
        try {
            RoleEnum roleEnum = RoleEnum.valueOf(role.trim().toUpperCase());
            return principal.roles().contains(roleEnum) ||
                   principal.rolePrincipal() == roleEnum;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean canAccess(UserPrincipal principal, String resource) {
        if (principal == null || resource == null) return false;
        // Par défaut, les admins ont accès à tout
        if (principal.rolePrincipal() == RoleEnum.ADMIN) return true;
        
        // Mapping des ressources vers les privilèges
        String privilege = mapResourceToPrivilege(resource);
        if (privilege != null) {
            return hasPermission(principal, privilege);
        }
        
        // Par défaut, accès refusé si aucune correspondance
        return false;
    }
    
    /**
     * Mappe une ressource (nom de page/module) vers un privilège
     */
    private String mapResourceToPrivilege(String resource) {
        return switch (resource.toUpperCase()) {
            case "USERS", "GESTION_UTILISATEURS" -> Privileges.USERS_ACCESS;
            case "CABINET", "CABINETS", "GESTION_CABINET" -> Privileges.CABINET_ACCESS;
            case "PATIENTS", "GESTION_PATIENTS" -> Privileges.PATIENT_ACCESS;
            case "DOSSIERS", "DOSSIERS_MEDICAUX", "GERER_DOSSIERS" -> Privileges.DOSSIER_ACCESS;
            case "RDV", "RENDEZ_VOUS", "GERER_RDV" -> Privileges.RDV_ACCESS;
            case "CAISSE", "GESTION_CAISSE" -> Privileges.CAISSE_ACCESS;
            case "AGENDA", "AGENDA_MEDECIN", "GERER_AGENDA_MEDECIN" -> Privileges.AGENDA_ACCESS;
            default -> null;
        };
    }
}

