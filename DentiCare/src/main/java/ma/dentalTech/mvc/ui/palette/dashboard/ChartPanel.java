package ma.dentalTech.mvc.ui.palette.dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Panneau de graphique simple pour visualiser des données
 */
public class ChartPanel extends JPanel {
    
    private List<Double> data;
    private String title;
    private Color chartColor;
    private boolean showGrid;
    
    public ChartPanel(String title, Color chartColor) {
        this.title = title;
        this.chartColor = chartColor;
        this.data = new ArrayList<>();
        this.showGrid = true;
        setOpaque(false);
        setPreferredSize(new Dimension(250, 110));
        setMinimumSize(new Dimension(200, 100));
    }
    
    public void setData(List<Double> data) {
        this.data = new ArrayList<>(data);
        repaint();
    }
    
    public void addDataPoint(double value) {
        data.add(value);
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int chartWidth = width - padding * 2;
        int chartHeight = height - padding * 2;
        
        // Titre
        if (title != null && !title.isEmpty()) {
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2d.setColor(new Color(51, 65, 85));
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (width - titleWidth) / 2, 20);
        }
        
        if (data.isEmpty()) {
            g2d.setColor(new Color(148, 163, 184));
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2d.drawString("Aucune donnée disponible", width / 2 - 80, height / 2);
            g2d.dispose();
            return;
        }
        
        // Grille
        if (showGrid) {
            g2d.setColor(new Color(226, 232, 240));
            g2d.setStroke(new BasicStroke(1));
            for (int i = 0; i <= 5; i++) {
                int y = padding + (chartHeight * i / 5);
                g2d.draw(new Line2D.Float(padding, y, width - padding, y));
            }
        }
        
        // Trouver min et max
        double min = data.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max = data.stream().mapToDouble(Double::doubleValue).max().orElse(100);
        double range = max - min;
        if (range == 0) range = 1;
        
        // Dessiner le graphique en ligne
        g2d.setColor(chartColor);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int pointCount = data.size();
        if (pointCount > 1) {
            int[] xPoints = new int[pointCount];
            int[] yPoints = new int[pointCount];
            
            for (int i = 0; i < pointCount; i++) {
                xPoints[i] = padding + (chartWidth * i / (pointCount - 1));
                double normalizedValue = (data.get(i) - min) / range;
                yPoints[i] = padding + chartHeight - (int)(chartHeight * normalizedValue);
            }
            
            // Ligne
            for (int i = 0; i < pointCount - 1; i++) {
                g2d.draw(new Line2D.Float(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]));
            }
            
            // Points
            g2d.setColor(chartColor);
            for (int i = 0; i < pointCount; i++) {
                g2d.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
            }
        }
        
        // Axes
        g2d.setColor(new Color(148, 163, 184));
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(new Line2D.Float(padding, padding, padding, height - padding));
        g2d.draw(new Line2D.Float(padding, height - padding, width - padding, height - padding));
        
        g2d.dispose();
    }
}
