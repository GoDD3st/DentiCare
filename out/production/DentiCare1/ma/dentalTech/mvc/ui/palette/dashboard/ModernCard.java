package ma.dentalTech.mvc.ui.palette.dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Carte moderne avec effet glassmorphism et ombres
 */
public class ModernCard extends JPanel {
    
    // 🎨 Nouveau background DentiCare - blanc avec transparence subtile
    private static final Color CARD_BG = new Color(255, 255, 255, 250);
    private static final Color BORDER_COLOR = new Color(230, 230, 230, 100);
    private static final int CORNER_RADIUS = 16;
    private static final int SHADOW_OFFSET = 4;
    
    private String title;
    private Icon icon;
    private Color accentColor;
    private boolean withGradient;
    
    public ModernCard(String title) {
        this(title, null, null, false);
    }
    
    public ModernCard(String title, Icon icon, Color accentColor) {
        this(title, icon, accentColor, false);
    }
    
    public ModernCard(String title, Icon icon, Color accentColor, boolean withGradient) {
        this.title = title;
        this.icon = icon;
        this.accentColor = accentColor != null ? accentColor : new Color(99, 102, 241);
        this.withGradient = withGradient;
        
        setOpaque(false);
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setLayout(new BorderLayout(0, 8));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // 🎨 Style moderne SaaS : ombre douce simulée, fond blanc
        
        // Ombre douce simulée (border + couleur)
        g2d.setColor(new Color(0, 0, 0, 8)); // Ombre très légère
        g2d.fill(new RoundRectangle2D.Float(
            SHADOW_OFFSET, SHADOW_OFFSET,
            width - SHADOW_OFFSET * 2, height - SHADOW_OFFSET * 2,
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        // Fond blanc ou avec gradient subtil si spécifié
        if (withGradient && accentColor != null) {
            // Gradient très subtil pour les graphiques
            GradientPaint gradient = new GradientPaint(
                0, 0, accentColor,
                0, height, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 200)
            );
            g2d.setPaint(gradient);
        } else {
            g2d.setColor(Color.WHITE); // Fond blanc pour les cartes normales
        }
        
        g2d.fill(new RoundRectangle2D.Float(
            0, 0, width - SHADOW_OFFSET, height - SHADOW_OFFSET,
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        // Bordure discrète
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(227, 242, 253)); // Bleu très clair pour bordure subtile
        g2d.draw(new RoundRectangle2D.Float(
            0.5f, 0.5f, width - SHADOW_OFFSET - 1, height - SHADOW_OFFSET - 1,
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        g2d.dispose();
    }
    
    public void setTitle(String title) {
        this.title = title;
        repaint();
    }
    
    public JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titlePanel.setOpaque(false);
        
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setBorder(new EmptyBorder(0, 0, 0, 8));
            titlePanel.add(iconLabel);
        }
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(30, 41, 59));
        titlePanel.add(titleLabel);
        
        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }
}
