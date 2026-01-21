package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.common.Adresse;
import ma.dentalTech.entities.CabinetMedicale.CabinetMedicale;
import ma.dentalTech.entities.Staff.Staff;
import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.entities.enums.RoleEnum;
import ma.dentalTech.entities.enums.SexeEnum;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.MyButton;
import ma.dentalTech.mvc.ui.palette.fields.CustomTextField;
import ma.dentalTech.mvc.ui.palette.utils.ImageTools;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class CabinetPanel extends JPanel {

    private final UserPrincipal principal;
    private CabinetMedicale cabinet;
    private List<Staff> staffList;

    // Champs d'information du cabinet
    private CustomTextField txtNom, txtEmail, txtAdresse, txtTel1, txtTel2, txtSiteWeb, txtDescription;

    // Boutons d'action
    private JButton btnSave, btnCancel, btnAddStaff, btnRefresh;

    public CabinetPanel(UserPrincipal principal) {
        this.principal = principal;
        this.cabinet = loadCabinetInfo();
        this.staffList = loadStaffList();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        fillCabinetData();
    }

    private JPanel buildHeader() {
        // Header panel avec titre et bouton alignés horizontalement (comme UsersPanel)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Section header avec titre à gauche et bouton à droite
        JLabel sectionTitle = new JLabel("Gestion du Cabinet");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(33, 37, 41));

        btnAddStaff = new JButton("Ajouter du Staff");
        btnAddStaff.setPreferredSize(new Dimension(220, 45));
        btnAddStaff.setBackground(new Color(46, 204, 113));
        btnAddStaff.setForeground(Color.WHITE);
        btnAddStaff.setFocusPainted(false);
        btnAddStaff.setBorderPainted(false);
        btnAddStaff.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddStaff.addActionListener(this::addStaffAction);

        headerPanel.add(sectionTitle, BorderLayout.WEST);
        headerPanel.add(btnAddStaff, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Section Informations générales
        JPanel infoPanel = buildCabinetInfoPanel();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        content.add(infoPanel, gbc);

        // Section Personnel
        JPanel staffPanel = buildStaffPanel();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        content.add(staffPanel, gbc);

        return content;
    }

    private JPanel buildCabinetInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Informations Générales"));
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Ligne 1: Nom et Email
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nom du cabinet:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNom = new CustomTextField("");
        txtNom.setPreferredSize(new Dimension(200, 30));
        panel.add(txtNom, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmail = new CustomTextField("");
        txtEmail.setPreferredSize(new Dimension(200, 30));
        panel.add(txtEmail, gbc);

        // Ligne 2: Adresse
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtAdresse = new CustomTextField("");
        txtAdresse.setPreferredSize(new Dimension(400, 30));
        panel.add(txtAdresse, gbc);

        // Ligne 3: Téléphones
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panel.add(new JLabel("Téléphone 1:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTel1 = new CustomTextField("");
        txtTel1.setPreferredSize(new Dimension(150, 30));
        panel.add(txtTel1, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Téléphone 2:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTel2 = new CustomTextField("");
        txtTel2.setPreferredSize(new Dimension(150, 30));
        panel.add(txtTel2, gbc);

        // Ligne 4: Site web et Description
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panel.add(new JLabel("Site web:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtSiteWeb = new CustomTextField("");
        txtSiteWeb.setPreferredSize(new Dimension(200, 30));
        panel.add(txtSiteWeb, gbc);

        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtDescription = new CustomTextField("");
        txtDescription.setPreferredSize(new Dimension(200, 30));
        panel.add(txtDescription, gbc);

        return panel;
    }

    private JPanel buildStaffPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Personnel du Cabinet"));
        panel.setOpaque(false);

        // Liste du personnel
        JList<String> staffListComponent = new JList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Remplir la liste avec les données du personnel
        for (Staff staff : staffList) {
            String userInfo = getUserInfoForStaff(staff);
            listModel.addElement(userInfo);
        }

        staffListComponent.setModel(listModel);
        staffListComponent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        staffListComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(staffListComponent);
        scrollPane.setPreferredSize(new Dimension(400, 150));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Statistiques
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setOpaque(false);

        int totalStaff = staffList.size();
        int medecins = (int) staffList.stream().filter(s -> getRoleForStaff(s).equals("MEDECIN")).count();
        int secretaires = (int) staffList.stream().filter(s -> getRoleForStaff(s).equals("SECRETAIRE")).count();

        JLabel lblStats = new JLabel(String.format("Total: %d | Médecins: %d | Secrétaires: %d",
                                                  totalStaff, medecins, secretaires));
        lblStats.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStats.setForeground(new Color(100, 100, 100));

        statsPanel.add(lblStats);
        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);

        btnSave = new MyButton("Enregistrer", ImageTools.loadIcon("/static/icons/save.png", 16, 16), new Font("Segoe UI", Font.BOLD, 12));
        btnSave.addActionListener(this::saveAction);
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);

        btnCancel = new MyButton("Annuler", null, new Font("Segoe UI", Font.PLAIN, 12));
        btnCancel.addActionListener(this::cancelAction);

        footer.add(btnCancel);
        footer.add(btnSave);

        return footer;
    }

    private CabinetMedicale loadCabinetInfo() {
        // Simulation de chargement des informations du cabinet
        Adresse adresse = Adresse.builder()
                .rue("123 Avenue Mohammed V")
                .ville("Casablanca")
                .codePostal("20000")
                .région("Grand Casablanca")
                .pays("Maroc")
                .build();

        CabinetMedicale cab = CabinetMedicale.builder()
                .idCabinet(1L)
                .nom("Cabinet Dentaire Central")
                .email("contact@cabinetcentral.ma")
                .adresse(adresse)
                .tel1("+212 522 123 456")
                .tel2("+212 522 123 457")
                .siteWeb("www.cabinetcentral.ma")
                .description("Cabinet dentaire moderne avec équipements de dernière génération")
                .build();
        return cab;
    }

    private List<Staff> loadStaffList() {
        // Simulation de chargement du personnel
        List<Staff> list = new ArrayList<>();

        // Ajouter quelques membres du personnel fictifs
        Staff s1 = new Staff();
        s1.setIdUser(1L);
        s1.setSalaire(15000.0);
        list.add(s1);

        Staff s2 = new Staff();
        s2.setIdUser(2L);
        s2.setSalaire(12000.0);
        list.add(s2);

        return list;
    }

    private String getUserInfoForStaff(Staff staff) {
        // Simulation de récupération des informations utilisateur
        String role = getRoleForStaff(staff);
        String name = "Utilisateur " + staff.getIdUser(); // En production, récupérer depuis la BD
        return String.format("%s - %s (%.0f DH)", name, role, staff.getSalaire());
    }

    private String getRoleForStaff(Staff staff) {
        // Simulation de détermination du rôle
        // En production, cela viendrait des tables de jointure
        return staff.getIdUser() % 2 == 0 ? "MEDECIN" : "SECRETAIRE";
    }

    private void fillCabinetData() {
        txtNom.setText(cabinet.getNom() != null ? cabinet.getNom() : "");
        txtEmail.setText(cabinet.getEmail() != null ? cabinet.getEmail() : "");
        // Pour l'adresse, on affiche une version simplifiée
        String adresseStr = "";
        if (cabinet.getAdresse() != null) {
            Adresse addr = cabinet.getAdresse();
            adresseStr = String.format("%s, %s %s, %s",
                addr.getRue() != null ? addr.getRue() : "",
                addr.getVille() != null ? addr.getVille() : "",
                addr.getCodePostal() != null ? addr.getCodePostal() : "",
                addr.getPays() != null ? addr.getPays() : "");
        }
        txtAdresse.setText(adresseStr);
        txtTel1.setText(cabinet.getTel1() != null ? cabinet.getTel1() : "");
        txtTel2.setText(cabinet.getTel2() != null ? cabinet.getTel2() : "");
        txtSiteWeb.setText(cabinet.getSiteWeb() != null ? cabinet.getSiteWeb() : "");
        txtDescription.setText(cabinet.getDescription() != null ? cabinet.getDescription() : "");
    }

    // Actions
    private void saveAction(ActionEvent e) {
        // Mettre à jour les données du cabinet
        cabinet.setNom(txtNom.getText().trim());
        cabinet.setEmail(txtEmail.getText().trim());
        // Note: L'adresse n'est pas modifiée directement dans cette version simplifiée
        // Pour une vraie implémentation, il faudrait un éditeur d'adresse séparé
        cabinet.setTel1(txtTel1.getText().trim());
        cabinet.setTel2(txtTel2.getText().trim());
        cabinet.setSiteWeb(txtSiteWeb.getText().trim());
        cabinet.setDescription(txtDescription.getText().trim());

        // Simulation d'enregistrement
        JOptionPane.showMessageDialog(this, "Informations du cabinet enregistrées avec succès !\n\nNote: L'adresse n'est modifiable que via une interface dédiée.");
    }

    private void cancelAction(ActionEvent e) {
        fillCabinetData(); // Restaurer les données originales
        JOptionPane.showMessageDialog(this, "Modifications annulées.");
    }

    private void refreshAction(ActionEvent e) {
        cabinet = loadCabinetInfo();
        staffList = loadStaffList();
        fillCabinetData();

        // Recréer le panneau de contenu pour refléter les changements
        removeAll();
        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        revalidate();
        repaint();

        JOptionPane.showMessageDialog(this, "Données actualisées !");
    }

    private void addStaffAction(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
            "Fonctionnalité d'ajout de personnel à implémenter.\n\n" +
            "Cette fonction permettra de :\n" +
            "- Ajouter de nouveaux médecins\n" +
            "- Ajouter de nouvelles secrétaires\n" +
            "- Configurer les spécialités\n" +
            "- Définir les horaires de travail",
            "Fonctionnalité à venir",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
