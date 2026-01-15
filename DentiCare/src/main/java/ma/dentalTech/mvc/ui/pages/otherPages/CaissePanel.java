package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;
import ma.dentalTech.service.modules.finance.api.CaisseService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class CaissePanel extends JPanel {

    private final UserPrincipal principal;
    private final CaisseService caisseService;
    private JTable caisseTable;
    private DefaultTableModel tableModel;

    public CaissePanel(UserPrincipal principal) {
        this.principal = principal;
        this.caisseService = ApplicationContext.getBean(CaisseService.class);
        initializeUI();
        loadCaisseData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header with stats
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = createContent();
        add(content, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Gestion de la Caisse");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));
        header.add(title, BorderLayout.WEST);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        ActionButton btnStats = new ActionButton("Statistiques", ActionButton.ButtonType.VIEW);
        btnStats.setPreferredSize(new Dimension(140, 36));
        btnStats.addActionListener(e -> showStatistics());

        ActionButton btnExport = new ActionButton("Exporter", ActionButton.ButtonType.EXPORT);
        btnExport.setPreferredSize(new Dimension(120, 36));
        btnExport.addActionListener(e -> exportReport());

        ActionButton btnRefresh = new ActionButton("Actualiser", ActionButton.ButtonType.VIEW);
        btnRefresh.setPreferredSize(new Dimension(120, 36));
        btnRefresh.addActionListener(e -> loadCaisseData());

        buttonPanel.add(btnStats);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnRefresh);
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        // Stats cards
        JPanel statsPanel = createStatsPanel();
        content.add(statsPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Type", "Montant", "Date", "Description", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Actions column
            }
        };

        caisseTable = new JTable(tableModel);
        caisseTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        caisseTable.setRowHeight(40);
        caisseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        caisseTable.getTableHeader().setBackground(new Color(248, 249, 250));
        caisseTable.getTableHeader().setForeground(new Color(127, 140, 141));
        caisseTable.setShowGrid(true);
        caisseTable.setGridColor(new Color(238, 238, 238));
        caisseTable.setSelectionBackground(new Color(230, 242, 255));
        caisseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        caisseTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        caisseTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        caisseTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        caisseTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        caisseTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        caisseTable.getColumnModel().getColumn(5).setPreferredWidth(200);

        // Set custom renderer and editor for Actions column
        caisseTable.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        caisseTable.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(caisseTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        content.add(scrollPane, BorderLayout.CENTER);

        return content;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Card 1: Total Entrées
        JPanel card1 = createStatCard("Total Entrées", "0.00 DH", new Color(39, 174, 96));
        statsPanel.add(card1);

        // Card 2: Total Sorties
        JPanel card2 = createStatCard("Total Sorties", "0.00 DH", new Color(231, 76, 60));
        statsPanel.add(card2);

        // Card 3: Solde
        JPanel card3 = createStatCard("Solde Actuel", "0.00 DH", new Color(52, 152, 219));
        statsPanel.add(card3);

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(127, 140, 141));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadCaisseData() {
        tableModel.setRowCount(0);
        try {
            List<Caisse> caisseList = caisseService.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (Caisse caisse : caisseList) {
                String date = caisse.getDateEncassement() != null 
                    ? caisse.getDateEncassement().format(formatter)
                    : "";
                String montant = caisse.getMontant() != null 
                    ? String.format("%.2f DH", caisse.getMontant())
                    : "0.00 DH";
                String description = caisse.getReference() != null ? caisse.getReference() : "Transaction";
                
                tableModel.addRow(new Object[]{
                    caisse.getIdCaisse(),
                    caisse.getModeEncaissement() != null ? caisse.getModeEncaissement().name() : "Transaction",
                    montant,
                    date,
                    description,
                    ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des données: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showStatistics() {
        JOptionPane.showMessageDialog(this,
            "Statistiques de la caisse:\n" +
            "- Total Entrées: 0.00 DH\n" +
            "- Total Sorties: 0.00 DH\n" +
            "- Solde: 0.00 DH",
            "Statistiques",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportReport() {
        JOptionPane.showMessageDialog(this,
            "Fonctionnalité d'export à implémenter.\n" +
            "Cette fonction permettra d'exporter les données de la caisse en format PDF/Excel.",
            "Fonctionnalité à venir",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleViewAction(int row) {
        Long caisseId = (Long) tableModel.getValueAt(row, 0);
        try {
            Optional<Caisse> caisseOpt = caisseService.findByID(caisseId);
            if (caisseOpt.isPresent()) {
                Caisse c = caisseOpt.get();
                String info = String.format(
                    "ID: %d\nType: %s\nMontant: %.2f DH\nDate: %s\nRéférence: %s",
                    c.getIdCaisse(),
                    c.getModeEncaissement() != null ? c.getModeEncaissement().name() : "Transaction",
                    c.getMontant() != null ? c.getMontant() : 0.0,
                    c.getDateEncassement() != null ? c.getDateEncassement().toString() : "",
                    c.getReference() != null ? c.getReference() : ""
                );
                JOptionPane.showMessageDialog(this, info, "Détails Transaction", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEditAction(int row) {
        JOptionPane.showMessageDialog(this, "Fonctionnalité de modification à implémenter");
    }

    private void handleDeleteAction(int row) {
        Long caisseId = (Long) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer cette transaction ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Optional<Caisse> caisseOpt = caisseService.findByID(caisseId);
                if (caisseOpt.isPresent()) {
                    caisseService.delete(caisseOpt.get());
                    JOptionPane.showMessageDialog(this, "Transaction supprimée avec succès");
                    loadCaisseData();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Custom renderer for action buttons
    private class ActionButtonRenderer implements javax.swing.table.TableCellRenderer {
        private JPanel panel;
        private ActionButton viewBtn, editBtn, deleteBtn;

        public ActionButtonRenderer() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);
            
            viewBtn = new ActionButton("Voir", ActionButton.ButtonType.VIEW, true);
            editBtn = new ActionButton("Modifier", ActionButton.ButtonType.EDIT, true);
            deleteBtn = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE, true);
            
            panel.add(viewBtn);
            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return panel;
        }
    }

    // Custom editor for action buttons
    private class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private ActionButton viewBtn, editBtn, deleteBtn;
        private int currentRow;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);
            
            viewBtn = new ActionButton("Voir", ActionButton.ButtonType.VIEW, true);
            editBtn = new ActionButton("Modifier", ActionButton.ButtonType.EDIT, true);
            deleteBtn = new ActionButton("Supprimer", ActionButton.ButtonType.DELETE, true);
            
            viewBtn.addActionListener(e -> {
                fireEditingStopped();
                handleViewAction(currentRow);
            });
            
            editBtn.addActionListener(e -> {
                fireEditingStopped();
                handleEditAction(currentRow);
            });
            
            deleteBtn.addActionListener(e -> {
                fireEditingStopped();
                handleDeleteAction(currentRow);
            });
            
            panel.add(viewBtn);
            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}
