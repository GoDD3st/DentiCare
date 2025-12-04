package ma.dentalTech.mvc.ui.modules.notification;

import ma.dentalTech.mvc.controllers.modules.notification.swing_implementation.NotificationControllerImpl;
import ma.dentalTech.mvc.dto.NotificationDTO;
import ma.dentalTech.entities.enums.NotificationTypeEnum;
import ma.dentalTech.entities.enums.PrioriteEnum;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationView extends JFrame {
    
    private final NotificationControllerImpl controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public NotificationView(NotificationControllerImpl controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Gestion des Notifications");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Color primaryColor = new Color(0x4DB6AC);
        Color backgroundColor = new Color(0xFFFFFF);
        
        setBackground(backgroundColor);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(backgroundColor);
        
        JButton btnNouveau = new JButton("Nouvelle Notification");
        btnNouveau.setBackground(primaryColor);
        btnNouveau.setForeground(Color.WHITE);
        btnNouveau.addActionListener(e -> afficherFormulaire());
        
        JButton btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.addActionListener(e -> rafraichirListe());
        
        topPanel.add(btnNouveau);
        topPanel.add(btnRafraichir);
        
        String[] columns = {"ID", "Titre", "Message", "Date", "Heure", "Type", "Priorité"};
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
        
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setForeground(Color.RED);
        btnSupprimer.addActionListener(e -> supprimerSelectionne());
        
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
        JDialog dialog = new JDialog(this, "Nouvelle Notification", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JComboBox<NotificationTypeEnum> titreCombo = new JComboBox<>(NotificationTypeEnum.values());
        JTextArea messageField = new JTextArea(3, 20);
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JComboBox<NotificationTypeEnum> typeCombo = new JComboBox<>(NotificationTypeEnum.values());
        JComboBox<PrioriteEnum> prioriteCombo = new JComboBox<>(PrioriteEnum.values());
        JTextArea descriptionField = new JTextArea(3, 20);
        
        panel.add(new JLabel("Titre:"));
        panel.add(titreCombo);
        panel.add(new JLabel("Message:"));
        panel.add(new JScrollPane(messageField));
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Heure (HH:MM:SS):"));
        panel.add(timeField);
        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Priorité:"));
        panel.add(prioriteCombo);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionField));
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            try {
                NotificationDTO dto = NotificationDTO.builder()
                        .titre((NotificationTypeEnum) titreCombo.getSelectedItem())
                        .message(messageField.getText())
                        .date(java.time.LocalDate.parse(dateField.getText()))
                        .time(java.time.LocalTime.parse(timeField.getText()))
                        .type((NotificationTypeEnum) typeCombo.getSelectedItem())
                        .priorite((PrioriteEnum) prioriteCombo.getSelectedItem())
                        .description(descriptionField.getText())
                        .utilisateurIds(new ArrayList<>()) // TODO: Sélectionner les utilisateurs
                        .build();
                controller.creerNotification(dto);
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
        List<NotificationDTO> notifications = controller.getAllNotifications();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (NotificationDTO notif : notifications) {
            tableModel.addRow(new Object[]{
                    notif.getId(),
                    notif.getTitre(),
                    notif.getMessage(),
                    notif.getDate() != null ? notif.getDate().format(dateFormatter) : "",
                    notif.getTime() != null ? notif.getTime().format(timeFormatter) : "",
                    notif.getType(),
                    notif.getPriorite()
            });
        }
    }
    
    private void supprimerSelectionne() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir supprimer cette notification?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.supprimerNotification(id);
            }
        } else {
            afficherErreur("Veuillez sélectionner une notification");
        }
    }
    
    public void afficherMessage(String message, String titre) {
        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

