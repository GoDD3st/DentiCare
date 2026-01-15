package ma.dentalTech.service.modules.auth.api;

import ma.dentalTech.mvc.dto.authentificationDtos.AuthRequest;
import ma.dentalTech.mvc.dto.authentificationDtos.AuthResult;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;

public interface AuthenticationService {
    AuthResult authenticate(AuthRequest request);
    void logout(UserPrincipal principal);
}

