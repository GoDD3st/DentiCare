package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.conf.ApplicationContext;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.entities.enums.RoleEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParametragePanel extends JPanel {

    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    private UserPrincipal principal;
    private JTabbedPane tabbedPane;


    // Composants pour l'onglet Sécurité
    private JCheckBox chkDoubleAuth, chkLogsAudit, chkBackupAuto;
    private JSpinner spinnerDureeSession;

    // Composants pour l'onglet Notifications
    private JCheckBox chkNotifEmail, chkNotifSMS, chkNotifApp;
    private JComboBox<String> cmbFreqSauvegarde;

    public ParametragePanel(UserPrincipal principal) {
        this.principal = principal;
        initializePanel();
        loadCurrentSettings();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel mainTitle = new JLabel("Paramétrage du Système");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainTitle.setForeground(new Color(52, 58, 64));

        String subtitle = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy", java.util.Locale.FRENCH));
        JLabel subtitleLabel = new JLabel("Configuration et paramètres - " + subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));

        titlePanel.add(mainTitle, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        // Tabbed pane for different settings categories
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));


        // Onglet Sécurité (uniquement pour admins)
        if (isAdmin()) {
            tabbedPane.addTab("Sécurité", createSecurityTab());
        }

        // Onglet Notifications
        tabbedPane.addTab("Notifications", createNotificationsTab());

        // Onglet Sauvegarde (uniquement pour admins)
        if (isAdmin()) {
            tabbedPane.addTab("Sauvegarde", createBackupTab());
        }

        add(titlePanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }


    private JPanel createSecurityTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialisation des champs
        chkDoubleAuth = new JCheckBox("Authentification double facteur");
        chkLogsAudit = new JCheckBox("Logs d'audit complets");
        chkBackupAuto = new JCheckBox("Sauvegarde automatique");

        spinnerDureeSession = new JSpinner(new SpinnerNumberModel(30, 5, 480, 5)); // minutes
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinnerDureeSession, "# minutes");
        spinnerDureeSession.setEditor(editor);

        // Ajout des champs
        addFormField(formPanel, gbc, "Durée maximale de session:", spinnerDureeSession, 0);
        addFormField(formPanel, gbc, "Sécurité:", chkDoubleAuth, 1);
        addFormField(formPanel, gbc, "", chkLogsAudit, 2);
        addFormField(formPanel, gbc, "", chkBackupAuto, 3);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_BACKGROUND);

        JButton btnSave = new JButton("Enregistrer");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> saveSecuritySettings());

        buttonPanel.add(btnSave);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createNotificationsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialisation des champs
        chkNotifEmail = new JCheckBox("Notifications par email");
        chkNotifSMS = new JCheckBox("Notifications par SMS");
        chkNotifApp = new JCheckBox("Notifications dans l'application");

        // Ajout des champs
        addFormField(formPanel, gbc, "Canaux de notification:", chkNotifEmail, 0);
        addFormField(formPanel, gbc, "", chkNotifSMS, 1);
        addFormField(formPanel, gbc, "", chkNotifApp, 2);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_BACKGROUND);

        JButton btnSave = new JButton("Enregistrer");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> saveNotificationSettings());

        buttonPanel.add(btnSave);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBackupTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialisation des champs
        cmbFreqSauvegarde = new JComboBox<>(new String[]{
            "Jamais", "Quotidienne", "Hebdomadaire", "Mensuelle"
        });

        JTextArea infoArea = new JTextArea(
            "Configuration des sauvegardes automatiques :\n" +
            "• Quotidienne : Tous les jours à minuit\n" +
            "• Hebdomadaire : Tous les dimanches\n" +
            "• Mensuelle : Le 1er du mois\n\n" +
            "Les sauvegardes incluent :\n" +
            "• Base de données complète\n" +
            "• Fichiers uploadés\n" +
            "• Configuration système"
        );
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBackground(CARD_BACKGROUND);

        // Ajout des champs
        addFormField(formPanel, gbc, "Fréquence de sauvegarde:", cmbFreqSauvegarde, 0);
        addFormField(formPanel, gbc, "Information:", new JScrollPane(infoArea), 1);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_BACKGROUND);

        JButton btnSave = new JButton("Enregistrer");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> saveBackupSettings());

        JButton btnBackupNow = new JButton("Sauvegarder maintenant");
        btnBackupNow.setBackground(new Color(52, 152, 219));
        btnBackupNow.setForeground(Color.WHITE);
        btnBackupNow.setFocusPainted(false);
        btnBackupNow.setBorderPainted(false);
        btnBackupNow.addActionListener(e -> performBackupNow());

        buttonPanel.add(btnBackupNow);
        buttonPanel.add(btnSave);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void loadCurrentSettings() {
        // Charger les paramètres actuels depuis la base ou la config
        // Pour l'instant, valeurs par défaut

        chkDoubleAuth.setSelected(false);
        chkLogsAudit.setSelected(true);
        chkBackupAuto.setSelected(true);

        chkNotifEmail.setSelected(true);
        chkNotifSMS.setSelected(false);
        chkNotifApp.setSelected(true);

        cmbFreqSauvegarde.setSelectedItem("Hebdomadaire");
    }


    private void saveSecuritySettings() {
        JOptionPane.showMessageDialog(this,
            "Paramètres de sécurité enregistrés avec succès !",
            "Succès",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveNotificationSettings() {
        JOptionPane.showMessageDialog(this,
            "Paramètres de notifications enregistrés avec succès !",
            "Succès",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveBackupSettings() {
        JOptionPane.showMessageDialog(this,
            "Paramètres de sauvegarde enregistrés avec succès !",
            "Succès",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void performBackupNow() {
        JOptionPane.showMessageDialog(this,
            "Sauvegarde manuelle lancée !\n" +
            "La sauvegarde sera disponible dans le dossier de sauvegarde.",
            "Sauvegarde en cours",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isAdmin() {
        return principal != null && principal.roles() != null &&
               principal.roles().contains(RoleEnum.ADMIN);
    }
}
