package ma.dentalTech.mvc.ui.modules.caisse;

import ma.dentalTech.mvc.controllers.modules.caisse.swing_implementation.CaisseControllerImpl;
import ma.dentalTech.mvc.dto.FactureDTO;
import ma.dentalTech.entities.enums.FactureStatutEnum;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CaisseView extends JFrame {
    
    private final CaisseControllerImpl controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public CaisseView(CaisseControllerImpl controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Gestion de la Caisse");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Color primaryColor = new Color(0x4DB6AC);
        Color backgroundColor = new Color(0xFFFFFF);
        Color successColor = new Color(0x81C784);
        
        setBackground(backgroundColor);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(backgroundColor);
        
        JButton btnNouvelleFacture = new JButton("Nouvelle Facture");
        btnNouvelleFacture.setBackground(primaryColor);
        btnNouvelleFacture.setForeground(Color.WHITE);
        btnNouvelleFacture.addActionListener(e -> afficherFormulaireFacture());
        
        JButton btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.addActionListener(e -> rafraichirListe());
        
        JButton btnPaiement = new JButton("Enregistrer Paiement");
        btnPaiement.setBackground(successColor);
        btnPaiement.setForeground(Color.WHITE);
        btnPaiement.addActionListener(e -> afficherFormulairePaiement());
        
        topPanel.add(btnNouvelleFacture);
        topPanel.add(btnPaiement);
        topPanel.add(btnRafraichir);
        
        String[] columns = {"ID", "Patient", "Total", "Payé", "Reste", "Statut", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(backgroundColor);
        
        JButton btnModifier = new JButton("Modifier");
        JButton btnPDF = new JButton("Générer PDF");
        btnPDF.setBackground(primaryColor);
        btnPDF.setForeground(Color.WHITE);
        btnPDF.addActionListener(e -> genererPDF());
        
        bottomPanel.add(btnModifier);
        bottomPanel.add(btnPDF);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public void afficherListeFactures() {
        rafraichirListe();
    }
    
    public void afficherFormulaireFacture() {
        JDialog dialog = new JDialog(this, "Nouvelle Facture", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField patientIdField = new JTextField();
        JTextField totaleField = new JTextField();
        JTextField payeField = new JTextField();
        JComboBox<FactureStatutEnum> statutCombo = new JComboBox<>(FactureStatutEnum.values());
        JTextField situationIdField = new JTextField();
        
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Total Facture:"));
        panel.add(totaleField);
        panel.add(new JLabel("Total Payé:"));
        panel.add(payeField);
        panel.add(new JLabel("Statut:"));
        panel.add(statutCombo);
        panel.add(new JLabel("Situation Financière ID:"));
        panel.add(situationIdField);
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            try {
                FactureDTO dto = FactureDTO.builder()
                        .patientId(Long.parseLong(patientIdField.getText()))
                        .totaleFacture(Double.parseDouble(totaleField.getText()))
                        .totalePaye(Double.parseDouble(payeField.getText()))
                        .reste(Double.parseDouble(totaleField.getText()) - Double.parseDouble(payeField.getText()))
                        .statut((FactureStatutEnum) statutCombo.getSelectedItem())
                        .situationFinanciereId(Long.parseLong(situationIdField.getText()))
                        .build();
                controller.creerFacture(dto);
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
    
    private void afficherFormulairePaiement() {
        int row = table.getSelectedRow();
        if (row < 0) {
            afficherErreur("Veuillez sélectionner une facture");
            return;
        }
        
        Long factureId = (Long) tableModel.getValueAt(row, 0);
        String montantStr = JOptionPane.showInputDialog(this, "Montant du paiement:", "Paiement", JOptionPane.QUESTION_MESSAGE);
        
        if (montantStr != null && !montantStr.trim().isEmpty()) {
            try {
                Double montant = Double.parseDouble(montantStr);
                controller.enregistrerPaiement(factureId, montant);
            } catch (Exception e) {
                afficherErreur("Erreur: " + e.getMessage());
            }
        }
    }
    
    public void rafraichirListe() {
        tableModel.setRowCount(0);
        List<FactureDTO> factures = controller.getAllFactures();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (FactureDTO facture : factures) {
            tableModel.addRow(new Object[]{
                    facture.getId(),
                    facture.getPatientNom() != null ? facture.getPatientNom() : facture.getPatientId(),
                    String.format("%.2f", facture.getTotaleFacture()),
                    String.format("%.2f", facture.getTotalePaye()),
                    String.format("%.2f", facture.getReste()),
                    facture.getStatut(),
                    facture.getDateFacture() != null ? facture.getDateFacture().format(formatter) : ""
            });
        }
    }
    
    private void genererPDF() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long factureId = (Long) tableModel.getValueAt(row, 0);
            controller.genererFacturePDF(factureId);
        } else {
            afficherErreur("Veuillez sélectionner une facture");
        }
    }
    
    public void afficherMessage(String message, String titre) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

