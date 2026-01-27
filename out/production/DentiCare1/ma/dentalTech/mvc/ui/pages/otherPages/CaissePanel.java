package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.entities.enums.FactureStatutEnum;
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

        JButton btnCreate = new JButton("Créer une facture");
        btnCreate.setPreferredSize(new Dimension(160, 36));
        btnCreate.setBackground(new Color(46, 204, 113));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setBorderPainted(false);
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCreate.addActionListener(e -> showCreateFactureDialog());

        ActionButton btnStats = new ActionButton("Statistiques", ActionButton.ButtonType.VIEW);
        btnStats.setPreferredSize(new Dimension(140, 36));
        btnStats.addActionListener(e -> showStatistics());

        ActionButton btnExport = new ActionButton("Exporter", ActionButton.ButtonType.EXPORT);
        btnExport.setPreferredSize(new Dimension(120, 36));
        btnExport.addActionListener(e -> exportReport());

        ActionButton btnRefresh = new ActionButton("Actualiser", ActionButton.ButtonType.VIEW);
        btnRefresh.setPreferredSize(new Dimension(120, 36));
        btnRefresh.addActionListener(e -> loadCaisseData());

        buttonPanel.add(btnCreate);
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
                ViewCaisseDialog dialog = new ViewCaisseDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    caisseOpt.get()
                );
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des détails: " + e.getMessage(),
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
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);

            // Create icon buttons (view, edit, delete)
            JButton viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            JButton editBtn = createIconButton("edit", new Color(243, 156, 18)); // Edit icon
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
            JButton editBtn = createIconButton("edit", new Color(243, 156, 18)); // Edit icon
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
                btn.setText(iconName.equals("see") ? "O" : iconName.equals("edit") ? "*" : "X");
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            }
        } catch (Exception e) {
            // Fallback to text
            btn.setText(iconName.equals("see") ? "O" : iconName.equals("add") ? "*" : "X");
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        }

        return btn;
    }

    private void showCreateFactureDialog() {
        CreateFactureDialog dialog = new CreateFactureDialog(
            (Frame) SwingUtilities.getWindowAncestor(this)
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadCaisseData(); // Recharger les données
            JOptionPane.showMessageDialog(this,
                "Facture créée avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Dialog moderne pour créer une nouvelle facture
    private class CreateFactureDialog extends JDialog {
        private JTextField txtTotaleFacture, txtTotalePaye;
        private JComboBox<FactureStatutEnum> cmbStatut;
        private JSpinner spinnerDateFacture;
        private JComboBox<String> cmbPatient;
        private boolean saved = false;
        private java.util.Map<String, Long> patientMap = new java.util.HashMap<>();

        public CreateFactureDialog(Frame parent) {
            super(parent, "Créer une nouvelle facture", true);
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 650);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(46, 204, 113));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Créer une nouvelle facture");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);

            // Content
            JPanel contentPanel = new JPanel(new GridBagLayout());
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Initialize fields
            txtTotaleFacture = new JTextField();
            txtTotalePaye = new JTextField();
            cmbStatut = new JComboBox<>(FactureStatutEnum.values());
            spinnerDateFacture = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDateFacture, "dd/MM/yyyy");
            spinnerDateFacture.setEditor(dateEditor);
            spinnerDateFacture.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            cmbPatient = new JComboBox<>();

            // Load patients
            loadPatients();

            // Set default values
            txtTotalePaye.setText("0.0");
            cmbStatut.setSelectedItem(FactureStatutEnum.IMPAYEE);

            // Fields
            String[] labels = {"Patient:", "Montant total:", "Montant payé:", "Statut:", "Date de facture:"};
            JComponent[] components = {cmbPatient, txtTotaleFacture, txtTotalePaye, cmbStatut, spinnerDateFacture};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                if (components[i] instanceof JTextField || components[i] instanceof JComboBox || components[i] instanceof JSpinner) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                }
                contentPanel.add(components[i], gbc);
            }

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(248, 249, 250));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JButton btnSave = new JButton("Créer");
            JButton btnCancel = new JButton("Annuler");

            btnSave.setPreferredSize(new Dimension(100, 35));
            btnCancel.setPreferredSize(new Dimension(100, 35));

            btnSave.addActionListener(e -> saveFacture());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadPatients() {
            try {
                Object serviceObj = ApplicationContext.getBean("patientService");
                if (serviceObj instanceof ma.dentalTech.service.modules.patient.api.PatientService) {
                    ma.dentalTech.service.modules.patient.api.PatientService service =
                        (ma.dentalTech.service.modules.patient.api.PatientService) serviceObj;
                    java.util.List<ma.dentalTech.entities.Patient.Patient> patients = service.findAll();

                    cmbPatient.addItem("-- Sélectionner un patient --");
                    for (ma.dentalTech.entities.Patient.Patient p : patients) {
                        String displayText = p.getNom() + " (" + (p.getTelephone() != null ? p.getTelephone() : "N/A") + ")";
                        cmbPatient.addItem(displayText);
                        patientMap.put(displayText, p.getIdPatient());
                    }
                }
            } catch (Exception e) {
                cmbPatient.addItem("Erreur de chargement des patients");
                e.printStackTrace();
            }
        }

        private void saveFacture() {
            // Validation
            if (cmbPatient.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtTotaleFacture.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir le montant total", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double totaleFacture = Double.parseDouble(txtTotaleFacture.getText().trim());
                double totalePaye = Double.parseDouble(txtTotalePaye.getText().trim());
                double reste = totaleFacture - totalePaye;

                // Créer la facture
                Facture facture = Facture.builder()
                    .totaleFacture(totaleFacture)
                    .totalePaye(totalePaye)
                    .reste(reste)
                    .statut((FactureStatutEnum) cmbStatut.getSelectedItem())
                    .dateFacture(((java.util.Date) spinnerDateFacture.getValue()).toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                    .build();

                // Sauvegarder dans la base de données
                Object serviceObj = ApplicationContext.getBean("factureService");
                if (serviceObj instanceof ma.dentalTech.service.modules.facture.api.FactureService) {
                    ma.dentalTech.service.modules.facture.api.FactureService service =
                        (ma.dentalTech.service.modules.facture.api.FactureService) serviceObj;
                    service.create(facture);
                    saved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Service de facture non disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir des montants valides", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création de la facture: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog moderne pour voir les détails d'une transaction caisse
    private class ViewCaisseDialog extends JDialog {
        public ViewCaisseDialog(Frame parent, Caisse caisse) {
            super(parent, "Détails de la transaction", true);
            initializeDialog(caisse);
        }

        private void initializeDialog(Caisse caisse) {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 500);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(52, 152, 219));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Détails de la transaction");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);

            // Content
            JPanel contentPanel = new JPanel(new GridBagLayout());
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Préparer les données
            String id = caisse.getIdCaisse() != null ? caisse.getIdCaisse().toString() : "";
            String type = caisse.getModeEncaissement() != null ? caisse.getModeEncaissement().name() : "Transaction";
            String montant = caisse.getMontant() != null ? String.format("%.2f DH", caisse.getMontant()) : "0.00 DH";
            String date = caisse.getDateEncassement() != null ? caisse.getDateEncassement().toString() : "";
            String reference = caisse.getReference() != null ? caisse.getReference() : "";

            // Créer les champs en lecture seule
            JTextField txtId = new JTextField(id);
            JTextField txtType = new JTextField(type);
            JTextField txtMontant = new JTextField(montant);
            JTextField txtDate = new JTextField(date);
            JTextField txtReference = new JTextField(reference);

            // Désactiver tous les champs (lecture seule)
            txtId.setEditable(false);
            txtType.setEditable(false);
            txtMontant.setEditable(false);
            txtDate.setEditable(false);
            txtReference.setEditable(false);

            // Fond gris clair pour indiquer qu'ils sont en lecture seule
            Color readonlyBg = new Color(240, 240, 240);
            txtId.setBackground(readonlyBg);
            txtType.setBackground(readonlyBg);
            txtMontant.setBackground(readonlyBg);
            txtDate.setBackground(readonlyBg);
            txtReference.setBackground(readonlyBg);

            // Fields labels and components
            String[] labels = {"ID Transaction:", "Type:", "Montant:", "Date:", "Référence:"};
            JComponent[] components = {txtId, txtType, txtMontant, txtDate, txtReference};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                components[i].setPreferredSize(new Dimension(250, 30));
                contentPanel.add(components[i], gbc);
            }

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(248, 249, 250));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JButton btnClose = new JButton("Fermer");
            btnClose.setPreferredSize(new Dimension(100, 35));
            btnClose.addActionListener(e -> dispose());

            buttonPanel.add(btnClose);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }
}
