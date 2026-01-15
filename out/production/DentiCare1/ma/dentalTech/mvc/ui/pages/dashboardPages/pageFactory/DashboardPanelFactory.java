package ma.dentalTech.mvc.ui.pages.dashboardPages.pageFactory;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.dto.profileDtos.ProfileData;
import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;

import javax.swing.*;
import ma.dentalTech.mvc.ui.pages.dashboardPages.AdminDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.DefaultDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.DoctorDashboardPanel;
import ma.dentalTech.mvc.ui.pages.dashboardPages.SecretaryDashboardPanel;
import ma.dentalTech.mvc.ui.pages.otherPages.*;
import java.util.function.Consumer;

public final class DashboardPanelFactory {

    private DashboardPanelFactory(){}

    public static JComponent create(UserPrincipal principal) {
        RoleEnum role = (principal != null) ? principal.rolePrincipal() : null;

        if (role == null) {
            return new DefaultDashboardPanel(principal);
        }

        return switch (role) {
            case ADMIN      -> new AdminDashboardPanel(principal);
            case MEDECIN    -> new DoctorDashboardPanel(principal);
            case SECRETAIRE -> new SecretaryDashboardPanel(principal);
            default         -> new DefaultDashboardPanel(principal);
        };
    }

    public static JComponent createPanel(ApplicationPages page, UserPrincipal principal) {
        return switch (page) {
            case DASHBOARD -> create(principal);
            case PATIENTS -> new PatientsPanel(principal);
            case CAISSE -> new CaissePanel(principal);
            case CABINET -> new CabinetPanel(principal);
            case USERS -> new UsersPanel(principal);
            case CABINETS -> new CabinetsPanel(principal);
            case DOSSIERS_MEDICAUX -> new DossiersPanel(principal);
            case PARAMETRAGE -> new ParametragePanel(principal);
            case NOTIFICATIONS -> new NotificationsPanel(principal);
            case PROFILE -> {
                // Create ProfileData from UserPrincipal (limited data available)
                ProfileData profileData = new ProfileData(
                    principal.id(),
                    principal.nom(),
                    "", // prenom not available in UserPrincipal
                    principal.email(),
                    "", // avatar not available
                    "", // tel not available
                    ""  // adresse not available
                );
                // Empty consumer for profile save callback
                Consumer<ProfileData> onProfileSaved = savedData -> {};
                yield new ma.dentalTech.mvc.ui.pages.profilePages.ProfilePanel(null, null, profileData, onProfileSaved);
            }
            default -> new DefaultDashboardPanel(principal);
        };
    }
}
