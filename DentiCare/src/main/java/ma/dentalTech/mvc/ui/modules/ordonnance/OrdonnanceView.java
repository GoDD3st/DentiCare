package ma.dentalTech.mvc.ui.modules.ordonnance;

import ma.dentalTech.mvc.controllers.modules.ordonnance.swing_implementation.OrdonnanceControllerImpl;
import ma.dentalTech.mvc.dto.OrdonnanceDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrdonnanceView extends JFrame {
    
    private final OrdonnanceControllerImpl controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField dateField;
    private JTextField consultationIdField;
    
    public OrdonnanceView(OrdonnanceControllerImpl controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Gestion des Ordonnances");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Couleurs de la charte
        Color primaryColor = new Color(0x4DB6AC); // Bleu clair
        Color backgroundColor = new Color(0xFFFFFF); // Blanc
        Color secondaryColor = new Color(0xE0E0E0); // Gris clair
        
        setBackground(backgroundColor);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        
        // Panel supérieur avec boutons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(backgroundColor);
        
        JButton btnNouveau = new JButton("Nouvelle Ordonnance");
        btnNouveau.setBackground(primaryColor);
        btnNouveau.setForeground(Color.WHITE);
        btnNouveau.addActionListener(e -> afficherFormulaire());
        
        JButton btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.addActionListener(e -> rafraichirListe());
        
        topPanel.add(btnNouveau);
        topPanel.add(btnRafraichir);
        
        // Table
        String[] columns = {"ID", "Date", "Consultation ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Panel inférieur avec boutons d'action
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(backgroundColor);
        
        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierSelectionne());
        
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setForeground(Color.RED);
        btnSupprimer.addActionListener(e -> supprimerSelectionne());
        
        bottomPanel.add(btnModifier);
        bottomPanel.add(btnSupprimer);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public void afficherListe() {
        rafraichirListe();
    }
    
    public void afficherFormulaire() {
        JDialog dialog = new JDialog(this, "Nouvelle Ordonnance", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Date:"));
        dateField = new JTextField();
        panel.add(dateField);
        
        panel.add(new JLabel("Consultation ID:"));
        consultationIdField = new JTextField();
        panel.add(consultationIdField);
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            try {
                OrdonnanceDTO dto = OrdonnanceDTO.builder()
                        .date(java.time.LocalDate.parse(dateField.getText()))
                        .consultationId(Long.parseLong(consultationIdField.getText()))
                        .build();
                controller.creerOrdonnance(dto);
                dialog.dispose();
            } catch (Exception ex) {
                afficherErreur("Erreur: " + ex.getMessage());
            }
        });
        
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnValider);
        buttonPanel.add(btnAnnuler);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    public void rafraichirListe() {
        tableModel.setRowCount(0);
        List<OrdonnanceDTO> ordonnances = controller.getAllOrdonnances();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (OrdonnanceDTO ord : ordonnances) {
            tableModel.addRow(new Object[]{
                    ord.getId(),
                    ord.getDate() != null ? ord.getDate().format(formatter) : "",
                    ord.getConsultationId()
            });
        }
    }
    
    private void modifierSelectionne() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) tableModel.getValueAt(row, 0);
            // TODO: Ouvrir formulaire de modification
            afficherMessage("Modification à implémenter", "Info");
        } else {
            afficherErreur("Veuillez sélectionner une ordonnance");
        }
    }
    
    private void supprimerSelectionne() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer cette ordonnance?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.supprimerOrdonnance(id);
            }
        } else {
            afficherErreur("Veuillez sélectionner une ordonnance");
        }
    }
    
    public void afficherMessage(String message, String titre) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

