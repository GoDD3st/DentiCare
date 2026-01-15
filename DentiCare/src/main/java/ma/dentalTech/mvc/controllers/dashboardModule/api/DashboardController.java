package ma.dentalTech.mvc.controllers.dashboardModule.api;

import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;

import javax.swing.*;

public interface DashboardController {
    JComponent onNavigateRequested(ApplicationPages page);
    void onLogoutRequested();
    void onExitRequested();
}

