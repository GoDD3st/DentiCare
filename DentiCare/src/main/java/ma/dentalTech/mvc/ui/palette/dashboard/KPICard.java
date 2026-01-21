package ma.dentalTech.mvc.ui.palette.dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Carte KPI moderne avec icône, valeur et indicateur de tendance
 */
public class KPICard extends JPanel {
    
    private static final int CORNER_RADIUS = 12;
    private static final int SHADOW_OFFSET = 3;
    
    private String title;
    private String value;
    private String trend;
    private Icon icon;
    private Color gradientStart;
    private Color gradientEnd;
    private Color iconBgColor;
    
    public KPICard(String title, String value, String trend, Icon icon, Color gradientStart, Color gradientEnd) {
        this.title = title;
        this.value = value;
        this.trend = trend;
        this.icon = icon;
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;
        this.iconBgColor = gradientStart;
        
        setOpaque(false);
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setLayout(new BorderLayout(0, 6));
        setPreferredSize(new Dimension(160, 100));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        setMinimumSize(new Dimension(140, 90));
        
        buildUI();
    }
    
    private void buildUI() {
        // 🎨 Style moderne SaaS : fond blanc, valeur mise en évidence, titre descriptif
        setLayout(new BorderLayout());
        
        // Valeur principale - Chiffres très lisibles, texte principal
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Très lisible
        valueLabel.setForeground(new Color(38, 50, 56)); // #263238 - Texte principal
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(valueLabel, BorderLayout.CENTER);
        
        // Titre descriptif en bas - texte secondaire
        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            titleLabel.setForeground(new Color(96, 125, 139)); // #607D8B - Texte secondaire
            titleLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
            add(titleLabel, BorderLayout.SOUTH);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // 🎨 Style moderne SaaS : fond blanc, ombre légère simulée, coins arrondis
        
        // Ombre douce simulée (border + couleur)
        g2d.setColor(new Color(0, 0, 0, 8)); // Ombre très légère
        g2d.fill(new RoundRectangle2D.Float(
            SHADOW_OFFSET, SHADOW_OFFSET,
            width - SHADOW_OFFSET * 2, height - SHADOW_OFFSET * 2,
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        // Fond blanc
        g2d.setColor(Color.WHITE); // #FFFFFF
        g2d.fill(new RoundRectangle2D.Float(
            0, 0, width - SHADOW_OFFSET, height - SHADOW_OFFSET,
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        // Bordure discrète
        g2d.setColor(new Color(227, 242, 253)); // Bleu très clair pour bordure subtile
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Float(
            0.5f, 0.5f, width - SHADOW_OFFSET - 1, height - SHADOW_OFFSET - 1,
            CORNER_RADIUS, CORNER_RADIUS
        ));
        
        g2d.dispose();
    }
    
    public void setValue(String value) {
        this.value = value;
        repaint();
    }
    
    public void setTrend(String trend) {
        this.trend = trend;
        repaint();
    }
}
