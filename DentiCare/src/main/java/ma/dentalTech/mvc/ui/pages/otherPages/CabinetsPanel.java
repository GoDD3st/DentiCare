package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.common.Adresse;
import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Optional;

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

        // Header panel avec titre et bouton alignés horizontalement (layout responsive)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        headerPanel.setPreferredSize(new Dimension(headerPanel.getPreferredSize().width, 80)); // Hauteur fixe

        // Panel gauche pour le titre
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        JLabel sectionTitle = new JLabel("Gestion des Cabinets Médicaux");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));
        leftPanel.add(sectionTitle);

        // Panel droit pour le bouton
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        JButton btnCreate = new JButton("Créer un cabinet");
        btnCreate.setPreferredSize(new Dimension(200, 40)); // Bouton plus petit
        btnCreate.setBackground(new Color(46, 204, 113));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setBorderPainted(false);
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreate.addActionListener(e -> showCreateCabinetDialog());
        rightPanel.add(btnCreate);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Card container pour le tableau (sans le bouton)
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Table avec actions
        String[] cabinetColumns = {"ID", "Nom", "Adresse", "Téléphone", "Médecins", "Statut", "Actions"};
        cabinetsModel = new DefaultTableModel(cabinetColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Actions column is editable (maintenant index 6)
            }
        };

        loadCabinetsData();

        cabinetsTable = createTableWithActions(cabinetsModel, 6); // Actions column maintenant à index 6

        // Cacher la colonne ID (index 0)
        cabinetsTable.getColumnModel().getColumn(0).setMinWidth(0);
        cabinetsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        cabinetsTable.getColumnModel().getColumn(0).setWidth(0);
        cabinetsTable.getColumnModel().getColumn(0).setPreferredWidth(0);
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

        // Ajouter le tableau au card
        card.add(new JScrollPane(cabinetsTable), BorderLayout.CENTER);

        // Content wrapper avec BoxLayout pour header + espace + tableau (comme UsersPanel)
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setOpaque(false);

        contentWrapper.add(headerPanel);
        contentWrapper.add(Box.createVerticalStrut(15)); // Espace entre header et tableau
        contentWrapper.add(card);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);

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
                    Long idCabinet = cabinet.getIdCabinet();
                    String nom = cabinet.getNom() != null ? cabinet.getNom() : "";
                    String adresse = "";
                    if (cabinet.getAdresse() != null) {
                        adresse = cabinet.getAdresse().toString();
                    }
                    String telephone = cabinet.getTel1() != null ? cabinet.getTel1() : "";
                    
                    // Count doctors - you might need to implement this in the service
                    int nombreMedecins = 0; // TODO: Get actual count from service
                    
                    String statut = "Actif"; // You can determine this based on your logic
                    
                    cabinetsModel.addRow(new Object[]{idCabinet, nom, adresse, telephone, String.valueOf(nombreMedecins), statut, ""});
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
            JButton editBtn = createIconButton("edit", new Color(243, 156, 18)); // Edit icon
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

    private class ActionButtonEditor extends DefaultCellEditor {
        public ActionButtonEditor() {
            super(new JCheckBox());
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
                showViewCabinetDialog(targetRow);
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

    private void showCreateCabinetDialog() {
        CreateCabinetDialog dialog = new CreateCabinetDialog(
            (Frame) SwingUtilities.getWindowAncestor(this)
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadCabinetsData(); // Recharger les données
        JOptionPane.showMessageDialog(this,
                "Cabinet créé avec succès !",
                "Succès",
            JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showEditCabinetDialog(int row) {
        if (row < 0 || row >= cabinetsModel.getRowCount()) {
            return;
        }

        // Récupérer l'ID du cabinet
        Long cabinetId = (Long) cabinetsModel.getValueAt(row, 0);
        try {
            // Récupérer le cabinet complet depuis la base de données
            Object serviceObj = ApplicationContext.getBean("cabinetMedicaleService");
            if (serviceObj instanceof ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) {
                ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService service =
                    (ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) serviceObj;

                Optional<ma.dentalTech.entities.CabinetMedicale.CabinetMedicale> cabinetOpt = service.findByID(cabinetId);
                if (cabinetOpt.isPresent()) {
                    EditCabinetDialog dialog = new EditCabinetDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this),
                        cabinetOpt.get()
                    );
                    dialog.setVisible(true);
                    if (dialog.isSaved()) {
                        loadCabinetsData(); // Recharger les données
        JOptionPane.showMessageDialog(this,
                            "Cabinet modifié avec succès !",
                            "Succès",
            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement du cabinet: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
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
            try {
                // Récupérer l'ID du cabinet
                Long cabinetId = (Long) cabinetsModel.getValueAt(row, 0);

                // Supprimer de la base de données
                Object serviceObj = ApplicationContext.getBean("cabinetMedicaleService");
                if (serviceObj instanceof ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) {
                    ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService service =
                        (ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) serviceObj;
                    service.deleteByID(cabinetId);

                    // Recharger les données du tableau
                    loadCabinetsData();

                    JOptionPane.showMessageDialog(this,
                        "Cabinet \"" + nom + "\" supprimé avec succès !",
                        "Suppression réussie",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Service de cabinet non disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Dialog moderne pour créer un nouveau cabinet
    private class CreateCabinetDialog extends JDialog {
        private JTextField txtNom, txtEmail, txtTel1, txtTel2, txtSiteWeb, txtInstagram, txtFacebook, txtRue, txtVille, txtCodePostal, txtRegion, txtPays, txtLogo;
        private JTextArea txtDescription;
        private JButton btnSelectLogo;
        private boolean saved = false;

        public CreateCabinetDialog(Frame parent) {
            super(parent, "Créer un nouveau cabinet", true);
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 700);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(46, 204, 113));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Créer un nouveau cabinet");
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
            txtEmail = new JTextField();
            txtLogo = new JTextField();
            btnSelectLogo = new JButton("Parcourir...");
            txtTel1 = new JTextField();
            txtTel2 = new JTextField();
            txtSiteWeb = new JTextField();
            txtInstagram = new JTextField();
            txtFacebook = new JTextField();
            txtRue = new JTextField();
            txtVille = new JTextField();
            txtCodePostal = new JTextField();
            txtRegion = new JTextField();
            txtPays = new JTextField();
            txtDescription = new JTextArea(3, 20);
            txtDescription.setLineWrap(true);
            txtDescription.setWrapStyleWord(true);

            // Set default country
            txtPays.setText("Maroc");

            // Configure logo selection
            btnSelectLogo.setPreferredSize(new Dimension(100, 30));
            btnSelectLogo.addActionListener(e -> selectLogoFile());

            // Logo panel (JTextField + Button)
            JPanel logoPanel = new JPanel(new BorderLayout(5, 0));
            logoPanel.add(txtLogo, BorderLayout.CENTER);
            logoPanel.add(btnSelectLogo, BorderLayout.EAST);

            // Fields labels and components
            String[] labels = {"Nom du cabinet:", "Email:", "Logo:", "Téléphone 1:", "Téléphone 2:",
                             "Site web:", "Instagram:", "Facebook:", "Rue:", "Ville:",
                             "Code postal:", "Région:", "Pays:", "Description:"};
            JComponent[] components = {txtNom, txtEmail, logoPanel, txtTel1, txtTel2, txtSiteWeb,
                                    txtInstagram, txtFacebook, txtRue, txtVille, txtCodePostal,
                                    txtRegion, txtPays, new JScrollPane(txtDescription)};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                if (components[i] instanceof JTextField) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                } else if (components[i] instanceof JScrollPane) {
                    components[i].setPreferredSize(new Dimension(250, 60));
                } else if (components[i] instanceof JPanel) {
                    // Logo panel
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

            btnSave.addActionListener(e -> saveCabinet());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveCabinet() {
            // Validation
            if (txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir le nom du cabinet", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir l'email du cabinet", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtTel1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir au moins un numéro de téléphone", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Créer l'adresse (seulement si des champs sont remplis)
                Adresse.AdresseBuilder adresseBuilder = Adresse.builder();
                String rue = txtRue.getText().trim();
                String ville = txtVille.getText().trim();
                String codePostal = txtCodePostal.getText().trim();
                String region = txtRegion.getText().trim();
                String pays = txtPays.getText().trim();

                if (!rue.isEmpty()) adresseBuilder.rue(rue);
                if (!ville.isEmpty()) adresseBuilder.ville(ville);
                if (!codePostal.isEmpty()) adresseBuilder.codePostal(codePostal);
                if (!region.isEmpty()) adresseBuilder.région(region);
                if (!pays.isEmpty()) adresseBuilder.pays(pays);

                Adresse adresse = adresseBuilder.build();

                // Créer le cabinet
                CabinetMedicale.CabinetMedicaleBuilder cabinetBuilder = CabinetMedicale.builder()
                    .nom(txtNom.getText().trim())
                    .email(txtEmail.getText().trim())
                    .tel1(txtTel1.getText().trim())
                    .adresse(adresse);

                // Ajouter les champs optionnels seulement s'ils ne sont pas vides
                String logo = txtLogo.getText().trim();
                String tel2 = txtTel2.getText().trim();
                String siteWeb = txtSiteWeb.getText().trim();
                String instagram = txtInstagram.getText().trim();
                String facebook = txtFacebook.getText().trim();
                String description = txtDescription.getText().trim();

                if (!logo.isEmpty()) cabinetBuilder.logo(logo);
                if (!tel2.isEmpty()) cabinetBuilder.tel2(tel2);
                if (!siteWeb.isEmpty()) cabinetBuilder.siteWeb(siteWeb);
                if (!instagram.isEmpty()) cabinetBuilder.instagram(instagram);
                if (!facebook.isEmpty()) cabinetBuilder.facebook(facebook);
                if (!description.isEmpty()) cabinetBuilder.description(description);

                CabinetMedicale cabinet = cabinetBuilder.build();

                // Debug logging
                System.out.println("Tentative de création du cabinet: " + cabinet.getNom());
                System.out.println("Email: " + cabinet.getEmail());
                System.out.println("Tel1: " + cabinet.getTel1());
                if (cabinet.getAdresse() != null) {
                    System.out.println("Adresse - Rue: " + cabinet.getAdresse().getRue());
                    System.out.println("Adresse - Ville: " + cabinet.getAdresse().getVille());
                }

                // Sauvegarder dans la base de données
                Object serviceObj = ApplicationContext.getBean("cabinetMedicaleService");
                if (serviceObj instanceof ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) {
                    ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService service =
                        (ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) serviceObj;

                    System.out.println("Service trouvé, appel de create()...");
                    CabinetMedicale createdCabinet = service.create(cabinet);
                    System.out.println("Cabinet créé avec succès, ID: " + createdCabinet.getIdCabinet());

                    saved = true;
                    dispose();
                } else {
                    System.out.println("Service de cabinet non trouvé ou type incorrect: " + serviceObj.getClass().getName());
                    JOptionPane.showMessageDialog(this, "Service de cabinet non disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.err.println("Erreur détaillée lors de la création du cabinet:");
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création du cabinet: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private void selectLogoFile() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner un logo");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Filtre pour les images
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images (*.png, *.jpg, *.jpeg, *.gif)", "png", "jpg", "jpeg", "gif");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                txtLogo.setText(selectedFile.getAbsolutePath());
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog moderne pour modifier un cabinet existant
    private class EditCabinetDialog extends JDialog {
        private JTextField txtNom, txtEmail, txtTel1, txtTel2, txtSiteWeb, txtInstagram, txtFacebook, txtRue, txtVille, txtCodePostal, txtRegion, txtPays, txtLogo;
        private JTextArea txtDescription;
        private JButton btnSelectLogo;
        private boolean saved = false;
        private CabinetMedicale cabinet;

        public EditCabinetDialog(Frame parent, CabinetMedicale cabinet) {
            super(parent, "Modifier le cabinet", true);
            this.cabinet = cabinet;
            initializeDialog();
        }

        private void initializeDialog() {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 700);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(46, 204, 113));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Modifier le cabinet");
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
            txtEmail = new JTextField();
            txtLogo = new JTextField();
            btnSelectLogo = new JButton("Parcourir...");
            txtTel1 = new JTextField();
            txtTel2 = new JTextField();
            txtSiteWeb = new JTextField();
            txtInstagram = new JTextField();
            txtFacebook = new JTextField();
            txtRue = new JTextField();
            txtVille = new JTextField();
            txtCodePostal = new JTextField();
            txtRegion = new JTextField();
            txtPays = new JTextField();
            txtDescription = new JTextArea(3, 20);
            txtDescription.setLineWrap(true);
            txtDescription.setWrapStyleWord(true);

            // Configure logo selection
            btnSelectLogo.setPreferredSize(new Dimension(100, 30));
            btnSelectLogo.addActionListener(e -> selectLogoFile());

            // Load cabinet data
            if (cabinet != null) {
                txtNom.setText(cabinet.getNom() != null ? cabinet.getNom() : "");
                txtEmail.setText(cabinet.getEmail() != null ? cabinet.getEmail() : "");
                txtLogo.setText(cabinet.getLogo() != null ? cabinet.getLogo() : "");
                txtTel1.setText(cabinet.getTel1() != null ? cabinet.getTel1() : "");
                txtTel2.setText(cabinet.getTel2() != null ? cabinet.getTel2() : "");
                txtSiteWeb.setText(cabinet.getSiteWeb() != null ? cabinet.getSiteWeb() : "");
                txtInstagram.setText(cabinet.getInstagram() != null ? cabinet.getInstagram() : "");
                txtFacebook.setText(cabinet.getFacebook() != null ? cabinet.getFacebook() : "");
                txtDescription.setText(cabinet.getDescription() != null ? cabinet.getDescription() : "");

                // Load address data
                if (cabinet.getAdresse() != null) {
                    txtRue.setText(cabinet.getAdresse().getRue() != null ? cabinet.getAdresse().getRue() : "");
                    txtVille.setText(cabinet.getAdresse().getVille() != null ? cabinet.getAdresse().getVille() : "");
                    txtCodePostal.setText(cabinet.getAdresse().getCodePostal() != null ? cabinet.getAdresse().getCodePostal() : "");
                    txtRegion.setText(cabinet.getAdresse().getRégion() != null ? cabinet.getAdresse().getRégion() : "");
                    txtPays.setText(cabinet.getAdresse().getPays() != null ? cabinet.getAdresse().getPays() : "");
                }
            }

            // Logo panel (JTextField + Button)
            JPanel logoPanel = new JPanel(new BorderLayout(5, 0));
            logoPanel.add(txtLogo, BorderLayout.CENTER);
            logoPanel.add(btnSelectLogo, BorderLayout.EAST);

            // Fields labels and components
            String[] labels = {"Nom du cabinet:", "Email:", "Logo:", "Téléphone 1:", "Téléphone 2:",
                             "Site web:", "Instagram:", "Facebook:", "Rue:", "Ville:",
                             "Code postal:", "Région:", "Pays:", "Description:"};
            JComponent[] components = {txtNom, txtEmail, logoPanel, txtTel1, txtTel2, txtSiteWeb,
                                    txtInstagram, txtFacebook, txtRue, txtVille, txtCodePostal,
                                    txtRegion, txtPays, new JScrollPane(txtDescription)};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                if (components[i] instanceof JTextField) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                } else if (components[i] instanceof JScrollPane) {
                    components[i].setPreferredSize(new Dimension(250, 60));
                } else if (components[i] instanceof JPanel) {
                    // Logo panel
                    components[i].setPreferredSize(new Dimension(250, 30));
                }
                contentPanel.add(components[i], gbc);
            }

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(248, 249, 250));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JButton btnSave = new JButton("Modifier");
            JButton btnCancel = new JButton("Annuler");

            btnSave.setPreferredSize(new Dimension(100, 35));
            btnCancel.setPreferredSize(new Dimension(100, 35));

            btnSave.addActionListener(e -> saveCabinet());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveCabinet() {
            // Validation
            if (txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir le nom du cabinet", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir l'email du cabinet", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtTel1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir au moins un numéro de téléphone", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Créer l'adresse (seulement si des champs sont remplis)
                Adresse.AdresseBuilder adresseBuilder = Adresse.builder();
                String rue = txtRue.getText().trim();
                String ville = txtVille.getText().trim();
                String codePostal = txtCodePostal.getText().trim();
                String region = txtRegion.getText().trim();
                String pays = txtPays.getText().trim();

                if (!rue.isEmpty()) adresseBuilder.rue(rue);
                if (!ville.isEmpty()) adresseBuilder.ville(ville);
                if (!codePostal.isEmpty()) adresseBuilder.codePostal(codePostal);
                if (!region.isEmpty()) adresseBuilder.région(region);
                if (!pays.isEmpty()) adresseBuilder.pays(pays);

                Adresse adresse = adresseBuilder.build();

                // Mettre à jour le cabinet existant
                cabinet.setNom(txtNom.getText().trim());
                cabinet.setEmail(txtEmail.getText().trim());
                cabinet.setTel1(txtTel1.getText().trim());
                cabinet.setAdresse(adresse);

                // Ajouter les champs optionnels seulement s'ils ne sont pas vides
                String logo = txtLogo.getText().trim();
                String tel2 = txtTel2.getText().trim();
                String siteWeb = txtSiteWeb.getText().trim();
                String instagram = txtInstagram.getText().trim();
                String facebook = txtFacebook.getText().trim();
                String description = txtDescription.getText().trim();

                cabinet.setLogo(logo.isEmpty() ? null : logo);
                cabinet.setTel2(tel2.isEmpty() ? null : tel2);
                cabinet.setSiteWeb(siteWeb.isEmpty() ? null : siteWeb);
                cabinet.setInstagram(instagram.isEmpty() ? null : instagram);
                cabinet.setFacebook(facebook.isEmpty() ? null : facebook);
                cabinet.setDescription(description.isEmpty() ? null : description);

                // Sauvegarder dans la base de données
                Object serviceObj = ApplicationContext.getBean("cabinetMedicaleService");
                if (serviceObj instanceof ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) {
                    ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService service =
                        (ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) serviceObj;

                    System.out.println("Tentative de modification du cabinet: " + cabinet.getNom());
                    CabinetMedicale updatedCabinet = service.update(cabinet.getIdCabinet(), cabinet);
                    System.out.println("Cabinet modifié avec succès, ID: " + updatedCabinet.getIdCabinet());

                    saved = true;
                    dispose();
                } else {
                    System.out.println("Service de cabinet non trouvé ou type incorrect: " + serviceObj.getClass().getName());
                    JOptionPane.showMessageDialog(this, "Service de cabinet non disponible", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.err.println("Erreur détaillée lors de la modification du cabinet:");
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la modification du cabinet: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        private void selectLogoFile() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Sélectionner un logo");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // Filtre pour les images
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images (*.png, *.jpg, *.jpeg, *.gif)", "png", "jpg", "jpeg", "gif");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                txtLogo.setText(selectedFile.getAbsolutePath());
            }
        }

        public boolean isSaved() {
            return saved;
        }
    }

    private void showViewCabinetDialog(int row) {
        if (row < 0 || row >= cabinetsModel.getRowCount()) {
            return;
        }

        // Récupérer l'ID du cabinet
        Long cabinetId = (Long) cabinetsModel.getValueAt(row, 0);
        try {
            // Récupérer le cabinet complet depuis la base de données
            Object serviceObj = ApplicationContext.getBean("cabinetMedicaleService");
            if (serviceObj instanceof ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) {
                ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService service =
                    (ma.dentalTech.service.modules.cabinetMedicale.api.CabinetMedicaleService) serviceObj;

                Optional<ma.dentalTech.entities.CabinetMedicale.CabinetMedicale> cabinetOpt = service.findByID(cabinetId);
                if (cabinetOpt.isPresent()) {
                    ViewCabinetDialog dialog = new ViewCabinetDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this),
                        cabinetOpt.get()
                    );
                    dialog.setVisible(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des détails du cabinet: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Dialog moderne pour voir les détails d'un cabinet
    private class ViewCabinetDialog extends JDialog {
        public ViewCabinetDialog(Frame parent, CabinetMedicale cabinet) {
            super(parent, "Détails du cabinet", true);
            initializeDialog(cabinet);
        }

        private void initializeDialog(CabinetMedicale cabinet) {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 600);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(52, 152, 219));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Détails du cabinet");
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
            String nom = cabinet.getNom() != null ? cabinet.getNom() : "";
            String email = cabinet.getEmail() != null ? cabinet.getEmail() : "";
            String logo = cabinet.getLogo() != null ? cabinet.getLogo() : "Aucun";
            String tel1 = cabinet.getTel1() != null ? cabinet.getTel1() : "";
            String tel2 = cabinet.getTel2() != null ? cabinet.getTel2() : "";
            String siteWeb = cabinet.getSiteWeb() != null ? cabinet.getSiteWeb() : "";
            String instagram = cabinet.getInstagram() != null ? cabinet.getInstagram() : "";
            String facebook = cabinet.getFacebook() != null ? cabinet.getFacebook() : "";
            String description = cabinet.getDescription() != null ? cabinet.getDescription() : "";

            // Adresse formatée
            String adresseStr = "";
            if (cabinet.getAdresse() != null) {
                StringBuilder adr = new StringBuilder();
                if (cabinet.getAdresse().getRue() != null && !cabinet.getAdresse().getRue().trim().isEmpty()) {
                    adr.append(cabinet.getAdresse().getRue().trim());
                }
                if (cabinet.getAdresse().getVille() != null && !cabinet.getAdresse().getVille().trim().isEmpty()) {
                    if (adr.length() > 0) adr.append(", ");
                    adr.append(cabinet.getAdresse().getVille().trim());
                }
                if (cabinet.getAdresse().getCodePostal() != null && !cabinet.getAdresse().getCodePostal().trim().isEmpty()) {
                    if (adr.length() > 0) adr.append(" ");
                    adr.append(cabinet.getAdresse().getCodePostal().trim());
                }
                if (cabinet.getAdresse().getRégion() != null && !cabinet.getAdresse().getRégion().trim().isEmpty()) {
                    if (adr.length() > 0) adr.append(", ");
                    adr.append(cabinet.getAdresse().getRégion().trim());
                }
                if (cabinet.getAdresse().getPays() != null && !cabinet.getAdresse().getPays().trim().isEmpty()) {
                    if (adr.length() > 0) adr.append(", ");
                    adr.append(cabinet.getAdresse().getPays().trim());
                }
                adresseStr = adr.toString();
            }

            // Créer les champs en lecture seule
            JTextField txtNom = new JTextField(nom);
            JTextField txtEmail = new JTextField(email);
            JTextField txtLogo = new JTextField(logo);
            JTextField txtTel1 = new JTextField(tel1);
            JTextField txtTel2 = new JTextField(tel2);
            JTextField txtSiteWeb = new JTextField(siteWeb);
            JTextField txtInstagram = new JTextField(instagram);
            JTextField txtFacebook = new JTextField(facebook);
            JTextArea txtDescription = new JTextArea(description);
            txtDescription.setLineWrap(true);
            txtDescription.setWrapStyleWord(true);
            JTextField txtAdresse = new JTextField(adresseStr);

            // Désactiver tous les champs (lecture seule)
            txtNom.setEditable(false);
            txtEmail.setEditable(false);
            txtLogo.setEditable(false);
            txtTel1.setEditable(false);
            txtTel2.setEditable(false);
            txtSiteWeb.setEditable(false);
            txtInstagram.setEditable(false);
            txtFacebook.setEditable(false);
            txtDescription.setEditable(false);
            txtAdresse.setEditable(false);

            // Fond gris clair pour indiquer qu'ils sont en lecture seule
            Color readonlyBg = new Color(240, 240, 240);
            txtNom.setBackground(readonlyBg);
            txtEmail.setBackground(readonlyBg);
            txtLogo.setBackground(readonlyBg);
            txtTel1.setBackground(readonlyBg);
            txtTel2.setBackground(readonlyBg);
            txtSiteWeb.setBackground(readonlyBg);
            txtInstagram.setBackground(readonlyBg);
            txtFacebook.setBackground(readonlyBg);
            txtDescription.setBackground(readonlyBg);
            txtAdresse.setBackground(readonlyBg);

            // Fields labels and components
            String[] labels = {"Nom du cabinet:", "Email:", "Logo:", "Téléphone 1:", "Téléphone 2:",
                             "Site web:", "Instagram:", "Facebook:", "Adresse:", "Description:"};
            JComponent[] components = {txtNom, txtEmail, txtLogo, txtTel1, txtTel2, txtSiteWeb,
                                    txtInstagram, txtFacebook, txtAdresse, new JScrollPane(txtDescription)};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                if (components[i] instanceof JTextField) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                } else if (components[i] instanceof JScrollPane) {
                    components[i].setPreferredSize(new Dimension(250, 60));
                }
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
