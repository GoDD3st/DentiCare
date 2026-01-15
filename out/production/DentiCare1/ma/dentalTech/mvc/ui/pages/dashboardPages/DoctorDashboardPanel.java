package ma.dentalTech.mvc.ui.pages.dashboardPages;

import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.ActionButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DoctorDashboardPanel extends JPanel {

    public DoctorDashboardPanel(UserPrincipal principal) {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(244, 247, 246));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Container principal avec scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(244, 247, 246));
        
        // Titre
        JLabel title = new JLabel("Dashboard Médecin");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        contentPanel.add(title);

        // Section Dossiers Médicaux
        contentPanel.add(createDossiersSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Consultations
        contentPanel.add(createConsultationsSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Ordonnances
        contentPanel.add(createOrdonnancesSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Certificats
        contentPanel.add(createCertificatsSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Actes
        contentPanel.add(createActesSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Caisse
        contentPanel.add(createCaisseSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Situations Financières
        contentPanel.add(createSituationsFinancieresSection());
        contentPanel.add(Box.createVerticalStrut(30));

        // Section Factures
        contentPanel.add(createFacturesSection());

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

    private JPanel createDossiersSection() {
        JPanel card = createCard("Gérer les Dossiers Médicaux");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Créer un Dossier", ActionButton.ButtonType.ADD));
        buttonPanel.add(new ActionButton("Chercher et Consulter Dossier", ActionButton.ButtonType.VIEW));
        
        String[] dossierColumns = {"Patient", "Date Création", "Dernière Modification", "Actions"};
        Object[][] dossierData = {
            {"Ahmed Benali", "2024-01-10", "2024-01-15", "Consulter | Modifier | Charger Document | Supprimer"},
            {"Fatima Alami", "2024-01-08", "2024-01-14", "Consulter | Modifier | Charger Document | Supprimer"}
        };
        
        JTable table = createTable(dossierColumns, dossierData, false);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createConsultationsSection() {
        JPanel card = createCard("Gérer Consultation Cs");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Créer et Commencer une Cs", ActionButton.ButtonType.START));
        
        String[] consultationColumns = {"Patient", "Date", "Statut", "Actions"};
        Object[][] consultationData = {
            {"Ahmed Benali", "2024-01-15 10:00", "En cours", "Consulter | Modifier | Terminer | ..."},
            {"Fatima Alami", "2024-01-15 11:00", "Planifiée", "Consulter | Modifier | Commencer | Supprimer"}
        };
        
        JTable table = createTable(consultationColumns, consultationData, false);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createOrdonnancesSection() {
        JPanel card = createCard("Gérer les Ordonnances");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Créer Ordonnance", ActionButton.ButtonType.ADD));
        
        String[] ordonnanceColumns = {"Patient", "Date", "Médicaments", "Actions"};
        Object[][] ordonnanceData = {
            {"Ahmed Benali", "2024-01-15", "3 médicaments", "Consulter | Modifier | Imprimer | Supprimer"}
        };
        
        JTable table = createTable(ordonnanceColumns, ordonnanceData, false);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createCertificatsSection() {
        JPanel card = createCard("Gérer les Certificats");
        
        String[] certificatColumns = {"Patient", "Type", "Date", "Actions"};
        Object[][] certificatData = {
            {"Ahmed Benali", "Arrêt de travail", "2024-01-15", "Consulter | Modifier | Imprimer | Supprimer"}
        };
        
        JTable table = createTable(certificatColumns, certificatData, false);
        
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createActesSection() {
        JPanel card = createCard("Gérer les Actes");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Ajouter Acte", ActionButton.ButtonType.ADD));
        
        String[] acteColumns = {"Libellé", "Code", "Tarif", "Actions"};
        Object[][] acteData = {
            {"Consultation Générale", "CSG", "200 DH", "Consulter | Modifier | Appliquer Remise | Supprimer"}
        };
        
        JTable table = createTable(acteColumns, acteData, false);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createCaisseSection() {
        JPanel card = createCard("Gérer la Caisse");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Consulter Statistiques Caisse", ActionButton.ButtonType.VIEW));
        
        card.add(buttonPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createSituationsFinancieresSection() {
        JPanel card = createCard("Gérer les Situations Financières");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Créer une SF", ActionButton.ButtonType.ADD));
        buttonPanel.add(new ActionButton("Lister les SF de tous les patients", ActionButton.ButtonType.VIEW));
        
        String[] sfColumns = {"Patient", "Montant Total", "Payé", "Reste", "Actions"};
        Object[][] sfData = {
            {"Ahmed Benali", "1500 DH", "1000 DH", "500 DH", "Consulter SF | Réinitialiser | Gérer Factures"}
        };
        
        JTable table = createTable(sfColumns, sfData, false);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createFacturesSection() {
        JPanel card = createCard("Gérer Tous les Factures");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(new ActionButton("Lister tous les factures", ActionButton.ButtonType.VIEW));
        
        String[] factureColumns = {"Numéro", "Patient", "Montant", "Date", "Actions"};
        Object[][] factureData = {
            {"FAC-2024-001", "Ahmed Benali", "500 DH", "2024-01-15", "Consulter | Modifier | Imprimer | Supprimer"}
        };
        
        JTable table = createTable(factureColumns, factureData, false);
        
        card.add(buttonPanel, BorderLayout.NORTH);
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        
        return card;
    }

    private JTable createTable(String[] columns, Object[][] data, boolean editable) {
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

    private void addActionButtonsToTable(JTable table, int actionColumnIndex, String[] buttonTexts, ActionButton.ButtonType[] buttonTypes) {
        // Simplification : les boutons seront affichés dans une colonne séparée
        // Pour une implémentation complète, il faudrait créer un renderer personnalisé
        // Pour l'instant, on laisse la colonne Actions vide dans les données
    }

    private void addStatusBadge(JTable table, int columnIndex, int rowIndex, String text, Color bgColor, Color fgColor) {
        table.setValueAt(text, rowIndex, columnIndex);
    }
}
