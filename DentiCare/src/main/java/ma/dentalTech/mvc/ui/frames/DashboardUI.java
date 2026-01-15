package ma.dentalTech.mvc.ui.frames;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import lombok.Getter;
import lombok.Setter;
import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.mvc.controllers.dashboardModule.api.DashboardController;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.dto.profileDtos.ProfileData;
import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.mvc.ui.pages.pagesNames.ApplicationPages;
import ma.dentalTech.mvc.ui.palette.menu.MyMenuBar;
import ma.dentalTech.mvc.ui.pages.commonPages.CenterPanel;
import ma.dentalTech.mvc.ui.pages.commonPages.FooterPanel;
import ma.dentalTech.mvc.ui.pages.commonPages.HeaderBannerPanel;
import ma.dentalTech.mvc.ui.palette.utils.HeaderWindowControls;
import ma.dentalTech.mvc.ui.palette.notification.NotificationLevel;
import ma.dentalTech.mvc.ui.palette.sidebarBuilder.NavigationSpecs;
import ma.dentalTech.mvc.ui.palette.sidebarBuilder.SidebarBuilder;
import ma.dentalTech.mvc.ui.palette.utils.ImageTools;
import ma.dentalTech.mvc.ui.palette.alert.Alert;
import ma.dentalTech.service.modules.auth.api.AuthorizationService;
// import ma.dentalTech.service.modules.profile.api.ProfileService; // TODO: Implement ProfileService

@Getter @Setter
public class DashboardUI extends JFrame {

    private final DashboardController controller;
    private final AuthorizationService authorizationService;

    private UserPrincipal principal;

    // UI parts
    private HeaderBannerPanel headerBanner;
    private FooterPanel footer;
    private CenterPanel center;

    // Drag window
    private Point dragOffset;

    public DashboardUI(DashboardController controller,
                       AuthorizationService authorizationService,
                       UserPrincipal principal) {

        this.controller = controller;
        this.authorizationService = authorizationService;
        this.principal = principal;

        setSize(1200, 750); // Taille par défaut responsive
        setMinimumSize(new Dimension(1024, 650)); // Taille minimale
        setLocationRelativeTo(null); // Centrer la fenêtre
        setResizable(true); // Permettre le redimensionnement

        // ✅ comme LoginUI
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setJMenuBar(buildMenuBar());
        setContentPane(buildRoot());

        // default
        navigateTo(ApplicationPages.DASHBOARD);

        showNotificationsCount(5, NotificationLevel.INFO);

        setVisible(true);
    }

    // API Controller -> View ———————————————————————————————————————————————
    public void refreshSession(UserPrincipal principal) {
        this.principal = principal;
        if (headerBanner != null) headerBanner.refresh(principal);
        //if (sideBar != null) sideBar.refresh(principal); // si tu as cette méthode (sinon supprime la ligne)
    }

    public void refreshHeaderFromProfile(ProfileData p) {
        if (p == null) return;

        // 1) Mettre à jour principal (session) si tu veux refléter partout
        //    (selon ton design, tu peux aussi juste rafraîchir le header)
        if (principal != null) {
            // On garde l'id/login/roles/privileges existants, on remplace juste nom/email/avatar si souhaité
            // ⚠️ Ici, UserPrincipal est un record ? sinon adapte.
            principal = new UserPrincipal(
                    principal.id(),
                    (safe(p.prenom()) + " " + safe(p.nom())).trim(),
                    p.email(),
                    principal.login(),
                    principal.rolePrincipal(),
                    principal.roles(),
                    principal.privileges()
            );
        }

        // 2) Rafraîchir le header
        if (headerBanner != null) {
            headerBanner.refresh(principal);

            // avatar : si ton HeaderBannerPanel supporte un setter d’avatar, appelle-le ici
            // headerBanner.setAvatarPath(p.avatar());
            try {
                // p.avatar() = "avatars/u1_xxx.png" (relatif)
                ImageIcon icon = ImageTools.loadAvatarFromProfilePath(p.avatar(), 40, 40);
                headerBanner.setAvatarIcon(icon);
            } catch (Exception ignored) {}

        }
    }

    // petit helper local
    private String safe(String s) { return s == null ? "" : s; }


    public void navigateTo(ApplicationPages page) {
        openPage(page);
    }

