package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class UsersPanel extends BasePlaceholderPanel {
    public UsersPanel(UserPrincipal principal) {
        super("Backoffice — Users", principal);
    }
}
