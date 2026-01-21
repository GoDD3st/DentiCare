package ma.dentalTech.mvc.ui.palette.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Table moderne avec style amélioré
 */
public class ModernTable extends JTable {
    
    // 🎨 Couleurs modernes SaaS selon spécifications
    private static final Color HEADER_BG = new Color(245, 247, 250); // #F5F7FA - Gris clair
    private static final Color HEADER_FG = new Color(38, 50, 56); // #263238 - Texte principal
    private static final Color ROW_EVEN = new Color(255, 255, 255); // #FFFFFF - Blanc
    private static final Color ROW_ODD = new Color(245, 247, 250); // #F5F7FA - Gris clair
    private static final Color ROW_HOVER = new Color(227, 242, 253); // #E3F2FD - Bleu très clair
    private static final Color BORDER_COLOR = new Color(227, 242, 253); // #E3F2FD - Bleu très clair
    
    public ModernTable(DefaultTableModel model) {
        super(model);
        // Ne pas appeler setupTable() ici, attendre que la table soit complètement initialisée
    }
    
    @Override
    public void addNotify() {
        super.addNotify();
        // Appeler setupTable() une fois que le composant est ajouté à sa hiérarchie parente
        SwingUtilities.invokeLater(() -> setupTable());
    }
    
    private void setupTable() {
        try {
            setRowHeight(40); // Hauteur confortable
            setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Police moderne
            setForeground(new Color(38, 50, 56)); // #263238 - Texte principal
            setBackground(Color.WHITE); // #FFFFFF
            setGridColor(BORDER_COLOR);
            setShowGrid(true);
            setIntercellSpacing(new Dimension(0, 0));
            setSelectionBackground(new Color(30, 136, 229, 20)); // #1E88E5 avec transparence
            setSelectionForeground(new Color(38, 50, 56)); // #263238

            // Header sera configuré dans configureHeader()
            configureHeader();
            
            // Renderer personnalisé pour les cellules - seulement si la table est initialisée
            if (getModel() != null) {
                setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        
                        // Couleur de fond alternée
                        if (!isSelected) {
                            c.setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                        }
                        
                        // Bordure gauche pour la première colonne
                        if (column == 0) {
                            ((JComponent) c).setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR),
                                BorderFactory.createEmptyBorder(4, 10, 4, 10)
                            ));
                        } else {
                            ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                        }
                        
                        return c;
                    }
                });
            }
        } catch (Exception e) {
            // Ignorer les erreurs d'initialisation - la table fonctionnera avec les valeurs par défaut
            System.err.println("Warning: Could not setup ModernTable: " + e.getMessage());
        }
    }

    private void configureHeader() {
        // Header personnalisé - configuré de manière différée si nécessaire
        try {
            JTableHeader header = getTableHeader();
            if (header != null) {
                header.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Police moderne
                header.setBackground(HEADER_BG);
                header.setForeground(HEADER_FG);
                header.setReorderingAllowed(false);
                header.setPreferredSize(new Dimension(Math.max(header.getWidth(), 400), 40)); // Hauteur confortable
            }
        } catch (Exception e) {
            // Ignore configuration errors - table will still work with default header
            System.err.println("Warning: Could not configure table header: " + e.getMessage());
        }
    }

    @Override
    public void setModel(javax.swing.table.TableModel dataModel) {
        super.setModel(dataModel);
        // Ne pas appeler setupTable() ici directement, utiliser invokeLater pour éviter les problèmes d'initialisation
        if (isDisplayable()) {
            SwingUtilities.invokeLater(() -> setupTable());
        }
    }

    @Override
    protected void configureEnclosingScrollPane() {
        try {
            super.configureEnclosingScrollPane();
            configureHeader();
        } catch (Exception e) {
            // Ignore configuration errors
            System.err.println("Warning: Could not configure table in scroll pane: " + e.getMessage());
        }
    }
}