    public void showNotificationsCount(int count) {
        if (headerBanner != null) headerBanner.setNotificationCount(count);
    }

    public void showNotificationsCount(int count, NotificationLevel level) {
        if (headerBanner != null) headerBanner.setNotificationCount(count, level);
    }



    /**
     * Navigation : le SideBar appelle cette méthode.
     * - Le controller construit/retourne le panel de la page demandée
     * - Le panel est injecté dans le CenterPanel (CardLayout)
     *
     * Côté controller, adapte la signature pour retourner un JComponent :
     *     JComponent onNavigateRequested(DashboardPage page);
     */
    private void openPage(ApplicationPages page) {
        if (page == null || center == null) return;

        JComponent view = controller.onNavigateRequested(page);
        if (view != null)  center.upsertPage(page, view);
        center.showPage(page);
    }

    // UI Builders ———————————————————————————————————————————————————————
    private JMenuBar buildMenuBar() {
        return new MyMenuBar(
                e -> controller.onLogoutRequested(),
                e -> controller.onExitRequested()
        );
    }

    private JComponent buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(0, 24, 0, 16));
        topBar.setPreferredSize(new Dimension(0, 56));

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
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12)); // Espacement ajusté
        rightPanel.setOpaque(false);

        // Nom utilisateur
        if (principal != null) {
            JLabel userLabel = new JLabel(principal.nom());
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userLabel.setForeground(new Color(73, 80, 87));
            rightPanel.add(userLabel);
        }

        // Icône notifications avec badge
        JButton notifButton = createNotificationButton();
        rightPanel.add(notifButton);

        // Séparateur
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(1, 24));
        separator.setBackground(new Color(222, 226, 230));
        rightPanel.add(separator);

        // Boutons fenêtre (Minimize, Maximize/Restore, Close)
        JPanel windowControls = createWindowControls();
        rightPanel.add(windowControls);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        // Installer le drag de fenêtre sur toute la topBar
        installWindowDrag(topBar);

        return topBar;
    }

    private JButton createNotificationButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Notifications");

        // Icône notification
        ImageIcon icon = loadIcon("notifications", 20, 20);
        if (icon != null) {
            button.setIcon(icon);
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

        button.addActionListener(e -> {
            // Ouvrir les notifications
            openPage(ApplicationPages.NOTIFICATIONS);
        });

        return button;
    }

    private JPanel createWindowControls() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12)); // Centrage vertical
        controls.setOpaque(false);

        // Bouton Minimize
        JButton minimizeBtn = createModernWindowButton("—", "Minimiser");
        minimizeBtn.addActionListener(e -> setState(Frame.ICONIFIED));

        // Bouton Maximize/Restore
        JButton maximizeBtn = createModernWindowButton("⬜", "Maximiser");
        maximizeBtn.addActionListener(e -> {
            int state = getExtendedState();
            if ((state & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
                maximizeBtn.setText("⬜");
                maximizeBtn.setToolTipText("Maximiser");
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                maximizeBtn.setText("❐");
                maximizeBtn.setToolTipText("Restaurer");
            }
        });

        // Bouton Close
        JButton closeBtn = createModernWindowButton("✕", "Fermer");
        closeBtn.addActionListener(e -> {
            boolean ok = Alert.confirm(this, "Voulez-vous vraiment quitter l'application ?");
            if (ok) {
                dispose();
                System.exit(0);
            }
        });

        // Hover effects spécifiques pour chaque bouton
        setupMinimizeButtonHover(minimizeBtn);
        setupMaximizeButtonHover(maximizeBtn);
        setupCloseButtonHover(closeBtn);

        controls.add(minimizeBtn);
        controls.add(maximizeBtn);
        controls.add(closeBtn);

        return controls;
    }

    private JButton createModernWindowButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Plus grand pour les icônes
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(46, 32)); // Légèrement plus large pour les icônes
        button.setForeground(new Color(73, 80, 87)); // Couleur par défaut
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0)); // Pas de marge interne

        return button;
    }

    private void setupMinimizeButtonHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(52, 152, 219)); // Bleu au hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(73, 80, 87)); // Couleur normale
            }
        });
    }

    private void setupMaximizeButtonHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(52, 152, 219)); // Bleu au hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(73, 80, 87)); // Couleur normale
            }
        });
    }

    private void setupCloseButtonHover(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.RED); // Rouge au hover pour le bouton close
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(new Color(73, 80, 87)); // Couleur normale
            }
        });
    }

    // la fenêtre sans décoration, a perdu l'option de la glisser dans l'écran là où on veut
    // avec installWindowDrag avec un component en paramètre, on peut ajouter un mouvement (Motion) de glissement à partir de ce componenet
    private void installWindowDrag(Component c) {
        c.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                dragOffset = e.getPoint();
            }
        });
        c.addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                if (dragOffset == null) return;
                Point p = e.getLocationOnScreen();
                setLocation(p.x - dragOffset.x, p.y - dragOffset.y);
            }
        });
    }

    private JComponent buildSideBar() {
        // Pour les admins, utiliser la nouvelle sidebar moderne
        if (principal != null && principal.rolePrincipal() == RoleEnum.ADMIN) {
            return createModernAdminSidebar();
        }

        // Pour les autres rôles, utiliser l'ancienne sidebar
        var items = NavigationSpecs.forPrincipal(principal);

        return SidebarBuilder.build(
                this, // parentForAlerts
                principal,
                authorizationService,
                items,
                (source, pageId) -> {
                    ApplicationPages page = ApplicationPages.valueOf(pageId);
                    openPage(page);
                },
                true // hideForbidden : true = cacher, false = désactiver
        );
    }

    private JComponent createModernAdminSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(248, 249, 250));
        sidebar.setBorder(new EmptyBorder(24, 0, 24, 0));
        sidebar.setPreferredSize(new Dimension(240, 0));

        // Logo/Brand
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(0, 24, 32, 24));

        JLabel logo = new JLabel("DentiCare");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(new Color(52, 152, 219));
        brandPanel.add(logo);

        sidebar.add(brandPanel);

        // Navigation items
        String[][] navItems = {
            {"Dashboard", "dashboard"},
            {"Utilisateurs", "users"},
            {"Cabinets", "cabinet"},
            {"Notifications", "notifications"},
            {"Paramètres", "settings"}
        };

        for (String[] item : navItems) {
            sidebar.add(createNavItem(item[0], item[1]));
            sidebar.add(Box.createVerticalStrut(4)); // Espacement
        }

        // Spacer pour pousser le contenu vers le haut
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JComponent createNavItem(String text, String iconName) {
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

        // Texte
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(new Color(52, 58, 64));

        item.add(textLabel);

        // Hover effect
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(233, 236, 239));
                item.setOpaque(true);
                textLabel.setForeground(new Color(52, 152, 219));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setOpaque(false);
                textLabel.setForeground(new Color(52, 58, 64));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigation logic
                try {
                    ApplicationPages page = switch (text) {
                        case "Dashboard" -> ApplicationPages.DASHBOARD;
                        case "Utilisateurs" -> ApplicationPages.USERS;
                        case "Cabinets" -> ApplicationPages.CABINETS;
                        case "Notifications" -> ApplicationPages.NOTIFICATIONS;
                        case "Paramètres" -> ApplicationPages.PARAMETRAGE;
                        default -> ApplicationPages.DASHBOARD;
                    };
                    openPage(page);
                } catch (Exception ex) {
                    System.err.println("Erreur navigation: " + ex.getMessage());
                }
            }
        });

        return item;
    }

    private ImageIcon loadIcon(String iconName, int width, int height) {
        // Utiliser ImageTools.loadIcon
        try {
            return ma.dentalTech.mvc.ui.palette.utils.ImageTools.loadIcon(iconName, width, height);
        } catch (Exception e) {
            // Fallback: créer une icône vide
            return new ImageIcon(new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB));
        }
    }

    private JComponent buildCenter() {
        center = new CenterPanel();
        return center;
    }

    private JComponent buildFooter() {
        footer = new FooterPanel();
        return footer;
    }

    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // TopBar avec contrôles de fenêtre intégrés
        root.add(buildTopBar(), BorderLayout.NORTH);

        // SideBar
        root.add(buildSideBar(), BorderLayout.WEST);

        // Contenu principal (Center)
        root.add(buildCenter(), BorderLayout.CENTER);

        return root;
    }


}
