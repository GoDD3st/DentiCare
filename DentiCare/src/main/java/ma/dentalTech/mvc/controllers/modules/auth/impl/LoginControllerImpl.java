package ma.dentalTech.mvc.controllers.modules.auth.impl;

import ma.dentalTech.mvc.controllers.modules.auth.api.LoginController;
import ma.dentalTech.mvc.dto.authentificationDtos.AuthRequest;
import ma.dentalTech.mvc.dto.authentificationDtos.AuthResult;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.frames.DashboardUI;
import ma.dentalTech.mvc.ui.frames.LoginUI;
import ma.dentalTech.service.modules.auth.api.AuthenticationService;
import ma.dentalTech.service.modules.auth.api.AuthorizationService;

import javax.swing.*;

public class LoginControllerImpl implements LoginController {

    private LoginUI loginUI;
    private final AuthenticationService authenticationService;
    private final AuthorizationService authorizationService;

    public LoginControllerImpl(LoginUI loginUI,
                               AuthenticationService authenticationService,
                               AuthorizationService authorizationService) {
        this.loginUI = loginUI;
        this.authenticationService = authenticationService;
        this.authorizationService = authorizationService;
    }

    public void setLoginUI(LoginUI loginUI) {
        this.loginUI = loginUI;
    }

    @Override
    public void onLoginRequested(String login, String password) {
        AuthRequest request = new AuthRequest(login, password);
        AuthResult result = authenticationService.authenticate(request);
        
        if (result.isSuccess()) {
            onLoginSuccess(result.getPrincipal());
        } else {
            // Afficher les erreurs de champs si présentes
            if (!result.getFieldErrors().isEmpty()) {
                loginUI.showFieldErrors(result.getFieldErrors());
            }
            // Afficher le message d'erreur global
            onLoginFailure(result.getMessage());
        }
    }

    @Override
    public void onLoginSuccess(UserPrincipal principal) {
        // Fermer la fenêtre de login
        loginUI.setVisible(false);
        loginUI.dispose();

        // Créer et afficher le dashboard
        SwingUtilities.invokeLater(() -> {
            try {
                ma.dentalTech.mvc.controllers.dashboardModule.api.DashboardController dashboardController =
                        new ma.dentalTech.mvc.controllers.dashboardModule.impl.DashboardControllerImpl(principal);
                DashboardUI dashboard = new DashboardUI(
                        dashboardController,
                        authorizationService,
                        principal
                );
                dashboard.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur lors de l'ouverture du dashboard: " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                loginUI.setVisible(true);
            }
        });
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        loginUI.showGlobalError(errorMessage);
    }
}

