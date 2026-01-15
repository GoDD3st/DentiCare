package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class PatientsPanel extends BasePlaceholderPanel {
    public PatientsPanel(UserPrincipal principal) {
        super("Module Patients", principal);
    }
}
