package ma.dentalTech.mvc.ui.modules.certificat;

import ma.dentalTech.mvc.controllers.modules.certificat.swing_implementation.CertificatControllerImpl;
import ma.dentalTech.mvc.dto.CertificatDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CertificatView extends JFrame {
    
    private final CertificatControllerImpl controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public CertificatView(CertificatControllerImpl controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Gestion des Certificats");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Color primaryColor = new Color(0x4DB6AC);
        Color backgroundColor = new Color(0xFFFFFF);
        
        setBackground(backgroundColor);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(backgroundColor);
        
        JButton btnNouveau = new JButton("Nouveau Certificat");
        btnNouveau.setBackground(primaryColor);
        btnNouveau.setForeground(Color.WHITE);
        btnNouveau.addActionListener(e -> afficherFormulaire());
        
        JButton btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.addActionListener(e -> rafraichirListe());
        
        topPanel.add(btnNouveau);
        topPanel.add(btnRafraichir);
        
        String[] columns = {"ID", "Date Début", "Date Fin", "Durée", "Patient", "Médecin"};
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
        JDialog dialog = new JDialog(this, "Nouveau Certificat", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField dateDebutField = new JTextField();
        JTextField dateFinField = new JTextField();
        JTextField dureeField = new JTextField();
        JTextField patientIdField = new JTextField();
        JTextField medecinIdField = new JTextField();
        JTextArea noteField = new JTextArea(3, 20);
        
        panel.add(new JLabel("Date Début:"));
        panel.add(dateDebutField);
        panel.add(new JLabel("Date Fin:"));
        panel.add(dateFinField);
        panel.add(new JLabel("Durée (jours):"));
        panel.add(dureeField);
        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);
        panel.add(new JLabel("Médecin ID:"));
        panel.add(medecinIdField);
        panel.add(new JLabel("Note Médecin:"));
        panel.add(new JScrollPane(noteField));
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            try {
                CertificatDTO dto = CertificatDTO.builder()
                        .dateDebut(java.time.LocalDate.parse(dateDebutField.getText()))
                        .dateFin(java.time.LocalDate.parse(dateFinField.getText()))
                        .duree(Integer.parseInt(dureeField.getText()))
                        .patientId(Long.parseLong(patientIdField.getText()))
                        .medecinId(Long.parseLong(medecinIdField.getText()))
                        .noteMedecin(noteField.getText())
                        .build();
                controller.creerCertificat(dto);
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
        List<CertificatDTO> certificats = controller.getAllCertificats();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (CertificatDTO cert : certificats) {
            tableModel.addRow(new Object[]{
                    cert.getId(),
                    cert.getDateDebut() != null ? cert.getDateDebut().format(formatter) : "",
                    cert.getDateFin() != null ? cert.getDateFin().format(formatter) : "",
                    cert.getDuree(),
                    cert.getPatientNom() != null ? cert.getPatientNom() : cert.getPatientId(),
                    cert.getMedecinNom() != null ? cert.getMedecinNom() : cert.getMedecinId()
            });
        }
    }
    
    private void supprimerSelectionne() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer ce certificat?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.supprimerCertificat(id);
            }
        } else {
            afficherErreur("Veuillez sélectionner un certificat");
        }
    }
    
    public void afficherMessage(String message, String titre) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

