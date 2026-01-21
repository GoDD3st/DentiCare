package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;
import ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class DossiersPanel extends JPanel {

    private final UserPrincipal principal;
    private final DossierMedicaleService dossierService;
    private JTable dossiersTable;
    private DefaultTableModel tableModel;

    public DossiersPanel(UserPrincipal principal) {
        this.principal = principal;
        this.dossierService = ApplicationContext.getBean(DossierMedicaleService.class);
        initializeUI();
        loadDossiers();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header
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

        JLabel title = new JLabel("Dossiers Médicaux");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));
        header.add(title, BorderLayout.WEST);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        ActionButton btnAdd = new ActionButton("Créer Dossier", ActionButton.ButtonType.ADD);
        btnAdd.setPreferredSize(new Dimension(160, 36));
        btnAdd.addActionListener(e -> showCreateDossierDialog());

        ActionButton btnRefresh = new ActionButton("Actualiser", ActionButton.ButtonType.VIEW);
        btnRefresh.setPreferredSize(new Dimension(120, 36));
        btnRefresh.addActionListener(e -> loadDossiers());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        // Table
        String[] columns = {"ID", "Patient", "Date Création", "Dernière Modification", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Actions column
            }
        };

        dossiersTable = new JTable(tableModel);
        dossiersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dossiersTable.setRowHeight(40);
        dossiersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        dossiersTable.getTableHeader().setBackground(new Color(248, 249, 250));
        dossiersTable.getTableHeader().setForeground(new Color(127, 140, 141));
        dossiersTable.setShowGrid(true);
        dossiersTable.setGridColor(new Color(238, 238, 238));
        dossiersTable.setSelectionBackground(new Color(230, 242, 255));
        dossiersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        dossiersTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        dossiersTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        dossiersTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        dossiersTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        dossiersTable.getColumnModel().getColumn(4).setPreferredWidth(200);

        // Set custom renderer and editor for Actions column
        dossiersTable.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());
        dossiersTable.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(dossiersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        content.add(scrollPane, BorderLayout.CENTER);

        return content;
    }

    private void loadDossiers() {
        tableModel.setRowCount(0);
        try {
            List<DossierMedicale> dossiers = dossierService.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (DossierMedicale dossier : dossiers) {
                String dateCreation = dossier.getDateDeCreation() != null 
                    ? dossier.getDateDeCreation().format(formatter) 
                    : "";
                String dateModif = dossier.getDateDerniereModification() != null 
                    ? dossier.getDateDerniereModification().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "";
                String patientName = dossier.getPatient() != null && dossier.getPatient().getNom() != null
                    ? dossier.getPatient().getNom()
                    : "N/A";
                
                tableModel.addRow(new Object[]{
                    dossier.getIdDossier(),
                    patientName,
                    dateCreation,
                    dateModif,
                    ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des dossiers: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showCreateDossierDialog() {
        JOptionPane.showMessageDialog(this, 
            "Fonctionnalité de création de dossier médical à implémenter.\n" +
            "Cette fonction permettra de créer un nouveau dossier médical pour un patient.",
            "Fonctionnalité à venir",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleViewAction(int row) {
        Long dossierId = (Long) tableModel.getValueAt(row, 0);
        try {
            Optional<DossierMedicale> dossierOpt = dossierService.findByID(dossierId);
            if (dossierOpt.isPresent()) {
                DossierMedicale d = dossierOpt.get();
                String info = String.format(
                    "ID: %d\nPatient: %s\nDate Création: %s",
                    d.getIdDossier(),
                    d.getPatient() != null ? d.getPatient().getNom() : "N/A",
                    d.getDateDeCreation() != null ? d.getDateDeCreation().toString() : ""
                );
                JOptionPane.showMessageDialog(this, info, "Détails Dossier", JOptionPane.INFORMATION_MESSAGE);
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
        Long dossierId = (Long) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ce dossier médical ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Optional<DossierMedicale> dossierOpt = dossierService.findByID(dossierId);
                if (dossierOpt.isPresent()) {
                    dossierService.delete(dossierOpt.get());
                    JOptionPane.showMessageDialog(this, "Dossier supprimé avec succès");
                    loadDossiers();
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
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);

            // Create icon buttons (view, edit, delete)
            JButton viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            JButton editBtn = createIconButton("add", new Color(243, 156, 18)); // Edit icon
            JButton deleteBtn = createIconButton("delete", new Color(231, 76, 60)); // Delete icon

            panel.add(viewBtn);
            panel.add(editBtn);
            panel.add(deleteBtn);

            return panel;
        }
    }

    // Custom editor for action buttons
    private class ActionButtonEditor extends DefaultCellEditor {
        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);
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
                fireEditingStopped();
                handleViewAction(targetRow);
            });

            editBtn.addActionListener(e -> {
                fireEditingStopped();
                handleEditAction(targetRow);
            });

            deleteBtn.addActionListener(e -> {
                fireEditingStopped();
                handleDeleteAction(targetRow);
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
