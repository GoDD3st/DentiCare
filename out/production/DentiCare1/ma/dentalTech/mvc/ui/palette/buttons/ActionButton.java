package ma.dentalTech.mvc.ui.palette.buttons;

import javax.swing.*;
import java.awt.*;

public class ActionButton extends JButton {
    
    public enum ButtonType {
        ADD(new Color(39, 174, 96)),      // #27ae60
        EDIT(new Color(243, 156, 18)),     // #f39c12
        DELETE(new Color(231, 76, 60)),     // #e74c3c
        VIEW(new Color(52, 152, 219)),     // #3498db
        PRINT(new Color(22, 160, 133)),     // #16a085
        START(new Color(39, 174, 96)),      // #27ae60
        END(new Color(230, 126, 34)),      // #e67e22
        CONFIRM(new Color(39, 174, 96)),   // #27ae60
        CANCEL(new Color(149, 165, 166)),  // #95a5a6
        EXPORT(new Color(155, 89, 182));   // #9b59b6
        
        private final Color color;
        
        ButtonType(Color color) {
            this.color = color;
        }
        
        public Color getColor() {
            return color;
        }
    }
    
    public ActionButton(String text, ButtonType type) {
        this(text, type, false);
    }
    
    public ActionButton(String text, ButtonType type, boolean small) {
        super(text);
        setBackground(type.getColor());
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, small ? 10 : 12));
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(small ? 80 : 120, small ? 28 : 36));
        
        // Effet hover
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(type.getColor().darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(type.getColor());
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
        g2.dispose();
        
        super.paintComponent(g);
    }
}
