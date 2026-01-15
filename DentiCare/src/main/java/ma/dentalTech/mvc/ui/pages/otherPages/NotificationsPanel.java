package ma.dentalTech.mvc.ui.pages.otherPages;

import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.entities.enums.PrioriteEnum;
import ma.dentalTech.entities.enums.NotificationTypeEnum;
import ma.dentalTech.mvc.dto.authentificationDtos.UserPrincipal;
import ma.dentalTech.mvc.ui.palette.buttons.MyButton;
import ma.dentalTech.mvc.ui.palette.utils.ImageTools;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationsPanel extends JPanel {

    private final UserPrincipal principal;
    private JTable notificationsTable;
    private DefaultTableModel tableModel;
    private List<Notification> notifications;
    private JButton btnRefresh, btnMarkAllRead, btnDeleteRead;

    public NotificationsPanel(UserPrincipal principal) {
        this.principal = principal;
        this.notifications = loadNotifications();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        loadNotificationsData();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // Titre
        JLabel lblTitle = new JLabel("Notifications");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));

        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        btnRefresh = new MyButton("Actualiser", ImageTools.loadIcon("/static/icons/refresh.png", 16, 16), new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.addActionListener(this::refreshAction);

        btnMarkAllRead = new MyButton("Tout marquer comme lu", null, new Font("Segoe UI", Font.PLAIN, 12));
        btnMarkAllRead.addActionListener(this::markAllReadAction);

        btnDeleteRead = new MyButton("Supprimer les lues", null, new Font("Segoe UI", Font.PLAIN, 12));
        btnDeleteRead.addActionListener(this::deleteReadAction);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnMarkAllRead);
        buttonPanel.add(btnDeleteRead);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Configuration de la table
        String[] columnNames = {"Statut", "Type", "Titre", "Message", "Date", "Priorité", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Uniquement la colonne Actions
            }
        };

        notificationsTable = new JTable(tableModel);
        notificationsTable.setRowHeight(50);
        notificationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        notificationsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        // Configuration des colonnes
        notificationsTable.getColumnModel().getColumn(0).setPreferredWidth(60); // Statut
        notificationsTable.getColumnModel().getColumn(1).setPreferredWidth(80); // Type
        notificationsTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Titre
        notificationsTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Message
        notificationsTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Date
        notificationsTable.getColumnModel().getColumn(5).setPreferredWidth(80); // Priorité
        notificationsTable.getColumnModel().getColumn(6).setPreferredWidth(120); // Actions

        // Renderer personnalisé pour les cellules
        notificationsTable.setDefaultRenderer(Object.class, new NotificationTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(notificationsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setOpaque(false);

        // Statistiques (toutes les notifications sont considérées comme non lues pour l'exemple)
        int total = notifications.size();
        int unread = total; // Simulation : toutes sont non lues

        JLabel lblStats = new JLabel(String.format("Total: %d | Non lues: %d", total, unread));
        lblStats.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStats.setForeground(new Color(100, 100, 100));

        footer.add(lblStats);
        return footer;
    }

    private List<Notification> loadNotifications() {
        // Simulation de chargement des notifications
        // En production, cela viendrait de la base de données
        List<Notification> list = new ArrayList<>();

        // Exemples de notifications
        Notification notif1 = Notification.builder()
                .idNotif(1L)
                .titre(NotificationTypeEnum.RAPPEL)
                .message("Vous avez une consultation demain à 14h avec Mme Dupont.")
                .type(NotificationTypeEnum.RAPPEL)
                .priorite(PrioriteEnum.MOYENNE)
                .date(java.time.LocalDate.now())
                .heure(java.time.LocalTime.now())
                .build();
        list.add(notif1);

        Notification notif2 = Notification.builder()
                .idNotif(2L)
                .titre(NotificationTypeEnum.INFO)
                .message("La facture #1234 a été payée avec succès.")
                .type(NotificationTypeEnum.INFO)
                .priorite(PrioriteEnum.BASSE)
                .date(java.time.LocalDate.now().minusDays(1))
                .heure(java.time.LocalTime.now())
                .build();
        list.add(notif2);

        Notification notif3 = Notification.builder()
                .idNotif(3L)
                .titre(NotificationTypeEnum.ALERTE)
                .message("Le médicament Paracétamol 500mg n'est plus disponible.")
                .type(NotificationTypeEnum.ALERTE)
                .priorite(PrioriteEnum.CRITIQUE)
                .date(java.time.LocalDate.now())
                .heure(java.time.LocalTime.now().minusHours(2))
                .build();
        list.add(notif3);

        return list;
    }

    private void loadNotificationsData() {
        tableModel.setRowCount(0);

        for (Notification notif : notifications) {
            Object[] row = {
                "Non lu", // Simulation : toutes sont non lues
                notif.getType().toString(),
                notif.getTitre().toString(), // Titre est enum, on convertit en string
                notif.getMessage(),
                notif.getDate() != null ? notif.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
                notif.getPriorite().toString(),
                "Actions" // Placeholder pour les boutons d'action
            };
            tableModel.addRow(row);
        }
    }

    // Actions
    private void refreshAction(ActionEvent e) {
        notifications = loadNotifications();
        loadNotificationsData();
        JOptionPane.showMessageDialog(this, "Notifications actualisées !");
    }

    private void markAllReadAction(ActionEvent e) {
        // Simulation : en production, cela mettrait à jour la base de données
        JOptionPane.showMessageDialog(this, "Toutes les notifications ont été marquées comme lues.\n(Fonctionnalité à implémenter avec la base de données)");
    }

    private void deleteReadAction(ActionEvent e) {
        // Simulation : supprimer quelques notifications pour l'exemple
        if (!notifications.isEmpty()) {
            notifications.remove(0); // Supprimer la première
            loadNotificationsData();
            JOptionPane.showMessageDialog(this, "Les notifications lues ont été supprimées.\n(Fonctionnalité à implémenter avec la base de données)");
        } else {
            JOptionPane.showMessageDialog(this, "Aucune notification à supprimer.");
        }
    }

    // Renderer personnalisé pour la table
    private static class NotificationTableCellRenderer extends JLabel implements TableCellRenderer {
        public NotificationTableCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            String text = value != null ? value.toString() : "";

            // Coloration selon la priorité (colonne 5)
            if (column == 5) {
                String priorite = table.getValueAt(row, 5).toString();
                switch (priorite) {
                    case "CRITIQUE":
                        setBackground(new Color(255, 235, 235));
                        setForeground(new Color(220, 53, 69));
                        break;
                    case "HAUTE":
                        setBackground(new Color(255, 243, 205));
                        setForeground(new Color(255, 193, 7));
                        break;
                    case "MOYENNE":
                        setBackground(new Color(255, 250, 240));
                        setForeground(new Color(255, 140, 0));
                        break;
                    default:
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                }
            } else if (column == 0) { // Statut
                boolean isLu = "Lu".equals(text);
                setBackground(isLu ? new Color(240, 255, 240) : new Color(255, 250, 205));
                setForeground(isLu ? new Color(25, 135, 84) : new Color(255, 193, 7));
            } else {
                setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
            }

            setText(text);
            setFont(column == 2 ? new Font("Segoe UI", Font.BOLD, 11) : new Font("Segoe UI", Font.PLAIN, 11));

            return this;
        }
    }
}
