package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class CaissePanel extends BasePlaceholderPanel {
    public CaissePanel(UserPrincipal principal) {
        super("Module Caisse", principal);
    }
}
