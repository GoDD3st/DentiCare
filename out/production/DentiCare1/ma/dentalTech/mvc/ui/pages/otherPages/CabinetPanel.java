package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class CabinetPanel extends BasePlaceholderPanel {
    public CabinetPanel(UserPrincipal principal) {
        super("Cabinet courant", principal);
    }
}
