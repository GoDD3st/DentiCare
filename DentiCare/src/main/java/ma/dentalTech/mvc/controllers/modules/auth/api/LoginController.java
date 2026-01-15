package ma.dentalTech.mvc.controllers.modules.auth.api;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;

public interface LoginController {
    void onLoginRequested(String login, String password);
    void onLoginSuccess(UserPrincipal principal);
    void onLoginFailure(String errorMessage);
}

