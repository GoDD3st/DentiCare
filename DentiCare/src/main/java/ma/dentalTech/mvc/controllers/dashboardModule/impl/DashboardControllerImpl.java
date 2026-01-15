package ma.dentalTech.mvc.controllers.dashboardModule.impl;

import ma.dentalTech.mvc.controllers.dashboardModule.api.DashboardController;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.dashboardPages.pageFactory.DashboardPanelFactory;
import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;
import ma.dentalTech.service.modules.auth.api.AuthenticationService;
import ma.dentalTech.service.modules.auth.api.AuthorizationService;
import ma.dentalTech.conf.ApplicationContext;

import javax.swing.*;

public class DashboardControllerImpl implements DashboardController {

    private final UserPrincipal principal;

    public DashboardControllerImpl(UserPrincipal principal) {
        this.principal = principal;
    }

    @Override
    public JComponent onNavigateRequested(ApplicationPages page) {
        return DashboardPanelFactory.createPanel(page, principal);
    }

    @Override
    public void onLogoutRequested() {
        // Fermer toutes les fenêtres
        for (var window : java.awt.Window.getWindows()) {
            window.dispose();
        }

        // Redémarrer le processus de login comme dans Main.java
        SwingUtilities.invokeLater(() -> {
            try {
                // Configuration du Look and Feel
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du Look and Feel: " + e.getMessage());
            }

            // Récupération des dépendances via ApplicationContext
            AuthenticationService authenticationService = ApplicationContext.getBean(AuthenticationService.class);
            AuthorizationService authorizationService = ApplicationContext.getBean(AuthorizationService.class);

            // Création de l'UI de login
            ma.dentalTech.mvc.ui.frames.LoginUI tempLoginUI = new ma.dentalTech.mvc.ui.frames.LoginUI(null);

            // Création du contrôleur
            ma.dentalTech.mvc.controllers.modules.auth.impl.LoginControllerImpl loginController =
                new ma.dentalTech.mvc.controllers.modules.auth.impl.LoginControllerImpl(
                    tempLoginUI,
                    authenticationService,
                    authorizationService
                );

            // Injection du contrôleur dans LoginUI
            tempLoginUI.dispose();
            final ma.dentalTech.mvc.ui.frames.LoginUI loginUI = new ma.dentalTech.mvc.ui.frames.LoginUI(loginController);
            loginController.setLoginUI(loginUI);

            // Afficher la fenêtre de login
            loginUI.setVisible(true);
        });
    }

    @Override
    public void onExitRequested() {
        System.exit(0);
    }
}

