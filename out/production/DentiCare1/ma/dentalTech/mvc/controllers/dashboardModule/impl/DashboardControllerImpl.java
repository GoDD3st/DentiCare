package ma.dentalTech.mvc.controllers.dashboardModule.impl;

import ma.dentalTech.mvc.controllers.dashboardModule.api.DashboardController;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.dashboardPages.pageFactory.DashboardPanelFactory;
import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;

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
        // TODO: Implémenter la logique de déconnexion
        System.out.println("Logout requested");
        System.exit(0);
    }

    @Override
    public void onExitRequested() {
        System.exit(0);
    }
}

