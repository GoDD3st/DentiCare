package ma.dentalTech.mvc.ui.pages.profilePages;

import java.util.function.Consumer;
import ma.dentalTech.mvc.dto.profileDtos.ProfileData;
import ma.dentalTech.mvc.ui.palette.alert.Alert;
import ma.dentalTech.mvc.ui.palette.fields.CustomTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private ProfileData data;

    // UI components
    private CustomTextField tfPrenom, tfNom, tfEmail, tfTel, tfAdresse;
    private JButton btSave, btChangePassword;

    // Header labels
    private JLabel lblName;
    private JLabel lblEmail;

    // callback pour mettre à jour le headerPanel
    private final Consumer<ProfileData> onProfileSaved;

    public ProfilePanel(Object controllerIgnored, Object serviceIgnored, ProfileData data, Consumer<ProfileData> onProfileSaved) {
        this.data = data;
        this.onProfileSaved = onProfileSaved;

        setLayout(new BorderLayout(16, 16));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(22, 22, 22, 22));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        fillFromData();
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setOpaque(false);

        // Name and Email
        lblName = new JLabel("—");
        lblName.setFont(new Font("Optima", Font.BOLD, 28));

        lblEmail = new JLabel("—");
        lblEmail.setFont(new Font("Optima", Font.PLAIN, 14));
        lblEmail.setForeground(new Color(90, 90, 90));

        JPanel left = new JPanel(new GridLayout(0, 1, 0, 6));
        left.setOpaque(false);
        left.add(lblName);
        left.add(lblEmail);

        header.add(left, BorderLayout.WEST);

        return header;
    }

    private JComponent buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        tfPrenom  = ctf("Prénom");
        tfNom     = ctf("Nom");
        tfEmail   = ctf("Email");
        tfTel     = ctf("Téléphone");
        tfAdresse = ctf("Adresse");

        int row = 0;
        row = addRow(form, gc, row, "Prénom", tfPrenom);
        row = addRow(form, gc, row, "Nom", tfNom);
        row = addRow(form, gc, row, "Email", tfEmail);
        row = addRow(form, gc, row, "Téléphone", tfTel);
        row = addRow(form, gc, row, "Adresse", tfAdresse);

        return form;
    }

    private JComponent buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        p.setOpaque(false);

        btSave = new JButton("Enregistrer");
        btSave.setFont(new Font("Optima", Font.BOLD, 16));
        btSave.addActionListener(e -> onSave());

        btChangePassword = new JButton("Changer mot de passe");
        btChangePassword.setFont(new Font("Optima", Font.BOLD, 16));
        btChangePassword.addActionListener(e -> openChangePasswordDialog());

        p.add(btSave);
        p.add(btChangePassword);
        return p;
    }

    private void onSave() {
        if (data == null || data.userId() == null) {
            Alert.error(this, "Impossible de sauvegarder : profil non chargé.");
            return;
        }

        // Create updated ProfileData
        ProfileData updatedData = new ProfileData(
                data.userId(),
                readValue(tfPrenom),
                readValue(tfNom),
                readValue(tfEmail),
                "", // avatar - TODO
                readValue(tfTel),
                readValue(tfAdresse)
        );

        this.data = updatedData;
        fillFromData();

        // Notify dashboard of profile changes
        if (onProfileSaved != null) onProfileSaved.accept(this.data);
        Alert.success(this, "Profil enregistré.");
    }

    private String readValue(CustomTextField tf) {
        if (tf == null) return null;
        String s = tf.getText();
        if (s == null) return "";
        if (s.equals(tf.getHint())) return ""; // ignore hint
        return s.trim();
    }

    private void openChangePasswordDialog() {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this), "Changer mot de passe", Dialog.ModalityType.APPLICATION_MODAL);
        d.setLayout(new BorderLayout());

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField pfCur = new JPasswordField(20);
        JPasswordField pfNew = new JPasswordField(20);
        JPasswordField pfCnf = new JPasswordField(20);

        int row = 0;
        row = addRow(content, gc, row, "Mot de passe actuel", pfCur);
        row = addRow(content, gc, row, "Nouveau mot de passe", pfNew);
        row = addRow(content, gc, row, "Confirmer", pfCnf);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footer.setBackground(Color.WHITE);

        JButton btOk = new JButton("Valider");
        JButton btCancel = new JButton("Annuler");
        btCancel.addActionListener(ev -> d.dispose());

        btOk.addActionListener(ev -> {
            String cur = new String(pfCur.getPassword());
            String nw = new String(pfNew.getPassword());
            String cf = new String(pfCnf.getPassword());

            if (cur.isEmpty() || nw.isEmpty() || cf.isEmpty()) {
                JOptionPane.showMessageDialog(d, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!nw.equals(cf)) {
                JOptionPane.showMessageDialog(d, "La confirmation ne correspond pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Implement actual password change
            JOptionPane.showMessageDialog(d, "Mot de passe modifié (TODO).", "Succès", JOptionPane.INFORMATION_MESSAGE);
            d.dispose();
        });

        footer.add(btOk);
        footer.add(btCancel);

        d.add(content, BorderLayout.CENTER);
        d.add(footer, BorderLayout.SOUTH);
        d.pack();
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void fillFromData() {
        if (data == null) return;

        setValue(tfPrenom, data.prenom());
        setValue(tfNom, data.nom());
        setValue(tfEmail, data.email());
        setValue(tfTel, data.tel());
        setValue(tfAdresse, data.adresse());

        // Header labels
        String fullName = (safe(data.prenom()) + " " + safe(data.nom())).trim();
        lblName.setText(fullName.isBlank() ? "Mon Profil" : fullName);
        lblEmail.setText(safe(data.email()));
    }

    private void setValue(CustomTextField tf, String value) {
        if (tf == null) return;

        String v = value == null ? "" : value.trim();

        if (v.isBlank()) {
            // mode hint / lost
            tf.setText(tf.getHint());
            tf.setFont(tf.getLostFont());
            tf.setForeground(tf.getLostColor());
        } else {
            // mode valeur / gain
            tf.setText(v);
            tf.setFont(tf.getGainFont());
            tf.setForeground(tf.getGainColor());
        }

        tf.repaint();
    }

    private CustomTextField ctf(String hint) {
        CustomTextField t = new CustomTextField(hint);
        t.setPreferredSize(new Dimension(320, 42));
        return t;
    }

    private int addRow(JPanel form, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridy = row;
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.weightx = 0;

        JLabel l = new JLabel(label);
        l.setFont(new Font("Optima", Font.BOLD, 14));
        form.add(l, gc);

        gc.gridx = 1;
        gc.weightx = 1;
        form.add(field, gc);

        return row + 1;
    }

    private String safe(String s) { return s == null ? "" : s; }
}