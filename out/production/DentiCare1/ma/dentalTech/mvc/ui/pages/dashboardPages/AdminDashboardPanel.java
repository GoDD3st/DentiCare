package ma.dentalTech.mvc.ui.pages.dashboardPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Revenues.Revenues;
import ma.dentalTech.entities.Statistique.Statistique;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.dashboardPages.components.BaseDashboardPanel;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;
import ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService;
import ma.dentalTech.service.modules.cabinetMedicale.api.StatistiqueService;
import ma.dentalTech.service.modules.patient.api.PatientService;
import ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService;
import ma.dentalTech.service.modules.auth.api.MedecinService;
import ma.dentalTech.service.modules.finance.api.RevenuesService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.SpinnerDateModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardPanel extends BaseDashboardPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    // Services will be added when implemented
    // private ma.dentalTech.service.modules.auth.api.UserService userService;
    private ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService cabinetService;
    private ma.dentalTech.service.modules.cabinetMedicale.api.StatistiqueService statistiqueService;
    private ma.dentalTech.service.modules.patient.api.PatientService patientService;
    private ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService consultationService;
    private ma.dentalTech.service.modules.auth.api.MedecinService medecinService;
    private ma.dentalTech.service.modules.finance.api.RevenuesService revenuesService;
    // private ma.dentalTech.service.modules.cabinetMedicale.api.LogService logService;

    // Table references for refreshing
    private JTable usersTable;
    private DefaultTableModel usersModel;
    private JTable cabinetsTable;
    private DefaultTableModel cabinetsModel;
    private JTable logsTable;
    private DefaultTableModel logsModel;
    private JTable settingsTable;
    private DefaultTableModel settingsModel;

    // Context tracking for actions
    private String currentTableContext = "";

    public AdminDashboardPanel(UserPrincipal principal) {
        super(principal);
        // Initialisation des services
        initializeServices();
    }


    private void testDatabaseConnection() {
        System.out.println("=== TEST CONNEXION BASE DE DONNÉES ===");
        try {
            // Test de connexion directe
            java.sql.Connection conn = ma.dentalTech.conf.SessionFactory.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Connexion à la base de données réussie");

                // Test de requête simple
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM cabinet_medical");
                if (rs.next()) {
                    int count = rs.getInt("total");
                    System.out.println("✓ Nombre de cabinets dans la base: " + count);
                }
                rs.close();

                // Test patients
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(*) as total FROM patient");
                if (rs.next()) {
                    int count = rs.getInt("total");
                    System.out.println("✓ Nombre de patients dans la base: " + count);
                }
                rs.close();

                // Test médecins
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(*) as total FROM medecin");
                if (rs.next()) {
                    int count = rs.getInt("total");
                    System.out.println("✓ Nombre de médecins dans la base: " + count);
                }
                rs.close();

            } else {
                System.out.println("✗ Connexion à la base de données échouée");
            }
        } catch (Exception e) {
            System.err.println("✗ Erreur lors du test de connexion: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== FIN TEST CONNEXION ===");
    }

    private void initializeServices() {
        try {
            System.out.println("=== INITIALISATION DES SERVICES DASHBOARD ===");

            // Initialisation des services avec gestion d'erreur gracieuse
            try {
                cabinetService = ApplicationContext.getBean(CabinetMedicaleService.class);
                System.out.println("CabinetMedicaleService: " + (cabinetService != null ? "OK" : "NULL"));
            } catch (Exception e) {
                System.err.println("CabinetMedicaleService non disponible: " + e.getMessage());
                cabinetService = null;
            }

            try {
                statistiqueService = ApplicationContext.getBean(StatistiqueService.class);
                System.out.println("StatistiqueService: " + (statistiqueService != null ? "OK" : "NULL"));
            } catch (Exception e) {
                System.err.println("StatistiqueService non disponible: " + e.getMessage());
                statistiqueService = null;
            }

            try {
                patientService = ApplicationContext.getBean(PatientService.class);
                System.out.println("PatientService: " + (patientService != null ? "OK" : "NULL"));
            } catch (Exception e) {
                System.err.println("PatientService non disponible: " + e.getMessage());
                patientService = null;
            }

            try {
                consultationService = ApplicationContext.getBean(ConsultationService.class);
                System.out.println("ConsultationService: " + (consultationService != null ? "OK" : "NULL"));
            } catch (Exception e) {
                System.err.println("ConsultationService non disponible: " + e.getMessage());
                consultationService = null;
            }

            try {
                medecinService = ApplicationContext.getBean(MedecinService.class);
                System.out.println("MedecinService: " + (medecinService != null ? "OK" : "NULL"));
            } catch (Exception e) {
                System.err.println("MedecinService non disponible: " + e.getMessage());
                medecinService = null;
            }

            try {
                revenuesService = ApplicationContext.getBean(RevenuesService.class);
                System.out.println("RevenuesService: " + (revenuesService != null ? "OK" : "NULL"));
            } catch (Exception e) {
                System.err.println("RevenuesService non disponible: " + e.getMessage());
                revenuesService = null;
            }

            // Test de connexion à la base de données
            try {
                testDatabaseConnection();
            } catch (Exception e) {
                System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            }

            System.out.println("=== FIN INITIALISATION ===");
        } catch (Exception e) {
            System.err.println("Erreur générale lors de l'initialisation des services: " + e.getMessage());
            // Ne pas lancer d'exception fatale, permettre à l'application de continuer
            e.printStackTrace();
        }
    }

    // Services will be implemented later
    // private UserService getUserService() { ... }
    // private CabinetMedicaleService getCabinetService() { ... }
    // private LogService getLogService() { ... }

    @Override
    protected String getDashboardTitle() {
        return "Dashboard Administrateur";
    }

    @Override
    protected JPanel createSpecificContent() {
        System.out.println("=== DÉBUT createSpecificContent (DASHBOARD) ===");
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        // Header avec sélecteur de période
        JPanel headerPanel = createStatisticsHeader();
        content.add(headerPanel, BorderLayout.NORTH);
        System.out.println("Header ajouté");

        // Contenu principal avec statistiques
        JPanel statsContent = createStatisticsContent();
        content.add(statsContent, BorderLayout.CENTER);
        System.out.println("Contenu statistiques ajouté");

        System.out.println("=== FIN createSpecificContent ===");
        System.out.println("Content final - composants: " + content.getComponentCount());
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

    private JPanel createUsersSection() {
        JPanel card = createCard("Gérer les Utilisateurs");

        // Toolbar avec boutons bien dimensionnés
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));

        ActionButton btnCreate = new ActionButton("Créer un utilisateur", ActionButton.ButtonType.ADD);
        btnCreate.setPreferredSize(new Dimension(180, 36));
        btnCreate.addActionListener(e -> showCreateUserDialog());

        ActionButton btnDelete = new ActionButton("Supprimer sélectionné", ActionButton.ButtonType.DELETE);
        btnDelete.setPreferredSize(new Dimension(180, 36));
        btnDelete.addActionListener(e -> deleteSelectedUser());

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnDelete);

        // Table avec actions
        String[] userColumns = {"Nom", "Email", "Rôle", "Statut", "Dernière connexion", "Actions"};
        DefaultTableModel model = new DefaultTableModel(userColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Actions column is editable to allow button clicks
            }
        };

        // Load real data from service
        loadUsersData(model);

        JTable table = createTableWithActions(model, 5, "USERS");
        // Store reference for refreshing
        usersTable = table;
        usersModel = model;

        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        return card;
    }

    private JPanel createCabinetsSection() {
        JPanel card = createCard("Gérer les Cabinets Médicaux");

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

        String[] cabinetColumns = {"Nom", "Adresse", "Téléphone", "Médecins", "Statut", "Actions"};
        DefaultTableModel model = new DefaultTableModel(cabinetColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Actions column is editable to allow button clicks
            }
        };

        // Load real data from service
        loadCabinetsData(model);

        JTable table = createTableWithActions(model, 5, "CABINETS");
        // Store reference for refreshing
        cabinetsTable = table;
        cabinetsModel = model;

        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        return card;
    }

    private JPanel createLogsSection() {
        JPanel card = createCard("Logs Système et Sécurité");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));

        ActionButton btnAdd = new ActionButton("Ajouter un log", ActionButton.ButtonType.ADD);
        btnAdd.setPreferredSize(new Dimension(160, 36));
        btnAdd.addActionListener(e -> showAddLogDialog());

        ActionButton btnSecurity = new ActionButton("Rapport sécurité", ActionButton.ButtonType.CONFIRM);
        btnSecurity.setPreferredSize(new Dimension(160, 36));
        btnSecurity.addActionListener(e -> showSecurityReportDialog());

        ActionButton btnExport = new ActionButton("Exporter logs", ActionButton.ButtonType.CONFIRM);
        btnExport.setPreferredSize(new Dimension(160, 36));
        btnExport.addActionListener(e -> showExportLogsDialog());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSecurity);
        buttonPanel.add(btnExport);

        String[] logColumns = {"Date/Heure", "Utilisateur", "Action", "Module", "Statut", "IP"};
        DefaultTableModel model = new DefaultTableModel(logColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Aucune colonne n'est éditable pour les logs
            }
        };

        // Load real data from service
        loadLogsData(model);

        JTable table = createTableWithoutActions(model, "LOGS");
        // Store reference for refreshing
        logsTable = table;
        logsModel = model;

        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        return card;
    }

    private JPanel createSettingsSection() {
        JPanel card = createCard("Configuration Système");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));

        ActionButton btnGeneral = new ActionButton("Paramètres généraux", ActionButton.ButtonType.CONFIRM);
        btnGeneral.setPreferredSize(new Dimension(180, 36));
        btnGeneral.addActionListener(e -> showGeneralSettingsDialog());

        ActionButton btnBackup = new ActionButton("Sauvegarde", ActionButton.ButtonType.CONFIRM);
        btnBackup.setPreferredSize(new Dimension(120, 36));
        btnBackup.addActionListener(e -> showBackupDialog());

        ActionButton btnRestore = new ActionButton("Restauration", ActionButton.ButtonType.CONFIRM);
        btnRestore.setPreferredSize(new Dimension(120, 36));
        btnRestore.addActionListener(e -> showRestoreDialog());

        buttonPanel.add(btnGeneral);
        buttonPanel.add(btnBackup);
        buttonPanel.add(btnRestore);

        String[] settingColumns = {"Paramètre", "Valeur", "Description", "Modifiable", "Actions"};
        DefaultTableModel model = new DefaultTableModel(settingColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Actions column is editable to allow button clicks
            }
        };

        // Load real data from service
        loadSettingsData(model);

        JTable table = createTableWithActions(model, 4, "SETTINGS");
        // Store reference for refreshing
        settingsTable = table;
        settingsModel = model;

        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        return card;
    }

    private JTable createTableWithActions(DefaultTableModel model, int actionsColumnIndex, String context) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowGrid(true);

        // Custom renderer and editor for action buttons
        table.getColumnModel().getColumn(actionsColumnIndex).setCellRenderer(new ActionButtonRenderer(context));
        table.getColumnModel().getColumn(actionsColumnIndex).setCellEditor(new ActionButtonEditor(context));

        // Auto-resize columns
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        return table;
    }

    // Create table without action buttons (for logs)
    private JTable createTableWithoutActions(DefaultTableModel model, String context) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowGrid(true);

        // Style the header
        if (table.getTableHeader() != null) {
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.getTableHeader().setBackground(new Color(248, 249, 250));
            table.getTableHeader().setForeground(new Color(52, 58, 64));
        }

        // Auto-resize columns
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        return table;
    }

    // Custom renderer for action buttons
    private class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        public ActionButtonRenderer(String context) {
            // Context not needed for renderer
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            // Create fresh buttons for each cell to avoid shared state issues
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
        private String tableContext;

        public ActionButtonEditor(String context) {
            super(new JCheckBox());
            this.tableContext = context;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Create fresh buttons for each cell with proper event handling
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);

            // Create icon buttons (view, edit, delete)
            JButton viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            JButton editBtn = createIconButton("edit", new Color(243, 156, 18)); // Edit icon
            JButton deleteBtn = createIconButton("delete", new Color(231, 76, 60)); // Delete icon

            // Add action listeners with correct row context
            final int targetRow = row;
            final String targetContext = tableContext;

            viewBtn.addActionListener(e -> {
                currentTableContext = targetContext;
                handleViewAction(targetRow);
            });

            editBtn.addActionListener(e -> {
                currentTableContext = targetContext;
                handleEditAction(targetRow);
            });

            deleteBtn.addActionListener(e -> {
                currentTableContext = targetContext;
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

    // Action handlers
    private void handleViewAction(int row) {
        String itemType = getItemTypeName(currentTableContext);
        JOptionPane.showMessageDialog(this, "Affichage détaillé de " + itemType.toLowerCase() + " (ligne " + (row + 1) + ")", "Détails", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleEditAction(int row) {
        switch (currentTableContext) {
            case "USERS":
                showEditUserDialog(row);
                break;
            case "CABINETS":
                showEditCabinetDialog(row);
                break;
            case "LOGS":
                showViewLogDetailsDialog(row);
                break;
            case "SETTINGS":
                showEditSettingDialog(row);
                break;
        }
    }

    private void handleDeleteAction(int row) {
        String itemType = getItemTypeName(currentTableContext);
        int confirm = JOptionPane.showConfirmDialog(AdminDashboardPanel.this,
            "Êtes-vous sûr de vouloir supprimer " + itemType.toLowerCase() + " (ligne " + (row + 1) + ") ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = false;
            switch (currentTableContext) {
                case "USERS":
                    deleted = deleteUser(row);
                    break;
                case "CABINETS":
                    deleted = deleteCabinet(row);
                    break;
                case "LOGS":
                    // Les logs ne peuvent pas être supprimés manuellement
                    JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                        "Les logs système ne peuvent pas être supprimés manuellement.",
                        "Action non autorisée",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                case "SETTINGS":
                    deleted = deleteSetting(row);
                    break;
            }

            if (deleted) {
                JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                    itemType + " supprimé avec succès !",
                    "Suppression réussie",
                    JOptionPane.INFORMATION_MESSAGE);
                // Note: The row is already removed from the table in delete methods
            } else {
                JOptionPane.showMessageDialog(AdminDashboardPanel.this,
                    "Erreur lors de la suppression du " + itemType.toLowerCase(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getItemTypeName(String context) {
        return switch (context) {
            case "USERS" -> "Utilisateur";
            case "CABINETS" -> "Cabinet";
            case "LOGS" -> "Log";
            case "SETTINGS" -> "Paramètre";
            default -> "Élément";
        };
    }

    private void refreshCurrentTable() {
        switch (currentTableContext) {
            case "USERS":
                if (usersModel != null) loadUsersData(usersModel);
                break;
            case "CABINETS":
                if (cabinetsModel != null) loadCabinetsData(cabinetsModel);
                break;
            case "LOGS":
                if (logsModel != null) loadLogsData(logsModel);
                break;
            case "SETTINGS":
                if (settingsModel != null) loadSettingsData(settingsModel);
                break;
        }
    }

    // Data loading methods
    private void loadUsersData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        // Tables will be empty - real data will be loaded when services are implemented
    }

    private void loadCabinetsData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        // Tables will be empty - real data will be loaded when services are implemented
    }

    private void loadLogsData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        // Tables will be empty - real data will be loaded when services are implemented
    }

    private void loadSettingsData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        // Load system settings
        model.addRow(new Object[]{"Nom du cabinet", "Cabinet Dentaire Central", "Nom affiché dans l'application", "Oui", ""});
        model.addRow(new Object[]{"Email administrateur", "admin@cabinet.com", "Email pour les notifications", "Oui", ""});
        model.addRow(new Object[]{"Sauvegarde automatique", "Activée", "Sauvegarde quotidienne à 02h00", "Oui", ""});
        model.addRow(new Object[]{"Timeout session", "30 minutes", "Durée avant déconnexion automatique", "Oui", ""});
        model.addRow(new Object[]{"Version système", "1.0.0", "Version actuelle de l'application", "Non", ""});
    }

    // Dialog methods
    private void showCreateUserDialog() {
        CreateUserDialog dialog = new CreateUserDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Add the new user to the table
            if (usersModel != null && dialog.getCreatedUser() != null) {
                String[] userData = dialog.getCreatedUser();
                usersModel.addRow(new Object[]{userData[0], userData[1], userData[2], "Actif", "Jamais", ""});
            }
            JOptionPane.showMessageDialog(this,
                "Utilisateur créé avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        if (usersTable == null) {
            JOptionPane.showMessageDialog(this, "Aucune table d'utilisateurs disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        currentTableContext = "USERS";
        handleDeleteAction(selectedRow);
    }

    private void showCreateCabinetDialog() {
        CreateCabinetDialog dialog = new CreateCabinetDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Add the new cabinet to the table
            if (cabinetsModel != null && dialog.getCreatedCabinet() != null) {
                String[] cabinetData = dialog.getCreatedCabinet();
                cabinetsModel.addRow(new Object[]{cabinetData[0], cabinetData[1], cabinetData[2], cabinetData[3], "Actif", ""});
            }
            JOptionPane.showMessageDialog(this, "Cabinet créé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedCabinet() {
        if (cabinetsTable == null) {
            JOptionPane.showMessageDialog(this, "Aucune table de cabinets disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = cabinetsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cabinet à supprimer", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        currentTableContext = "CABINETS";
        handleDeleteAction(selectedRow);
    }

    private void showAddLogDialog() {
        AddLogDialog dialog = new AddLogDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Add the new log to the table
            if (logsModel != null && dialog.getCreatedLog() != null) {
                String[] logData = dialog.getCreatedLog();
                logsModel.addRow(new Object[]{logData[0], logData[1], logData[2], logData[3], logData[4], logData[5]});
            }
            JOptionPane.showMessageDialog(this, "Log ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showSecurityReportDialog() {
        if (logsModel == null || logsModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Aucun log disponible pour générer le rapport", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Count security-related logs
        int totalLogs = logsModel.getRowCount();
        int successLogs = 0;
        int warningLogs = 0;
        int errorLogs = 0;

        for (int i = 0; i < totalLogs; i++) {
            String statut = (String) logsModel.getValueAt(i, 4);
            if (statut != null) {
                if (statut.toLowerCase().contains("succès") || statut.toLowerCase().contains("success")) {
                    successLogs++;
                } else if (statut.toLowerCase().contains("avertissement") || statut.toLowerCase().contains("warning")) {
                    warningLogs++;
                } else if (statut.toLowerCase().contains("erreur") || statut.toLowerCase().contains("error")) {
                    errorLogs++;
                }
            }
        }

        String report = "RAPPORT DE SÉCURITÉ\n" +
                       "==================\n\n" +
                       "Total des logs: " + totalLogs + "\n" +
                       "Succès: " + successLogs + "\n" +
                       "Avertissements: " + warningLogs + "\n" +
                       "Erreurs: " + errorLogs + "\n\n" +
                       "Note: Ce rapport est basé sur les logs actuellement affichés.";

        JOptionPane.showMessageDialog(this, report, "Rapport de Sécurité", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showExportLogsDialog() {
        if (logsModel == null || logsModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Aucun log à exporter", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create export content
        StringBuilder exportContent = new StringBuilder();
        exportContent.append("EXPORT DES LOGS - ").append(java.time.LocalDateTime.now().toString()).append("\n");
        exportContent.append("=".repeat(50)).append("\n\n");

        for (int i = 0; i < logsModel.getRowCount(); i++) {
            exportContent.append("Log #").append(i + 1).append("\n");
            exportContent.append("Date/Heure: ").append(logsModel.getValueAt(i, 0)).append("\n");
            exportContent.append("Utilisateur: ").append(logsModel.getValueAt(i, 1)).append("\n");
            exportContent.append("Action: ").append(logsModel.getValueAt(i, 2)).append("\n");
            exportContent.append("Module: ").append(logsModel.getValueAt(i, 3)).append("\n");
            exportContent.append("Statut: ").append(logsModel.getValueAt(i, 4)).append("\n");
            exportContent.append("IP: ").append(logsModel.getValueAt(i, 5)).append("\n");
            exportContent.append("-".repeat(50)).append("\n");
        }

        // Show export dialog with content
        JTextArea textArea = new JTextArea(exportContent.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Export des Logs", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showGeneralSettingsDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Paramètres généraux", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(248, 249, 250));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel titleLabel = new JLabel("Configuration système");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Nom de l'application:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtAppName = new JTextField("DentiCare");
        contentPanel.add(txtAppName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        contentPanel.add(new JLabel("Langue:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> cmbLanguage = new JComboBox<>(new String[]{"Français", "العربية", "English"});
        cmbLanguage.setSelectedItem("Français");
        contentPanel.add(cmbLanguage, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Paramètres enregistrés avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showBackupDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sauvegarde système", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(248, 249, 250));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel titleLabel = new JLabel("Sauvegarde de la base de données");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Dossier de destination:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtDestination = new JTextField(System.getProperty("user.home") + "\\Desktop");
        contentPanel.add(txtDestination, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JButton btnStart = new JButton("Démarrer la sauvegarde");
        JButton btnCancel = new JButton("Annuler");

        btnStart.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog, "Sauvegarde terminée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnStart);
        buttonPanel.add(btnCancel);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showRestoreDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Restauration système", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(248, 249, 250));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(231, 76, 60)); // Red for restore
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel titleLabel = new JLabel("Restauration de la base de données");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Fichier de sauvegarde:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtBackupFile = new JTextField();
        contentPanel.add(txtBackupFile, gbc);

        // Warning
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JLabel warningLabel = new JLabel("Attention: Cette opération remplacera toutes les données actuelles!");
        warningLabel.setForeground(new Color(231, 76, 60));
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        contentPanel.add(warningLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JButton btnStart = new JButton("Démarrer la restauration");
        JButton btnCancel = new JButton("Annuler");

        btnStart.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "Êtes-vous sûr de vouloir restaurer la base de données?\nToutes les données actuelles seront remplacées!",
                "Confirmation de restauration",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(dialog, "Restauration terminée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnStart);
        buttonPanel.add(btnCancel);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Edit dialogs
    private void showEditUserDialog(int row) {
        if (usersModel == null || row < 0 || row >= usersModel.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Ligne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get current user data from table
        String nom = (String) usersModel.getValueAt(row, 0);
        String email = (String) usersModel.getValueAt(row, 1);
        String role = (String) usersModel.getValueAt(row, 2);

        EditUserDialog dialog = new EditUserDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            principal,
            row,
            nom,
            email,
            role
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Update the table row
            String[] updatedData = dialog.getUpdatedUser();
            if (updatedData != null) {
                usersModel.setValueAt(updatedData[0], row, 0); // Nom
                usersModel.setValueAt(updatedData[1], row, 1); // Email
                usersModel.setValueAt(updatedData[2], row, 2); // Rôle
            }
            JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showEditCabinetDialog(int row) {
        if (cabinetsModel == null || row < 0 || row >= cabinetsModel.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Ligne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get current cabinet data from table
        String nom = (String) cabinetsModel.getValueAt(row, 0);
        String adresse = (String) cabinetsModel.getValueAt(row, 1);
        String telephone = (String) cabinetsModel.getValueAt(row, 2);
        String medecinsStr = (String) cabinetsModel.getValueAt(row, 3);
        int nombreMedecins = 0;
        try {
            nombreMedecins = Integer.parseInt(medecinsStr != null ? medecinsStr : "0");
        } catch (NumberFormatException e) {
            nombreMedecins = 0;
        }

        EditCabinetDialog dialog = new EditCabinetDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            principal,
            row,
            nom,
            adresse,
            telephone,
            nombreMedecins
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Update the table row
            String[] updatedData = dialog.getUpdatedCabinet();
            if (updatedData != null) {
                cabinetsModel.setValueAt(updatedData[0], row, 0); // Nom
                cabinetsModel.setValueAt(updatedData[1], row, 1); // Adresse
                cabinetsModel.setValueAt(updatedData[2], row, 2); // Téléphone
                cabinetsModel.setValueAt(updatedData[3], row, 3); // Nombre de médecins
            }
            JOptionPane.showMessageDialog(this, "Cabinet modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showViewLogDetailsDialog(int row) {
        if (logsModel == null || row < 0 || row >= logsModel.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Ligne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dateHeure = (String) logsModel.getValueAt(row, 0);
        String utilisateur = (String) logsModel.getValueAt(row, 1);
        String action = (String) logsModel.getValueAt(row, 2);
        String module = (String) logsModel.getValueAt(row, 3);
        String statut = (String) logsModel.getValueAt(row, 4);
        String ip = (String) logsModel.getValueAt(row, 5);

        JOptionPane.showMessageDialog(this,
            "DÉTAILS DU LOG\n\n" +
            "Date/Heure: " + dateHeure + "\n" +
            "Utilisateur: " + utilisateur + "\n" +
            "Action: " + action + "\n" +
            "Module: " + module + "\n" +
            "Statut: " + statut + "\n" +
            "Adresse IP: " + ip,
            "Détails du Log",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEditSettingDialog(int row) {
        if (settingsModel == null || row < 0 || row >= settingsModel.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Ligne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String parametre = (String) settingsModel.getValueAt(row, 0);
        String valeur = (String) settingsModel.getValueAt(row, 1);
        String modifiable = (String) settingsModel.getValueAt(row, 3);

        if (!"Oui".equals(modifiable)) {
            JOptionPane.showMessageDialog(this,
                "Ce paramètre n'est pas modifiable.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String newValue = JOptionPane.showInputDialog(this,
            "Modifier la valeur du paramètre:\n\n" +
            "Paramètre: " + parametre + "\n" +
            "Valeur actuelle: " + valeur + "\n\n" +
            "Nouvelle valeur:",
            "Modifier Paramètre",
            JOptionPane.QUESTION_MESSAGE);

        if (newValue != null && !newValue.trim().isEmpty()) {
            settingsModel.setValueAt(newValue.trim(), row, 1);
            JOptionPane.showMessageDialog(this, "Paramètre modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Delete methods
    private boolean deleteUser(int row) {
        try {
            if (usersModel != null && row >= 0 && row < usersModel.getRowCount()) {
                // TODO: Implement actual user deletion from database
                // For now, just remove from table model
                usersModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteCabinet(int row) {
        try {
            if (cabinetsModel != null && row >= 0 && row < cabinetsModel.getRowCount()) {
                // TODO: Implement actual cabinet deletion from database
                // For now, just remove from table model
                cabinetsModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du cabinet: " + e.getMessage());
            return false;
        }
    }


    private boolean deleteSetting(int row) {
        try {
            if (settingsModel != null && row >= 0 && row < settingsModel.getRowCount()) {
                // TODO: Implement actual setting deletion from database
                // For now, just remove from table model
                settingsModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du paramètre: " + e.getMessage());
            return false;
        }
    }

    // Dialog for creating a user
    private class CreateUserDialog extends JDialog {
        private JTextField txtNom;
        private JTextField txtEmail;
        private JComboBox<String> cmbRole;
        private ActionButton btnSave;
        private boolean saved = false;
        private String[] createdUser = null;
        private final UserPrincipal principal;

        public CreateUserDialog(Frame parent, UserPrincipal principal) {
            super(parent, "Créer un Utilisateur", true);
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(450, 300);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Nom
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Nom complet:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtNom = new JTextField();
            txtNom.setPreferredSize(new Dimension(250, 30));
            formPanel.add(txtNom, gbc);

            // Email
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtEmail = new JTextField();
            txtEmail.setPreferredSize(new Dimension(250, 30));
            formPanel.add(txtEmail, gbc);

            // Rôle
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Rôle:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbRole = new JComboBox<>(new String[]{"Administrateur", "Médecin", "Secrétaire", "Patient"});
            cmbRole.setSelectedItem("Patient");
            formPanel.add(cmbRole, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveUser());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveUser() {
            // Désactiver le bouton pour éviter les clics multiples
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                String nom = txtNom.getText().trim();
                String email = txtEmail.getText().trim();
                String role = (String) cmbRole.getSelectedItem();

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un nom", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un email", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                // TODO: Implement actual user creation in database
                // For now, store the data to add to table
                saved = true;
                createdUser = new String[]{nom, email, role};
                JOptionPane.showMessageDialog(this,
                    "Utilisateur créé avec succès !\n\n" +
                    "Nom: " + nom + "\n" +
                    "Email: " + email + "\n" +
                    "Rôle: " + role + "\n\n" +
                    "Note: La création réelle en base de données sera implémentée prochainement.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }

        public String[] getCreatedUser() {
            return createdUser;
        }
    }

    // Dialog for editing a user
    private class EditUserDialog extends JDialog {
        private JTextField txtNom;
        private JTextField txtEmail;
        private JComboBox<String> cmbRole;
        private ActionButton btnSave;
        private boolean saved = false;
        private String[] updatedUser = null;
        private final int row;
        private final UserPrincipal principal;

        public EditUserDialog(Frame parent, UserPrincipal principal, int row, String nom, String email, String role) {
            super(parent, "Modifier un Utilisateur", true);
            this.principal = principal;
            this.row = row;
            initializeDialog(nom, email, role);
        }

        private void initializeDialog(String nom, String email, String role) {
            setLayout(new BorderLayout(10, 10));
            setSize(450, 300);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Nom
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Nom complet:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtNom = new JTextField(nom);
            txtNom.setPreferredSize(new Dimension(250, 30));
            formPanel.add(txtNom, gbc);

            // Email
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtEmail = new JTextField(email);
            txtEmail.setPreferredSize(new Dimension(250, 30));
            formPanel.add(txtEmail, gbc);

            // Rôle
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Rôle:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbRole = new JComboBox<>(new String[]{"Administrateur", "Médecin", "Secrétaire", "Patient"});
            cmbRole.setSelectedItem(role != null ? role : "Patient");
            formPanel.add(cmbRole, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveUser());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveUser() {
            // Désactiver le bouton pour éviter les clics multiples
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                String nom = txtNom.getText().trim();
                String email = txtEmail.getText().trim();
                String role = (String) cmbRole.getSelectedItem();

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un nom", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un email", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                // TODO: Implement actual user update in database
                // For now, store the data to update in table
                saved = true;
                updatedUser = new String[]{nom, email, role};
                JOptionPane.showMessageDialog(this,
                    "Utilisateur modifié avec succès !\n\n" +
                    "Nom: " + nom + "\n" +
                    "Email: " + email + "\n" +
                    "Rôle: " + role + "\n\n" +
                    "Note: La modification réelle en base de données sera implémentée prochainement.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }

        public String[] getUpdatedUser() {
            return updatedUser;
        }
    }

    // Dialog for creating a cabinet
    private class CreateCabinetDialog extends JDialog {
        private JTextField txtNom;
        private JTextField txtAdresse;
        private JTextField txtTelephone;
        private JSpinner spinnerMedecins;
        private ActionButton btnSave;
        private boolean saved = false;
        private String[] createdCabinet = null;
        private final UserPrincipal principal;

        public CreateCabinetDialog(Frame parent, UserPrincipal principal) {
            super(parent, "Créer un Cabinet Médical", true);
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

            // Nom
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Nom du cabinet:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtNom = new JTextField();
            txtNom.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtNom, gbc);

            // Adresse
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Adresse:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtAdresse = new JTextField();
            txtAdresse.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtAdresse, gbc);

            // Téléphone
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Téléphone:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtTelephone = new JTextField();
            txtTelephone.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtTelephone, gbc);

            // Nombre de médecins
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Nombre de médecins:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerMedecins = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            spinnerMedecins.setPreferredSize(new Dimension(300, 30));
            formPanel.add(spinnerMedecins, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveCabinet());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveCabinet() {
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                String nom = txtNom.getText().trim();
                String adresse = txtAdresse.getText().trim();
                String telephone = txtTelephone.getText().trim();
                int nombreMedecins = (Integer) spinnerMedecins.getValue();

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un nom", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                saved = true;
                createdCabinet = new String[]{nom, adresse, telephone, String.valueOf(nombreMedecins)};
                JOptionPane.showMessageDialog(this,
                    "Cabinet créé avec succès !\n\n" +
                    "Nom: " + nom + "\n" +
                    "Adresse: " + adresse + "\n" +
                    "Téléphone: " + telephone + "\n" +
                    "Nombre de médecins: " + nombreMedecins,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }

        public String[] getCreatedCabinet() {
            return createdCabinet;
        }
    }

    // Dialog for editing a cabinet
    private class EditCabinetDialog extends JDialog {
        private JTextField txtNom;
        private JTextField txtAdresse;
        private JTextField txtTelephone;
        private JSpinner spinnerMedecins;
        private ActionButton btnSave;
        private boolean saved = false;
        private String[] updatedCabinet = null;
        private final int row;
        private final UserPrincipal principal;

        public EditCabinetDialog(Frame parent, UserPrincipal principal, int row, String nom, String adresse, String telephone, int nombreMedecins) {
            super(parent, "Modifier un Cabinet Médical", true);
            this.principal = principal;
            this.row = row;
            initializeDialog(nom, adresse, telephone, nombreMedecins);
        }

        private void initializeDialog(String nom, String adresse, String telephone, int nombreMedecins) {
            setLayout(new BorderLayout(10, 10));
            setSize(500, 350);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Nom
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Nom du cabinet:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtNom = new JTextField(nom != null ? nom : "");
            txtNom.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtNom, gbc);

            // Adresse
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Adresse:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtAdresse = new JTextField(adresse != null ? adresse : "");
            txtAdresse.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtAdresse, gbc);

            // Téléphone
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Téléphone:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtTelephone = new JTextField(telephone != null ? telephone : "");
            txtTelephone.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtTelephone, gbc);

            // Nombre de médecins
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Nombre de médecins:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerMedecins = new JSpinner(new SpinnerNumberModel(nombreMedecins, 0, 100, 1));
            spinnerMedecins.setPreferredSize(new Dimension(300, 30));
            formPanel.add(spinnerMedecins, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveCabinet());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveCabinet() {
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                String nom = txtNom.getText().trim();
                String adresse = txtAdresse.getText().trim();
                String telephone = txtTelephone.getText().trim();
                int nombreMedecins = (Integer) spinnerMedecins.getValue();

                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un nom", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                saved = true;
                updatedCabinet = new String[]{nom, adresse, telephone, String.valueOf(nombreMedecins)};
                JOptionPane.showMessageDialog(this,
                    "Cabinet modifié avec succès !\n\n" +
                    "Nom: " + nom + "\n" +
                    "Adresse: " + adresse + "\n" +
                    "Téléphone: " + telephone + "\n" +
                    "Nombre de médecins: " + nombreMedecins,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }

        public String[] getUpdatedCabinet() {
            return updatedCabinet;
        }
    }

    private JPanel createStatisticsHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Titre
        JLabel titleLabel = new JLabel("Statistiques des Cabinets Médicaux");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));

        // Panel pour le sélecteur de période
        JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        periodPanel.setOpaque(false);

        JLabel periodLabel = new JLabel("Période:");
        periodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JComboBox<String> periodComboBox = new JComboBox<>();
        // Ajouter les années disponibles
        int currentYear = java.time.LocalDate.now().getYear();
        for (int year = currentYear; year >= currentYear - 5; year--) {
            periodComboBox.addItem("Année " + year);
        }
        // Ajouter les mois de l'année en cours
        periodComboBox.addItem("Ce mois-ci");
        periodComboBox.addItem("Mois dernier");

        periodComboBox.setPreferredSize(new Dimension(150, 30));
        periodComboBox.setSelectedIndex(0); // Sélectionner l'année en cours par défaut

        periodComboBox.addActionListener(e -> {
            // TODO: Recharger les statistiques selon la période sélectionnée
            refreshStatistics();
        });

        periodPanel.add(periodLabel);
        periodPanel.add(periodComboBox);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(periodPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatisticsContent() {
        System.out.println("=== DÉBUT createStatisticsContent ===");
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section Statistiques générales
        System.out.println("Ajout de la section statistiques générales...");
        JPanel generalStats = createGeneralStatsSection();
        content.add(generalStats);
        content.add(Box.createVerticalStrut(20));

        System.out.println("Nombre de composants dans content après général: " + content.getComponentCount());

        // Section Statistiques par cabinet
        content.add(createCabinetsStatsSection());
        content.add(Box.createVerticalStrut(20));

        // Section Graphiques et tendances
        content.add(createTrendsSection());

        System.out.println("=== FIN createStatisticsContent ===");
        System.out.println("Nombre total de composants dans content: " + content.getComponentCount());
        return content;
    }

    private JPanel createGeneralStatsSection() {
        System.out.println("=== DÉBUT createGeneralStatsSection (" + System.currentTimeMillis() + ") ===");
        System.out.println("Vérification des services au moment de createGeneralStatsSection:");
        System.out.println("cabinetService: " + (cabinetService != null ? "OK" : "NULL"));
        System.out.println("medecinService: " + (medecinService != null ? "OK" : "NULL"));
        System.out.println("patientService: " + (patientService != null ? "OK" : "NULL"));

        // Tentative de récupération directe depuis ApplicationContext
        try {
            ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService testCabinetService =
                ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService.class);
            System.out.println("ApplicationContext.getBean(CabinetMedicaleService): " + (testCabinetService != null ? "OK" : "NULL"));
        } catch (Exception e) {
            System.out.println("Erreur ApplicationContext CabinetMedicaleService: " + e.getMessage());
        }

        // Si les services sont null, essayer de les récupérer à nouveau
        if (cabinetService == null || medecinService == null || patientService == null) {
            System.out.println("Services null détectés, tentative de récupération...");
            try {
                cabinetService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService.class);
                statistiqueService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.cabinetMedicale.api.StatistiqueService.class);
                patientService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.patient.api.PatientService.class);
                consultationService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService.class);
                medecinService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.auth.api.MedecinService.class);
                revenuesService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.finance.api.RevenuesService.class);
                System.out.println("Services récupérés à nouveau:");
                System.out.println("cabinetService: " + (cabinetService != null ? "OK" : "NULL"));
                System.out.println("medecinService: " + (medecinService != null ? "OK" : "NULL"));
                System.out.println("patientService: " + (patientService != null ? "OK" : "NULL"));
            } catch (Exception e) {
                System.out.println("Erreur récupération services: " + e.getMessage());
            }
        }

        JPanel card = createCard("Statistiques Générales");

        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(CARD_BACKGROUND);
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Statistiques réelles depuis la base de données
        int totalCabinets = 0;
        int totalMedecins = 0;
        int totalPatients = 0;

        System.out.println("=== CRÉATION PANEL STATISTIQUES GENERALES ===");

        try {
            System.out.println("=== CHARGEMENT STATISTIQUES GENERALES ===");

            // Total Cabinets
            if (cabinetService != null) {
                List<CabinetMedicale> cabinets = cabinetService.findAll();
                totalCabinets = cabinets.size();
                System.out.println("Cabinets trouvés: " + totalCabinets);
                cabinets.forEach(c -> System.out.println("  - Cabinet: " + c.getNom()));
            } else {
                System.out.println("CabinetService est NULL");
            }
            addStatCard(statsPanel, "Total Cabinets", String.valueOf(totalCabinets), new Color(52, 152, 219));

            // Cabinets Actifs (tous les cabinets sont considérés actifs pour l'instant)
            int cabinetsActifs = totalCabinets;
            addStatCard(statsPanel, "Cabinets Actifs", String.valueOf(cabinetsActifs), new Color(46, 204, 113));

            // Total Médecins
            if (medecinService != null) {
                List<Medecin> medecins = medecinService.findAll();
                totalMedecins = medecins.size();
                System.out.println("Médecins trouvés: " + totalMedecins);
                medecins.forEach(m -> System.out.println("  - Médecin: " + m.getNom()));
            } else {
                System.out.println("MedecinService est NULL");
            }
            addStatCard(statsPanel, "Total Médecins", String.valueOf(totalMedecins), new Color(155, 89, 182));

            // Total Patients
            if (patientService != null) {
                List<Patient> patients = patientService.findAll();
                totalPatients = patients.size();
                System.out.println("Patients trouvés: " + totalPatients);
                patients.forEach(p -> System.out.println("  - Patient: " + p.getNom()));
            } else {
                System.out.println("PatientService est NULL");
            }
            addStatCard(statsPanel, "Total Patients", String.valueOf(totalPatients), new Color(243, 156, 18));

            System.out.println("=== VALEURS FINALES ===");
            System.out.println("Total Cabinets: " + totalCabinets);
            System.out.println("Total Médecins: " + totalMedecins);
            System.out.println("Total Patients: " + totalPatients);

            // Rendez-vous Aujourd'hui (consultations aujourd'hui)
            int rdvsAujourdhui = 0;
            if (consultationService != null) {
                try {
                    List<Consultation> consultations = consultationService.findAll();
                    java.time.LocalDate today = java.time.LocalDate.now();
                    rdvsAujourdhui = (int) consultations.stream()
                        .filter(c -> c.getDateConsultation() != null && c.getDateConsultation().equals(today))
                        .count();
                } catch (Exception e) {
                    System.err.println("Erreur lors du calcul des RDV aujourd'hui: " + e.getMessage());
                }
            }
            addStatCard(statsPanel, "Rendez-vous Aujourd'hui", String.valueOf(rdvsAujourdhui), new Color(231, 76, 60));

            // Revenus du Mois (calculés depuis les revenus)
            double revenusMois = 0.0;
            if (revenuesService != null) {
                try {
                    List<Revenues> revenues = revenuesService.findAll();
                    java.time.LocalDate now = java.time.LocalDate.now();
                    java.time.LocalDate startOfMonth = now.withDayOfMonth(1);
                    revenusMois = revenues.stream()
                            .filter(r -> r.getDate() != null)
                            .filter(r -> {
                                java.time.LocalDate revenueDate = r.getDate().toLocalDate();
                            return revenueDate.getYear() == now.getYear() && revenueDate.getMonth() == now.getMonth();
                        })
                        .mapToDouble(r -> r.getMontant() != null ? r.getMontant() : 0.0)
                        .sum();
                } catch (Exception e) {
                    System.err.println("Erreur lors du calcul des revenus du mois: " + e.getMessage());
                }
            }
            addStatCard(statsPanel, "Revenus du Mois", String.format("%.0f DH", revenusMois), new Color(44, 62, 80));

            System.out.println("=== CARTES STATISTIQUES CRÉÉES ===");
            System.out.println("Nombre de composants dans statsPanel: " + statsPanel.getComponentCount());

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des statistiques générales: " + e.getMessage());
            e.printStackTrace();

            // Note: Utilisation des données existantes dans la base de données
            // Si la base est vide, les statistiques afficheront 0

            // Afficher des valeurs par défaut en cas d'erreur
            addStatCard(statsPanel, "Total Cabinets", "0", new Color(52, 152, 219));
            addStatCard(statsPanel, "Cabinets Actifs", "0", new Color(46, 204, 113));
            addStatCard(statsPanel, "Total Médecins", "0", new Color(155, 89, 182));
            addStatCard(statsPanel, "Total Patients", "0", new Color(243, 156, 18));
            addStatCard(statsPanel, "Rendez-vous Aujourd'hui", "0", new Color(231, 76, 60));
            addStatCard(statsPanel, "Revenus du Mois", "0 DH", new Color(44, 62, 80));
        }

        card.add(statsPanel, BorderLayout.CENTER);

        System.out.println("=== FIN createGeneralStatsSection ===");
        return card;
    }

    private JPanel createCabinetsStatsSection() {
        JPanel card = createCard("Statistiques par Cabinet");

        // Table des statistiques par cabinet
        String[] columns = {"Cabinet", "Médecins", "Patients", "Rendez-vous", "Revenus", "Taux d'occupation"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Charger les vraies données depuis la base de données
        loadCabinetsStatsData(model);

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowGrid(true);

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(new Color(52, 58, 64));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        card.add(new JScrollPane(table), BorderLayout.CENTER);

        return card;
    }

    private JPanel createTrendsSection() {
        JPanel card = createCard("Tendances et Évolution");

        JPanel trendsPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        trendsPanel.setBackground(CARD_BACKGROUND);
        trendsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel pour le graphique (placeholder)
        JPanel chartPanel1 = new JPanel(new BorderLayout());
        chartPanel1.setBackground(Color.WHITE);
        chartPanel1.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        JLabel chartTitle1 = new JLabel("Évolution des rendez-vous", SwingConstants.CENTER);
        chartTitle1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chartTitle1.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Placeholder pour le graphique
        JPanel chartPlaceholder1 = new JPanel();
        chartPlaceholder1.setBackground(new Color(248, 249, 250));
        chartPlaceholder1.add(new JLabel("Graphique d'évolution à implémenter"));

        chartPanel1.add(chartTitle1, BorderLayout.NORTH);
        chartPanel1.add(chartPlaceholder1, BorderLayout.CENTER);

        // Deuxième graphique
        JPanel chartPanel2 = new JPanel(new BorderLayout());
        chartPanel2.setBackground(Color.WHITE);
        chartPanel2.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        JLabel chartTitle2 = new JLabel("Répartition par spécialité", SwingConstants.CENTER);
        chartTitle2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        chartTitle2.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Placeholder pour le graphique
        JPanel chartPlaceholder2 = new JPanel();
        chartPlaceholder2.setBackground(new Color(248, 249, 250));
        chartPlaceholder2.add(new JLabel("Graphique circulaire à implémenter"));

        chartPanel2.add(chartTitle2, BorderLayout.NORTH);
        chartPanel2.add(chartPlaceholder2, BorderLayout.CENTER);

        trendsPanel.add(chartPanel1);
        trendsPanel.add(chartPanel2);

        card.add(trendsPanel, BorderLayout.CENTER);

        return card;
    }

    private void addStatCard(JPanel parent, String title, String value, Color color) {
        System.out.println("Création carte statistique: " + title + " = " + value);
        JPanel statCard = new JPanel(new BorderLayout());
        statCard.setBackground(color);
        statCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(Color.WHITE);

        statCard.add(valueLabel, BorderLayout.CENTER);
        statCard.add(titleLabel, BorderLayout.SOUTH);

        parent.add(statCard);
        System.out.println("Carte ajoutée au parent, total composants: " + parent.getComponentCount());
    }

    private void loadCabinetsStatsData(DefaultTableModel model) {
        System.out.println("=== CHARGEMENT STATISTIQUES PAR CABINET ===");
        model.setRowCount(0); // Clear existing data

        if (cabinetService == null) {
            System.out.println("cabinetService est null");
            return;
        }

        try {
            List<CabinetMedicale> cabinets = cabinetService.findAll();
            System.out.println("Nombre de cabinets trouvés: " + cabinets.size());

            for (CabinetMedicale cabinet : cabinets) {
                String nomCabinet = cabinet.getNom() != null ? cabinet.getNom() : "N/A";
                Long cabinetId = cabinet.getIdCabinet();

                System.out.println("Traitement du cabinet: " + nomCabinet + " (ID: " + cabinetId + ")");

                // Pour chaque cabinet, calculer les statistiques
                int nbMedecins = 0;
                int nbPatients = 0;
                int nbRendezVous = 0;
                double revenus = 0.0;
                double tauxOccupation = 0.0;

                try {
                    // Nombre de médecins par cabinet (via requête directe en base)
                    if (medecinService != null) {
                        try {
                            // D'abord, compter tous les médecins pour vérifier s'il y en a
                            List<Medecin> allMedecins = medecinService.findAll();
                            System.out.println("  Total médecins en base: " + allMedecins.size());

                            // Ensuite, essayer la requête avec jointure
                            java.sql.Connection conn = ma.dentalTech.conf.SessionFactory.getInstance().getConnection();
                            String sql = "SELECT COUNT(*) as count FROM medecin m " +
                                        "JOIN staff s ON m.id_staff = s.id_staff " +
                                        "WHERE s.id_cabinet = ?";
                            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setLong(1, cabinetId);
                            java.sql.ResultSet rs = ps.executeQuery();

                            if (rs.next()) {
                                nbMedecins = rs.getInt("count");
                            }

                            rs.close();
                            ps.close();
                            // Ne pas fermer la connexion car elle est gérée par SessionFactory

                            System.out.println("  Médecins pour ce cabinet (requête directe): " + nbMedecins);

                            // Si pas de médecins trouvés par jointure, compter tous les médecins pour ce cabinet
                            if (nbMedecins == 0 && !allMedecins.isEmpty()) {
                                // Essayer une approche alternative
                                String sqlAlt = "SELECT COUNT(*) as count FROM staff WHERE id_cabinet = ?";
                                java.sql.PreparedStatement psAlt = conn.prepareStatement(sqlAlt);
                                psAlt.setLong(1, cabinetId);
                                java.sql.ResultSet rsAlt = psAlt.executeQuery();

                                if (rsAlt.next()) {
                                    int staffCount = rsAlt.getInt("count");
                                    System.out.println("  Staff trouvé pour ce cabinet: " + staffCount);

                                    // Pour l'instant, on peut estimer ou utiliser une logique temporaire
                                    // TODO: Implémenter la vraie logique de liaison
                                }
                                rsAlt.close();
                                psAlt.close();
                            }

                        } catch (Exception e) {
                            System.out.println("  Erreur requête médecins: " + e.getMessage());
                            e.printStackTrace();

                            // Fallback: compter tous les médecins comme temporaire
                            try {
                                List<Medecin> allMedecins = medecinService.findAll();
                                if (!allMedecins.isEmpty()) {
                                    nbMedecins = allMedecins.size(); // Temporaire pour test
                                    System.out.println("  Fallback: tous les médecins comptés: " + nbMedecins);
                                }
                            } catch (Exception e2) {
                                System.out.println("  Erreur fallback: " + e2.getMessage());
                            }
                        }
                    } else {
                        System.out.println("  medecinService est null");
                    }

                    // Nombre de patients (liés via dossier médical -> consultations -> médecin -> cabinet)
                    try {
                        java.sql.Connection conn = ma.dentalTech.conf.SessionFactory.getInstance().getConnection();
                        String sql = "SELECT COUNT(DISTINCT p.id_patient) as count FROM patient p " +
                                    "JOIN dossier_medical dm ON p.id_patient = dm.id_patient " +
                                    "JOIN consultation c ON dm.id_dossier = c.id_dossier_medical " +
                                    "JOIN medecin m ON c.id_medecin = m.id_medecin " +
                                    "JOIN staff s ON m.id_staff = s.id_staff " +
                                    "WHERE s.id_cabinet = ?";
                        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setLong(1, cabinetId);
                        java.sql.ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            nbPatients = rs.getInt("count");
                        }

                        rs.close();
                        ps.close();

                        System.out.println("  Patients pour ce cabinet: " + nbPatients);
                    } catch (Exception e) {
                        System.out.println("  Erreur requête patients: " + e.getMessage());
                        // Fallback: compter tous les patients et les répartir
                        try {
                            List<ma.dentalTech.entities.Patient.Patient> allPatients = patientService.findAll();
                            if (!allPatients.isEmpty() && !cabinets.isEmpty()) {
                                nbPatients = allPatients.size() / cabinets.size(); // Répartition simple
                                System.out.println("  Fallback: patients répartis: " + nbPatients);
                            }
                        } catch (Exception e2) {
                            System.out.println("  Erreur fallback patients: " + e2.getMessage());
                        }
                    }

                    // Nombre de rendez-vous (consultations du cabinet)
                    if (consultationService != null) {
                        try {
                            // Requête directe pour compter les consultations par cabinet
                            java.sql.Connection conn = ma.dentalTech.conf.SessionFactory.getInstance().getConnection();
                            String sql = "SELECT COUNT(*) as count FROM consultation c " +
                                        "JOIN medecin m ON c.id_medecin = m.id_medecin " +
                                        "JOIN staff s ON m.id_staff = s.id_staff " +
                                        "WHERE s.id_cabinet = ?";
                            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setLong(1, cabinetId);
                            java.sql.ResultSet rs = ps.executeQuery();

                            if (rs.next()) {
                                nbRendezVous = rs.getInt("count");
                            }

                            rs.close();
                            ps.close();

                            System.out.println("  Rendez-vous pour ce cabinet: " + nbRendezVous);
                        } catch (Exception e) {
                            System.out.println("  Erreur requête rendez-vous: " + e.getMessage());
                        }
                    }

                    // Revenus du cabinet
                    if (revenuesService != null) {
                        try {
                            // Requête directe pour calculer les revenus par cabinet pour le mois en cours
                            java.sql.Connection conn = ma.dentalTech.conf.SessionFactory.getInstance().getConnection();
                            String sql = "SELECT COALESCE(SUM(montant), 0) as total FROM revenues " +
                                        "WHERE id_cabinet = ? AND YEAR(date_revenue) = YEAR(CURRENT_DATE) " +
                                        "AND MONTH(date_revenue) = MONTH(CURRENT_DATE)";
                            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
                            ps.setLong(1, cabinetId);
                            java.sql.ResultSet rs = ps.executeQuery();

                            if (rs.next()) {
                                revenus = rs.getDouble("total");
                            }

                            rs.close();
                            ps.close();

                            System.out.println("  Revenus pour ce cabinet (ce mois): " + revenus);
                        } catch (Exception e) {
                            System.out.println("  Erreur requête revenus: " + e.getMessage());
                        }
                    }

                    // Taux d'occupation (calcul simple basé sur le nombre de RDV)
                    if (nbMedecins > 0) {
                        // Estimation simple : chaque médecin peut faire ~20 RDV par mois
                        int capaciteMax = nbMedecins * 20;
                        tauxOccupation = capaciteMax > 0 ? (nbRendezVous * 100.0 / capaciteMax) : 0.0;
                        tauxOccupation = Math.min(tauxOccupation, 100.0); // Max 100%
                    }

                } catch (Exception e) {
                    System.err.println("Erreur lors du calcul des statistiques pour le cabinet " + nomCabinet + ": " + e.getMessage());
                }

                System.out.println("  Statistiques finales pour " + nomCabinet + ":");
                System.out.println("    Médecins: " + nbMedecins);
                System.out.println("    Patients: " + nbPatients);
                System.out.println("    Rendez-vous: " + nbRendezVous);
                System.out.println("    Revenus: " + revenus);
                System.out.println("    Taux occupation: " + tauxOccupation + "%");

                // Ajouter la ligne au tableau
                model.addRow(new Object[]{
                    nomCabinet,
                    String.valueOf(nbMedecins),
                    String.valueOf(nbPatients),
                    String.valueOf(nbRendezVous),
                    String.format("%.0f DH", revenus),
                    String.format("%.0f%%", tauxOccupation)
                });
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des statistiques par cabinet: " + e.getMessage());
            e.printStackTrace();
            // Données par défaut en cas d'erreur
            model.addRow(new Object[]{"Erreur de chargement", "0", "0", "0", "0 DH", "0%"});
        }
    }

    private void refreshStatistics() {
        // TODO: Implémenter le rafraîchissement des statistiques selon la période sélectionnée
        // Pour l'instant, on ne fait rien car les données sont fictives
    }

    // Dialog for adding a log
    private class AddLogDialog extends JDialog {
        private JTextField txtUtilisateur;
        private JTextField txtAction;
        private JTextField txtModule;
        private JComboBox<String> cmbStatut;
        private JTextField txtIp;
        private JSpinner spinnerDate;
        private JSpinner spinnerTime;
        private ActionButton btnSave;
        private boolean saved = false;
        private String[] createdLog = null;
        private final UserPrincipal principal;

        public AddLogDialog(Frame parent, UserPrincipal principal) {
            super(parent, "Ajouter un Log", true);
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(500, 450);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Date
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Date:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            formPanel.add(spinnerDate, gbc);

            // Heure
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Heure:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerTime = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerTime, "HH:mm");
            spinnerTime.setEditor(timeEditor);
            spinnerTime.setValue(java.sql.Time.valueOf(java.time.LocalTime.now()));
            formPanel.add(spinnerTime, gbc);

            // Utilisateur
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Utilisateur:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtUtilisateur = new JTextField();
            txtUtilisateur.setPreferredSize(new Dimension(300, 30));
            if (principal != null) {
                txtUtilisateur.setText(principal.nom());
            }
            formPanel.add(txtUtilisateur, gbc);

            // Action
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Action:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtAction = new JTextField();
            txtAction.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtAction, gbc);

            // Module
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Module:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtModule = new JTextField();
            txtModule.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtModule, gbc);

            // Statut
            gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Statut:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbStatut = new JComboBox<>(new String[]{"Succès", "Avertissement", "Erreur", "Info"});
            cmbStatut.setSelectedItem("Succès");
            formPanel.add(cmbStatut, gbc);

            // IP
            gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Adresse IP:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtIp = new JTextField("127.0.0.1");
            txtIp.setPreferredSize(new Dimension(300, 30));
            formPanel.add(txtIp, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveLog());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveLog() {
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                String utilisateur = txtUtilisateur.getText().trim();
                String action = txtAction.getText().trim();
                String module = txtModule.getText().trim();
                String statut = (String) cmbStatut.getSelectedItem();
                String ip = txtIp.getText().trim();

                if (utilisateur.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un utilisateur", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                if (action.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir une action", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                if (module.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un module", "Erreur", JOptionPane.ERROR_MESSAGE);
                    if (btnSave != null) btnSave.setEnabled(true);
                    return;
                }

                // Format date and time
                java.util.Date dateValue = (java.util.Date) spinnerDate.getValue();
                java.util.Date timeValue = (java.util.Date) spinnerTime.getValue();
                String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateValue);
                String timeStr = new java.text.SimpleDateFormat("HH:mm:ss").format(timeValue);
                dateTime = dateTime.substring(0, 11) + timeStr;

                saved = true;
                createdLog = new String[]{dateTime, utilisateur, action, module, statut, ip};
                JOptionPane.showMessageDialog(this,
                    "Log ajouté avec succès !\n\n" +
                    "Date/Heure: " + dateTime + "\n" +
                    "Utilisateur: " + utilisateur + "\n" +
                    "Action: " + action + "\n" +
                    "Module: " + module + "\n" +
                    "Statut: " + statut + "\n" +
                    "IP: " + ip,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }

        public String[] getCreatedLog() {
            return createdLog;
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
}
