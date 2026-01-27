package ma.dentalTech.mvc.ui.pages.dashboardPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.dashboardPages.components.BaseDashboardPanel;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SecretaryDashboardPanel extends BaseDashboardPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    // Services
    private ma.dentalTech.service.modules.patient.api.PatientService patientService;
    private ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService consultationService;
    private ma.dentalTech.service.modules.facture.api.FactureService factureService;

    // Table references for refreshing
    private JTable patientsTable;
    private DefaultTableModel patientsModel;
    private JTable rdvTable;
    private DefaultTableModel rdvModel;
    private JTable facturesTable;
    private DefaultTableModel facturesModel;

    public SecretaryDashboardPanel(UserPrincipal principal) {
        super(principal);
        // Initialisation lazy des services
    }

    private ma.dentalTech.service.modules.patient.api.PatientService getPatientService() {
        if (patientService == null) {
            try {
                patientService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.patient.api.PatientService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de PatientService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return patientService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService getConsultationService() {
        if (consultationService == null) {
            try {
                consultationService = ma.dentalTech.conf.ApplicationContext.getBean(
                    ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de ConsultationService (dashboard secrétaire): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return consultationService;
    }

    private ma.dentalTech.service.modules.facture.api.FactureService getFactureService() {
        if (factureService == null) {
            try {
                Object bean = ma.dentalTech.conf.ApplicationContext.getBean("factureService");
                if (bean instanceof ma.dentalTech.service.modules.facture.api.FactureService service) {
                    factureService = service;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de FactureService (dashboard secrétaire): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return factureService;
    }

    @Override
    protected String getDashboardTitle() {
        return "Dashboard Secrétaire";
    }

    @Override
    protected JPanel createSpecificContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section Patients
        content.add(createPatientsSection());
        content.add(Box.createVerticalStrut(20));

        // Section Rendez-vous
        content.add(createRendezVousSection());
        content.add(Box.createVerticalStrut(20));

        // Section Caisse
        content.add(createCaisseSection());
        content.add(Box.createVerticalStrut(20));

        // Section Factures
        content.add(createFacturesSection());

        return content;
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setBorder(new EmptyBorder(0, 0, 16, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        return card;
    }

    private JPanel createPatientsSection() {
        JPanel card = createCard("Gérer Patients");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnAdd = new ActionButton("Ajouter Patient", ActionButton.ButtonType.ADD);
        btnAdd.setPreferredSize(new Dimension(160, 36));
        btnAdd.addActionListener(e -> showAddPatientDialog());
        
        ActionButton btnList = new ActionButton("Consulter Liste Patients", ActionButton.ButtonType.VIEW);
        btnList.setPreferredSize(new Dimension(200, 36));
        btnList.addActionListener(e -> showPatientsList());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnList);
        
        String[] patientColumns = {"Nom & Prénom", "Téléphone", "Date Inscription", "Actions"};
        DefaultTableModel model = new DefaultTableModel(patientColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        
        // Load real data from service
        loadPatientsData(model);
        
        JTable table = createTableWithActions(model, 3);
        // Store reference for refreshing
        patientsTable = table;
        patientsModel = model;
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createRendezVousSection() {
        JPanel card = createCard("Gérer Rendez-vous Patients");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnPlan = new ActionButton("Planifier RDV", ActionButton.ButtonType.ADD);
        btnPlan.setPreferredSize(new Dimension(150, 36));
        btnPlan.addActionListener(e -> showPlanRDVDialog());
        
        ActionButton btnHistory = new ActionButton("Consulter Historiques RDV", ActionButton.ButtonType.VIEW);
        btnHistory.setPreferredSize(new Dimension(220, 36));
        btnHistory.addActionListener(e -> showRDVHistory());
        
        ActionButton btnQueue = new ActionButton("Gérer Liste d'Attente", ActionButton.ButtonType.VIEW);
        btnQueue.setPreferredSize(new Dimension(180, 36));
        btnQueue.addActionListener(e -> showQueueManagement());
        
        buttonPanel.add(btnPlan);
        buttonPanel.add(btnHistory);
        buttonPanel.add(btnQueue);
        
        String[] rdvColumns = {"Patient", "Date & Heure", "Statut", "Actions"};
        DefaultTableModel model = new DefaultTableModel(rdvColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        
        // Load real data from service
        loadRDVData(model);
        
        JTable table = createTableWithActions(model, 3);
        // Store reference for refreshing
        rdvTable = table;
        rdvModel = model;
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createCaisseSection() {
        JPanel card = createCard("Gérer la Caisse");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnStats = new ActionButton("Consulter Statistiques de Caisse", ActionButton.ButtonType.VIEW);
        btnStats.setPreferredSize(new Dimension(260, 36));
        btnStats.addActionListener(e -> showCaisseStats());
        
        ActionButton btnExport = new ActionButton("Exporter Rapport", ActionButton.ButtonType.EXPORT);
        btnExport.setPreferredSize(new Dimension(160, 36));
        btnExport.addActionListener(e -> exportReport());
        
        ActionButton btnSituation = new ActionButton("Consulter Situation Financière", ActionButton.ButtonType.VIEW);
        btnSituation.setPreferredSize(new Dimension(240, 36));
        btnSituation.addActionListener(e -> showFinancialSituation());
        
        buttonPanel.add(btnStats);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnSituation);
        
        card.add(buttonPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createFacturesSection() {
        JPanel card = createCard("Gérer Factures");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnGenerate = new ActionButton("Générer Facture", ActionButton.ButtonType.ADD);
        btnGenerate.setPreferredSize(new Dimension(160, 36));
        btnGenerate.addActionListener(e -> showGenerateFactureDialog());
        
        buttonPanel.add(btnGenerate);
        
        String[] factureColumns = {"Numéro", "Patient", "Montant", "Date", "Statut", "Actions"};
        DefaultTableModel model = new DefaultTableModel(factureColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        
        // Load real data from service
        loadFacturesData(model);
        
        JTable table = createTableWithActions(model, 5);
        // Store reference for refreshing
        facturesTable = table;
        facturesModel = model;
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JTable createTableWithActions(DefaultTableModel model, int actionColumnIndex) {
        JTable table = new JTable(model);
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 249, 250));
        table.getTableHeader().setForeground(new Color(127, 140, 141));
        table.setShowGrid(true);
        table.setGridColor(new Color(238, 238, 238));
        table.setSelectionBackground(new Color(230, 242, 255));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set custom renderer and editor for Actions column
        table.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new ActionButtonEditor(new JCheckBox()));
        
        return table;
    }

    // Custom renderer for action buttons
    private class ActionButtonRenderer implements TableCellRenderer {
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
                btn.setText(iconName.equals("see") ? "O" : iconName.equals("edit") ? "*" : "X");
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        }

        return btn;
    }

    private void handleViewAction(int row) {
        JOptionPane.showMessageDialog(SecretaryDashboardPanel.this, "Voir détails - Ligne " + (row + 1));
    }

    private void handleEditAction(int row) {
        JOptionPane.showMessageDialog(SecretaryDashboardPanel.this, "Modifier - Ligne " + (row + 1));
    }

    private void handleDeleteAction(int row) {
        int confirm = JOptionPane.showConfirmDialog(SecretaryDashboardPanel.this, 
            "Êtes-vous sûr de vouloir supprimer cet élément ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(SecretaryDashboardPanel.this, "Élément supprimé - Ligne " + (row + 1));
        }
    }

    // Data loading methods
    private void loadPatientsData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();
            if (ps != null) {
                java.util.List<ma.dentalTech.entities.Patient.Patient> patients = ps.findAll();
                for (ma.dentalTech.entities.Patient.Patient patient : patients) {
                    String fullName = patient.getNom();
                    String phone = patient.getTelephone() != null ? patient.getTelephone() : "";
                    String inscriptionDate = patient.getDateCreation() != null ?
                        patient.getDateCreation().toString() : "";
                    model.addRow(new Object[]{fullName, phone, inscriptionDate, ""});
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des patients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRDVData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            var service = getConsultationService();
            if (service == null) {
                return;
            }

            java.util.List<ma.dentalTech.entities.Consultation.Consultation> consultations = service.findAll();
            java.time.format.DateTimeFormatter dateFormatter =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (ma.dentalTech.entities.Consultation.Consultation c : consultations) {
                String patientName = "N/A";
                if (c.getDossierMedicale() != null
                        && c.getDossierMedicale().getPatient() != null
                        && c.getDossierMedicale().getPatient().getNom() != null) {
                    patientName = c.getDossierMedicale().getPatient().getNom();
                }

                String dateHeure = "";
                if (c.getDateConsultation() != null) {
                    dateHeure = c.getDateConsultation().format(dateFormatter);
                }
                if (c.getHeureConsultation() != null) {
                    dateHeure = dateHeure.isEmpty()
                        ? c.getHeureConsultation().toString()
                        : dateHeure + " " + c.getHeureConsultation().toString();
                }

                String statut = c.getStatut() != null ? c.getStatut().name() : "";

                model.addRow(new Object[]{patientName, dateHeure, statut, ""});
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des RDV (via consultations): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadFacturesData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            var service = getFactureService();
            if (service == null) {
                return;
            }

            java.util.List<ma.dentalTech.entities.Facture.Facture> factures = service.findAll();
            java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (ma.dentalTech.entities.Facture.Facture facture : factures) {
                String numero = facture.getIdFacture() != null
                    ? "FAC-" + facture.getIdFacture()
                    : "";

                String patientName = "N/A";
                if (facture.getConsultation() != null
                        && facture.getConsultation().getDossierMedicale() != null
                        && facture.getConsultation().getDossierMedicale().getPatient() != null
                        && facture.getConsultation().getDossierMedicale().getPatient().getNom() != null) {
                    patientName = facture.getConsultation().getDossierMedicale().getPatient().getNom();
                }

                String montant = facture.getTotaleFacture() != null
                    ? String.format("%.2f DH", facture.getTotaleFacture())
                    : "0.00 DH";

                String date = facture.getDateFacture() != null
                    ? facture.getDateFacture().format(formatter)
                    : "";

                String statut = facture.getStatut() != null ? facture.getStatut().name() : "";

                model.addRow(new Object[]{
                    numero,
                    patientName,
                    montant,
                    date,
                    statut,
                    ""
                });
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des factures (dashboard secrétaire): " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Dialog methods
    private void showAddPatientDialog() {
        ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();

        if (ps == null) {
            JOptionPane.showMessageDialog(this,
                "Le service patient n'est pas disponible. Veuillez vérifier la configuration.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        AddPatientDialog dialog = new AddPatientDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            ps,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Refresh the patients table
            if (patientsModel != null) {
                loadPatientsData(patientsModel);
            }
            JOptionPane.showMessageDialog(this, "Patient ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showPatientsList() {
        ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();

        if (ps == null) {
            JOptionPane.showMessageDialog(this,
                "Le service patient n'est pas disponible. Veuillez vérifier la configuration.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            java.util.List<ma.dentalTech.entities.Patient.Patient> patients = ps.findAll();

            if (patients.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun patient trouvé dans la base de données.", "Liste des patients", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("LISTE DES PATIENTS (").append(patients.size()).append(")\n\n");

            for (ma.dentalTech.entities.Patient.Patient patient : patients) {
                sb.append("• ").append(patient.getNom());
                if (patient.getTelephone() != null && !patient.getTelephone().isEmpty()) {
                    sb.append(" - ").append(patient.getTelephone());
                }
                if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
                    sb.append(" - ").append(patient.getEmail());
                }
                sb.append("\n");
            }

            // Create a scrollable dialog for long lists
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Liste des patients", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des patients: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPlanRDVDialog() {
        ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();

        if (ps == null) {
            JOptionPane.showMessageDialog(this,
                "Le service patient n'est pas disponible. Veuillez vérifier la configuration.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        PlanRDVDialog dialog = new PlanRDVDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            ps,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Refresh the RDV table
            if (rdvModel != null) {
                loadRDVData(rdvModel);
            }
            JOptionPane.showMessageDialog(this, "Rendez-vous planifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showRDVHistory() {
        StringBuilder sb = new StringBuilder();
        sb.append("HISTORIQUE DES RENDEZ-VOUS\n\n");

        // TODO: Implement RDV service to get real data
        // For now, show sample data
        sb.append("Aujourd'hui:\n");
        sb.append("• Ahmed Benali - 10:00 - Consultation générale - Terminé\n");
        sb.append("• Fatima Alami - 11:00 - Contrôle dentaire - En cours\n\n");

        sb.append("Hier:\n");
        sb.append("• Mohamed Tazi - 09:30 - Urgence - Terminé\n");
        sb.append("• Sanae Bennani - 14:00 - Dépose de devis - Annulé\n\n");

        sb.append("Avant-hier:\n");
        sb.append("• Karim Alaoui - 16:00 - Suivi traitement - Terminé\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Historique des RDV", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showQueueManagement() {
        StringBuilder sb = new StringBuilder();
        sb.append("LISTE D'ATTENTE - ").append(java.time.LocalDate.now()).append("\n\n");

        // TODO: Implement queue management service
        sb.append("Position 1: Ahmed Benali - Arrivée: 08:30\n");
        sb.append("  Motif: Consultation urgence - Douleur dentaire\n");
        sb.append("  Statut: En attente\n\n");

        sb.append("Position 2: Fatima Alami - Arrivée: 09:15\n");
        sb.append("  Motif: Contrôle de routine\n");
        sb.append("  Statut: En attente\n\n");

        sb.append("Position 3: Mohamed Tazi - Arrivée: 09:45\n");
        sb.append("  Motif: Consultation pour devis\n");
        sb.append("  Statut: En attente\n\n");

        sb.append("Actions disponibles:\n");
        sb.append("• Appeler le patient suivant\n");
        sb.append("• Annuler un rendez-vous\n");
        sb.append("• Réorganiser la liste\n");
        sb.append("• Marquer comme arrivé\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Gestion de la liste d'attente", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCaisseStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("STATISTIQUES DE CAISSE - ").append(java.time.LocalDate.now().getMonth()).append(" ").append(java.time.LocalDate.now().getYear()).append("\n");
        sb.append("=".repeat(60)).append("\n\n");

        // TODO: Implement caisse service to get real data
        sb.append("RECETTES DU MOIS:\n");
        sb.append("• Consultations: 45,000 DH (15 patients)\n");
        sb.append("• Traitements: 78,500 DH (23 patients)\n");
        sb.append("• Autres: 12,300 DH\n");
        sb.append("• TOTAL: 135,800 DH\n\n");

        sb.append("DEPENSES DU MOIS:\n");
        sb.append("• Salaires: 35,000 DH\n");
        sb.append("• Fournitures médicales: 18,500 DH\n");
        sb.append("• Loyer: 8,000 DH\n");
        sb.append("• Autres charges: 7,200 DH\n");
        sb.append("• TOTAL: 68,700 DH\n\n");

        sb.append("RESULTAT NET: 67,100 DH\n\n");

        sb.append("STATISTIQUES DETAILLEES:\n");
        sb.append("• Nombre de patients: 38\n");
        sb.append("• Consultations réalisées: 42\n");
        sb.append("• Factures payées: 35\n");
        sb.append("• Factures en attente: 7\n");
        sb.append("• Taux de paiement: 83%\n\n");

        sb.append("TOP TRAITEMENTS:\n");
        sb.append("1. Détartrage: 15,000 DH (8 patients)\n");
        sb.append("2. Carie traitement: 12,500 DH (5 patients)\n");
        sb.append("3. Extraction: 9,800 DH (4 patients)\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(this, scrollPane, "Statistiques de caisse", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportReport() {
        // Create a file chooser for selecting export location
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter le rapport de caisse");
        fileChooser.setSelectedFile(new java.io.File("rapport_caisse_" +
            java.time.LocalDate.now().toString() + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (java.io.FileWriter writer = new java.io.FileWriter(fileToSave)) {
                writer.write("RAPPORT DE CAISSE - " + java.time.LocalDate.now() + "\n");
                writer.write("=".repeat(50) + "\n\n");

                writer.write("RECETTES DU MOIS:\n");
                writer.write("• Consultations: 45,000 DH\n");
                writer.write("• Traitements: 78,500 DH\n");
                writer.write("• TOTAL: 123,500 DH\n\n");

                writer.write("DEPENSES DU MOIS:\n");
                writer.write("• Salaires: 35,000 DH\n");
                writer.write("• Fournitures: 18,500 DH\n");
                writer.write("• TOTAL: 53,500 DH\n\n");

                writer.write("RESULTAT NET: 70,000 DH\n");

                JOptionPane.showMessageDialog(this,
                    "Rapport exporté avec succès vers:\n" + fileToSave.getAbsolutePath(),
                    "Export réussi",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'export: " + e.getMessage(),
                    "Erreur d'export",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showFinancialSituation() {
        StringBuilder sb = new StringBuilder();
        sb.append("SITUATION FINANCIÈRE DÉTAILLÉE\n");
        sb.append("=".repeat(50)).append("\n\n");

        // TODO: Implement financial service to get real data
        sb.append("ACTIFS:\n");
        sb.append("• Caisse: 25,000 DH\n");
        sb.append("• Banque: 150,000 DH\n");
        sb.append("• Créances clients: 15,000 DH\n");
        sb.append("• TOTAL ACTIFS: 190,000 DH\n\n");

        sb.append("PASSIFS:\n");
        sb.append("• Dettes fournisseurs: 12,000 DH\n");
        sb.append("• Emprunts: 45,000 DH\n");
        sb.append("• Charges sociales: 8,000 DH\n");
        sb.append("• TOTAL PASSIFS: 65,000 DH\n\n");

        sb.append("CAPITAUX PROPRES: 125,000 DH\n\n");

        sb.append("ANALYSE PAR PATIENT:\n");
        sb.append("• Ahmed Benali: 5,200 DH (Soldé)\n");
        sb.append("• Fatima Alami: 3,800 DH (En cours)\n");
        sb.append("• Mohamed Tazi: 0 DH (Soldé)\n");
        sb.append("• Sanae Bennani: 6,000 DH (Paiement échelonné)\n\n");

        sb.append("INDICATEURS DE PERFORMANCE:\n");
        sb.append("• Ratio liquidité: 2.9\n");
        sb.append("• Délai moyen de paiement: 15 jours\n");
        sb.append("• Taux de marge: 35%\n");

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 500));

        JOptionPane.showMessageDialog(this, scrollPane, "Situation financière", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showGenerateFactureDialog() {
        ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();

        if (ps == null) {
            JOptionPane.showMessageDialog(this,
                "Le service patient n'est pas disponible. Veuillez vérifier la configuration.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        GenerateFactureDialog dialog = new GenerateFactureDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            ps,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Refresh the factures table
            if (facturesModel != null) {
                loadFacturesData(facturesModel);
            }
            JOptionPane.showMessageDialog(this, "Facture générée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Dialog for adding a patient
    private class AddPatientDialog extends JDialog {
        private JTextField txtNom;
        private JSpinner spinnerDateNaissance;
        private JComboBox<ma.dentalTech.entities.enums.SexeEnum> cmbSexe;
        private JTextField txtAdresse;
        private JTextField txtEmail;
        private JTextField txtTelephone;
        private JComboBox<ma.dentalTech.entities.enums.AssuranceEnum> cmbAssurance;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.patient.api.PatientService patientService;
        private final UserPrincipal principal;

        public AddPatientDialog(Frame parent,
                               ma.dentalTech.service.modules.patient.api.PatientService patientService,
                               UserPrincipal principal) {
            super(parent, "Ajouter un Patient", true);
            this.patientService = patientService;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(500, 400);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Nom
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Nom:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtNom = new JTextField();
            txtNom.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtNom, gbc);

            // Date de naissance
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Date de naissance:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDateNaissance = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDateNaissance, "dd/MM/yyyy");
            spinnerDateNaissance.setEditor(dateEditor);
            spinnerDateNaissance.setValue(java.sql.Date.valueOf(java.time.LocalDate.now().minusYears(30)));
            formPanel.add(spinnerDateNaissance, gbc);

            // Sexe
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Sexe:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbSexe = new JComboBox<>(ma.dentalTech.entities.enums.SexeEnum.values());
            cmbSexe.setSelectedItem(ma.dentalTech.entities.enums.SexeEnum.HOMME);
            formPanel.add(cmbSexe, gbc);

            // Adresse
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Adresse:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtAdresse = new JTextField();
            txtAdresse.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtAdresse, gbc);

            // Email
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtEmail = new JTextField();
            txtEmail.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtEmail, gbc);

            // Téléphone
            gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Téléphone:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtTelephone = new JTextField();
            txtTelephone.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtTelephone, gbc);

            // Assurance
            gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Assurance:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbAssurance = new JComboBox<>(ma.dentalTech.entities.enums.AssuranceEnum.values());
            cmbAssurance.setSelectedItem(ma.dentalTech.entities.enums.AssuranceEnum.CNSS);
            formPanel.add(cmbAssurance, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ActionButton btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> savePatient());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void savePatient() {
            try {
                String nom = txtNom.getText().trim();
                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir le nom du patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.time.LocalDate dateNaissance = null;
                if (spinnerDateNaissance.getValue() != null) {
                    java.util.Date dateValue = (java.util.Date) spinnerDateNaissance.getValue();
                    dateNaissance = dateValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                }

                ma.dentalTech.entities.Patient.Patient patient = ma.dentalTech.entities.Patient.Patient.builder()
                    .nom(nom)
                    .dateNaissance(dateNaissance)
                    .sexe((ma.dentalTech.entities.enums.SexeEnum) cmbSexe.getSelectedItem())
                    .adresse(txtAdresse.getText().trim())
                    .email(txtEmail.getText().trim())
                    .telephone(txtTelephone.getText().trim())
                    .assurance((ma.dentalTech.entities.enums.AssuranceEnum) cmbAssurance.getSelectedItem())
                    .build();

                patientService.create(patient);
                saved = true;
                JOptionPane.showMessageDialog(this, "Patient ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog for planning an RDV
    private class PlanRDVDialog extends JDialog {
        private JComboBox<PatientComboItem> cmbPatient;
        private JSpinner spinnerDate;
        private JSpinner spinnerTime;
        private JTextArea txtMotif;
        private JComboBox<String> cmbStatut;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.patient.api.PatientService patientService;
        private final UserPrincipal principal;

        public PlanRDVDialog(Frame parent,
                           ma.dentalTech.service.modules.patient.api.PatientService patientService,
                           UserPrincipal principal) {
            super(parent, "Planifier un Rendez-vous", true);
            this.patientService = patientService;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(500, 350);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Patient selection
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Patient:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbPatient = new JComboBox<>();
            cmbPatient.setPreferredSize(new Dimension(300, 30));
            loadPatients();
            formPanel.add(cmbPatient, gbc);

            // Date du RDV
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Date:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(1)));
            formPanel.add(spinnerDate, gbc);

            // Heure du RDV
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Heure:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerTime = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerTime, "HH:mm");
            spinnerTime.setEditor(timeEditor);
            spinnerTime.setValue(java.sql.Time.valueOf(java.time.LocalTime.of(9, 0)));
            formPanel.add(spinnerTime, gbc);

            // Statut
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Statut:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbStatut = new JComboBox<>(new String[]{"PLANIFIE", "CONFIRME", "EN_ATTENTE"});
            cmbStatut.setSelectedItem("PLANIFIE");
            formPanel.add(cmbStatut, gbc);

            // Motif
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Motif:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
            txtMotif = new JTextArea(3, 20);
            txtMotif.setLineWrap(true);
            txtMotif.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(txtMotif);
            scrollPane.setPreferredSize(new Dimension(300, 60));
            formPanel.add(scrollPane, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ActionButton btnSave = new ActionButton("Planifier", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveRDV());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadPatients() {
            try {
                java.util.List<ma.dentalTech.entities.Patient.Patient> patients = patientService.findAll();
                cmbPatient.removeAllItems();
                for (ma.dentalTech.entities.Patient.Patient patient : patients) {
                    cmbPatient.addItem(new PatientComboItem(patient));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des patients: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void saveRDV() {
            try {
                PatientComboItem selected = (PatientComboItem) cmbPatient.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.time.LocalDate dateRDV = null;
                if (spinnerDate.getValue() != null) {
                    java.util.Date dateValue = (java.util.Date) spinnerDate.getValue();
                    dateRDV = dateValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                }

                java.time.LocalTime heureRDV = null;
                if (spinnerTime.getValue() != null) {
                    java.util.Date timeValue = (java.util.Date) spinnerTime.getValue();
                    heureRDV = timeValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalTime();
                }

                // TODO: Implement RDV service and entity creation
                // For now, just show success message
                saved = true;
                JOptionPane.showMessageDialog(this, "Rendez-vous planifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la planification: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }

        // Helper class for combo box
        private class PatientComboItem {
            private final ma.dentalTech.entities.Patient.Patient patient;

            public PatientComboItem(ma.dentalTech.entities.Patient.Patient patient) {
                this.patient = patient;
            }

            public ma.dentalTech.entities.Patient.Patient getPatient() {
                return patient;
            }

            @Override
            public String toString() {
                return patient.getNom() + (patient.getTelephone() != null ? " - " + patient.getTelephone() : "");
            }
        }
    }

    // Dialog for generating a facture
    private class GenerateFactureDialog extends JDialog {
        private JComboBox<PatientComboItem> cmbPatient;
        private JList<String> listActes;
        private JTextField txtTotal;
        private JSpinner spinnerDate;
        private JComboBox<String> cmbStatut;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.patient.api.PatientService patientService;
        private final UserPrincipal principal;

        public GenerateFactureDialog(Frame parent,
                                   ma.dentalTech.service.modules.patient.api.PatientService patientService,
                                   UserPrincipal principal) {
            super(parent, "Générer une Facture", true);
            this.patientService = patientService;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(600, 500);
            setLocationRelativeTo(getParent());

            // Main panel
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Patient selection
            gbc.gridx = 0; gbc.gridy = 0;
            mainPanel.add(new JLabel("Patient:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbPatient = new JComboBox<>();
            cmbPatient.setPreferredSize(new Dimension(300, 30));
            loadPatients();
            mainPanel.add(cmbPatient, gbc);

            // Date de facture
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            mainPanel.add(new JLabel("Date de facture:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            mainPanel.add(spinnerDate, gbc);

            // Actes médicaux
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            mainPanel.add(new JLabel("Actes médicaux:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;

            // Sample actes - TODO: Load from service
            String[] actes = {
                "Consultation générale - 200 DH",
                "Détartrage complet - 300 DH",
                "Traitement carie - 150 DH",
                "Extraction dent - 250 DH",
                "Radiographie - 100 DH"
            };
            listActes = new JList<>(actes);
            listActes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane actesScrollPane = new JScrollPane(listActes);
            actesScrollPane.setPreferredSize(new Dimension(300, 120));
            mainPanel.add(actesScrollPane, gbc);

            // Total
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.weighty = 0.0;
            mainPanel.add(new JLabel("Total:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtTotal = new JTextField("0 DH");
            txtTotal.setEditable(false);
            txtTotal.setPreferredSize(new Dimension(200, 30));
            mainPanel.add(txtTotal, gbc);

            // Statut
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
            mainPanel.add(new JLabel("Statut:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbStatut = new JComboBox<>(new String[]{"PAYEE", "PARTIELLEMENT_PAYEE", "IMPAYEE"});
            cmbStatut.setSelectedItem("IMPAYEE");
            mainPanel.add(cmbStatut, gbc);

            // Update total when selection changes
            listActes.addListSelectionListener(e -> updateTotal());

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ActionButton btnSave = new ActionButton("Générer Facture", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveFacture());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(mainPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadPatients() {
            try {
                java.util.List<ma.dentalTech.entities.Patient.Patient> patients = patientService.findAll();
                cmbPatient.removeAllItems();
                for (ma.dentalTech.entities.Patient.Patient patient : patients) {
                    cmbPatient.addItem(new PatientComboItem(patient));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des patients: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void updateTotal() {
            int[] selectedIndices = listActes.getSelectedIndices();
            int total = 0;
            for (int index : selectedIndices) {
                switch (index) {
                    case 0: total += 200; break; // Consultation
                    case 1: total += 300; break; // Détartrage
                    case 2: total += 150; break; // Carie
                    case 3: total += 250; break; // Extraction
                    case 4: total += 100; break; // Radio
                }
            }
            txtTotal.setText(total + " DH");
        }

        private void saveFacture() {
            try {
                PatientComboItem selected = (PatientComboItem) cmbPatient.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (listActes.getSelectedIndices().length == 0) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un acte médical", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // TODO: Implement facture service and entity creation
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Facture générée avec succès!\n\n" +
                    "Patient: " + selected.getPatient().getNom() + "\n" +
                    "Montant: " + txtTotal.getText() + "\n" +
                    "Statut: " + cmbStatut.getSelectedItem(),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la génération: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }

        // Helper class for combo box
        private class PatientComboItem {
            private final ma.dentalTech.entities.Patient.Patient patient;

            public PatientComboItem(ma.dentalTech.entities.Patient.Patient patient) {
                this.patient = patient;
            }

            public ma.dentalTech.entities.Patient.Patient getPatient() {
                return patient;
            }

            @Override
            public String toString() {
                return patient.getNom() + (patient.getTelephone() != null ? " - " + patient.getTelephone() : "");
            }
        }
    }
}
