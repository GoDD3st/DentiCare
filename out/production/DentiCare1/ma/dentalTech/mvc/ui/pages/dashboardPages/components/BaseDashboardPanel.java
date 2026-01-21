package ma.dentalTech.mvc.ui.pages.dashboardPages.components;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class BaseDashboardPanel extends JPanel {

    protected final UserPrincipal principal;
    
    // 🎨 Palette de couleurs moderne SaaS/médical selon spécifications
    protected static final Color BLUE_PRIMARY = new Color(30, 136, 229);      // #1E88E5 - Bleu principal
    protected static final Color BLUE_DARK = new Color(21, 101, 192);        // #1565C0 - Bleu foncé (hover/actif)
    protected static final Color BLUE_LIGHT = new Color(227, 242, 253);      // #E3F2FD - Bleu très clair (background)
    protected static final Color WHITE = new Color(255, 255, 255);           // #FFFFFF - Blanc
    protected static final Color GRAY_LIGHT = new Color(245, 247, 250);      // #F5F7FA - Gris clair (zones secondaires)
    protected static final Color TEXT_PRIMARY = new Color(38, 50, 56);        // #263238 - Texte principal
    protected static final Color TEXT_SECONDARY = new Color(96, 125, 139);    // #607D8B - Texte secondaire

    // Compatibilité - anciens noms pour éviter les erreurs
    protected static final Color DENTICARE_BLUE = BLUE_PRIMARY;
    protected static final Color DENTICARE_BLUE_DARK = BLUE_DARK;
    protected static final Color DENTICARE_GREEN = new Color(39, 174, 96);      // Vert médical (conservé pour graphiques)
    protected static final Color DENTICARE_GREEN_LIGHT = new Color(46, 204, 113);
    protected static final Color DENTICARE_GRAY_LIGHT = GRAY_LIGHT;
    protected static final Color DENTICARE_GRAY_MEDIUM = TEXT_SECONDARY;
    protected static final Color DENTICARE_WHITE = WHITE;
    protected static final Color DENTICARE_BACKGROUND = BLUE_LIGHT;
    protected static final Color GRADIENT_START = BLUE_PRIMARY;
    protected static final Color GRADIENT_MID = BLUE_DARK;
    protected static final Color GRADIENT_END = DENTICARE_GREEN;
    protected static final Color BACKGROUND_LIGHT = BLUE_LIGHT;

    public BaseDashboardPanel(UserPrincipal principal) {
        this.principal = principal;
        initializeLayout();
        setupComponents();
    }

    private void initializeLayout() {
        setLayout(new BorderLayout());
        setOpaque(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();

        // 🎨 Fond moderne SaaS : bleu très clair uniforme (#E3F2FD)
        g2d.setColor(BLUE_LIGHT);
        g2d.fill(new Rectangle2D.Float(0, 0, width, height));

        g2d.dispose();
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
        
        // 🎨 Personnalisation moderne de la scrollbar avec nouvelles couleurs
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(30, 136, 229, 120); // #1E88E5 avec transparence
                this.trackColor = new Color(227, 242, 253, 50); // #E3F2FD avec transparence
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        return scrollPane;
    }

    protected JPanel createContentPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Fond transparent pour laisser voir le dégradé
                super.paintComponent(g);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16)); // Padding réduit pour maximiser l'espace
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header moderne avec titre - espacement généreux
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Espacement généreux
        
        // 🎨 Titre moderne SaaS : gras, taille supérieure, couleur texte principal
        JLabel title = new JLabel(getDashboardTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Taille supérieure
        title.setForeground(TEXT_PRIMARY); // #263238
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // 🎨 Sous-titre : texte secondaire, plus petit, couleur plus claire
        JLabel subtitle = new JLabel("Tableau de bord - " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRENCH)));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Plus petit
        subtitle.setForeground(TEXT_SECONDARY); // #607D8B
        subtitle.setBorder(new EmptyBorder(6, 0, 0, 0));
        
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);
        titleContainer.add(title);
        titleContainer.add(subtitle);
        
        headerPanel.add(titleContainer, BorderLayout.WEST);
        panel.add(headerPanel);

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