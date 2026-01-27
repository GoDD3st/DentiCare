package ma.dentalTech.mvc.ui.palette.sidebarBuilder;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.alert.Alert;
import ma.dentalTech.mvc.ui.palette.buttons.PillNavButtonLight;
import ma.dentalTech.mvc.ui.palette.buttons.NavSectionButton;
import ma.dentalTech.mvc.ui.palette.utils.ImageTools;
import ma.dentalTech.service.modules.auth.api.AuthorizationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public final class SidebarBuilder {

    private SidebarBuilder(){}

    public interface Navigator {
        //void go(MyButton source, String pageId);
       //  rendre  Navigator plus générique (pro)
        void go(AbstractButton source, String pageId);
    }

    public static JComponent build(
            Component parentForAlerts,
            UserPrincipal principal,
            AuthorizationService auth,
            List<NavSpec> items,
            Navigator navigator,
            boolean hideForbidden // true: cacher, false: désactiver
    ) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidebar.setBackground(Color.WHITE);

        JLabel title = new JLabel("Modules");
        title.setFont(new Font("Optima", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(0, 6, 12, 0));
        sidebar.add(title);

        // group by section (garde l'ordre)
        Map<String, List<NavSpec>> bySection = new LinkedHashMap<>();

        // Collections pour gérer l'état des sections
        java.util.List<PillNavButtonLight> allButtons = new java.util.ArrayList<>();
        Map<String, NavSectionButton> sectionButtons = new HashMap<>();
        Map<String, List<PillNavButtonLight>> sectionItems = new HashMap<>();

        for (NavSpec it : items) {
            bySection.computeIfAbsent(it.section(), k -> new ArrayList<>()).add(it);
        }

        for (var entry : bySection.entrySet()) {
            String sectionName = entry.getKey();
            List<NavSpec> sectionSpecs = entry.getValue();

            // Créer le bouton de section cliquable
            NavSectionButton sectionBtn = new NavSectionButton(sectionName);
            sectionButtons.put(sectionName, sectionBtn);
            sectionItems.put(sectionName, new ArrayList<>());

            sidebar.add(sectionBtn);

            // Créer et ajouter les éléments de navigation pour cette section
            for (NavSpec spec : sectionSpecs) {
                boolean allowed = (spec.privilege() == null || spec.privilege().isBlank())
                                  || auth.hasPermission(principal, spec.privilege());

                if (!allowed && hideForbidden) continue;

                ImageIcon icon = safeIcon(spec.iconPath(), 28, 28);
                PillNavButtonLight btn = new PillNavButtonLight(spec.label(), icon);

                btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
                btn.setAlignmentX(Component.LEFT_ALIGNMENT);
                btn.setEnabled(allowed);

                btn.addActionListener(e -> {
                    if (!btn.isEnabled()) {
                        Alert.warning(parentForAlerts, "Accès refusé : privilège requis = " + spec.privilege());
                        return;
                    }
                    // Désactiver tous les autres boutons
                    for (PillNavButtonLight b : allButtons) b.setActive(false);
                    btn.setActive(true);

                    navigator.go(null, spec.pageId());
                });

                sidebar.add(btn);
                allButtons.add(btn);
                sectionItems.get(sectionName).add(btn);

                sidebar.add(Box.createVerticalStrut(6));
            }

            // Gestionnaire pour développer/réduire la section
            final String finalSectionName = sectionName;
            sectionBtn.addActionListener(e -> {
                boolean currentlyExpanded = sectionBtn.isExpanded();
                sectionBtn.setExpanded(!currentlyExpanded);

                // Masquer/afficher tous les boutons de cette section
                for (PillNavButtonLight btn : sectionItems.get(finalSectionName)) {
                    btn.setVisible(!currentlyExpanded);
                }

                // Revalider et repeindre la sidebar
                sidebar.revalidate();
                sidebar.repaint();
            });

            sidebar.add(Box.createVerticalStrut(10));
        }

        sidebar.add(Box.createVerticalGlue());

        JScrollPane sp = new JScrollPane(sidebar);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getVerticalScrollBar().setUnitIncrement(14);
        return sp;
    }

    // Méthode supprimée - remplacée par NavSectionButton

    private static ImageIcon safeIcon(String path, int w, int h) {
        try {
            return ImageTools.loadIcon(path, w, h);
        } catch (Exception ex) {
            return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
        }
    }
}
