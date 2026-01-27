package ma.dentalTech.mvc.ui.palette.buttons;


import ma.dentalTech.mvc.ui.palette.utils.ImageTools;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PillNavButtonLight extends JButton {

    private boolean active = false;
    private boolean hover  = false;
    private boolean pressed = false;

    private final ImageIcon normalIcon;
    private final ImageIcon hoverIcon;

    // Couleurs dérivées de l’icône (comme ton MyButton)
    private final Color baseColor;      // couleur dominante icon
    private final Color accentColor;    // accent (hover/active)
    private final Color overlayHover;   // fond hover
    private final Color overlayActive;  // fond active

    // Style
    private final int radius = 22;

    public PillNavButtonLight(String text, ImageIcon icon) {

        this.normalIcon = icon;
        this.baseColor  = ImageTools.getDominantColor(icon);
        this.accentColor = baseColor.brighter();

        // Overlay léger (transparent)
        this.overlayHover  = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 28);
        this.overlayActive = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40);

        // Icône hover colorisée
        this.hoverIcon = ImageTools.applyColorOverlay(icon, accentColor, 0.35f);

        setText(text);
        setIcon(normalIcon);

        setFont(new Font("Optima", Font.BOLD, 18));
        setForeground(new Color(35, 35, 35)); // texte sombre (fond clair)

        setFocusPainted(false);
        setBorderPainted(true);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusable(false);

        setHorizontalAlignment(SwingConstants.LEFT);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setIconTextGap(12);

        // Padding interne (pill)
        setBorder(new EmptyBorder(10, 14, 10, 14));

        // Taille
        setPreferredSize(new Dimension(260, 52));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                hover = true;
                if (isEnabled()) {
                    setIcon(hoverIcon);
                    setForeground(accentColor.darker());
                }
                repaint();
            }

            @Override public void mouseExited(MouseEvent e) {
                hover = false;
                pressed = false;
                if (!active && isEnabled()) {
                    setIcon(normalIcon);
                    setForeground(new Color(35, 35, 35));
                }
                repaint();
            }

            @Override public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    pressed = true;
                    repaint();
                }
            }

            @Override public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;

        if (active) {
            setIcon(hoverIcon);
            setForeground(accentColor.darker());
        } else {
            setIcon(hover ? hoverIcon : normalIcon);
            setForeground(new Color(35, 35, 35));
        }
        repaint();
    }

    public boolean isActive() { return active; }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1) Bordure subtile pour indiquer que c'est un bouton cliquable
        g2.setColor(new Color(200, 200, 200, 50));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);

        // 2) Overlay (hover/active/pressed) sous forme de rectangle arrondi
        if (active || hover || pressed) {
            Color overlayColor;
            if (pressed) {
                overlayColor = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 120);
            } else {
                overlayColor = active ? overlayActive : overlayHover;
            }
            g2.setColor(overlayColor);
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        }

        // 2) Petite barre d’accent à gauche (option pro)
        if (active) {
            g2.setColor(accentColor);
            g2.fillRoundRect(6, 10, 6, getHeight() - 20, 6, 6);
        }

        // 4) Bordure plus visible au hover pour renforcer l'effet bouton
        if (hover) {
            g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 100));
            g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, radius-2, radius-2);
        }

        // 5) Petit indicateur "cliquable" (point discret à droite)
        if (isEnabled()) {
            g2.setColor(new Color(150, 150, 150, 80));
            int centerY = getHeight() / 2;
            g2.fillOval(getWidth() - 20, centerY - 2, 4, 4);
        }

        g2.dispose();

        // 3) Laisser Swing dessiner icône + texte par-dessus
        super.paintComponent(g);
    }
}
