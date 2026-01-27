package ma.dentalTech.mvc.ui.palette.menu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import ma.dentalTech.mvc.ui.palette.utils.ImageTools;

public class MyMenuBar extends JMenuBar {

    // 🎨 Palette (à ajuster selon ton thème)
    private static final Color MENU_BG        = new Color(32, 34, 37);
    private static final Color MENU_FG        = Color.WHITE;
    private static final Color MENU_HOVER_BG  = new Color(60, 63, 65);

    private static final Font MENU_FONT =
            new Font("Optima", Font.PLAIN, 16);

    public MyMenuBar(ActionListener onLogout, ActionListener onExit) {

        setBackground(MENU_BG);
        setBorder(new EmptyBorder(4, 8, 4, 8));

        // Create session button instead of menu
        JButton sessionButton = buildSessionButton(onLogout, onExit);

        add(sessionButton);
    }

    // ———————————— Button Builder
    private JButton buildSessionButton(ActionListener onLogout, ActionListener onExit) {
        JButton button = new JButton();
        button.setIcon(ImageTools.loadIcon("menu", 24, 24)); // Use menu icon instead of user
        button.setBackground(MENU_BG);
        button.setBorder(new EmptyBorder(6, 12, 6, 12));
        button.setFocusPainted(false);
        button.setOpaque(true);

        // Create popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(MENU_BG));
        popupMenu.setBackground(MENU_BG);

        // Add menu items
        JMenuItem logout = buildMenuItem("Déconnexion", "user", onLogout);
        JMenuItem exit = buildMenuItem("Quitter", "cabinet", onExit);

        popupMenu.add(logout);
        popupMenu.addSeparator();
        popupMenu.add(exit);

        // Show popup when button is clicked
        button.addActionListener(e -> {
            popupMenu.show(button, 0, button.getHeight());
        });

        // Hover effect for button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(MENU_HOVER_BG);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(MENU_BG);
            }
        });

        return button;
    }

    private JMenuItem buildMenuItem(String text, String iconPath, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(MENU_FONT);
        item.setForeground(MENU_FG);
        item.setIcon(ImageTools.loadIcon(iconPath, 20, 20));
        item.setOpaque(true);
        item.setBackground(MENU_BG);
        item.setBorder(new EmptyBorder(6, 12, 6, 12));

        item.addActionListener(action);

        // Hover effect
        item.addChangeListener(e -> {
            ButtonModel model = item.getModel();
            if (model.isArmed() || model.isSelected()) {
                item.setBackground(MENU_HOVER_BG);
            } else {
                item.setBackground(MENU_BG);
            }
        });

        return item;
    }
}
