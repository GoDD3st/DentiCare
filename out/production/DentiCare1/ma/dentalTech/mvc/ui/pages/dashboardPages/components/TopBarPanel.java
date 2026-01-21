package ma.dentalTech.mvc.ui.pages.dashboardPages.components;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.alert.Alert;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TopBarPanel extends JPanel {

    public TopBarPanel(UserPrincipal principal, Runnable onNotificationsClick, Runnable onMinimize, Runnable onMaximize, Runnable onClose) {
        initializeLayout();
        setupContent(principal, onNotificationsClick, onMinimize, onMaximize, onClose);
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(0, 24, 0, 16));
        setPreferredSize(new Dimension(0, 56));
    }

    private void setupContent(UserPrincipal principal, Runnable onNotificationsClick, Runnable onMinimize, Runnable onMaximize, Runnable onClose) {
        // Partie gauche : Logo + Titre
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        leftPanel.setOpaque(false);

        // Logo
        JLabel logoLabel = new JLabel("DentiCare");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(new Color(52, 152, 219));

        // Titre
        String roleName = principal != null && principal.rolePrincipal() != null
            ? principal.rolePrincipal().name().toLowerCase()
            : "utilisateur";
        JLabel titleLabel = new JLabel("Dashboard " + roleName);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(new Color(73, 80, 87));

        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);

        // Partie droite : User info + Notifications + Window controls
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        rightPanel.setOpaque(false);

        // Nom utilisateur
        if (principal != null) {
            JLabel userLabel = new JLabel(principal.nom());
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userLabel.setForeground(new Color(73, 80, 87));
            rightPanel.add(userLabel);
        }

        // Icône notifications avec badge
        JButton notifButton = createNotificationButton(onNotificationsClick);
        rightPanel.add(notifButton);

        // Séparateur
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(1, 24));
        separator.setBackground(new Color(222, 226, 230));
        rightPanel.add(separator);

        // Boutons fenêtre (Minimize, Maximize/Restore, Close)
        JPanel windowControls = createWindowControls(onMinimize, onMaximize, onClose);
        rightPanel.add(windowControls);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private JButton createNotificationButton(Runnable onClick) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Notifications");

        // Icône notification
        try {
            ImageIcon icon = ma.dentalTech.mvc.ui.palette.utils.ImageTools.loadIcon("notifications", 20, 20);
            if (icon != null) {
                button.setIcon(icon);
            }
        } catch (Exception e) {
            // Fallback
        }

        // Badge (simulé)
        button.setText("3"); // Nombre de notifications non lues
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setForeground(Color.WHITE);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);

        // Style du bouton
        button.setPreferredSize(new Dimension(32, 32));
        button.setBackground(new Color(52, 152, 219));
        button.setOpaque(true);

        if (onClick != null) {
            button.addActionListener(e -> onClick.run());
        }

        return button;
    }

    private JPanel createWindowControls(Runnable onMinimize, Runnable onMaximize, Runnable onClose) {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        controls.setOpaque(false);

        // Bouton Minimize
        JButton minimizeBtn = createModernWindowButton("—", "Minimiser");
        if (onMinimize != null) {
            minimizeBtn.addActionListener(e -> onMinimize.run());
        }
        setupMinimizeButtonHover(minimizeBtn);

        // Bouton Maximize/Restore
        JButton maximizeBtn = createModernWindowButton("⬜", "Maximiser");
        if (onMaximize != null) {
            maximizeBtn.addActionListener(e -> onMaximize.run());
        }
        setupMaximizeButtonHover(maximizeBtn);

        // Bouton Close
        JButton closeBtn = createModernWindowButton("✕", "Fermer");
        if (onClose != null) {
            closeBtn.addActionListener(e -> {
                boolean ok = Alert.confirm(SwingUtilities.getWindowAncestor(this), "Voulez-vous vraiment quitter l'application ?");
                if (ok && onClose != null) {
                    onClose.run();
                }
            });
        }
        setupCloseButtonHover(closeBtn);

        controls.add(minimizeBtn);
        controls.add(maximizeBtn);
        controls.add(closeBtn);

        return controls;
    }

    private JButton createModernWindowButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(46, 32));
        button.setForeground(new Color(73, 80, 87));
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));

        return button;
    }

    private void setupMinimizeButtonHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(52, 152, 219));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(73, 80, 87));
            }
        });
    }

    private void setupMaximizeButtonHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(52, 152, 219));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(73, 80, 87));
            }
        });
    }

    private void setupCloseButtonHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(73, 80, 87));
            }
        });
    }
}