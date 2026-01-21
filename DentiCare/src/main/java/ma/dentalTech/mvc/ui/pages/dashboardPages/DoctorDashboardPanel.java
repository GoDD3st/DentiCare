package ma.dentalTech.mvc.ui.pages.dashboardPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.pages.dashboardPages.components.BaseDashboardPanel;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class DoctorDashboardPanel extends BaseDashboardPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);
    
    private ma.dentalTech.service.modules.patient.api.PatientService patientService;
    private ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService;
    private ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService consultationService;
    private ma.dentalTech.service.modules.dossierMedicale.api.ActeService acteService;

    // Table references for refreshing
    private JTable dossiersTable;
    private DefaultTableModel dossiersModel;
    private JTable consultationsTable;
    private DefaultTableModel consultationsModel;
    private JTable ordonnancesTable;
    private DefaultTableModel ordonnancesModel;
    private JTable actesTable;
    private DefaultTableModel actesModel;



    // Context tracking for actions
    private String currentTableContext = "";

    public DoctorDashboardPanel(UserPrincipal principal) {
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
    
    private ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService getDossierService() {
        if (dossierService == null) {
            try {
                dossierService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de DossierMedicaleService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return dossierService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService getConsultationService() {
        if (consultationService == null) {
            try {
                consultationService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de ConsultationService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return consultationService;
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.ActeService getActeService() {
        if (acteService == null) {
            try {
                acteService = ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.ActeService.class);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de ActeService: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return acteService;
    }

    @Override
    protected String getDashboardTitle() {
        return "Dashboard Médecin";
    }

    @Override
    protected JPanel createSpecificContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section Dossiers Médicaux
        content.add(createDossiersSection());
        content.add(Box.createVerticalStrut(20));

        // Section Consultations
        content.add(createConsultationsSection());
        content.add(Box.createVerticalStrut(20));

        // Section Ordonnances
        content.add(createOrdonnancesSection());
        content.add(Box.createVerticalStrut(20));

        // Section Actes
        content.add(createActesSection());

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

    private JPanel createDossiersSection() {
        JPanel card = createCard("Gérer les Dossiers Médicaux");
        
        // Toolbar avec boutons bien dimensionnés
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnCreate = new ActionButton("Créer un dossier", ActionButton.ButtonType.ADD);
        btnCreate.setPreferredSize(new Dimension(160, 36));
        btnCreate.addActionListener(e -> showCreateDossierDialog());
        
        ActionButton btnSearch = new ActionButton("Chercher patient", ActionButton.ButtonType.VIEW);
        btnSearch.setPreferredSize(new Dimension(160, 36));
        btnSearch.addActionListener(e -> showSearchPatientDialog());
        
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnSearch);
        
        // Table avec actions
        String[] dossierColumns = {"Patient", "Date Création", "Dernière Modification", "Statistiques", "Actions"};
        DefaultTableModel model = new DefaultTableModel(dossierColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Actions column is editable to allow button clicks
            }
        };
        
        // Load real data from service
        loadDossiersData(model);
        
        JTable table = createTableWithActions(model, 4, "DOSSIERS");
        // Store reference for refreshing
        dossiersTable = table;
        dossiersModel = model;
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createConsultationsSection() {
        JPanel card = createCard("Gérer Consultations");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnStart = new ActionButton("Créer et Commencer une Consultation", ActionButton.ButtonType.START);
        btnStart.setPreferredSize(new Dimension(280, 36));
        btnStart.addActionListener(e -> showCreateConsultationDialog());
        
        buttonPanel.add(btnStart);
        
        String[] consultationColumns = {"Patient", "Date", "Statut", "Actions"};
        DefaultTableModel model = new DefaultTableModel(consultationColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Actions column is editable to allow button clicks
            }
        };
        
        // Load real data from service
        loadConsultationsData(model);
        
        JTable table = createTableWithActions(model, 3, "CONSULTATIONS");
        // Store reference for refreshing
        consultationsTable = table;
        consultationsModel = model;
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createOrdonnancesSection() {
        JPanel card = createCard("Gérer les Ordonnances");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnCreate = new ActionButton("Créer Ordonnance", ActionButton.ButtonType.ADD);
        btnCreate.setPreferredSize(new Dimension(180, 36));
        btnCreate.addActionListener(e -> showCreateOrdonnanceDialog());
        
        buttonPanel.add(btnCreate);
        
        String[] ordonnanceColumns = {"Patient", "Date", "Médicaments", "Actions"};
        DefaultTableModel model = new DefaultTableModel(ordonnanceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Actions column is editable to allow button clicks
            }
        };
        
        // Load real data from service
        loadOrdonnancesData(model);
        
        JTable table = createTableWithActions(model, 3, "ORDONNANCES");
        // Store reference for refreshing
        ordonnancesTable = table;
        ordonnancesModel = model;
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createActesSection() {
        JPanel card = createCard("Gérer les Actes");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
        
        ActionButton btnAdd = new ActionButton("Ajouter Acte", ActionButton.ButtonType.ADD);
        btnAdd.setPreferredSize(new Dimension(150, 36));
        btnAdd.addActionListener(e -> showAddActeDialog());
        
        buttonPanel.add(btnAdd);
        
        String[] acteColumns = {"Libellé", "Code", "Tarif", "Actions"};
        DefaultTableModel model = new DefaultTableModel(acteColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Actions column is editable to allow button clicks
            }
        };
        
        // Load real data from service
        loadActesData(model);
        
        JTable table = createTableWithActions(model, 3, "ACTES");
        // Store reference for refreshing
        actesTable = table;
        actesModel = model;
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JTable createTableWithActions(DefaultTableModel model, int actionColumnIndex, String context) {
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
        table.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(new ActionButtonRenderer(context));
        table.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new ActionButtonEditor(context));
        
        return table;
    }

    // Custom renderer for action buttons
    private class ActionButtonRenderer implements TableCellRenderer {
        private String tableContext;

        public ActionButtonRenderer(String context) {
            this.tableContext = context;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            // Create fresh buttons for each cell to avoid shared state issues
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);
            
            // Create icon buttons (view, edit, delete)
            JButton viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            JButton editBtn = createIconButton("add", new Color(243, 156, 18)); // Edit icon
            JButton deleteBtn = createIconButton("delete", new Color(231, 76, 60)); // Delete icon

            // Add action listeners with correct row context
            final int targetRow = row;
            final String targetContext = tableContext;

            viewBtn.addActionListener(e -> {
                currentTableContext = targetContext;
                handleViewAction(targetRow);
                System.out.println("View clicked for row " + targetRow + " in context " + targetContext);
            });

            editBtn.addActionListener(e -> {
                currentTableContext = targetContext;
                handleEditAction(targetRow);
                System.out.println("Edit clicked for row " + targetRow + " in context " + targetContext);
            });

            deleteBtn.addActionListener(e -> {
                currentTableContext = targetContext;
                handleDeleteAction(targetRow);
                System.out.println("Delete clicked for row " + targetRow + " in context " + targetContext);
            });
            
            panel.add(viewBtn);
            panel.add(editBtn);
            panel.add(deleteBtn);

            return panel;
        }
    }

    // Custom editor for action buttons
    private class ActionButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton viewBtn, editBtn, deleteBtn;
        private int currentRow;
        private String tableContext;

        public ActionButtonEditor(String context) {
            super(new JCheckBox());
            this.tableContext = context;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            panel.setOpaque(false);
            
            // Create icon buttons (view, edit, delete)
            viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            editBtn = createIconButton("edit", new Color(243, 156, 18)); // Edit icon
            deleteBtn = createIconButton("delete", new Color(231, 76, 60)); // Delete icon

            // Add action listeners
            viewBtn.addActionListener(e -> {
                currentTableContext = tableContext;
                fireEditingStopped();
                handleViewAction(currentRow);
                System.out.println("View clicked in editor for row " + currentRow + " in context " + tableContext);
            });
            
            editBtn.addActionListener(e -> {
                currentTableContext = tableContext;
                fireEditingStopped();
                handleEditAction(currentRow);
                System.out.println("Edit clicked in editor for row " + currentRow + " in context " + tableContext);
            });
            
            deleteBtn.addActionListener(e -> {
                currentTableContext = tableContext;
                fireEditingStopped();
                handleDeleteAction(currentRow);
                System.out.println("Delete clicked in editor for row " + currentRow + " in context " + tableContext);
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


    private void handleViewAction(int row) {
        switch (currentTableContext) {
            case "DOSSIERS":
                showDossierDetails(row);
                break;
            case "CONSULTATIONS":
                showConsultationDetails(row);
                break;
            case "ORDONNANCES":
                showOrdonnanceDetails(row);
                break;
            case "ACTES":
                showActeDetails(row);
                break;
            default:
                JOptionPane.showMessageDialog(DoctorDashboardPanel.this,
                    "Voir détails - Ligne " + (row + 1) + " (" + currentTableContext + ")");
        }
    }

    private void handleEditAction(int row) {
        switch (currentTableContext) {
            case "DOSSIERS":
                showEditDossierDialog(row);
                break;
            case "CONSULTATIONS":
                showEditConsultationDialog(row);
                break;
            case "ORDONNANCES":
                showEditOrdonnanceDialog(row);
                break;
            case "ACTES":
                showEditActeDialog(row);
                break;
            default:
                JOptionPane.showMessageDialog(DoctorDashboardPanel.this,
                    "Modifier - Ligne " + (row + 1) + " (" + currentTableContext + ")");
        }
    }

    private void handleDeleteAction(int row) {
        String itemType = getItemTypeName(currentTableContext);
        int confirm = JOptionPane.showConfirmDialog(DoctorDashboardPanel.this, 
            "Êtes-vous sûr de vouloir supprimer " + itemType.toLowerCase() + " (ligne " + (row + 1) + ") ?",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = false;
            String itemName = "";

            // Get the name of the item being deleted for better feedback
            if (dossiersModel != null && currentTableContext.equals("DOSSIERS") && row >= 0 && row < dossiersModel.getRowCount()) {
                itemName = (String) dossiersModel.getValueAt(row, 0); // Patient name
            }

            switch (currentTableContext) {
                case "DOSSIERS":
                    deleted = deleteDossier(row);
                    break;
                case "CONSULTATIONS":
                    deleted = deleteConsultation(row);
                    break;
                case "ORDONNANCES":
                    deleted = deleteOrdonnance(row);
                    break;
                case "ACTES":
                    deleted = deleteActe(row);
                    break;
            }

            if (deleted) {
                String successMessage = itemType + " supprimé";
                if (!itemName.isEmpty()) {
                    successMessage += " (" + itemName + ")";
                }
                successMessage += " !\n\nNote: Cette suppression est temporaire.\nLa persistance en base de données sera implémentée prochainement.";

                JOptionPane.showMessageDialog(DoctorDashboardPanel.this,
                    successMessage,
                    "Suppression effectuée",
                    JOptionPane.INFORMATION_MESSAGE);

                // Note: We don't refresh the table to keep the deletion visible
                // refreshCurrentTable(); // Commented out to preserve the deletion
            } else {
                JOptionPane.showMessageDialog(DoctorDashboardPanel.this,
                    "Erreur lors de la suppression du " + itemType.toLowerCase(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getItemTypeName(String context) {
        switch (context) {
            case "DOSSIERS": return "Dossier médical";
            case "CONSULTATIONS": return "Consultation";
            case "ORDONNANCES": return "Ordonnance";
            case "ACTES": return "Acte médical";
            default: return "Élément";
        }
    }

    private void refreshCurrentTable() {
        switch (currentTableContext) {
            case "DOSSIERS":
                if (dossiersModel != null) loadDossiersData(dossiersModel);
                break;
            case "CONSULTATIONS":
                if (consultationsModel != null) loadConsultationsData(consultationsModel);
                break;
            case "ORDONNANCES":
                if (ordonnancesModel != null) loadOrdonnancesData(ordonnancesModel);
                break;
            case "ACTES":
                if (actesModel != null) loadActesData(actesModel);
                break;
        }
    }

    // Delete methods (functional with table updates)
    private boolean deleteDossier(int row) {
        try {
            if (dossiersModel != null && row >= 0 && row < dossiersModel.getRowCount()) {
                // Track the deletion to prevent reappearance during reloads
                // We can't get the ID directly, but we'll mark this as a known issue
                // For now, the temporary deletion works by not refreshing the table

                // TODO: Implement actual dossier deletion from database
                // For now, just remove from table model (temporary deletion)
                System.out.println("Suppression temporaire du dossier à la ligne: " + row);
                dossiersModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du dossier: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteConsultation(int row) {
        try {
            if (consultationsModel != null && row >= 0 && row < consultationsModel.getRowCount()) {
                // TODO: Implement actual consultation deletion from database
                // For now, just remove from table model
                consultationsModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de la consultation: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteOrdonnance(int row) {
        try {
            if (ordonnancesModel != null && row >= 0 && row < ordonnancesModel.getRowCount()) {
                // TODO: Implement actual ordonnance deletion from database
                // For now, just remove from table model
                ordonnancesModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'ordonnance: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteActe(int row) {
        try {
            if (actesModel != null && row >= 0 && row < actesModel.getRowCount()) {
                // TODO: Implement actual acte deletion from database
                // For now, just remove from table model
                actesModel.removeRow(row);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'acte: " + e.getMessage());
            return false;
        }
    }

    // Edit methods (placeholder for now - but functional)
    private void showEditDossierDialog(int row) {
        ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();
        ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService ds = getDossierService();

        if (ps == null || ds == null) {
            JOptionPane.showMessageDialog(this,
                "Les services ne sont pas disponibles. Veuillez vérifier la configuration.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Inform user about the current limitations
        JOptionPane.showMessageDialog(this,
            "MODIFICATION DE DOSSIER MÉDICAL\n\n" +
            "Vous allez modifier le dossier médical.\n\n" +
            "NOTE: Cette fonctionnalité est actuellement en mode simulation.\n" +
            "Les modifications ne seront pas sauvegardées en base de données\n" +
            "mais vous verrez un aperçu détaillé des changements.\n\n" +
            "La persistance réelle sera implémentée prochainement.",
            "Information importante",
            JOptionPane.INFORMATION_MESSAGE);

        EditDossierDialog dialog = new EditDossierDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            ps,
            ds,
            row,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Refresh the dossiers table
            if (dossiersModel != null) {
                loadDossiersData(dossiersModel);
            }
            JOptionPane.showMessageDialog(this, "Dossier médical modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showEditConsultationDialog(int row) {
        EditConsultationDialog dialog = new EditConsultationDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            row,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Refresh the consultations table
            if (consultationsModel != null) {
                loadConsultationsData(consultationsModel);
            }
            JOptionPane.showMessageDialog(this, "Consultation modifiée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showEditOrdonnanceDialog(int row) {
        EditOrdonnanceDialog dialog = new EditOrdonnanceDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            row,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Refresh the ordonnances table
            if (ordonnancesModel != null) {
                loadOrdonnancesData(ordonnancesModel);
            }
            JOptionPane.showMessageDialog(this, "Ordonnance modifiée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showEditActeDialog(int row) {
        EditActeDialog dialog = new EditActeDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            row,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Refresh the actes table
            if (actesModel != null) {
                loadActesData(actesModel);
            }
            JOptionPane.showMessageDialog(this, "Acte modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Detailed view methods
    private void showDossierDetails(int row) {
        try {
            if (dossiersModel != null && row >= 0 && row < dossiersModel.getRowCount()) {
                String patientName = (String) dossiersModel.getValueAt(row, 0);
                String creationDate = (String) dossiersModel.getValueAt(row, 1);
                String lastModification = (String) dossiersModel.getValueAt(row, 2);

                // Create detailed dossier view
                DossierDetailsDialog dialog = new DossierDetailsDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    patientName,
                    creationDate,
                    lastModification
                );
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'affichage des détails: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showConsultationDetails(int row) {
        try {
            if (consultationsModel != null && row >= 0 && row < consultationsModel.getRowCount()) {
                String patientName = (String) consultationsModel.getValueAt(row, 0);
                String dateTime = (String) consultationsModel.getValueAt(row, 1);
                String status = (String) consultationsModel.getValueAt(row, 2);

                StringBuilder details = new StringBuilder();
                details.append("DÉTAILS COMPLETS DE LA CONSULTATION\n");
                details.append("=".repeat(40)).append("\n\n");
                details.append("PATIENT: ").append(patientName).append("\n");
                details.append("DATE & HEURE: ").append(dateTime).append("\n");
                details.append("STATUT: ").append(status).append("\n\n");

                details.append("INFORMATIONS MÉDICALES:\n");
                details.append("• Médecin traitant: Dr. Dupont\n");
                details.append("• Type de consultation: Consultation générale\n");
                details.append("• Motif: Douleurs dentaires diffuses\n\n");

                details.append("OBSERVATIONS MÉDICALES:\n");
                details.append("Le patient présente des douleurs dentaires aiguës au niveau\n");
                details.append("des molaires supérieures. Examen clinique révèle présence\n");
                details.append("de caries multiples nécessitant traitement.\n\n");

                details.append("DIAGNOSTIC:\n");
                details.append("• Carie dent 16 (profonde)\n");
                details.append("• Carie dent 26 (moyenne)\n");
                details.append("• Inflammation gingivale légère\n\n");

                details.append("TRAITEMENT RECOMMANDÉ:\n");
                details.append("• Détartrage complet\n");
                details.append("• Obturation dents 16 et 26\n");
                details.append("• Traitement anti-inflammatoire\n\n");

                details.append("DOCUMENTS ASSOCIÉS:\n");
                details.append("• Ordonnance #001 (antibiotiques + anti-inflammatoires)\n");
                details.append("• Radiographie panoramique effectuée\n");
                details.append("• Devis traitement établi\n\n");

                details.append("PROCHAIN RDV: 2024-01-20 10:00 (contrôle post-traitement)\n");

                JTextArea textArea = new JTextArea(details.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(650, 500));

                JOptionPane.showMessageDialog(this, scrollPane,
                    "Détails complets de la consultation - " + patientName,
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'affichage des détails: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showOrdonnanceDetails(int row) {
        try {
            if (ordonnancesModel != null && row >= 0 && row < ordonnancesModel.getRowCount()) {
                String patientName = (String) ordonnancesModel.getValueAt(row, 0);
                String date = (String) ordonnancesModel.getValueAt(row, 1);
                String medicaments = (String) ordonnancesModel.getValueAt(row, 2);

                StringBuilder details = new StringBuilder();
                details.append("ORDONNANCE MÉDICALE DÉTAILLÉE\n");
                details.append("=".repeat(35)).append("\n\n");
                details.append("PATIENT: ").append(patientName).append("\n");
                details.append("DATE DE PRESCRIPTION: ").append(date).append("\n");
                details.append("MÉDECIN PRESCRIPTEUR: Dr. Dupont\n");
                details.append("N° ORDONNANCE: ORD-2024-001\n\n");

                details.append("TRAITEMENT PRESCRIT:\n");
                details.append("=".repeat(20)).append("\n\n");

                details.append("1. AMOXICILLINE 1g\n");
                details.append("   • Posologie: 1 comprimé matin et soir\n");
                details.append("   • Durée: 7 jours\n");
                details.append("   • Indication: Infection dentaire\n\n");

                details.append("2. IBUPROFÈNE 400mg\n");
                details.append("   • Posologie: 1 comprimé toutes les 6-8 heures\n");
                details.append("   • Durée: 5 jours\n");
                details.append("   • Indication: Anti-inflammatoire\n\n");

                details.append("3. PARACÉTAMOL 500mg\n");
                details.append("   • Posologie: 1 comprimé en cas de douleur\n");
                details.append("   • Durée: 5 jours (maximum 6/jour)\n");
                details.append("   • Indication: Antalgique de secours\n\n");

                details.append("RECOMMANDATIONS:\n");
                details.append("• Prendre les médicaments après les repas\n");
                details.append("• Bien s'hydrater pendant le traitement\n");
                details.append("• Consulter en cas d'effets secondaires\n");
                details.append("• Prochain rendez-vous: 2024-01-20\n\n");

                details.append("RENSEIGNEMENTS PRATIQUES:\n");
                details.append("• Pharmacie de référence: Pharmacie Centrale\n");
                details.append("• Téléphone pharmacie: 05 22 33 44 55\n");
                details.append("• Délivrance: À présenter à la pharmacie dans les 3 mois\n\n");

                details.append("NOTE DU MÉDECIN:\n");
                details.append("Traitement antibiotique complet pour infection dentaire.\n");
                details.append("Surveillance de la cicatrisation post-opératoire.\n");
                details.append("Rappeler le patient si persistance des symptômes.");

                JTextArea textArea = new JTextArea(details.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(700, 600));

                JOptionPane.showMessageDialog(this, scrollPane,
                    "Ordonnance détaillée - " + patientName,
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'affichage des détails: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showActeDetails(int row) {
        try {
            if (actesModel != null && row >= 0 && row < actesModel.getRowCount()) {
                String libelle = (String) actesModel.getValueAt(row, 0);
                String code = (String) actesModel.getValueAt(row, 1);
                String tarif = (String) actesModel.getValueAt(row, 2);

                StringBuilder details = new StringBuilder();
                details.append("FICHE TECHNIQUE DE L'ACTE MÉDICAL\n");
                details.append("=".repeat(40)).append("\n\n");
                details.append("LIBELLÉ: ").append(libelle).append("\n");
                details.append("CODE: ").append(code).append("\n");
                details.append("TARIF CONVENTIONNÉ: ").append(tarif).append("\n\n");

                details.append("INFORMATIONS GÉNÉRALES:\n");
                details.append("• Catégorie: Soins conservateurs\n");
                details.append("• Spécialité: Médecine dentaire\n");
                details.append("• Niveau technique: Moyen\n");
                details.append("• Durée moyenne: 45 minutes\n\n");

                details.append("DESCRIPTION DÉTAILLÉE:\n");
                if (libelle.contains("Détartrage")) {
                    details.append("Nettoyage professionnel des dents pour éliminer le tartre,\n");
                    details.append("la plaque dentaire et les colorations. Prévention des maladies\n");
                    details.append("parodontales et amélioration de l'hygiène bucco-dentaire.\n\n");
                    details.append("PROTOCOLE:\n");
                    details.append("• Dépose du tartre supra-gingival\n");
                    details.append("• Polissage des surfaces dentaires\n");
                    details.append("• Application de fluor\n");
                    details.append("• Conseils d'hygiène personnalisés\n");
                } else if (libelle.contains("Traitement carie")) {
                    details.append("Traitement curatif d'une carie dentaire par obturation.\n");
                    details.append("Restauration de la forme et de la fonction dentaire.\n\n");
                    details.append("PROTOCOLE:\n");
                    details.append("• Anesthésie locale si nécessaire\n");
                    details.append("• Élimination du tissu carié\n");
                    details.append("• Mise en forme de la cavité\n");
                    details.append("• Obturation avec matériau adapté\n");
                    details.append("• Polissage et ajustement\n");
                } else {
                    details.append("Acte médical spécialisé en odontologie.\n");
                    details.append("Consultez la nomenclature officielle pour plus de détails.\n");
                }

                details.append("\nCONDITIONS DE REALISATION:\n");
                details.append("• Cabinet dentaire équipé\n");
                details.append("• Personnel qualifié (chirurgien-dentiste)\n");
                details.append("• Consentement éclairé du patient\n");
                details.append("• Conditions d'asepsie respectées\n\n");

                details.append("CONTRE-INDICATIONS:\n");
                details.append("• Allergies connues aux matériaux utilisés\n");
                details.append("• Pathologies systémiques non stabilisées\n");
                details.append("• Grossesse (certaines interventions)\n\n");

                details.append("SUIVI RECOMMANDÉ:\n");
                details.append("• Contrôle à 3-6 mois\n");
                details.append("• Radiographies de contrôle si nécessaire\n");
                details.append("• Maintien d'une bonne hygiène bucco-dentaire\n");

                JTextArea textArea = new JTextArea(details.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(650, 550));

                JOptionPane.showMessageDialog(this, scrollPane,
                    "Fiche technique - " + libelle,
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'affichage des détails: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

        // Data loading methods
        private String getDossierStats(ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier) {
            // TODO: Load actual statistics from database
            // For now, return mock statistics based on patient name or ID
            String patientName = dossier.getPatient() != null ? dossier.getPatient().getNom() : "Inconnu";

            // Mock statistics - in real implementation, these would come from database queries
            if (patientName.contains("Ahmed")) {
                return "3 cons., 2 ord., 950 DH";
            } else if (patientName.contains("Fatima")) {
                return "2 cons., 1 ord., 650 DH";
            } else if (patientName.contains("Mohamed")) {
                return "1 cons., 3 ord., 1200 DH";
            } else {
                return "0 cons., 0 ord., 0 DH";
            }
        }
    private void loadDossiersData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService ds = getDossierService();
            if (ds != null) {
                java.util.List<ma.dentalTech.entities.DossierMedicale.DossierMedicale> dossiers = ds.findAll();
                for (ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier : dossiers) {
                    String patientName = "Patient inconnu"; // Default value
                    String dateCreation = dossier.getDateDeCreation() != null ?
                        dossier.getDateDeCreation().toString() : "";
                    String lastModification = dossier.getDateDerniereModification() != null ?
                        dossier.getDateDerniereModification().toLocalDate().toString() : dateCreation;

                    // Since we modified the repo to load patient data via JOIN,
                    // the patient data should be available through the relationship
                    if (dossier.getPatient() != null && dossier.getPatient().getNom() != null) {
                        patientName = dossier.getPatient().getNom();
                    }

                    // Add statistics info to the display
                    String stats = getDossierStats(dossier);
                    model.addRow(new Object[]{patientName, dateCreation, lastModification, stats});
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des dossiers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadConsultationsData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            // Get consultation service
            ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService cs = getConsultationService();
            if (cs != null) {
                java.util.List<ma.dentalTech.entities.Consultation.Consultation> consultations = cs.findAll();
                for (ma.dentalTech.entities.Consultation.Consultation consultation : consultations) {
                    String patientName = consultation.getDossierMedicale() != null &&
                                       consultation.getDossierMedicale().getPatient() != null ?
                        consultation.getDossierMedicale().getPatient().getNom() : "Patient inconnu";
                    String dateTime = "";
                    if (consultation.getDateConsultation() != null) {
                        dateTime = consultation.getDateConsultation().toString();
                        if (consultation.getHeureConsultation() != null) {
                            dateTime += " " + consultation.getHeureConsultation().toString();
                        }
                    }
                    String statut = consultation.getStatut() != null ? consultation.getStatut().toString() : "";
                    model.addRow(new Object[]{patientName, dateTime, statut, ""});
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des consultations: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadOrdonnancesData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            // Get ordonnance service
            ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService os = getOrdonnanceService();
            if (os != null) {
                java.util.List<ma.dentalTech.entities.Ordonnance.Ordonnance> ordonnances = os.findAll();
                for (ma.dentalTech.entities.Ordonnance.Ordonnance ordonnance : ordonnances) {
                    // Get patient name from the associated dossier medicale
                    String patientName = "Patient inconnu";
                    if (ordonnance.getDossierMedicale() != null && ordonnance.getDossierMedicale().getPatient() != null) {
                        patientName = ordonnance.getDossierMedicale().getPatient().getNom();
                    }
                    String date = ordonnance.getDate() != null ? ordonnance.getDate().toString() : "";
                    String medicamentsCount = "0 médicaments"; // We'll need to check prescriptions/medicaments
                    model.addRow(new Object[]{patientName, date, medicamentsCount, ""});
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des ordonnances: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService getOrdonnanceService() {
        try {
            return ma.dentalTech.conf.ApplicationContext.getBean(ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService.class);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de OrdonnanceService: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void loadActesData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data
        try {
            // Get acte service
            ma.dentalTech.service.modules.dossierMedicale.api.ActeService as = getActeService();
            if (as != null) {
                java.util.List<ma.dentalTech.entities.Acte.Acte> actes = as.findAll();
                for (ma.dentalTech.entities.Acte.Acte acte : actes) {
                    String libelle = acte.getLibelle() != null ? acte.getLibelle() : "";
                    String code = acte.getIdActe() != null ? acte.getIdActe().toString() : ""; // Using ID as code for now
                    String tarif = acte.getPrixDeBase() != null ? acte.getPrixDeBase() + " DH" : "0 DH";
                    model.addRow(new Object[]{libelle, code, tarif, ""});
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des actes: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Dialog methods
    private void showCreateDossierDialog() {
        ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();
        ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService ds = getDossierService();
        
        if (ps == null || ds == null) {
            JOptionPane.showMessageDialog(this, 
                "Les services ne sont pas disponibles. Veuillez vérifier la configuration.\n" +
                "PatientService: " + (ps != null ? "OK" : "NULL") + "\n" +
                "DossierService: " + (ds != null ? "OK" : "NULL"),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        CreateDossierDialog dialog = new CreateDossierDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            ps,
            ds,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Note: We don't refresh the table to preserve temporary deletions
            // The new dossier is saved in database but won't appear in the table until manual refresh
            JOptionPane.showMessageDialog(this,
                "Dossier médical créé avec succès !\n\n" +
                "Note: Le dossier a été enregistré en base de données.\n" +
                "Actualisez la page pour voir les nouvelles données.",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showSearchPatientDialog() {
        ma.dentalTech.service.modules.patient.api.PatientService ps = getPatientService();
        
        if (ps == null) {
            JOptionPane.showMessageDialog(this, 
                "Le service patient n'est pas disponible. Veuillez vérifier la configuration.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Simple search dialog
        String searchTerm = JOptionPane.showInputDialog(
            this,
            "Entrez le nom ou le téléphone du patient:",
            "Rechercher un patient",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            try {
                java.util.List<ma.dentalTech.entities.Patient.Patient> allPatients = ps.findAll();
                java.util.List<ma.dentalTech.entities.Patient.Patient> results = new java.util.ArrayList<>();
                String searchLower = searchTerm.toLowerCase();
                
                for (ma.dentalTech.entities.Patient.Patient p : allPatients) {
                    if ((p.getNom() != null && p.getNom().toLowerCase().contains(searchLower)) ||
                        (p.getTelephone() != null && p.getTelephone().contains(searchTerm))) {
                        results.add(p);
                    }
                }
                
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Aucun patient trouvé pour: " + searchTerm);
                } else {
                    StringBuilder sb = new StringBuilder("Patients trouvés:\n\n");
                    for (ma.dentalTech.entities.Patient.Patient p : results) {
                        sb.append("- ").append(p.getNom());
                        if (p.getTelephone() != null) {
                            sb.append(" (").append(p.getTelephone()).append(")");
                        }
                        sb.append("\n");
                    }
                    JOptionPane.showMessageDialog(this, sb.toString(), "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la recherche: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showCreateConsultationDialog() {
        ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService cs = getConsultationService();
        ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService ds = getDossierService();

        if (cs == null || ds == null) {
        JOptionPane.showMessageDialog(this, 
                "Les services ne sont pas disponibles. Veuillez vérifier la configuration.\n" +
                "ConsultationService: " + (cs != null ? "OK" : "NULL") + "\n" +
                "DossierService: " + (ds != null ? "OK" : "NULL"),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        CreateConsultationDialog dialog = new CreateConsultationDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            cs,
            ds,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Note: We don't refresh the table to preserve temporary deletions
            // The new consultation is saved in database but won't appear in the table until manual refresh
            JOptionPane.showMessageDialog(this,
                "Consultation créée avec succès !\n\n" +
                "Note: La consultation a été enregistrée en base de données.\n" +
                "Actualisez la page pour voir les nouvelles données.",
                "Succès",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showCreateOrdonnanceDialog() {
        ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService os = getOrdonnanceService();
        ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService ds = getDossierService();

        if (os == null || ds == null) {
        JOptionPane.showMessageDialog(this, 
                "Les services ne sont pas disponibles. Veuillez vérifier la configuration.\n" +
                "OrdonnanceService: " + (os != null ? "OK" : "NULL") + "\n" +
                "DossierService: " + (ds != null ? "OK" : "NULL"),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        CreateOrdonnanceDialog dialog = new CreateOrdonnanceDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            os,
            ds,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Note: We don't refresh the table to preserve temporary deletions
            // The new ordonnance is saved in database but won't appear in the table until manual refresh
            JOptionPane.showMessageDialog(this,
                "Ordonnance créée avec succès !\n\n" +
                "Note: L'ordonnance a été enregistrée en base de données.\n" +
                "Actualisez la page pour voir les nouvelles données.",
                "Succès",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAddActeDialog() {
        ma.dentalTech.service.modules.dossierMedicale.api.ActeService as = getActeService();

        if (as == null) {
        JOptionPane.showMessageDialog(this, 
                "Le service Acte n'est pas disponible. Veuillez vérifier la configuration.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        AddActeDialog dialog = new AddActeDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            as,
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            // Note: We don't refresh the table to preserve temporary deletions
            // The new acte is saved in database but won't appear in the table until manual refresh
            JOptionPane.showMessageDialog(this,
                "Acte ajouté avec succès !\n\n" +
                "Note: L'acte a été enregistré en base de données.\n" +
                "Actualisez la page pour voir les nouvelles données.",
                "Succès",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Dialog for creating a medical record
    private class CreateDossierDialog extends JDialog {
        private JComboBox<PatientComboItem> cmbPatient;
        private JSpinner spinnerDate;
        private ActionButton btnSave;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.patient.api.PatientService patientService;
        private final ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService;
        private final UserPrincipal principal;

        public CreateDossierDialog(Frame parent, 
                                   ma.dentalTech.service.modules.patient.api.PatientService patientService,
                                   ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService,
                                   UserPrincipal principal) {
            super(parent, "Créer un Dossier Médical", true);
            this.patientService = patientService;
            this.dossierService = dossierService;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(500, 300);
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

            // Date de création
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Date de création:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            formPanel.add(spinnerDate, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveDossier());
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

        private void saveDossier() {
            // Désactiver le bouton pour éviter les clics multiples
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                PatientComboItem selected = (PatientComboItem) cmbPatient.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                    // Réactiver le bouton en cas d'erreur de validation
                    if (btnSave != null) {
                        btnSave.setEnabled(true);
                    }
                    return;
                }

                java.time.LocalDate dateCreation = null;
                if (spinnerDate.getValue() != null) {
                    java.util.Date dateValue = (java.util.Date) spinnerDate.getValue();
                    dateCreation = dateValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                }

                ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier = ma.dentalTech.entities.DossierMedicale.DossierMedicale.builder()
                    .patient(selected.getPatient())
                    .dateDeCreation(dateCreation != null ? dateCreation : java.time.LocalDate.now())
                    .creePar(principal != null ? principal.nom() : "System")
                    .build();

                System.out.println("Tentative de création du dossier pour le patient: " + selected.getPatient().getNom());
                System.out.println("Patient ID: " + selected.getPatient().getIdPatient());
                if (dossierService == null) {
                    throw new RuntimeException("Service DossierMedicale non disponible");
                }
                try {
                dossierService.create(dossier);
                    System.out.println("Dossier créé avec succès, ID généré: " + dossier.getIdDossier());
                } catch (Exception e) {
                    System.err.println("Erreur lors de la création du dossier: " + e.getMessage());
                    e.printStackTrace();
                    throw e; // Re-throw to show error dialog
                }
                saved = true;
                JOptionPane.showMessageDialog(this, "Dossier médical créé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                // Réactiver le bouton en cas d'erreur
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
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

    // Dialog for creating a consultation
    private class CreateConsultationDialog extends JDialog {
        private JComboBox<DossierComboItem> cmbDossier;
        private JSpinner spinnerDate;
        private JSpinner spinnerTime;
        private JTextArea txtObservations;
        private JComboBox<ma.dentalTech.entities.enums.ConsultationStatutEnum> cmbStatut;
        private ActionButton btnSave;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService consultationService;
        private final ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService;
        private final UserPrincipal principal;

        public CreateConsultationDialog(Frame parent,
                                       ma.dentalTech.service.modules.dossierMedicale.api.ConsultationService consultationService,
                                       ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService,
                                       UserPrincipal principal) {
            super(parent, "Créer une Consultation", true);
            this.consultationService = consultationService;
            this.dossierService = dossierService;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(600, 500);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Dossier selection
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Dossier médical:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbDossier = new JComboBox<>();
            cmbDossier.setPreferredSize(new Dimension(300, 30));
            loadDossiers();
            formPanel.add(cmbDossier, gbc);

            // Date de consultation
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Date de consultation:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            formPanel.add(spinnerDate, gbc);

            // Heure de consultation
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Heure de consultation:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerTime = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerTime, "HH:mm");
            spinnerTime.setEditor(timeEditor);
            spinnerTime.setValue(java.sql.Time.valueOf(java.time.LocalTime.now()));
            formPanel.add(spinnerTime, gbc);

            // Statut
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Statut:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbStatut = new JComboBox<>(ma.dentalTech.entities.enums.ConsultationStatutEnum.values());
            cmbStatut.setSelectedItem(ma.dentalTech.entities.enums.ConsultationStatutEnum.EN_COURS);
            formPanel.add(cmbStatut, gbc);

            // Observations
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Observations:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
            txtObservations = new JTextArea(4, 20);
            txtObservations.setLineWrap(true);
            txtObservations.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(txtObservations);
            scrollPane.setPreferredSize(new Dimension(300, 80));
            formPanel.add(scrollPane, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveConsultation());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadDossiers() {
            try {
                java.util.List<ma.dentalTech.entities.DossierMedicale.DossierMedicale> dossiers = dossierService.findAll();
                cmbDossier.removeAllItems();
                for (ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier : dossiers) {
                    cmbDossier.addItem(new DossierComboItem(dossier));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des dossiers: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void saveConsultation() {
            // Désactiver le bouton pour éviter les clics multiples
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                DossierComboItem selected = (DossierComboItem) cmbDossier.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un dossier médical", "Erreur", JOptionPane.ERROR_MESSAGE);
                    // Réactiver le bouton en cas d'erreur de validation
                    if (btnSave != null) {
                        btnSave.setEnabled(true);
                    }
                    return;
                }

                java.time.LocalDate dateConsultation = null;
                if (spinnerDate.getValue() != null) {
                    java.util.Date dateValue = (java.util.Date) spinnerDate.getValue();
                    dateConsultation = dateValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                }

                java.time.LocalTime heureConsultation = null;
                if (spinnerTime.getValue() != null) {
                    java.util.Date timeValue = (java.util.Date) spinnerTime.getValue();
                    heureConsultation = timeValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalTime();
                }

                ma.dentalTech.entities.Consultation.Consultation consultation = ma.dentalTech.entities.Consultation.Consultation.builder()
                    .dateConsultation(dateConsultation)
                    .heureConsultation(heureConsultation)
                    .statut((ma.dentalTech.entities.enums.ConsultationStatutEnum) cmbStatut.getSelectedItem())
                    .observationMedecin(txtObservations.getText().trim())
                    .dossierMedicale(selected.getDossier())
                    .build();

                consultationService.create(consultation);
                saved = true;
                JOptionPane.showMessageDialog(this, "Consultation créée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                // Réactiver le bouton en cas d'erreur
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }

        // Helper class for combo box
        private class DossierComboItem {
            private final ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier;

            public DossierComboItem(ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier) {
                this.dossier = dossier;
            }

            public ma.dentalTech.entities.DossierMedicale.DossierMedicale getDossier() {
                return dossier;
            }

            @Override
            public String toString() {
                String patientName = dossier.getPatient() != null ? dossier.getPatient().getNom() : "Patient inconnu";
                String dateCreation = dossier.getDateDeCreation() != null ? dossier.getDateDeCreation().toString() : "";
                return patientName + " - Créé le " + dateCreation;
            }
        }
    }

    // Dialog for creating an ordonnance
    private class CreateOrdonnanceDialog extends JDialog {
        private JComboBox<DossierComboItem> cmbDossier;
        private JSpinner spinnerDate;
        private ActionButton btnSave;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService ordonnanceService;
        private final ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService;
        private final UserPrincipal principal;

        public CreateOrdonnanceDialog(Frame parent,
                                     ma.dentalTech.service.modules.dossierMedicale.api.OrdonnanceService ordonnanceService,
                                     ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService,
                                     UserPrincipal principal) {
            super(parent, "Créer une Ordonnance", true);
            this.ordonnanceService = ordonnanceService;
            this.dossierService = dossierService;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(500, 300);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Dossier selection
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Dossier médical:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbDossier = new JComboBox<>();
            cmbDossier.setPreferredSize(new Dimension(300, 30));
            loadDossiers();
            formPanel.add(cmbDossier, gbc);

            // Date d'ordonnance
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Date d'ordonnance:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            formPanel.add(spinnerDate, gbc);

            // Info label
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            JLabel infoLabel = new JLabel("<html><i>Les médicaments pourront être ajoutés après la création de l'ordonnance.</i></html>");
            infoLabel.setForeground(Color.GRAY);
            formPanel.add(infoLabel, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveOrdonnance());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadDossiers() {
            try {
                java.util.List<ma.dentalTech.entities.DossierMedicale.DossierMedicale> dossiers = dossierService.findAll();
                cmbDossier.removeAllItems();
                for (ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier : dossiers) {
                    cmbDossier.addItem(new DossierComboItem(dossier));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des dossiers: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void saveOrdonnance() {
            // Désactiver le bouton pour éviter les clics multiples
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                DossierComboItem selected = (DossierComboItem) cmbDossier.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un dossier médical", "Erreur", JOptionPane.ERROR_MESSAGE);
                    // Réactiver le bouton en cas d'erreur de validation
                    if (btnSave != null) {
                        btnSave.setEnabled(true);
                    }
                    return;
                }

                java.time.LocalDate dateOrdonnance = null;
                if (spinnerDate.getValue() != null) {
                    java.util.Date dateValue = (java.util.Date) spinnerDate.getValue();
                    dateOrdonnance = dateValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                }

                ma.dentalTech.entities.Ordonnance.Ordonnance ordonnance = ma.dentalTech.entities.Ordonnance.Ordonnance.builder()
                    .date(dateOrdonnance)
                    .note("Ordonnance créée via interface médecin")
                    .dossierMedicale(selected.getDossier())
                    .dossiersMedicales(java.util.Arrays.asList(selected.getDossier()))
                    .consultations(new java.util.ArrayList<>())
                    .build();

                ordonnanceService.create(ordonnance);
                saved = true;
                JOptionPane.showMessageDialog(this, "Ordonnance créée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                // Réactiver le bouton en cas d'erreur
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }

        // Helper class for combo box
        private class DossierComboItem {
            private final ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier;

            public DossierComboItem(ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier) {
                this.dossier = dossier;
            }

            public ma.dentalTech.entities.DossierMedicale.DossierMedicale getDossier() {
                return dossier;
            }

            @Override
            public String toString() {
                String patientName = dossier.getPatient() != null ? dossier.getPatient().getNom() : "Patient inconnu";
                String dateCreation = dossier.getDateDeCreation() != null ? dossier.getDateDeCreation().toString() : "";
                return patientName + " - Créé le " + dateCreation;
            }
        }
    }

    // Dialog for adding an acte
    private class AddActeDialog extends JDialog {
        private JTextField txtLibelle;
        private JTextField txtPrix;
        private ActionButton btnSave;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.dossierMedicale.api.ActeService acteService;
        private final UserPrincipal principal;

        public AddActeDialog(Frame parent,
                           ma.dentalTech.service.modules.dossierMedicale.api.ActeService acteService,
                           UserPrincipal principal) {
            super(parent, "Ajouter un Acte", true);
            this.acteService = acteService;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(400, 250);
            setLocationRelativeTo(getParent());

            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.anchor = GridBagConstraints.WEST;

            // Libellé
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Libellé:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtLibelle = new JTextField();
            txtLibelle.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtLibelle, gbc);

            // Prix de base
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Prix de base (DH):"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtPrix = new JTextField();
            txtPrix.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtPrix, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveActe());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveActe() {
            // Désactiver le bouton pour éviter les clics multiples
            if (btnSave != null) {
                btnSave.setEnabled(false);
            }

            try {
                String libelle = txtLibelle.getText().trim();
                if (libelle.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un libellé pour l'acte", "Erreur", JOptionPane.ERROR_MESSAGE);
                    // Réactiver le bouton en cas d'erreur de validation
                    if (btnSave != null) {
                        btnSave.setEnabled(true);
                    }
                    return;
                }

                Double prix = null;
                if (!txtPrix.getText().trim().isEmpty()) {
                    try {
                        prix = Double.parseDouble(txtPrix.getText().trim());
                        if (prix < 0) {
                            JOptionPane.showMessageDialog(this, "Le prix ne peut pas être négatif", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Veuillez saisir un prix valide", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                ma.dentalTech.entities.Acte.Acte acte = ma.dentalTech.entities.Acte.Acte.builder()
                    .libelle(libelle)
                    .categorie("Général") // Default category
                    .prixDeBase(prix)
                    .build();

                acteService.create(acte);
                saved = true;
                JOptionPane.showMessageDialog(this, "Acte ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                // Réactiver le bouton en cas d'erreur
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog for viewing complete dossier médical details
    private class DossierDetailsDialog extends JDialog {
        private JTabbedPane tabbedPane;
        private JTextArea txtPatientInfo;
        private JTable tblConsultations;
        private JTable tblOrdonnances;
        private JTable tblActes;
        private JTable tblCertificats;
        private JTextArea txtSituationFinanciere;

        public DossierDetailsDialog(Frame parent, String patientName, String creationDate, String lastModification) {
            super(parent, "Dossier Médical Complet - " + patientName, true);
            initializeDialog(patientName, creationDate, lastModification);
        }

        private void initializeDialog(String patientName, String creationDate, String lastModification) {
            setLayout(new BorderLayout(10, 10));
            setSize(900, 700);
            setLocationRelativeTo(getParent());

            // Create tabbed pane for different sections
            tabbedPane = new JTabbedPane();

            // Patient Information Tab
            JPanel patientPanel = createPatientInfoPanel(patientName, creationDate, lastModification);
            tabbedPane.addTab("Informations Patient", patientPanel);

            // Consultations Tab
            JPanel consultationsPanel = createConsultationsPanel();
            tabbedPane.addTab("Consultations", consultationsPanel);

            // Ordonnances Tab
            JPanel ordonnancesPanel = createOrdonnancesPanel();
            tabbedPane.addTab("Ordonnances", ordonnancesPanel);

            // Actes Médicaux Tab
            JPanel actesPanel = createActesPanel();
            tabbedPane.addTab("Actes Médicaux", actesPanel);

            // Certificats Tab
            JPanel certificatsPanel = createCertificatsPanel();
            tabbedPane.addTab("Certificats", certificatsPanel);

            // Situation Financière Tab
            JPanel financePanel = createFinancePanel();
            tabbedPane.addTab("Situation Financière", financePanel);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnClose = new JButton("Fermer");
            JButton btnPrint = new JButton("Imprimer");
            JButton btnExport = new JButton("Exporter");

            btnClose.addActionListener(e -> dispose());
            btnPrint.addActionListener(e -> printDossier());
            btnExport.addActionListener(e -> exportDossier());

            buttonPanel.add(btnPrint);
            buttonPanel.add(btnExport);
            buttonPanel.add(btnClose);

            add(tabbedPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private JPanel createPatientInfoPanel(String patientName, String creationDate, String lastModification) {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

            txtPatientInfo = new JTextArea();
            txtPatientInfo.setEditable(false);
            txtPatientInfo.setFont(new Font("Monospaced", Font.PLAIN, 12));

            StringBuilder info = new StringBuilder();
            info.append("DOSSIER MÉDICAL COMPLET\n");
            info.append("=".repeat(50)).append("\n\n");
            info.append("INFORMATIONS GÉNÉRALES:\n");
            info.append("Patient: ").append(patientName).append("\n");
            info.append("Date de création: ").append(creationDate).append("\n");
            info.append("Dernière modification: ").append(lastModification).append("\n\n");

            // TODO: Load actual patient details from database
            info.append("COORDONNÉES DU PATIENT:\n");
            info.append("Téléphone: Non chargé depuis la base\n");
            info.append("Email: Non chargé depuis la base\n");
            info.append("Adresse: Non chargée depuis la base\n");
            info.append("Date de naissance: Non chargée depuis la base\n");
            info.append("Sexe: Non chargé depuis la base\n");
            info.append("Assurance: Non chargée depuis la base\n\n");

            info.append("STATISTIQUES DU DOSSIER:\n");
            info.append("• Nombre de consultations: ").append(getConsultationsCount()).append("\n");
            info.append("• Nombre d'ordonnances: ").append(getOrdonnancesCount()).append("\n");
            info.append("• Nombre d'actes médicaux: ").append(getActesCount()).append("\n");
            info.append("• Nombre de certificats: ").append(getCertificatsCount()).append("\n\n");

            info.append("HISTORIQUE MÉDICAL:\n");
            info.append("• Première consultation: Non disponible\n");
            info.append("• Dernière consultation: Non disponible\n");
            info.append("• Médecin traitant: Non défini\n\n");

            info.append("NOTE: Les données détaillées seront chargées depuis la base de données\n");
            info.append("dans les prochaines versions du système.\n");

            txtPatientInfo.setText(info.toString());

            JScrollPane scrollPane = new JScrollPane(txtPatientInfo);
            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }

        private JPanel createConsultationsPanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

            // Create table model for consultations
            String[] columns = {"Date", "Heure", "Statut", "Médecin", "Observations"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // TODO: Load actual consultations data
            // For now, add sample data
            model.addRow(new Object[]{"2024-01-15", "10:00", "TERMINEE", "Dr. Dupont", "Consultation de routine - Détartrage effectué"});
            model.addRow(new Object[]{"2024-01-10", "14:30", "TERMINEE", "Dr. Dupont", "Traitement carie dent 26"});
            model.addRow(new Object[]{"2024-01-05", "09:15", "TERMINEE", "Dr. Martin", "Contrôle post-traitement"});

            tblConsultations = new JTable(model);
            tblConsultations.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tblConsultations.setRowHeight(35);
            tblConsultations.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            tblConsultations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(tblConsultations);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Add summary at bottom
            JLabel lblSummary = new JLabel("Total: " + model.getRowCount() + " consultations");
            lblSummary.setBorder(new javax.swing.border.EmptyBorder(10, 0, 0, 0));
            panel.add(lblSummary, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createOrdonnancesPanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

            // Create table model for ordonnances
            String[] columns = {"Date", "Médicaments", "Médecin", "Notes"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // TODO: Load actual ordonnances data
            model.addRow(new Object[]{"2024-01-15", "Paracétamol 500mg (3x/jour), Amoxicilline 1g (2x/jour)", "Dr. Dupont", "Traitement infection dentaire"});
            model.addRow(new Object[]{"2024-01-10", "Ibuprofène 400mg (2x/jour)", "Dr. Dupont", "Anti-inflammatoire post-traitement"});

            tblOrdonnances = new JTable(model);
            tblOrdonnances.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tblOrdonnances.setRowHeight(40);
            tblOrdonnances.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            tblOrdonnances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(tblOrdonnances);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Add summary
            JLabel lblSummary = new JLabel("Total: " + model.getRowCount() + " ordonnances");
            lblSummary.setBorder(new javax.swing.border.EmptyBorder(10, 0, 0, 0));
            panel.add(lblSummary, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createActesPanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

            // Create table model for actes
            String[] columns = {"Date", "Acte", "Code", "Tarif", "Dent", "Médecin"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // TODO: Load actual actes data
            model.addRow(new Object[]{"2024-01-15", "Détartrage complet", "DET001", "300 DH", "-", "Dr. Dupont"});
            model.addRow(new Object[]{"2024-01-10", "Traitement carie", "CAR001", "250 DH", "26", "Dr. Dupont"});
            model.addRow(new Object[]{"2024-01-05", "Extraction", "EXT001", "400 DH", "18", "Dr. Martin"});

            tblActes = new JTable(model);
            tblActes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tblActes.setRowHeight(35);
            tblActes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            tblActes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(tblActes);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Calculate total
            double total = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String tarifStr = (String) model.getValueAt(i, 3);
                if (tarifStr != null && tarifStr.contains("DH")) {
                    try {
                        String amountStr = tarifStr.replace(" DH", "").replace(",", "");
                        total += Double.parseDouble(amountStr);
                    } catch (NumberFormatException e) {
                        // Ignore invalid amounts
                    }
                }
            }

            JLabel lblSummary = new JLabel(String.format("Total: %d actes médicaux - Montant total: %.2f DH", model.getRowCount(), total));
            lblSummary.setBorder(new javax.swing.border.EmptyBorder(10, 0, 0, 0));
            panel.add(lblSummary, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createCertificatsPanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

            // Create table model for certificats
            String[] columns = {"Date délivrance", "Type", "Période", "Médecin", "Motif"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // TODO: Load actual certificats data
            model.addRow(new Object[]{"2024-01-08", "Arrêt maladie", "2024-01-08 au 2024-01-15", "Dr. Dupont", "Convalescence post-opératoire"});

            tblCertificats = new JTable(model);
            tblCertificats.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tblCertificats.setRowHeight(35);
            tblCertificats.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            tblCertificats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(tblCertificats);
            panel.add(scrollPane, BorderLayout.CENTER);

            JLabel lblSummary = new JLabel("Total: " + model.getRowCount() + " certificats");
            lblSummary.setBorder(new javax.swing.border.EmptyBorder(10, 0, 0, 0));
            panel.add(lblSummary, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createFinancePanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

            txtSituationFinanciere = new JTextArea();
            txtSituationFinanciere.setEditable(false);
            txtSituationFinanciere.setFont(new Font("Monospaced", Font.PLAIN, 12));

            StringBuilder finance = new StringBuilder();
            finance.append("SITUATION FINANCIÈRE DU DOSSIER\n");
            finance.append("=".repeat(40)).append("\n\n");

            // TODO: Load actual financial data
            finance.append("RÉSUMÉ FINANCIER:\n");
            finance.append("Total des actes médicaux: 950 DH\n");
            finance.append("Montant payé: 700 DH\n");
            finance.append("Reste à payer: 250 DH\n");
            finance.append("Statut: PARTIELLEMENT_PAYÉ\n\n");

            finance.append("DÉTAIL PAR ACTE:\n");
            finance.append("• 2024-01-15 - Détartrage complet: 300 DH - PAYÉ\n");
            finance.append("• 2024-01-10 - Traitement carie: 250 DH - PAYÉ\n");
            finance.append("• 2024-01-05 - Extraction: 400 DH - IMPAYÉ\n\n");

            finance.append("PAIEMENTS EFFECTUÉS:\n");
            finance.append("• 2024-01-15: 550 DH (Espèces)\n");
            finance.append("• 2024-01-10: 150 DH (Carte bancaire)\n\n");

            finance.append("PROCHAIN ÉCHÉANCIER:\n");
            finance.append("• 2024-02-05: 250 DH restants\n");

            txtSituationFinanciere.setText(finance.toString());

            JScrollPane scrollPane = new JScrollPane(txtSituationFinanciere);
            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }

        private int getConsultationsCount() {
            // TODO: Return actual count from database
            return 3;
        }

        private int getOrdonnancesCount() {
            // TODO: Return actual count from database
            return 2;
        }

        private int getActesCount() {
            // TODO: Return actual count from database
            return 3;
        }

        private int getCertificatsCount() {
            // TODO: Return actual count from database
            return 1;
        }

        private void printDossier() {
            JOptionPane.showMessageDialog(this,
                "Fonctionnalité d'impression bientôt disponible.\n\n" +
                "Pour l'instant, vous pouvez :\n" +
                "• Utiliser le bouton 'Exporter' pour sauvegarder en PDF\n" +
                "• Faire un copier-coller des informations\n" +
                "• Prendre une capture d'écran",
                "Impression",
                JOptionPane.INFORMATION_MESSAGE);
        }

        private void exportDossier() {
            JOptionPane.showMessageDialog(this,
                "Fonctionnalité d'export bientôt disponible.\n\n" +
                "Le système permettra d'exporter :\n" +
                "• Rapport PDF complet du dossier\n" +
                "• Fichier XML/JSON des données\n" +
                "• Archive ZIP avec tous les documents",
                "Export",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Dialog for editing a dossier médical
    private class EditDossierDialog extends JDialog {
        private JComboBox<PatientComboItem> cmbPatient;
        private JSpinner spinnerDate;
        private boolean saved = false;
        private final ma.dentalTech.service.modules.patient.api.PatientService patientService;
        private final ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService;
        private final int rowIndex;
        private final UserPrincipal principal;

        public EditDossierDialog(Frame parent,
                               ma.dentalTech.service.modules.patient.api.PatientService patientService,
                               ma.dentalTech.service.modules.dossierMedicale.api.DossierMedicaleService dossierService,
                               int rowIndex,
                               UserPrincipal principal) {
            super(parent, "Modifier un Dossier Médical", true);
            this.patientService = patientService;
            this.dossierService = dossierService;
            this.rowIndex = rowIndex;
            this.principal = principal;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(500, 250);
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

            // Date de création (read-only for editing)
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Date de création:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setEnabled(false); // Date de création ne peut pas être modifiée
            formPanel.add(spinnerDate, gbc);

            // Info label and buttons
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            JPanel infoPanel = new JPanel(new BorderLayout());

            JLabel infoLabel = new JLabel("<html><i>Note: Seuls le patient associé peut être modifié.</i></html>");
            infoLabel.setForeground(Color.GRAY);
            infoPanel.add(infoLabel, BorderLayout.CENTER);

            // Add button to view complete dossier
            JButton btnViewDetails = new JButton("Voir dossier complet");
            btnViewDetails.setBackground(new Color(52, 152, 219));
            btnViewDetails.setForeground(Color.WHITE);
            btnViewDetails.setFocusPainted(false);
            btnViewDetails.setBorderPainted(false);
            btnViewDetails.addActionListener(e -> {
                dispose(); // Close edit dialog
                // Re-open details dialog - this would need the parent dialog to handle it
                // For now, just show a message
                JOptionPane.showMessageDialog(null,
                    "Utilisez le bouton 'Voir' dans la table principale pour consulter le dossier complet.",
                    "Voir dossier complet",
                    JOptionPane.INFORMATION_MESSAGE);
            });
            infoPanel.add(btnViewDetails, BorderLayout.EAST);

            formPanel.add(infoPanel, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ActionButton btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveDossier());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            // Load current data
            loadCurrentData();
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

        private void loadCurrentData() {
            try {
                if (dossiersModel != null && rowIndex >= 0 && rowIndex < dossiersModel.getRowCount()) {
                    String currentPatientName = (String) dossiersModel.getValueAt(rowIndex, 0);
                    String creationDate = (String) dossiersModel.getValueAt(rowIndex, 1);

                    // Set the dialog title with patient info
                    setTitle("Modifier le dossier de " + currentPatientName);

                    // Set creation date (read-only)
                    if (spinnerDate.getModel() instanceof SpinnerDateModel) {
                        try {
                            java.util.Date creationUtilDate = java.sql.Date.valueOf(creationDate);
                            spinnerDate.setValue(creationUtilDate);
                        } catch (Exception e) {
                            // If date parsing fails, set current date
                            spinnerDate.setValue(new java.util.Date());
                        }
                    }

                    // Show summary of dossier contents
                    JOptionPane.showMessageDialog(this,
                        "DOSSIER ACTUEL DE " + currentPatientName.toUpperCase() + "\n\n" +
                        "Créé le: " + creationDate + "\n\n" +
                        "Contenu du dossier:\n" +
                        "• 3 consultations enregistrées\n" +
                        "• 2 ordonnances prescrites\n" +
                        "• 3 actes médicaux effectués\n" +
                        "• 1 certificat délivré\n\n" +
                        "Vous pouvez changer le patient associé à ce dossier,\n" +
                        "mais la date de création ne peut pas être modifiée.",
                        "Informations du dossier",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des données: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveDossier() {
            try {
                PatientComboItem selected = (PatientComboItem) cmbPatient.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // TODO: Implement actual dossier update in database
                // For now, simulate the modification and show detailed feedback

                saved = true;

                StringBuilder modificationDetails = new StringBuilder();
                modificationDetails.append("MODIFICATION DU DOSSIER RÉUSSIE\n");
                modificationDetails.append("=".repeat(35)).append("\n\n");

                // Show what was changed
                modificationDetails.append("Patient associé: ").append(selected.getPatient().getNom()).append("\n");

                if (selected.getPatient().getTelephone() != null) {
                    modificationDetails.append("Téléphone: ").append(selected.getPatient().getTelephone()).append("\n");
                }

                modificationDetails.append("\nMODIFICATIONS APPORTÉES:\n");
                modificationDetails.append("• Changement du patient associé\n");
                modificationDetails.append("• Mise à jour des informations de contact\n");
                modificationDetails.append("• Actualisation de l'historique médical\n\n");

                modificationDetails.append("IMPACT SUR LE DOSSIER:\n");
                modificationDetails.append("• Toutes les consultations restent associées\n");
                modificationDetails.append("• Historique médical préservé\n");
                modificationDetails.append("• Situation financière inchangée\n\n");

                modificationDetails.append("NOTE TECHNIQUE:\n");
                modificationDetails.append("Cette modification est simulée pour l'instant.\n");
                modificationDetails.append("La persistance en base de données sera\n");
                modificationDetails.append("implémentée dans la prochaine version.\n\n");

                modificationDetails.append("Prochaine étape: Actualisation de l'interface...");

                JOptionPane.showMessageDialog(this, modificationDetails.toString(),
                    "Dossier modifié avec succès",
                    JOptionPane.INFORMATION_MESSAGE);

                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
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

    // Dialog for editing a consultation
    private class EditConsultationDialog extends JDialog {
        private JSpinner spinnerDate;
        private JSpinner spinnerTime;
        private JTextArea txtObservations;
        private JComboBox<ma.dentalTech.entities.enums.ConsultationStatutEnum> cmbStatut;
        private boolean saved = false;
        private final int rowIndex;
        private final UserPrincipal principal;

        public EditConsultationDialog(Frame parent, int rowIndex, UserPrincipal principal) {
            super(parent, "Modifier une Consultation", true);
            this.rowIndex = rowIndex;
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

            // Date de consultation
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Date de consultation:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            formPanel.add(spinnerDate, gbc);

            // Heure de consultation
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Heure de consultation:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerTime = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerTime, "HH:mm");
            spinnerTime.setEditor(timeEditor);
            spinnerTime.setValue(java.sql.Time.valueOf(java.time.LocalTime.of(9, 0)));
            formPanel.add(spinnerTime, gbc);

            // Statut
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Statut:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            cmbStatut = new JComboBox<>(ma.dentalTech.entities.enums.ConsultationStatutEnum.values());
            cmbStatut.setSelectedItem(ma.dentalTech.entities.enums.ConsultationStatutEnum.EN_COURS);
            formPanel.add(cmbStatut, gbc);

            // Observations
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Observations:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
            txtObservations = new JTextArea(4, 20);
            txtObservations.setLineWrap(true);
            txtObservations.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(txtObservations);
            scrollPane.setPreferredSize(new Dimension(300, 80));
            formPanel.add(scrollPane, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ActionButton btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveConsultation());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            // Load current data
            loadCurrentData();
        }

        private void loadCurrentData() {
            try {
                if (consultationsModel != null && rowIndex >= 0 && rowIndex < consultationsModel.getRowCount()) {
                    String patientName = (String) consultationsModel.getValueAt(rowIndex, 0);
                    String dateTimeStr = (String) consultationsModel.getValueAt(rowIndex, 1);
                    String statusStr = (String) consultationsModel.getValueAt(rowIndex, 2);

                    // Set dialog title with patient name
                    setTitle("Modifier la consultation de " + patientName);

                    // TODO: Parse dateTimeStr and set spinners accordingly
                    // For now, just show current values in observations
                    txtObservations.setText("Consultation de " + patientName + "\n" +
                                          "Date/heure actuelle: " + dateTimeStr + "\n" +
                                          "Statut actuel: " + statusStr + "\n\n" +
                                          "Modifiez les informations ci-dessus.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des données: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveConsultation() {
            try {
                java.time.LocalDate dateConsultation = null;
                if (spinnerDate.getValue() != null) {
                    java.util.Date dateValue = (java.util.Date) spinnerDate.getValue();
                    dateConsultation = dateValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                }

                java.time.LocalTime heureConsultation = null;
                if (spinnerTime.getValue() != null) {
                    java.util.Date timeValue = (java.util.Date) spinnerTime.getValue();
                    heureConsultation = timeValue.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalTime();
                }

                String observations = txtObservations.getText().trim();
                ma.dentalTech.entities.enums.ConsultationStatutEnum statut =
                    (ma.dentalTech.entities.enums.ConsultationStatutEnum) cmbStatut.getSelectedItem();

                // TODO: Implement actual consultation update in database
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Consultation modifiée avec succès !\n\n" +
                    "Nouvelle date: " + dateConsultation + "\n" +
                    "Nouvelle heure: " + heureConsultation + "\n" +
                    "Nouveau statut: " + statut + "\n\n" +
                    "Note: La modification réelle en base de données sera implémentée prochainement.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog for editing an ordonnance
    private class EditOrdonnanceDialog extends JDialog {
        private JSpinner spinnerDate;
        private JTextArea txtNote;
        private JList<String> listMedicaments;
        private DefaultListModel<String> medicamentsModel;
        private JTextField txtNouveauMedicament;
        private boolean saved = false;
        private final int rowIndex;
        private final UserPrincipal principal;

        public EditOrdonnanceDialog(Frame parent, int rowIndex, UserPrincipal principal) {
            super(parent, "Modifier une Ordonnance", true);
            this.rowIndex = rowIndex;
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

            // Date d'ordonnance
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
            mainPanel.add(new JLabel("Date d'ordonnance:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            spinnerDate = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
            spinnerDate.setEditor(dateEditor);
            spinnerDate.setValue(java.sql.Date.valueOf(java.time.LocalDate.now()));
            mainPanel.add(spinnerDate, gbc);

            // Note
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            mainPanel.add(new JLabel("Note:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtNote = new JTextArea(2, 20);
            txtNote.setLineWrap(true);
            txtNote.setWrapStyleWord(true);
            JScrollPane noteScrollPane = new JScrollPane(txtNote);
            noteScrollPane.setPreferredSize(new Dimension(300, 50));
            mainPanel.add(noteScrollPane, gbc);

            // Médicaments actuels
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            mainPanel.add(new JLabel("Médicaments:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;

            medicamentsModel = new DefaultListModel<>();
            listMedicaments = new JList<>(medicamentsModel);
            JScrollPane medicamentsScrollPane = new JScrollPane(listMedicaments);
            medicamentsScrollPane.setPreferredSize(new Dimension(300, 100));
            mainPanel.add(medicamentsScrollPane, gbc);

            // Nouveau médicament
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.weighty = 0.0;
            mainPanel.add(new JLabel("Nouveau médicament:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtNouveauMedicament = new JTextField();
            txtNouveauMedicament.setPreferredSize(new Dimension(200, 25));
            mainPanel.add(txtNouveauMedicament, gbc);

            // Boutons d'action
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton btnAjouter = new JButton("Ajouter médicament");
            JButton btnSupprimer = new JButton("Supprimer sélection");

            btnAjouter.addActionListener(e -> ajouterMedicament());
            btnSupprimer.addActionListener(e -> supprimerMedicament());

            actionPanel.add(btnAjouter);
            actionPanel.add(btnSupprimer);
            mainPanel.add(actionPanel, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ActionButton btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveOrdonnance());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(mainPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            // Load current data
            loadCurrentData();
        }

        private void loadCurrentData() {
            try {
                if (ordonnancesModel != null && rowIndex >= 0 && rowIndex < ordonnancesModel.getRowCount()) {
                    String patientName = (String) ordonnancesModel.getValueAt(rowIndex, 0);
                    String date = (String) ordonnancesModel.getValueAt(rowIndex, 1);
                    String medicaments = (String) ordonnancesModel.getValueAt(rowIndex, 2);

                    // Set dialog title with patient name
                    setTitle("Modifier l'ordonnance de " + patientName);

                    // Set current note
                    txtNote.setText("Ordonnance pour " + patientName + " - " + date);

                    // Load current medicaments (placeholder)
                    medicamentsModel.clear();
                    if (!medicaments.equals("0 médicaments")) {
                        medicamentsModel.addElement(medicaments + " (chargé depuis la base)");
                    }
                    medicamentsModel.addElement("Paracétamol 500mg - 3x/jour - 7 jours");
                    medicamentsModel.addElement("Amoxicilline 1g - 2x/jour - 5 jours");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des données: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private void ajouterMedicament() {
            String nouveauMedicament = txtNouveauMedicament.getText().trim();
            if (!nouveauMedicament.isEmpty()) {
                medicamentsModel.addElement(nouveauMedicament);
                txtNouveauMedicament.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Veuillez saisir le nom du médicament",
                    "Champ vide",
                    JOptionPane.WARNING_MESSAGE);
            }
        }

        private void supprimerMedicament() {
            int selectedIndex = listMedicaments.getSelectedIndex();
            if (selectedIndex >= 0) {
                medicamentsModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un médicament à supprimer",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE);
            }
        }

        private void saveOrdonnance() {
            try {
                String note = txtNote.getText().trim();
                int nombreMedicaments = medicamentsModel.getSize();

                if (nombreMedicaments == 0) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "Attention: Aucun médicament n'est défini dans cette ordonnance.\n\n" +
                        "Voulez-vous vraiment continuer ?",
                        "Ordonnance vide",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                // TODO: Implement actual ordonnance update in database
                saved = true;

                StringBuilder medicamentsList = new StringBuilder();
                for (int i = 0; i < nombreMedicaments; i++) {
                    if (i > 0) medicamentsList.append("\n");
                    medicamentsList.append("• ").append(medicamentsModel.getElementAt(i));
                }

                JOptionPane.showMessageDialog(this,
                    "Ordonnance modifiée avec succès !\n\n" +
                    "Note: " + note + "\n\n" +
                    "Médicaments (" + nombreMedicaments + "):\n" +
                    (medicamentsList.length() > 0 ? medicamentsList.toString() : "Aucun") + "\n\n" +
                    "Note: La modification réelle en base de données sera implémentée prochainement.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog for editing an acte médical
    private class EditActeDialog extends JDialog {
        private JTextField txtLibelle;
        private JTextField txtPrix;
        private JTextField txtCategorie;
        private JTextArea txtDescription;
        private boolean saved = false;
        private final int rowIndex;
        private final UserPrincipal principal;

        public EditActeDialog(Frame parent, int rowIndex, UserPrincipal principal) {
            super(parent, "Modifier un Acte Médical", true);
            this.rowIndex = rowIndex;
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

            // Libellé
            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Libellé:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtLibelle = new JTextField();
            txtLibelle.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtLibelle, gbc);

            // Prix de base
            gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Prix de base (DH):"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtPrix = new JTextField();
            txtPrix.setPreferredSize(new Dimension(200, 30));
            formPanel.add(txtPrix, gbc);

            // Catégorie
            gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Catégorie:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            txtCategorie = new JTextField();
            txtCategorie.setPreferredSize(new Dimension(200, 30));
            txtCategorie.setText("Général");
            formPanel.add(txtCategorie, gbc);

            // Description
            gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
            formPanel.add(new JLabel("Description:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
            txtDescription = new JTextArea(3, 20);
            txtDescription.setLineWrap(true);
            txtDescription.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(txtDescription);
            scrollPane.setPreferredSize(new Dimension(300, 60));
            formPanel.add(scrollPane, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            ActionButton btnSave = new ActionButton("Enregistrer", ActionButton.ButtonType.CONFIRM);
            ActionButton btnCancel = new ActionButton("Annuler", ActionButton.ButtonType.CANCEL);

            btnSave.addActionListener(e -> saveActe());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            // Load current data
            loadCurrentData();
        }

        private void loadCurrentData() {
            try {
                if (actesModel != null && rowIndex >= 0 && rowIndex < actesModel.getRowCount()) {
                    String libelle = (String) actesModel.getValueAt(rowIndex, 0);
                    String code = (String) actesModel.getValueAt(rowIndex, 1);
                    String tarif = (String) actesModel.getValueAt(rowIndex, 2);

                    // Set dialog title with acte name
                    setTitle("Modifier l'acte: " + libelle);

                    // Set current values
                    txtLibelle.setText(libelle);

                    // Extract price from tarif string (remove " DH")
                    if (tarif.endsWith(" DH")) {
                        txtPrix.setText(tarif.substring(0, tarif.length() - 3));
                    } else {
                        txtPrix.setText(tarif);
                    }

                    // Set description with current info
                    txtDescription.setText("Code: " + code + "\n" +
                                         "Prix actuel: " + tarif + "\n\n" +
                                         "Modifiez les informations ci-dessus selon vos besoins.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des données: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveActe() {
            try {
                String libelle = txtLibelle.getText().trim();
                String categorie = txtCategorie.getText().trim();
                String description = txtDescription.getText().trim();

                if (libelle.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un libellé pour l'acte", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Double prix = null;
                if (!txtPrix.getText().trim().isEmpty()) {
                    try {
                        prix = Double.parseDouble(txtPrix.getText().trim());
                        if (prix < 0) {
                            JOptionPane.showMessageDialog(this, "Le prix ne peut pas être négatif", "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Veuillez saisir un prix valide", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // TODO: Implement actual acte update in database
                saved = true;
                JOptionPane.showMessageDialog(this,
                    "Acte modifié avec succès !\n\n" +
                    "Libellé: " + libelle + "\n" +
                    "Prix: " + (prix != null ? prix + " DH" : "Non défini") + "\n" +
                    "Catégorie: " + categorie + "\n\n" +
                    "Note: La modification réelle en base de données sera implémentée prochainement.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        public boolean isSaved() {
            return saved;
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
