package ma.dentalTech.mvc.ui.pages.dashboardPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.dashboardPages.components.BaseDashboardPanel;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AdminDashboardPanel extends BaseDashboardPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    // Services will be added when implemented
    // private ma.dentalTech.service.modules.auth.api.UserService userService;
    // private ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService cabinetService;
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
        // Initialisation lazy des services
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
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section Utilisateurs
        content.add(createUsersSection());
        content.add(Box.createVerticalStrut(20));

        // Section Cabinets Médicaux
        content.add(createCabinetsSection());
        content.add(Box.createVerticalStrut(20));

        // Section Logs Système
        content.add(createLogsSection());
        content.add(Box.createVerticalStrut(20));

        // Section Configuration
        content.add(createSettingsSection());

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

        String[] logColumns = {"Date/Heure", "Utilisateur", "Action", "Module", "Statut", "IP", "Actions"};
        DefaultTableModel model = new DefaultTableModel(logColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Actions column is editable to allow button clicks
            }
        };

        // Load real data from service
        loadLogsData(model);

        JTable table = createTableWithActions(model, 6, "LOGS");
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

            // Use regular JButtons instead of ActionButton for better compatibility
            JButton viewBtn = new JButton("Voir");
            JButton editBtn = new JButton("Modifier");
            JButton deleteBtn = new JButton("Supprimer");

            // Style the buttons to look consistent
            viewBtn.setBackground(new Color(52, 152, 219)); // Blue
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFocusPainted(false);
            viewBtn.setBorderPainted(false);
            viewBtn.setPreferredSize(new Dimension(70, 25));

            editBtn.setBackground(new Color(243, 156, 18)); // Orange
            editBtn.setForeground(Color.WHITE);
            editBtn.setFocusPainted(false);
            editBtn.setBorderPainted(false);
            editBtn.setPreferredSize(new Dimension(70, 25));

            deleteBtn.setBackground(new Color(231, 76, 60)); // Red
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorderPainted(false);
            deleteBtn.setPreferredSize(new Dimension(80, 25));

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

            // Use regular JButtons instead of ActionButton for better compatibility
            JButton viewBtn = new JButton("Voir");
            JButton editBtn = new JButton("Modifier");
            JButton deleteBtn = new JButton("Supprimer");

            // Style the buttons to look consistent
            viewBtn.setBackground(new Color(52, 152, 219)); // Blue
            viewBtn.setForeground(Color.WHITE);
            viewBtn.setFocusPainted(false);
            viewBtn.setBorderPainted(false);
            viewBtn.setPreferredSize(new Dimension(70, 25));

            editBtn.setBackground(new Color(243, 156, 18)); // Orange
            editBtn.setForeground(Color.WHITE);
            editBtn.setFocusPainted(false);
            editBtn.setBorderPainted(false);
            editBtn.setPreferredSize(new Dimension(70, 25));

            deleteBtn.setBackground(new Color(231, 76, 60)); // Red
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorderPainted(false);
            deleteBtn.setPreferredSize(new Dimension(80, 25));

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
                    deleted = deleteLog(row);
                    break;
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
                logsModel.addRow(new Object[]{logData[0], logData[1], logData[2], logData[3], logData[4], logData[5], ""});
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
        JOptionPane.showMessageDialog(this, "Paramètres généraux - Fonctionnalité à implémenter", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showBackupDialog() {
        JOptionPane.showMessageDialog(this, "Sauvegarde système - Fonctionnalité à implémenter", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRestoreDialog() {
        JOptionPane.showMessageDialog(this, "Restauration système - Fonctionnalité à implémenter", "Info", JOptionPane.INFORMATION_MESSAGE);
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

    private boolean deleteLog(int row) {
        try {
            if (logsModel != null && row >= 0 && row < logsModel.getRowCount()) {
                // TODO: Implement actual log deletion from database
                // For now, just remove from table model
                logsModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du log: " + e.getMessage());
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
}
