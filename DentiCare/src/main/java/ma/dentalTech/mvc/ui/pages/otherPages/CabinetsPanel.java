package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CabinetsPanel extends JPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);
    
    private JTable cabinetsTable;
    private DefaultTableModel cabinetsModel;
    private final UserPrincipal principal;

    public CabinetsPanel(UserPrincipal principal) {
        this.principal = principal;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        initializePanel();
    }

    private void initializePanel() {
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel mainTitle = new JLabel("Administration - Gestion des Cabinets");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainTitle.setForeground(new Color(52, 58, 64));

        JLabel subtitle = new JLabel("Tableau de bord - " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy", java.util.Locale.FRENCH)));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(108, 117, 125));

        titlePanel.add(mainTitle, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.CENTER);

        // Card container
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Section header
        JLabel sectionTitle = new JLabel("Gestion des Cabinets Médicaux");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Toolbar avec boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));

        ActionButton btnCreate = new ActionButton("Créer un cabinet", ActionButton.ButtonType.ADD);
        btnCreate.setPreferredSize(new Dimension(160, 36));
        btnCreate.addActionListener(e -> showCreateCabinetDialog());

        ActionButton btnDelete = new ActionButton("Supprimer sélectionné", ActionButton.ButtonType.DELETE);
        btnDelete.setPreferredSize(new Dimension(160, 36));
        btnDelete.addActionListener(e -> deleteSelectedCabinet());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnDelete);

        // Table avec actions
        String[] cabinetColumns = {"Nom", "Adresse", "Téléphone", "Médecins", "Statut", "Actions"};
        cabinetsModel = new DefaultTableModel(cabinetColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Actions column is editable
            }
        };

        loadCabinetsData();

        cabinetsTable = createTableWithActions(cabinetsModel, 5);
        cabinetsTable.setRowHeight(40);
        cabinetsTable.setShowGrid(true);
        cabinetsTable.setGridColor(new Color(240, 240, 240));
        
        // Style header
        JTableHeader header = cabinetsTable.getTableHeader();
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(new Color(52, 58, 64));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);

        // Alternating row colors
        cabinetsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                }
                return c;
            }
        });

        card.add(sectionTitle, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);
        card.add(new JScrollPane(cabinetsTable), BorderLayout.SOUTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadCabinetsData() {
        cabinetsModel.setRowCount(0);
        
        // Try to get the service, but handle gracefully if not available
        try {
            Object serviceObj = ApplicationContext.getBean("cabinetMedicaleService");
            if (serviceObj != null && serviceObj instanceof ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) {
                ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService cabinetService = 
                    (ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) serviceObj;
                
                List<CabinetMedicale> cabinets = cabinetService.findAll();
                
                for (CabinetMedicale cabinet : cabinets) {
                    String nom = cabinet.getNom() != null ? cabinet.getNom() : "";
                    String adresse = "";
                    if (cabinet.getAdresse() != null) {
                        adresse = cabinet.getAdresse().toString();
                    }
                    String telephone = cabinet.getTel1() != null ? cabinet.getTel1() : "";
                    
                    // Count doctors - you might need to implement this in the service
                    int nombreMedecins = 0; // TODO: Get actual count from service
                    
                    String statut = "Actif"; // You can determine this based on your logic
                    
                    cabinetsModel.addRow(new Object[]{nom, adresse, telephone, String.valueOf(nombreMedecins), statut, ""});
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des cabinets: " + e.getMessage());
            // Don't show error dialog if service is not available, just log it
            if (!e.getMessage().contains("Bean non trouvé")) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des cabinets: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JTable createTableWithActions(DefaultTableModel model, int actionsColumnIndex) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        // Custom renderer and editor for action buttons with icons
        table.getColumnModel().getColumn(actionsColumnIndex).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(actionsColumnIndex).setCellEditor(new ActionButtonEditor());

        return table;
    }

    private class ActionButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);

            // Create icon buttons
            JButton viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            JButton editBtn = createIconButton("add", new Color(243, 156, 18)); // Edit icon
            JButton deleteBtn = createIconButton("delete", new Color(231, 76, 60)); // Delete icon

            panel.add(viewBtn);
            panel.add(editBtn);
            panel.add(deleteBtn);

            return panel;
        }

        private JButton createIconButton(String iconName, Color bgColor) {
            JButton btn = new JButton();
            btn.setBackground(bgColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(32, 32));

            // Load icon from resources
            try {
                String iconPath = "/icons/" + iconName + ".png";
                java.net.URL iconURL = getClass().getResource(iconPath);
                if (iconURL != null) {
                    ImageIcon icon = new ImageIcon(iconURL);
                    // Scale icon to fit button
                    Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(scaledImage));
                } else {
                    // Fallback to text if icon not found
                    btn.setText(iconName.equals("see") ? "O" : iconName.equals("add") ? "*" : "X");
                    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
                }
            } catch (Exception e) {
                // Fallback to text
                btn.setText(iconName.equals("see") ? "O" : iconName.equals("add") ? "*" : "X");
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            }

            return btn;
        }
    }

    private class ActionButtonEditor extends DefaultCellEditor {
        public ActionButtonEditor() {
            super(new JCheckBox());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);

            JButton viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            JButton editBtn = createIconButton("add", new Color(243, 156, 18)); // Edit icon
            JButton deleteBtn = createIconButton("delete", new Color(231, 76, 60)); // Delete icon

            final int targetRow = row;

            viewBtn.addActionListener(e -> {
                String nom = (String) cabinetsModel.getValueAt(targetRow, 0);
                String adresse = (String) cabinetsModel.getValueAt(targetRow, 1);
                String telephone = (String) cabinetsModel.getValueAt(targetRow, 2);
                String medecins = (String) cabinetsModel.getValueAt(targetRow, 3);
                JOptionPane.showMessageDialog(CabinetsPanel.this,
                    "Détails du cabinet:\n\n" +
                    "Nom: " + nom + "\n" +
                    "Adresse: " + adresse + "\n" +
                    "Téléphone: " + telephone + "\n" +
                    "Nombre de médecins: " + medecins,
                    "Détails",
                    JOptionPane.INFORMATION_MESSAGE);
            });

            editBtn.addActionListener(e -> {
                showEditCabinetDialog(targetRow);
            });

            deleteBtn.addActionListener(e -> {
                deleteCabinet(targetRow);
            });

            panel.add(viewBtn);
            panel.add(editBtn);
            panel.add(deleteBtn);

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        private JButton createIconButton(String iconName, Color bgColor) {
            JButton btn = new JButton();
            btn.setBackground(bgColor);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setPreferredSize(new Dimension(32, 32));

            // Load icon from resources
            try {
                String iconPath = "/icons/" + iconName + ".png";
                java.net.URL iconURL = getClass().getResource(iconPath);
                if (iconURL != null) {
                    ImageIcon icon = new ImageIcon(iconURL);
                    // Scale icon to fit button
                    Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(scaledImage));
                } else {
                    // Fallback to text if icon not found
                    btn.setText(iconName.equals("see") ? "O" : iconName.equals("add") ? "*" : "X");
                    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
                }
            } catch (Exception e) {
                // Fallback to text
                btn.setText(iconName.equals("see") ? "O" : iconName.equals("add") ? "*" : "X");
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            }

            return btn;
        }
    }

    private void showCreateCabinetDialog() {
        JOptionPane.showMessageDialog(this,
            "Fonctionnalité de création de cabinet à implémenter.\n" +
            "Utilisez le dashboard administrateur pour créer des cabinets.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEditCabinetDialog(int row) {
        if (row < 0 || row >= cabinetsModel.getRowCount()) {
            return;
        }

        JOptionPane.showMessageDialog(this,
            "Fonctionnalité de modification de cabinet à implémenter.\n" +
            "Utilisez le dashboard administrateur pour modifier des cabinets.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedCabinet() {
        if (cabinetsTable == null) {
            JOptionPane.showMessageDialog(this, "Aucune table disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = cabinetsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cabinet à supprimer", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        deleteCabinet(selectedRow);
    }

    private void deleteCabinet(int row) {
        if (row < 0 || row >= cabinetsModel.getRowCount()) {
            return;
        }

        String nom = (String) cabinetsModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer le cabinet \"" + nom + "\" ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Implement actual deletion from database
            cabinetsModel.removeRow(row);
            JOptionPane.showMessageDialog(this,
                "Cabinet supprimé avec succès !",
                "Suppression réussie",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
