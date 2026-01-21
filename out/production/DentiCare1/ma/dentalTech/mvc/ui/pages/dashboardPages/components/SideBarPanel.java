package ma.dentalTech.mvc.ui.pages.dashboardPages.components;

import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SideBarPanel extends JPanel {

    public SideBarPanel(UserPrincipal principal, NavigationCallback callback) {
        RoleEnum role = principal != null ? principal.rolePrincipal() : null;
        initializeLayout();
        setupContent(role, callback);
    }

    private void initializeLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // 🎨 Fond blanc ou bleu très clair selon spécifications
        setBackground(new Color(255, 255, 255)); // Blanc (#FFFFFF)
        setBorder(new EmptyBorder(24, 0, 24, 0));
        setPreferredSize(new Dimension(240, 0));
    }

    private void setupContent(RoleEnum role, NavigationCallback callback) {
        // Logo/Brand
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(0, 24, 32, 24));

        JLabel logo = new JLabel("DentiCare");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(new Color(30, 136, 229)); // #1E88E5 - Bleu principal
        brandPanel.add(logo);

        add(brandPanel);

        // Navigation items selon le rôle
        String[][] navItems = getNavigationItemsForRole(role);

        for (String[] item : navItems) {
            add(createNavItem(item[0], item[1], callback));
            add(Box.createVerticalStrut(4)); // Espacement
        }

        // Spacer pour pousser le contenu vers le haut
        add(Box.createVerticalGlue());
    }

    private String[][] getNavigationItemsForRole(RoleEnum role) {
        if (role == null) {
            return new String[][]{
                {"Dashboard", "dashboard"}
            };
        }

        return switch (role) {
            case ADMIN -> new String[][]{
                {"Dashboard", "dashboard"},
                {"Utilisateurs", "users"},
                {"Cabinets", "cabinet"},
                {"Notifications", "notifications"},
                {"Paramètres", "settings"}
            };
            case MEDECIN -> new String[][]{
                {"Dashboard", "dashboard"},
                {"Patients", "patients"},
                {"Dossiers Médicaux", "dossiers"},
                {"Consultations", "consultations"},
                {"Ordonnances", "ordonnances"},
                {"Actes", "actes"}
            };
            case SECRETAIRE -> new String[][]{
                {"Dashboard", "dashboard"},
                {"Patients", "patients"},
                {"Dossiers Médicaux", "dossiers"},
                {"Rendez-vous", "rdv"},
                {"Caisse", "caisse"},
                {"Statistiques", "stats"}
            };
            default -> new String[][]{
                {"Dashboard", "dashboard"}
            };
        };
    }

    private JComponent createNavItem(String text, String iconName, NavigationCallback callback) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        item.setOpaque(false);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setBorder(new EmptyBorder(4, 8, 4, 8));

        // Icône
        ImageIcon icon = loadIcon(iconName, 20, 20);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            item.add(iconLabel);
        }

        // Texte - couleur texte principal
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(new Color(38, 50, 56)); // #263238 - Texte principal

        item.add(textLabel);

        // 🎨 Hover effect moderne SaaS : fond bleu clair, texte bleu principal
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(227, 242, 253)); // #E3F2FD - Bleu très clair (hover)
                item.setOpaque(true);
                textLabel.setForeground(new Color(30, 136, 229)); // #1E88E5 - Bleu principal
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setOpaque(false);
                textLabel.setForeground(new Color(38, 50, 56)); // #263238 - Texte principal
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (callback != null) {
                    callback.onNavigate(text, iconName);
                }
            }
        });

        return item;
    }

    private ImageIcon loadIcon(String iconName, int width, int height) {
        try {
            return ma.dentalTech.mvc.ui.palette.utils.ImageTools.loadIcon(iconName, width, height);
        } catch (Exception e) {
            // Fallback: créer une icône vide
            return new ImageIcon(new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB));
        }
    }

    public interface NavigationCallback {
        void onNavigate(String itemName, String iconName);
    }
}