package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class DossiersPanel extends BasePlaceholderPanel {
    public DossiersPanel(UserPrincipal principal) {
        super("Dossiers Médicaux", principal);
    }
}
