package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.enums.AssuranceEnum;
import ma.dentalTech.entities.enums.SexeEnum;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;
import ma.dentalTech.service.modules.patient.api.PatientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PatientsPanel extends JPanel {

    private final UserPrincipal principal;
    private final PatientService patientService;
    private JTable patientsTable;
    private DefaultTableModel tableModel;

    public PatientsPanel(UserPrincipal principal) {
        this.principal = principal;
        this.patientService = ApplicationContext.getBean(PatientService.class);
        initializeUI();
        loadPatients();
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

        JLabel title = new JLabel("Gestion des Patients");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 37, 41));
        header.add(title, BorderLayout.WEST);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        ActionButton btnAdd = new ActionButton("Ajouter Patient", ActionButton.ButtonType.ADD);
        btnAdd.setPreferredSize(new Dimension(160, 36));
        btnAdd.addActionListener(e -> showAddPatientDialog());

        ActionButton btnRefresh = new ActionButton("Actualiser", ActionButton.ButtonType.VIEW);
        btnRefresh.setPreferredSize(new Dimension(120, 36));
        btnRefresh.addActionListener(e -> loadPatients());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        // Table
        String[] columns = {"ID", "Nom", "Date Naissance", "Sexe", "Téléphone", "Email", "Assurance", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Actions column
            }
        };

        patientsTable = new JTable(tableModel);
        patientsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        patientsTable.setRowHeight(40);
        patientsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        patientsTable.getTableHeader().setBackground(new Color(248, 249, 250));
        patientsTable.getTableHeader().setForeground(new Color(127, 140, 141));
        patientsTable.setShowGrid(true);
        patientsTable.setGridColor(new Color(238, 238, 238));
        patientsTable.setSelectionBackground(new Color(230, 242, 255));
        patientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        patientsTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        patientsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        patientsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        patientsTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        patientsTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        patientsTable.getColumnModel().getColumn(5).setPreferredWidth(180);
        patientsTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        patientsTable.getColumnModel().getColumn(7).setPreferredWidth(200);

        // Set custom renderer and editor for Actions column
        patientsTable.getColumnModel().getColumn(7).setCellRenderer(new ActionButtonRenderer());
        patientsTable.getColumnModel().getColumn(7).setCellEditor(new ActionButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(patientsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        content.add(scrollPane, BorderLayout.CENTER);

        return content;
    }

    private void loadPatients() {
        tableModel.setRowCount(0);
        try {
            List<Patient> patients = patientService.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Patient patient : patients) {
                String dateNaissance = patient.getDateNaissance() != null 
                    ? patient.getDateNaissance().format(formatter) 
                    : "";
                String sexe = patient.getSexe() != null ? patient.getSexe().name() : "";
                String assurance = patient.getAssurance() != null ? patient.getAssurance().name() : "";
                
                tableModel.addRow(new Object[]{
                    patient.getIdPatient(),
                    patient.getNom(),
                    dateNaissance,
                    sexe,
                    patient.getTelephone(),
                    patient.getEmail(),
                    assurance,
                    ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des patients: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showAddPatientDialog() {
        PatientDialog dialog = new PatientDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadPatients();
        }
    }

    private void showEditPatientDialog(int row) {
        Long patientId = (Long) tableModel.getValueAt(row, 0);
        try {
            Optional<Patient> patientOpt = patientService.findByID(patientId);
            if (patientOpt.isPresent()) {
                PatientDialog dialog = new PatientDialog((Frame) SwingUtilities.getWindowAncestor(this), patientOpt.get());
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    loadPatients();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient(int row) {
        Long patientId = (Long) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ce patient ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Optional<Patient> patientOpt = patientService.findByID(patientId);
                if (patientOpt.isPresent()) {
                    patientService.delete(patientOpt.get());
                    JOptionPane.showMessageDialog(this, "Patient supprimé avec succès");
                    loadPatients();
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
                showEditPatientDialog(targetRow);
            });

            deleteBtn.addActionListener(e -> {
                fireEditingStopped();
                deletePatient(targetRow);
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

    private void handleViewAction(int row) {
        Long patientId = (Long) tableModel.getValueAt(row, 0);
        try {
            Optional<Patient> patientOpt = patientService.findByID(patientId);
            if (patientOpt.isPresent()) {
                ViewPatientDialog dialog = new ViewPatientDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    patientOpt.get()
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

    // Patient Dialog
    private class PatientDialog extends JDialog {
        private JTextField txtNom, txtTelephone, txtEmail, txtAdresse;
        private JComboBox<SexeEnum> cmbSexe;
        private JComboBox<AssuranceEnum> cmbAssurance;
        private JSpinner spinnerDate;
        private boolean saved = false;
        private Patient patient;

        public PatientDialog(Frame parent, Patient patient) {
            super(parent, patient == null ? "Ajouter Patient" : "Modifier Patient", true);
            this.patient = patient;
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

            JLabel titleLabel = new JLabel(patient == null ? "Ajouter un patient" : "Modifier le patient");
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
            txtNom = new JTextField();
            cmbSexe = new JComboBox<>(SexeEnum.values());
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            txtTelephone = new JTextField();
            txtEmail = new JTextField();
            txtAdresse = new JTextField();
            cmbAssurance = new JComboBox<>(AssuranceEnum.values());

            // Set default date if creating new patient
            if (patient == null) {
                spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now().minusYears(25)));
            }

            // Load patient data if editing
            if (patient != null) {
                txtNom.setText(patient.getNom());
                if (patient.getSexe() != null) cmbSexe.setSelectedItem(patient.getSexe());
                if (patient.getDateNaissance() != null) {
                    spinnerDate.setValue(java.sql.Date.valueOf(patient.getDateNaissance()));
                }
                txtTelephone.setText(patient.getTelephone());
                txtEmail.setText(patient.getEmail());
                txtAdresse.setText(patient.getAdresse());
                if (patient.getAssurance() != null) cmbAssurance.setSelectedItem(patient.getAssurance());
            }

            // Fields
            String[] labels = {"Nom complet:", "Sexe:", "Date de naissance:", "Téléphone:",
                             "Email:", "Adresse:", "Assurance:"};
            JComponent[] components = {txtNom, cmbSexe, spinnerDate, txtTelephone,
                                    txtEmail, txtAdresse, cmbAssurance};

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

            JButton btnSave = new JButton(patient == null ? "Ajouter" : "Modifier");
            JButton btnCancel = new JButton("Annuler");

            btnSave.setPreferredSize(new Dimension(100, 35));
            btnCancel.setPreferredSize(new Dimension(100, 35));

            btnSave.addActionListener(e -> savePatient());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.0;
            panel.add(new JLabel(label), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            panel.add(field, gbc);
        }

        private void savePatient() {
            try {
                String nom = txtNom.getText().trim();
                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate dateNaissance = null;
                if (spinnerDate.getValue() != null) {
                    java.util.Date dateValue = (java.util.Date) spinnerDate.getValue();
                    dateNaissance = dateValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                }

                if (patient == null) {
                    // Create new
                    Patient newPatient = Patient.builder()
                        .nom(nom)
                        .sexe((SexeEnum) cmbSexe.getSelectedItem())
                        .dateNaissance(dateNaissance)
                        .telephone(txtTelephone.getText().trim())
                        .email(txtEmail.getText().trim())
                        .adresse(txtAdresse.getText().trim())
                        .assurance((AssuranceEnum) cmbAssurance.getSelectedItem())
                        .build();
                    patientService.create(newPatient);
                } else {
                    // Update
                    patient.setNom(nom);
                    patient.setSexe((SexeEnum) cmbSexe.getSelectedItem());
                    patient.setDateNaissance(dateNaissance);
                    patient.setTelephone(txtTelephone.getText().trim());
                    patient.setEmail(txtEmail.getText().trim());
                    patient.setAdresse(txtAdresse.getText().trim());
                    patient.setAssurance((AssuranceEnum) cmbAssurance.getSelectedItem());
                    patientService.update(patient.getIdPatient(), patient);
                }

                saved = true;
                JOptionPane.showMessageDialog(this, "Patient enregistré avec succès");
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog moderne pour voir les détails d'un patient
    private class ViewPatientDialog extends JDialog {
        public ViewPatientDialog(Frame parent, Patient patient) {
            super(parent, "Détails du patient", true);
            initializeDialog(patient);
        }

        private void initializeDialog(Patient patient) {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 600);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(52, 152, 219));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Détails du patient");
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
            String id = patient.getIdPatient() != null ? patient.getIdPatient().toString() : "";
            String nom = patient.getNom() != null ? patient.getNom() : "";
            String dateNaissance = patient.getDateNaissance() != null ? patient.getDateNaissance().toString() : "";
            String sexe = patient.getSexe() != null ? patient.getSexe().name() : "";
            String telephone = patient.getTelephone() != null ? patient.getTelephone() : "";
            String email = patient.getEmail() != null ? patient.getEmail() : "";
            String adresse = patient.getAdresse() != null ? patient.getAdresse() : "";
            String assurance = patient.getAssurance() != null ? patient.getAssurance().name() : "";

            // Créer les champs en lecture seule
            JTextField txtId = new JTextField(id);
            JTextField txtNom = new JTextField(nom);
            JTextField txtDateNaissance = new JTextField(dateNaissance);
            JTextField txtSexe = new JTextField(sexe);
            JTextField txtTelephone = new JTextField(telephone);
            JTextField txtEmail = new JTextField(email);
            JTextField txtAdresse = new JTextField(adresse);
            JTextField txtAssurance = new JTextField(assurance);

            // Désactiver tous les champs (lecture seule)
            txtId.setEditable(false);
            txtNom.setEditable(false);
            txtDateNaissance.setEditable(false);
            txtSexe.setEditable(false);
            txtTelephone.setEditable(false);
            txtEmail.setEditable(false);
            txtAdresse.setEditable(false);
            txtAssurance.setEditable(false);

            // Fond gris clair pour indiquer qu'ils sont en lecture seule
            Color readonlyBg = new Color(240, 240, 240);
            txtId.setBackground(readonlyBg);
            txtNom.setBackground(readonlyBg);
            txtDateNaissance.setBackground(readonlyBg);
            txtSexe.setBackground(readonlyBg);
            txtTelephone.setBackground(readonlyBg);
            txtEmail.setBackground(readonlyBg);
            txtAdresse.setBackground(readonlyBg);
            txtAssurance.setBackground(readonlyBg);

            // Fields labels and components
            String[] labels = {"ID Patient:", "Nom complet:", "Date de naissance:", "Sexe:",
                             "Téléphone:", "Email:", "Adresse:", "Assurance:"};
            JComponent[] components = {txtId, txtNom, txtDateNaissance, txtSexe, txtTelephone,
                                    txtEmail, txtAdresse, txtAssurance};

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
