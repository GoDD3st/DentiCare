package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;
import ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService;
import ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService;
import ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService;
import ma.dentalTech.service.modules.dossierMedicale.api.CertificatService;
import ma.dentalTech.service.modules.dossierMedicale.api.InterventionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DossiersPanel extends JPanel {

    private final UserPrincipal principal;
    private final DossierMedicaleService dossierService;
    private final ConsultationService consultationService;
    private final OrdonnanceService ordonnanceService;
    private final CertificatService certificatService;
    private final InterventionService interventionService;
    private JTable dossiersTable;
    private DefaultTableModel tableModel;

    public DossiersPanel(UserPrincipal principal) {
        this.principal = principal;
        this.dossierService = ApplicationContext.getBean(DossierMedicaleService.class);
        this.consultationService = ApplicationContext.getBean(ConsultationService.class);
        this.ordonnanceService = ApplicationContext.getBean(OrdonnanceService.class);
        this.certificatService = ApplicationContext.getBean(CertificatService.class);
        this.interventionService = ApplicationContext.getBean(InterventionService.class);
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
        CreateDossierDialog dialog = new CreateDossierDialog(
            (Frame) SwingUtilities.getWindowAncestor(this)
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadDossiers(); // Recharger les données
            JOptionPane.showMessageDialog(this,
                "Dossier médical créé avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleViewAction(int row) {
        Long dossierId = (Long) tableModel.getValueAt(row, 0);
        try {
            Optional<DossierMedicale> dossierOpt = dossierService.findByID(dossierId);
            if (dossierOpt.isPresent()) {
                ViewDossierDialog dialog = new ViewDossierDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    dossierOpt.get()
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
        Long dossierId = (Long) tableModel.getValueAt(row, 0);
        try {
            Optional<DossierMedicale> dossierOpt = dossierService.findByID(dossierId);
            if (dossierOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Dossier médical introuvable",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            EditDossierDialog dialog = new EditDossierDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    dossierOpt.get()
            );
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                loadDossiers();
                JOptionPane.showMessageDialog(this,
                        "Dossier médical modifié avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement du dossier: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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

    // Dialog moderne pour créer un nouveau dossier médical
    private class CreateDossierDialog extends JDialog {
        private JComboBox<String> cmbPatient;
        private JSpinner spinnerDateCreation;
        private boolean saved = false;
        private java.util.Map<String, Long> patientMap = new java.util.HashMap<>();

        public CreateDossierDialog(Frame parent) {
            super(parent, "Créer un nouveau dossier médical", true);
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 500);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(46, 204, 113));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Créer un nouveau dossier médical");
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
            cmbPatient = new JComboBox<>();
            spinnerDateCreation = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDateCreation, "dd/MM/yyyy");
            spinnerDateCreation.setEditor(dateEditor);
            spinnerDateCreation.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));

            // Load patients
            loadPatients();

            // Fields
            String[] labels = {"Patient:", "Date de création:"};
            JComponent[] components = {cmbPatient, spinnerDateCreation};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                if (components[i] instanceof JComboBox || components[i] instanceof JSpinner) {
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

            btnSave.addActionListener(e -> saveDossier());
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

        private void saveDossier() {
            // Validation
            if (cmbPatient.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Récupérer l'ID du patient sélectionné
                String selectedPatient = (String) cmbPatient.getSelectedItem();
                Long patientId = patientMap.get(selectedPatient);

                // Créer le patient (temporairement pour la relation)
                ma.dentalTech.entities.Patient.Patient patient = null;
                try {
                    Object patientServiceObj = ApplicationContext.getBean("patientService");
                    if (patientServiceObj instanceof ma.dentalTech.service.modules.patient.api.PatientService) {
                        ma.dentalTech.service.modules.patient.api.PatientService patientService =
                            (ma.dentalTech.service.modules.patient.api.PatientService) patientServiceObj;
                        Optional<ma.dentalTech.entities.Patient.Patient> patientOpt = patientService.findByID(patientId);
                        if (patientOpt.isPresent()) {
                            patient = patientOpt.get();
                        }
                    }
                } catch (Exception e) {
                    // Ignore patient loading error for now
                }

                // Créer le dossier médical
                DossierMedicale dossier = DossierMedicale.builder()
                    .dateDeCreation(((java.util.Date) spinnerDateCreation.getValue()).toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                    .patient(patient)
                    .build();

                // Sauvegarder dans la base de données
                Object serviceObj = ApplicationContext.getBean("dossierMedicaleService");
                if (serviceObj instanceof ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService) {
                    ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService service =
                        (ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService) serviceObj;
                    service.create(dossier);
                    saved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Service de dossier médical non disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création du dossier médical: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog moderne pour modifier un dossier médical existant
    private class EditDossierDialog extends JDialog {
        private JComboBox<String> cmbPatient;
        private boolean saved = false;
        private final DossierMedicale dossierOriginal;
        private final java.util.Map<String, Long> patientMap = new java.util.HashMap<>();

        public EditDossierDialog(Frame parent, DossierMedicale dossier) {
            super(parent, "Modifier le dossier médical", true);
            this.dossierOriginal = dossier;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 400);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(243, 156, 18));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Modifier le dossier médical");
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

            // Champs
            JTextField txtId = new JTextField(
                    dossierOriginal.getIdDossier() != null ? dossierOriginal.getIdDossier().toString() : "");
            txtId.setEditable(false);
            txtId.setBackground(new Color(240, 240, 240));

            cmbPatient = new JComboBox<>();
            loadPatientsForEdit();

            String[] labels = {"ID Dossier:", "Patient:"};
            JComponent[] components = {txtId, cmbPatient};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                components[i].setPreferredSize(new Dimension(250, 30));
                contentPanel.add(components[i], gbc);
            }

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(248, 249, 250));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JButton btnSave = new JButton("Enregistrer");
            JButton btnCancel = new JButton("Annuler");

            btnSave.setPreferredSize(new Dimension(120, 35));
            btnCancel.setPreferredSize(new Dimension(100, 35));

            btnSave.addActionListener(e -> saveChanges());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadPatientsForEdit() {
            try {
                Object serviceObj = ApplicationContext.getBean("patientService");
                if (serviceObj instanceof ma.dentalTech.service.modules.patient.api.PatientService service) {
                    java.util.List<ma.dentalTech.entities.Patient.Patient> patients = service.findAll();

                    cmbPatient.addItem("-- Sélectionner un patient --");
                    String currentDisplay = null;

                    for (ma.dentalTech.entities.Patient.Patient p : patients) {
                        String displayText = p.getNom() + " (" +
                                (p.getTelephone() != null ? p.getTelephone() : "N/A") + ")";
                        cmbPatient.addItem(displayText);
                        patientMap.put(displayText, p.getIdPatient());

                        if (dossierOriginal.getPatient() != null
                                && dossierOriginal.getPatient().getIdPatient() != null
                                && dossierOriginal.getPatient().getIdPatient().equals(p.getIdPatient())) {
                            currentDisplay = displayText;
                        }
                    }

                    if (currentDisplay != null) {
                        cmbPatient.setSelectedItem(currentDisplay);
                    } else {
                        cmbPatient.setSelectedIndex(0);
                    }
                }
            } catch (Exception e) {
                cmbPatient.addItem("Erreur de chargement des patients");
                e.printStackTrace();
            }
        }

        private void saveChanges() {
            if (cmbPatient.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner un patient",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String selectedPatient = (String) cmbPatient.getSelectedItem();
                Long patientId = patientMap.get(selectedPatient);

                // Recharger l'entité patient complète
                ma.dentalTech.entities.Patient.Patient patient = null;
                try {
                    Object patientServiceObj = ApplicationContext.getBean("patientService");
                    if (patientServiceObj instanceof ma.dentalTech.service.modules.patient.api.PatientService patientService) {
                        Optional<ma.dentalTech.entities.Patient.Patient> patientOpt = patientService.findByID(patientId);
                        if (patientOpt.isPresent()) {
                            patient = patientOpt.get();
                        }
                    }
                } catch (Exception e) {
                    // ignore, on gérera plus bas
                }

                if (patient == null) {
                    JOptionPane.showMessageDialog(this,
                            "Patient introuvable",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DossierMedicale toUpdate = DossierMedicale.builder()
                        .idDossier(dossierOriginal.getIdDossier())
                        .idEntite(dossierOriginal.getIdEntite())
                        .patient(patient)
                        .creePar(dossierOriginal.getCreePar())
                        .build();

                try {
                    dossierService.update(dossierOriginal.getIdDossier(), toUpdate);
                    saved = true;
                    dispose();
                } catch (RuntimeException ex) {
                    // Message métier (ex: unicité du dossier)
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(),
                            "Erreur métier",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour du dossier médical: " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog moderne pour voir les détails d'un dossier médical,
    // incluant l'historique complet (consultations, ordonnances, certificats, interventions)
    private class ViewDossierDialog extends JDialog {
        public ViewDossierDialog(Frame parent, DossierMedicale dossier) {
            super(parent, "Détails du dossier médical", true);
            initializeDialog(dossier);
        }

        private void initializeDialog(DossierMedicale dossier) {
            setLayout(new BorderLayout(20, 20));
            setSize(900, 600);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(52, 152, 219));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            String patientLabel = dossier.getPatient() != null && dossier.getPatient().getNom() != null
                    ? dossier.getPatient().getNom()
                    : "Patient inconnu";

            JLabel titleLabel = new JLabel("Dossier médical de : " + patientLabel);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);

            // Onglets
            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Résumé", createResumePanel(dossier));
            tabbedPane.addTab("Consultations", createConsultationsPanel(dossier));
            tabbedPane.addTab("Ordonnances", createOrdonnancesPanel(dossier));
            tabbedPane.addTab("Certificats", createCertificatsPanel(dossier));
            tabbedPane.addTab("Interventions", createInterventionsPanel(dossier));

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(248, 249, 250));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));

            JButton btnClose = new JButton("Fermer");
            btnClose.setPreferredSize(new Dimension(100, 35));
            btnClose.addActionListener(e -> dispose());

            buttonPanel.add(btnClose);

            add(headerPanel, BorderLayout.NORTH);
            add(tabbedPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private JPanel createResumePanel(DossierMedicale dossier) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            String id = dossier.getIdDossier() != null ? dossier.getIdDossier().toString() : "";
            String dateCreation = dossier.getDateDeCreation() != null ? dossier.getDateDeCreation().toString() : "";
            String patientNom = dossier.getPatient() != null ? dossier.getPatient().getNom() : "N/A";
            String patientTelephone = dossier.getPatient() != null && dossier.getPatient().getTelephone() != null
                    ? dossier.getPatient().getTelephone()
                    : "N/A";
            String medecinNom = dossier.getMedecin() != null ? dossier.getMedecin().getNom() : "Non assigné";

            JTextField txtId = new JTextField(id);
            JTextField txtDateCreation = new JTextField(dateCreation);
            JTextField txtPatientNom = new JTextField(patientNom);
            JTextField txtPatientTelephone = new JTextField(patientTelephone);
            JTextField txtMedecin = new JTextField(medecinNom);

            Color readonlyBg = new Color(240, 240, 240);
            for (JTextField f : new JTextField[]{txtId, txtDateCreation, txtPatientNom, txtPatientTelephone, txtMedecin}) {
                f.setEditable(false);
                f.setBackground(readonlyBg);
            }

            String[] labels = {"ID Dossier:", "Date de création:", "Nom du patient:", "Téléphone patient:", "Médecin:"};
            JComponent[] components = {txtId, txtDateCreation, txtPatientNom, txtPatientTelephone, txtMedecin};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.weightx = 0.0;
                panel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                components[i].setPreferredSize(new Dimension(300, 28));
                panel.add(components[i], gbc);
            }

            return panel;
        }

        private JPanel createConsultationsPanel(DossierMedicale dossier) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            String[] columns = {"Date", "Heure", "Statut", "Observation"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            try {
                List<Consultation> all = consultationService.findAll();
                List<Consultation> consultations = all.stream()
                        .filter(c -> c.getDossierMedicale() != null
                                && c.getDossierMedicale().getIdDossier() != null
                                && c.getDossierMedicale().getIdDossier().equals(dossier.getIdDossier()))
                        .sorted((c1, c2) -> {
                            if (c1.getDateConsultation() == null) return 1;
                            if (c2.getDateConsultation() == null) return -1;
                            return c1.getDateConsultation().compareTo(c2.getDateConsultation());
                        })
                        .collect(Collectors.toList());

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (Consultation c : consultations) {
                    String date = c.getDateConsultation() != null ? c.getDateConsultation().format(dateFormatter) : "";
                    String heure = c.getHeureConsultation() != null ? c.getHeureConsultation().toString() : "";
                    String statut = c.getStatut() != null ? c.getStatut().name() : "";
                    String obs = c.getObservationMedecin() != null ? c.getObservationMedecin() : "";
                    model.addRow(new Object[]{date, heure, statut, obs});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JTable table = new JTable(model);
            table.setRowHeight(28);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            panel.add(new JScrollPane(table), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createOrdonnancesPanel(DossierMedicale dossier) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            String[] columns = {"Date", "Note"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            try {
                List<Ordonnance> all = ordonnanceService.findAll();
                List<Ordonnance> ordonnances = all.stream()
                        .filter(o -> o.getDossierMedicale() != null
                                && o.getDossierMedicale().getIdDossier() != null
                                && o.getDossierMedicale().getIdDossier().equals(dossier.getIdDossier()))
                        .sorted((o1, o2) -> {
                            if (o1.getDate() == null) return 1;
                            if (o2.getDate() == null) return -1;
                            return o1.getDate().compareTo(o2.getDate());
                        })
                        .collect(Collectors.toList());

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (Ordonnance o : ordonnances) {
                    String date = o.getDate() != null ? o.getDate().format(dateFormatter) : "";
                    String note = o.getNote() != null ? o.getNote() : "";
                    model.addRow(new Object[]{date, note});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JTable table = new JTable(model);
            table.setRowHeight(28);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            panel.add(new JScrollPane(table), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createCertificatsPanel(DossierMedicale dossier) {
            JPanel panel = new JPanel(new BorderLayout());

            String[] columns = {"Date début", "Date fin", "Durée (jours)", "Note médecin"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            try {
                List<Certificat> all = certificatService.findAll();
                List<Certificat> certificats = all.stream()
                        .filter(c -> c.getDossierMedicale() != null
                                && c.getDossierMedicale().getIdDossier() != null
                                && c.getDossierMedicale().getIdDossier().equals(dossier.getIdDossier()))
                        .sorted((c1, c2) -> {
                            if (c1.getDateDebut() == null) return 1;
                            if (c2.getDateDebut() == null) return -1;
                            return c1.getDateDebut().compareTo(c2.getDateDebut());
                        })
                        .collect(Collectors.toList());

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (Certificat c : certificats) {
                    String debut = c.getDateDebut() != null ? c.getDateDebut().format(dateFormatter) : "";
                    String fin = c.getDateFin() != null ? c.getDateFin().format(dateFormatter) : "";
                    String duree = String.valueOf(c.getDuree());
                    String note = c.getNoteMedecin() != null ? c.getNoteMedecin() : "";
                    model.addRow(new Object[]{debut, fin, duree, note});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JTable table = new JTable(model);
            table.setRowHeight(28);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            panel.setBackground(Color.WHITE);
            panel.add(new JScrollPane(table), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createInterventionsPanel(DossierMedicale dossier) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            String[] columns = {"Date", "Consultation", "Acte", "Dent", "Prix patient"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            try {
                List<InterventionMedecin> all = interventionService.findAll();

                List<InterventionMedecin> interventions = all.stream()
                        .filter(i -> i.getConsultation() != null
                                && i.getConsultation().getDossierMedicale() != null
                                && i.getConsultation().getDossierMedicale().getIdDossier() != null
                                && i.getConsultation().getDossierMedicale().getIdDossier().equals(dossier.getIdDossier()))
                        .collect(Collectors.toList());

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (InterventionMedecin i : interventions) {
                    String date = i.getDateCreation() != null ? i.getDateCreation().format(dateFormatter) : "";
                    String consultationId = i.getConsultation() != null && i.getConsultation().getIdConsultation() != null
                            ? i.getConsultation().getIdConsultation().toString()
                            : "";
                    String acteLibelle = i.getActe() != null && i.getActe().getLibelle() != null
                            ? i.getActe().getLibelle()
                            : "";
                    String dent = i.getNumDent() != null ? i.getNumDent().toString() : "";
                    String prix = i.getPrixDePatient() != null ? String.format("%.2f", i.getPrixDePatient()) : "";

                    model.addRow(new Object[]{date, consultationId, acteLibelle, dent, prix});
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JTable table = new JTable(model);
            table.setRowHeight(28);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            panel.add(new JScrollPane(table), BorderLayout.CENTER);
            return panel;
        }
    }
}
