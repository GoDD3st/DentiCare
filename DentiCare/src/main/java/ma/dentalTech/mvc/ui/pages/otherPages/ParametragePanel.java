package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class ParametragePanel extends BasePlaceholderPanel {
    public ParametragePanel(UserPrincipal principal) {
        super("Paramétrage Cabinet", principal);
    }
}
