package ma.dentalTech.service.modules.auth.api;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;

public interface AuthorizationService {
    boolean hasPermission(UserPrincipal principal, String permission);
    boolean hasRole(UserPrincipal principal, String role);
    boolean canAccess(UserPrincipal principal, String resource);
}

