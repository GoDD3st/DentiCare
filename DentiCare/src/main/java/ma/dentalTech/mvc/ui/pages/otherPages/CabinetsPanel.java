package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class CabinetsPanel extends BasePlaceholderPanel {
    public CabinetsPanel(UserPrincipal principal) {
        super("Backoffice — Cabinets", principal);
    }
}
