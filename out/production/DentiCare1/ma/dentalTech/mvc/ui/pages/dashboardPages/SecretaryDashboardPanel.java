package ma.dentalTech.mvc.ui.pages.dashboardPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SecretaryDashboardPanel extends JPanel {

    public SecretaryDashboardPanel(UserPrincipal principal) {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(244, 247, 246));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Container principal avec scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(244, 247, 246));
        
        // Titre
        JLabel title = new JLabel("Dashboard Secrétaire");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        contentPanel.add(title);

        // Section Patients
        contentPanel.add(createPatientsSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Dossier Médical
        contentPanel.add(createDossierMedicalSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Antécédents
        contentPanel.add(createAntecedentsSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Rendez-vous
        contentPanel.add(createRendezVousSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Agenda Médecin
        contentPanel.add(createAgendaMedecinSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Caisse
        contentPanel.add(createCaisseSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Factures
        contentPanel.add(createFacturesSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Dashboard
        contentPanel.add(createDashboardSection());

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Bordure gauche bleue
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            new EmptyBorder(0, 10, 0, 0)
        ));
        JPanel blueBar = new JPanel();
        blueBar.setBackground(new Color(52, 152, 219));
        blueBar.setPreferredSize(new Dimension(4, titleLabel.getPreferredSize().height));
        titlePanel.add(blueBar, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        card.add(titlePanel, BorderLayout.NORTH);
        return card;
    }

    private JPanel createPatientsSection() {
        JPanel card = createCard("Gérer Patients");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Ajouter Patient", ActionButton.ButtonType.ADD));
        buttonPanel.add(new ActionButton("Consulter Liste Patients", ActionButton.ButtonType.VIEW));
        
        String[] patientColumns = {"Nom & Prénom", "Téléphone", "Date Inscription", "Actions"};
        Object[][] patientData = {
            {"Ahmed Benali", "0612345678", "2024-01-10", ""},
            {"Fatima Alami", "0698765432", "2024-01-08", ""}
        };
        
        JTable table = createTable(patientColumns, patientData);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createDossierMedicalSection() {
        JPanel card = createCard("Gérer Dossier Médicale");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Créer Dossier Médical", ActionButton.ButtonType.ADD));
        buttonPanel.add(new ActionButton("Consulter Dossier Médical", ActionButton.ButtonType.VIEW));
        
        card.add(buttonPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createAntecedentsSection() {
        JPanel card = createCard("Gérer Antécédents");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Ajouter", ActionButton.ButtonType.ADD));
        
        String[] antecedentColumns = {"Type", "Description", "Actions"};
        Object[][] antecedentData = {
            {"Allergie", "Pénicilline", ""}
        };
        
        JTable table = createTable(antecedentColumns, antecedentData);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createRendezVousSection() {
        JPanel card = createCard("Gérer Rendez-vous Patients");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Planifier RDV", ActionButton.ButtonType.ADD));
        buttonPanel.add(new ActionButton("Consulter Historiques RDV", ActionButton.ButtonType.VIEW));
        buttonPanel.add(new ActionButton("Gérer Liste d'Attente", ActionButton.ButtonType.VIEW));
        
        String[] rdvColumns = {"Patient", "Date & Heure", "Statut", "Actions"};
        Object[][] rdvData = {
            {"Ahmed Benali", "2024-01-16 10:00", "Confirmé", ""},
            {"Fatima Alami", "2024-01-16 11:00", "En attente", ""}
        };
        
        JTable table = createTable(rdvColumns, rdvData);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createAgendaMedecinSection() {
        JPanel card = createCard("Gérer l'Agenda Médecin");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Créer Agenda Mensuel", ActionButton.ButtonType.ADD));
        buttonPanel.add(new ActionButton("Consulter l'Agenda", ActionButton.ButtonType.VIEW));
        
        String[] agendaColumns = {"Date", "Plages Horaires", "Statut", "Actions"};
        Object[][] agendaData = {
            {"2024-01-16", "09:00 - 17:00", "Disponible", ""}
        };
        
        JTable table = createTable(agendaColumns, agendaData);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createCaisseSection() {
        JPanel card = createCard("Gérer la Caisse");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Consulter Statistiques de Caisse", ActionButton.ButtonType.VIEW));
        buttonPanel.add(new ActionButton("Exporter Rapport", ActionButton.ButtonType.EXPORT));
        buttonPanel.add(new ActionButton("Consulter Situation Financière", ActionButton.ButtonType.VIEW));
        
        card.add(buttonPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createFacturesSection() {
        JPanel card = createCard("Gérer Factures");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Générer Facture", ActionButton.ButtonType.ADD));
        
        String[] factureColumns = {"Numéro", "Patient", "Montant", "Date", "Statut", "Actions"};
        Object[][] factureData = {
            {"FAC-2024-001", "Ahmed Benali", "500 DH", "2024-01-15", "Payée", ""}
        };
        
        JTable table = createTable(factureColumns, factureData);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createDashboardSection() {
        JPanel card = createCard("Gérer Dashboard");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Consulter Dashboard", ActionButton.ButtonType.VIEW));
        buttonPanel.add(new ActionButton("Personnaliser Dashboard", ActionButton.ButtonType.EDIT));
        buttonPanel.add(new ActionButton("Consulter La File d'Attente", ActionButton.ButtonType.VIEW));
        buttonPanel.add(new ActionButton("Consulter Les Derniers Alertes", ActionButton.ButtonType.VIEW));
        buttonPanel.add(new ActionButton("Consulter Alertes", ActionButton.ButtonType.VIEW));
        buttonPanel.add(new ActionButton("Consulter Notifications", ActionButton.ButtonType.VIEW));
        
        // Widgets en grille
        JPanel widgetsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        widgetsPanel.setBackground(Color.WHITE);
        widgetsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        widgetsPanel.add(createWidget("File d'Attente", "3 patients en attente"));
        widgetsPanel.add(createWidget("Alertes", "2 nouvelles alertes"));
        
        JPanel notificationWidget = createWidget("Notifications", "5 notifications non lues");
        JPanel notificationButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        notificationButtons.setBackground(new Color(248, 249, 250));
        notificationButtons.add(new ActionButton("Marquer Notification Lue", ActionButton.ButtonType.EDIT, true));
        notificationWidget.add(notificationButtons, BorderLayout.SOUTH);
        widgetsPanel.add(notificationWidget);
        
        JPanel personalisationWidget = createWidget("Personnalisation", "");
        JPanel persoButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        persoButtons.setBackground(new Color(248, 249, 250));
        persoButtons.add(new ActionButton("Ajouter Widget", ActionButton.ButtonType.ADD, true));
        persoButtons.add(new ActionButton("Masquer Widget", ActionButton.ButtonType.DELETE, true));
        personalisationWidget.add(persoButtons, BorderLayout.SOUTH);
        widgetsPanel.add(personalisationWidget);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(widgetsPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createWidget(String title, String content) {
        JPanel widget = new JPanel(new BorderLayout());
        widget.setBackground(new Color(248, 249, 250));
        widget.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel contentLabel = new JLabel(content);
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentLabel.setForeground(new Color(100, 100, 100));
        
        widget.add(titleLabel, BorderLayout.NORTH);
        if (!content.isEmpty()) {
            widget.add(contentLabel, BorderLayout.CENTER);
        }
        
        return widget;
    }

    private JTable createTable(String[] columns, Object[][] data) {
        JTable table = new JTable(data, columns);
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 249, 250));
        table.getTableHeader().setForeground(new Color(127, 140, 141));
        table.setShowGrid(true);
        table.setGridColor(new Color(238, 238, 238));
        table.setSelectionBackground(new Color(230, 242, 255));
        table.setDefaultEditor(Object.class, null); // Désactiver l'édition par défaut
        
        return table;
    }
}
