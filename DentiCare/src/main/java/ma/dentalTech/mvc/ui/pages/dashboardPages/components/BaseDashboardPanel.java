package ma.dentalTech.mvc.ui.pages.dashboardPages.components;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class BaseDashboardPanel extends JPanel {

    protected final UserPrincipal principal;

    public BaseDashboardPanel(UserPrincipal principal) {
        this.principal = principal;
        initializeLayout();
        setupComponents();
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    private void setupComponents() {
        // Content area only (sera défini par les sous-classes)
        JScrollPane contentScroll = createContentScroll();
        add(contentScroll, BorderLayout.CENTER);
    }


    protected JScrollPane createContentScroll() {
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    protected JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Titre principal
        JLabel title = new JLabel(getDashboardTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 24, 0));
        panel.add(title);

        // Contenu spécifique au rôle
        JPanel specificContent = createSpecificContent();
        if (specificContent != null) {
            panel.add(specificContent);
        }

        return panel;
    }

    // Méthodes abstraites à implémenter par les sous-classes
    protected abstract String getDashboardTitle();
    protected abstract JPanel createSpecificContent();

}