package ma.dentalTech;

import ma.dentalTech.mvc.controllers.modules.auth.impl.LoginControllerImpl;
import ma.dentalTech.mvc.ui.frames.LoginUI;
import ma.dentalTech.service.modules.auth.api.AuthenticationService;
import ma.dentalTech.service.modules.auth.api.AuthorizationService;
import ma.dentalTech.conf.ApplicationContext;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configuration du Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du Look and Feel: " + e.getMessage());
        }

        // Récupération des dépendances via ApplicationContext
        AuthenticationService authenticationService = ApplicationContext.getBean(AuthenticationService.class);
        AuthorizationService authorizationService = ApplicationContext.getBean(AuthorizationService.class);

        // Création de l'UI de login (sans contrôleur pour l'instant)
        LoginUI tempLoginUI = new LoginUI(null);

        // Création du contrôleur
        LoginControllerImpl loginController = new LoginControllerImpl(
                tempLoginUI,
                authenticationService,
                authorizationService
        );

        // Injection du contrôleur dans LoginUI
        // On doit recréer LoginUI avec le contrôleur car il est final dans le constructeur
        tempLoginUI.dispose();
        final LoginUI loginUI = new LoginUI(loginController);
        loginController.setLoginUI(loginUI);

        // Afficher la fenêtre de login
        SwingUtilities.invokeLater(() -> {
            loginUI.setVisible(true);
        });


    }
}

