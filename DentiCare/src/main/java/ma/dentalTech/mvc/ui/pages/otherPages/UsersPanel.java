package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;
import ma.dentalTech.service.modules.auth.api.UtilisateurService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UsersPanel extends JPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);
    
    private JTable usersTable;
    private DefaultTableModel usersModel;
    private final UserPrincipal principal;
    private UtilisateurService utilisateurService;

    public UsersPanel(UserPrincipal principal) {
        this.principal = principal;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        try {
            utilisateurService = ApplicationContext.getBean(UtilisateurService.class);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de UtilisateurService: " + e.getMessage());
            e.printStackTrace();
        }
        
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

        JLabel mainTitle = new JLabel("Administration - Gestion des Utilisateurs");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainTitle.setForeground(new Color(52, 58, 64));

        JLabel subtitle = new JLabel("Tableau de bord - " + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy", java.util.Locale.FRENCH)));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(108, 117, 125));

        titlePanel.add(mainTitle, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.CENTER);

        // Header panel avec titre et bouton alignés horizontalement
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Section header avec titre à gauche et bouton à droite
        JLabel sectionTitle = new JLabel("Gestion des Utilisateurs");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        JButton btnCreate = new JButton("Créer un utilisateur");
        btnCreate.setPreferredSize(new Dimension(220, 45));
        btnCreate.setBackground(new Color(46, 204, 113));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setBorderPainted(false);
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreate.addActionListener(e -> showCreateUserDialog());

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(btnCreate, BorderLayout.EAST);

        // Card container pour le tableau (sans le bouton)
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Table avec les colonnes demandées
        String[] userColumns = {"Nom complet", "Téléphone", "CIN", "Rôle", "Dernière connexion", "Créateur", "Actions"};
        usersModel = new DefaultTableModel(userColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Actions column is editable
            }
        };

        loadUsersData();

        usersTable = createTableWithActions(usersModel, 6);
        usersTable.setRowHeight(40);
        usersTable.setShowGrid(true);
        usersTable.setGridColor(new Color(240, 240, 240));
        
        // Style header
        JTableHeader header = usersTable.getTableHeader();
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(new Color(52, 58, 64));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);

        // Alternating row colors
        usersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
                }
                return c;
            }
        });

        card.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel wrapper pour header et card avec espacement
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setOpaque(false);

        contentWrapper.add(headerPanel);
        contentWrapper.add(Box.createVerticalStrut(15)); // Espace entre header et tableau
        contentWrapper.add(card);

        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadUsersData() {
        usersModel.setRowCount(0);
        
        if (utilisateurService == null) {
            return;
        }

        try {
            List<Utilisateur> users = utilisateurService.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Date seulement, pas d'heure
            
            for (Utilisateur user : users) {
                String nom = user.getNom() != null ? user.getNom() : "";
                String email = user.getEmail() != null ? user.getEmail() : "";
                String login = user.getLogin() != null ? user.getLogin() : "";
                String telephone = user.getTel() != null ? user.getTel() : "";
                String cin = user.getCin() != null ? user.getCin() : "";
                
                // Get role depuis la base de données
                String roleStr = "Utilisateur";
                try {
                    // Requête pour récupérer le rôle de l'utilisateur
                    java.sql.Connection conn = ma.dentalTech.conf.SessionFactory.getInstance().getConnection();
                    String roleQuery = "SELECT r.libelle FROM role r " +
                                     "JOIN utilisateur_role ur ON r.id_role = ur.id_role " +
                                     "WHERE ur.id_user = ?";
                    java.sql.PreparedStatement ps = conn.prepareStatement(roleQuery);
                    ps.setLong(1, user.getIdUser());

                    java.sql.ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        String roleLibelle = rs.getString("libelle");
                        if (roleLibelle != null) {
                            roleStr = roleLibelle;
                            System.out.println("Rôle trouvé pour " + user.getNom() + ": " + roleStr);
                        }
                    } else {
                        System.out.println("Aucun rôle trouvé pour " + user.getNom());
                    }

                    rs.close();
                    ps.close();
                    // Ne pas fermer la connexion car elle est gérée par SessionFactory

                } catch (Exception e) {
                    System.err.println("Erreur lors de la récupération du rôle pour " + user.getNom() + ": " + e.getMessage());
                    roleStr = "Erreur";
                }
                
                String statut = "Actif"; // You can determine this based on your logic
                String derniereConnexion = user.getLastLoginDate() != null 
                    ? user.getLastLoginDate().format(formatter) 
                    : "Jamais";
                
                String createur = user.getCreePar() != null ? user.getCreePar() : "system";
                
                usersModel.addRow(new Object[]{nom, telephone, cin, roleStr, derniereConnexion, createur, ""});
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des utilisateurs: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des utilisateurs: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
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

            // Create icon buttons (view, edit, delete)
            JButton viewBtn = createIconButton("see", new Color(52, 152, 219)); // See icon
            JButton editBtn = createIconButton("add", new Color(243, 156, 18)); // Edit icon
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
                    btn.setText(iconName.equals("see") ? "O" : iconName.equals("delete") ? "X" : "*");
                    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
                }
            } catch (Exception e) {
                // Fallback to text
                btn.setText(iconName.equals("see") ? "O" : iconName.equals("delete") ? "X" : "*");
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
            JButton editBtn = createIconButton("add", new Color(243, 156, 18)); // Edit icon
            JButton deleteBtn = createIconButton("delete", new Color(231, 76, 60)); // Delete icon

            final int targetRow = row;

            viewBtn.addActionListener(e -> {
                showViewUserDialog(targetRow);
            });

            editBtn.addActionListener(e -> {
                showEditUserDialog(targetRow);
            });

            deleteBtn.addActionListener(e -> {
                deleteUser(targetRow);
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
                    btn.setText(iconName.equals("see") ? "O" : iconName.equals("delete") ? "X" : "*");
                    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
                }
            } catch (Exception e) {
                // Fallback to text
                btn.setText(iconName.equals("see") ? "O" : iconName.equals("delete") ? "X" : "*");
                btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            }

            return btn;
        }
    }

    private void showCreateUserDialog() {
        CreateUserDialog dialog = new CreateUserDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            principal
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadUsersData(); // Recharger les données
            JOptionPane.showMessageDialog(this,
                "Utilisateur créé avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showEditUserDialog(int row) {
        if (row < 0 || row >= usersModel.getRowCount()) {
            return;
        }

        // Récupérer les données actuelles
        String nom = (String) usersModel.getValueAt(row, 0);
        String email = (String) usersModel.getValueAt(row, 1);
        String login = (String) usersModel.getValueAt(row, 2);
        String telephone = (String) usersModel.getValueAt(row, 3);
        String cin = (String) usersModel.getValueAt(row, 4);
        String role = (String) usersModel.getValueAt(row, 5);

        EditUserDialog dialog = new EditUserDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            principal,
            row,
            nom, email, login, telephone, cin, role
        );
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadUsersData(); // Recharger les données
            JOptionPane.showMessageDialog(this,
                "Utilisateur modifié avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteUser(int row) {
        if (row < 0 || row >= usersModel.getRowCount()) {
            return;
        }

        String nom = (String) usersModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer l'utilisateur \"" + nom + "\" ?\nCette action est irréversible.",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Récupérer l'utilisateur complet depuis la base de données
                List<Utilisateur> users = utilisateurService.findAll();
                if (row < users.size()) {
                    Utilisateur userToDelete = users.get(row);

                    // Supprimer de la base de données
                    utilisateurService.delete(userToDelete);

                    // Recharger les données du tableau
                    loadUsersData();

                    JOptionPane.showMessageDialog(this,
                        "Utilisateur \"" + nom + "\" supprimé avec succès !",
                        "Suppression réussie",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showViewUserDialog(int row) {
        if (row < 0 || row >= usersModel.getRowCount()) {
            return;
        }

        // Récupérer l'utilisateur complet depuis la base de données
        try {
            List<Utilisateur> users = utilisateurService.findAll();
            if (row < users.size()) {
                Utilisateur user = users.get(row);
                ViewUserDialog dialog = new ViewUserDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    user,
                    principal
                );
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des détails utilisateur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Dialog moderne pour voir les détails d'un utilisateur
    private class ViewUserDialog extends JDialog {
        private JPasswordField passwordField;
        private JButton showPasswordBtn;
        private boolean passwordVisible = false;

        public ViewUserDialog(Frame parent, Utilisateur user, UserPrincipal principal) {
            super(parent, "Détails de l'utilisateur", true);
            initializeDialog(user, principal);
        }

        private void initializeDialog(Utilisateur user, UserPrincipal principal) {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 600);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(52, 152, 219));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Détails de l'utilisateur");
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
            String nom = user.getNom() != null ? user.getNom() : "";
            String email = user.getEmail() != null ? user.getEmail() : "";
            String login = user.getLogin() != null ? user.getLogin() : "";
            String telephone = user.getTel() != null ? user.getTel() : "";
            String cin = user.getCin() != null ? user.getCin() : "";
            String adresse = user.getAdresse() != null ? user.getAdresse() : "";
            String sexe = user.getSexe() != null ? user.getSexe().name() : "";
            String dateNaissance = user.getDateNaissance() != null ?
                user.getDateNaissance().toString() : "";
            String lastLogin = user.getLastLoginDate() != null ?
                user.getLastLoginDate().toString() : "Jamais";

            // Get role depuis la base de données
            String roleStr = "Utilisateur";
            try {
                java.sql.Connection conn = ma.dentalTech.conf.SessionFactory.getInstance().getConnection();
                String roleQuery = "SELECT r.libelle FROM role r " +
                                 "JOIN utilisateur_role ur ON r.id_role = ur.id_role " +
                                 "WHERE ur.id_user = ?";
                java.sql.PreparedStatement ps = conn.prepareStatement(roleQuery);
                ps.setLong(1, user.getIdUser());

                java.sql.ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String roleLibelle = rs.getString("libelle");
                    if (roleLibelle != null) {
                        roleStr = roleLibelle;
                    }
                }
                rs.close();
                ps.close();
            } catch (Exception e) {
                System.err.println("Erreur récupération rôle: " + e.getMessage());
            }

            String creePar = user.getCreePar() != null ? user.getCreePar() : "system";

            // Labels and values
            String[] labels = {"Nom complet:", "Email:", "Login:", "Mot de passe:", "Téléphone:",
                             "CIN:", "Adresse:", "Sexe:", "Date de naissance:", "Rôle:",
                             "Dernière connexion:", "Créé par:"};
            JComponent[] components = new JComponent[labels.length];

            // Créer les champs
            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

                if (i == 3) { // Mot de passe
                    JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
                    passwordPanel.setBackground(Color.WHITE);

                    passwordField = new JPasswordField("********");
                    passwordField.setEditable(false);
                    passwordField.setBackground(new Color(248, 249, 250));
                    passwordField.setEchoChar('*');

                    showPasswordBtn = new JButton("Voir");
                    showPasswordBtn.setPreferredSize(new Dimension(80, 25));
                    showPasswordBtn.setBackground(new Color(52, 152, 219));
                    showPasswordBtn.setForeground(Color.WHITE);
                    showPasswordBtn.setBorderPainted(false);
                    showPasswordBtn.setFocusPainted(false);
                    showPasswordBtn.addActionListener(e -> togglePasswordVisibility(user, principal));

                    passwordPanel.add(passwordField, BorderLayout.CENTER);
                    passwordPanel.add(showPasswordBtn, BorderLayout.EAST);
                    components[i] = passwordPanel;
                } else {
                    // Valeurs normales - obtenir la valeur selon l'index
                    String value = "";
                    switch (i) {
                        case 0: value = nom; break;
                        case 1: value = email; break;
                        case 2: value = login; break;
                        case 4: value = telephone; break;
                        case 5: value = cin; break;
                        case 6: value = adresse; break;
                        case 7: value = sexe; break;
                        case 8: value = dateNaissance; break;
                        case 9: value = roleStr; break;
                        case 10: value = lastLogin; break;
                        case 11: value = creePar; break;
                    }
                    JTextField valueField = new JTextField(value);
                    valueField.setEditable(false);
                    valueField.setBackground(new Color(248, 249, 250));
                    valueField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    components[i] = valueField;
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

        private void togglePasswordVisibility(Utilisateur user, UserPrincipal principal) {
            if (!passwordVisible) {
                // Demander le mot de passe admin
                JPasswordField adminPasswordField = new JPasswordField();
                int result = JOptionPane.showConfirmDialog(this,
                    adminPasswordField,
                    "Entrez le mot de passe administrateur:",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String adminPassword = new String(adminPasswordField.getPassword());

                    // TODO: Vérifier le mot de passe admin (pour l'instant on accepte tout)
                    // Ici vous devriez vérifier si adminPassword correspond au mot de passe de l'admin connecté

                    passwordField.setText(user.getMotDePass() != null ? user.getMotDePass() : "");
                    passwordField.setEchoChar((char) 0); // Afficher en clair
                    showPasswordBtn.setText("Cacher");
                    passwordVisible = true;
                }
            } else {
                passwordField.setText("********");
                passwordField.setEchoChar('*');
                showPasswordBtn.setText("Voir");
                passwordVisible = false;
            }
        }
    }

    // Dialog moderne pour créer un utilisateur
    private class CreateUserDialog extends JDialog {
        private JTextField txtNom, txtEmail, txtLogin, txtTelephone, txtCin, txtAdresse;
        private JPasswordField txtPassword;
        private JComboBox<String> cmbRole, cmbSexe;
        private JSpinner spinnerDateNaissance;
        private boolean saved = false;

        public CreateUserDialog(Frame parent, UserPrincipal principal) {
            super(parent, "Créer un nouvel utilisateur", true);
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

            JLabel titleLabel = new JLabel("Créer un nouvel utilisateur");
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
            txtLogin = new JTextField();
            txtTelephone = new JTextField();
            txtCin = new JTextField();
            txtAdresse = new JTextField();
            txtPassword = new JPasswordField();
            cmbRole = new JComboBox<>(new String[]{"Administrateur", "Médecin", "Secrétaire", "Patient"});
            cmbSexe = new JComboBox<>(new String[]{"HOMME", "FEMME"});

            // Date picker
            spinnerDateNaissance = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDateNaissance, "dd/MM/yyyy");
            spinnerDateNaissance.setEditor(dateEditor);
            spinnerDateNaissance.setValue(java.sql.Date.valueOf(java.time.LocalDate.now().minusYears(25)));

            // Fields
            String[] labels = {"Nom complet:", "Email:", "Login:", "Mot de passe:", "Téléphone:",
                             "CIN:", "Adresse:", "Sexe:", "Date de naissance:", "Rôle:"};
            JComponent[] components = {txtNom, txtEmail, txtLogin, txtPassword, txtTelephone,
                                    txtCin, txtAdresse, cmbSexe, spinnerDateNaissance, cmbRole};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                if (components[i] instanceof JTextField || components[i] instanceof JPasswordField) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                } else if (components[i] instanceof JComboBox) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                } else if (components[i] instanceof JSpinner) {
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

            btnSave.addActionListener(e -> saveUser());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveUser() {
            // Validation
            if (txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un nom", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un email", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtLogin.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un login", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtPassword.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un mot de passe", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Implement actual user creation in database
            saved = true;
            dispose();
        }

        public boolean isSaved() {
            return saved;
        }
    }

    // Dialog moderne pour modifier un utilisateur
    private class EditUserDialog extends JDialog {
        private JTextField txtNom, txtEmail, txtLogin, txtTelephone, txtCin, txtAdresse;
        private JPasswordField txtPassword;
        private JComboBox<String> cmbRole, cmbSexe;
        private JSpinner spinnerDateNaissance;
        private boolean saved = false;
        private final int row;

        public EditUserDialog(Frame parent, UserPrincipal principal, int row,
                            String nom, String email, String login, String telephone,
                            String cin, String role) {
            super(parent, "Modifier l'utilisateur", true);
            this.row = row;
            initializeDialog(nom, email, login, telephone, cin, role);
        }

        private void initializeDialog(String nom, String email, String login,
                                   String telephone, String cin, String role) {
            setLayout(new BorderLayout(20, 20));
            setSize(650, 650);
            setLocationRelativeTo(getParent());
            getContentPane().setBackground(new Color(248, 249, 250));

            // Header
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(243, 156, 18));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel titleLabel = new JLabel("Modifier l'utilisateur");
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

            // Initialize fields with existing values
            txtNom = new JTextField(nom);
            txtEmail = new JTextField(email);
            txtLogin = new JTextField(login);
            txtTelephone = new JTextField(telephone);
            txtCin = new JTextField(cin);
            txtAdresse = new JTextField(""); // TODO: Get from database
            txtPassword = new JPasswordField(); // Leave empty for security
            cmbRole = new JComboBox<>(new String[]{"Administrateur", "Médecin", "Secrétaire", "Patient"});
            cmbRole.setSelectedItem(role);
            cmbSexe = new JComboBox<>(new String[]{"HOMME", "FEMME"});

            // Date picker
            spinnerDateNaissance = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDateNaissance, "dd/MM/yyyy");
            spinnerDateNaissance.setEditor(dateEditor);
            spinnerDateNaissance.setValue(java.sql.Date.valueOf(java.time.LocalDate.now().minusYears(25)));

            // Fields
            String[] labels = {"Nom complet:", "Email:", "Login:", "Nouveau mot de passe:", "Téléphone:",
                             "CIN:", "Adresse:", "Sexe:", "Date de naissance:", "Rôle:"};
            JComponent[] components = {txtNom, txtEmail, txtLogin, txtPassword, txtTelephone,
                                    txtCin, txtAdresse, cmbSexe, spinnerDateNaissance, cmbRole};

            for (int i = 0; i < labels.length; i++) {
                gbc.gridx = 0; gbc.gridy = i;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel(labels[i]), gbc);

                gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
                if (components[i] instanceof JTextField || components[i] instanceof JPasswordField) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                } else if (components[i] instanceof JComboBox) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                } else if (components[i] instanceof JSpinner) {
                    components[i].setPreferredSize(new Dimension(250, 30));
                }
                contentPanel.add(components[i], gbc);
            }

            // Info label for password
            gbc.gridx = 0; gbc.gridy = labels.length;
            gbc.gridwidth = 2;
            JLabel infoLabel = new JLabel("ℹ Laissez le champ mot de passe vide pour garder l'ancien mot de passe");
            infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            infoLabel.setForeground(new Color(108, 117, 125));
            contentPanel.add(infoLabel, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(248, 249, 250));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JButton btnSave = new JButton("Enregistrer");
            JButton btnCancel = new JButton("Annuler");

            btnSave.setPreferredSize(new Dimension(120, 35));
            btnCancel.setPreferredSize(new Dimension(100, 35));

            btnSave.addActionListener(e -> saveUser());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnCancel);
            buttonPanel.add(btnSave);

            add(headerPanel, BorderLayout.NORTH);
            add(new JScrollPane(contentPanel), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void saveUser() {
            // Validation
            if (txtNom.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un nom", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un email", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txtLogin.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un login", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Implement actual user update in database
            saved = true;
            dispose();
        }

        public boolean isSaved() {
            return saved;
        }
    }
}
