package ma.dentalTech.mvc.ui.frames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ma.dentalTech.mvc.ui.palette.buttons.MyButton;
import ma.dentalTech.mvc.ui.palette.fields.CustomPasswordField;
import ma.dentalTech.mvc.ui.palette.fields.CustomTextField;
import ma.dentalTech.mvc.ui.palette.utils.ImageTools;
import ma.dentalTech.mvc.controllers.modules.auth.api.LoginController;

public class LoginUI extends JFrame {

    private final LoginController controller;

    private CustomTextField txt_lg;
    private CustomPasswordField txt_pass;

    private JLabel lbl_err_login, lbl_err_pass, lbl_err_global;

    // Bouton Login (référence nécessaire pour KeyBinding)
    private MyButton btLogin;

    public LoginUI(LoginController controller) {
        this.controller = controller;

        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        // Construction UI
        setContentPane(buildRoot());

        // KeyBinding ENTER = Login (bonne pratique Swing)
        installKeyBindings();

        setVisible(false);
    }

    // Actions ———————————————————————————————————————————————————————————————————
    private void loginAction(ActionEvent e) {
        clearErrors();
        String login = txt_lg.getText().trim();
        String pass = new String(txt_pass.getPassword());

        // Ignorer les valeurs placeholder (CustomTextField/CustomPasswordField gèrent déjà cela)
        // Mais on vérifie quand même au cas où
        if (login != null && login.equals("Utilisateur")) {
            login = "";
        }
        if (pass != null && pass.equals("********")) {
            pass = "";
        }

        // Validation simple
        if (login.isEmpty()) {
            showFieldError("login", "L'identifiant est requis");
            return;
        }
        if (pass.isEmpty()) {
            showFieldError("password", "Le mot de passe est requis");
            return;
        }

        // Appeler le controller
        if (controller != null) {
            controller.onLoginRequested(login, pass);
        } else {
            showGlobalError("Contrôleur non initialisé");
        }
    }

    private void showFieldError(String field, String message) {
        if ("login".equals(field)) {
            lbl_err_login.setText(message);
            txt_lg.setBorder(BorderFactory.createLineBorder(new Color(222, 112, 112), 2));
        } else if ("password".equals(field)) {
            lbl_err_pass.setText(message);
            txt_pass.setBorder(BorderFactory.createLineBorder(new Color(222, 112, 112), 2));
        } else {
            lbl_err_global.setText(message);
        }
    }

    // API Controller -> View
    public void showFieldErrors(Map<String, String> errors) {
        if (errors == null || errors.isEmpty()) return;

        lbl_err_login.setText(errors.getOrDefault("login", ""));
        lbl_err_pass.setText(errors.getOrDefault("password", ""));
        lbl_err_global.setText(errors.getOrDefault("_global", ""));

        txt_lg.setBorder(errors.containsKey("login")
                ? BorderFactory.createLineBorder(new Color(222, 112, 112), 2)
                : UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));

        txt_pass.setBorder(errors.containsKey("password")
                ? BorderFactory.createLineBorder(new Color(222, 112, 112), 2)
                : UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
    }

    public void clearErrors() {
        // Vider les Labels d'erreurs
        lbl_err_login.setText("");
        lbl_err_pass.setText("");
        lbl_err_global.setText("");

        // Réinitialiser les bords des textFields
        txt_lg.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        txt_pass.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
    }

    public void showGlobalError(String message) {
        if (lbl_err_global != null) {
            lbl_err_global.setText(message);
        }
    }

    // UI Builders ——————————————————————————————————————————————————————————————————————————
    // UI Header Builder (Logo) ———————————————————————————————————————————————————————————
    private JPanel buildHeader() {
        // Charger le logo depuis le chemin absolu
        ImageIcon logoIcon = loadLogoFromPath("C:\\Users\\marou\\Desktop\\New folder\\DentiCare logo png.png", 80, 80);
        
        JLabel lblLogo = new JLabel(logoIcon);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(20, 0, 10, 0));
        header.add(Box.createVerticalGlue());
        header.add(lblLogo);
        header.add(Box.createVerticalGlue());

        return header;
    }

    // Méthode pour charger le logo depuis un chemin absolu
    private ImageIcon loadLogoFromPath(String path, int width, int height) {
        try {
            File file = new File(path);
            if (file.exists()) {
                ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
                Image img = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                // Si le fichier n'existe pas, créer un placeholder circulaire comme dans la maquette
                return createCircularPlaceholder(width, height);
            }
        } catch (Exception e) {
            // En cas d'erreur, créer un placeholder
            return createCircularPlaceholder(width, height);
        }
    }

    // Créer un placeholder circulaire comme dans la maquette HTML
    private ImageIcon createCircularPlaceholder(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fond circulaire
        g2.setColor(new Color(244, 247, 246)); // #f4f7f6
        g2.fillOval(0, 0, width, height);
        
        // Bordure
        g2.setColor(new Color(52, 152, 219)); // #3498db
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, width - 2, height - 2);
        
        // Texte "LOGO"
        g2.setColor(new Color(52, 152, 219));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        FontMetrics fm = g2.getFontMetrics();
        String text = "LOGO";
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, x, y);
        
        g2.dispose();
        return new ImageIcon(img);
    }

    // UI Form Builder —————————————————————————————————————————————————————————————————————
    private JPanel buildCenterForm() {
        // Titre "S'authentifier"
        JLabel lblTitle = new JLabel("S'authentifier");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(44, 62, 80)); // #2c3e50
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Ligne LOGIN (label + champ)
        JLabel lblLoginLabel = new JLabel("Identifiant");
        lblLoginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLoginLabel.setForeground(new Color(85, 85, 85)); // #555
        lblLoginLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblLoginLabel.setBorder(new EmptyBorder(0, 0, 8, 0));

        txt_lg = new CustomTextField("Utilisateur");
        txt_lg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt_lg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt_lg.setPreferredSize(new Dimension(300, 40));
        // CustomTextField gère déjà le placeholder et le style

        // Erreur login
        lbl_err_login = new JLabel("");
        lbl_err_login.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_err_login.setForeground(new Color(222, 112, 112));
        lbl_err_login.setBorder(new EmptyBorder(4, 0, 0, 0));
        lbl_err_login.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Ligne PASSWORD (label + champ)
        JLabel lblPassLabel = new JLabel("Mot de passe");
        lblPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassLabel.setForeground(new Color(85, 85, 85)); // #555
        lblPassLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPassLabel.setBorder(new EmptyBorder(20, 0, 8, 0));

        txt_pass = new CustomPasswordField("********");
        txt_pass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt_pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt_pass.setPreferredSize(new Dimension(300, 40));
        // CustomPasswordField gère déjà le placeholder et le style

        // Erreur pass
        lbl_err_pass = new JLabel("");
        lbl_err_pass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_err_pass.setForeground(new Color(222, 112, 112));
        lbl_err_pass.setBorder(new EmptyBorder(4, 0, 0, 0));
        lbl_err_pass.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Erreur globale
        lbl_err_global = new JLabel("");
        lbl_err_global.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_err_global.setForeground(new Color(222, 112, 112));
        lbl_err_global.setBorder(new EmptyBorder(16, 0, 0, 0));
        lbl_err_global.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel central
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(20, 40, 20, 40));
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(lblTitle);
        center.add(Box.createVerticalStrut(10));
        center.add(lblLoginLabel);
        center.add(txt_lg);
        center.add(lbl_err_login);
        center.add(lblPassLabel);
        center.add(txt_pass);
        center.add(lbl_err_pass);
        center.add(lbl_err_global);

        return center;
    }

    // UI Footer Builder (Bouton Connexion) ———————————————————————————————————————————————
    private JPanel buildFooterButtons() {
        // Créer une icône pour le bouton de connexion (ou utiliser ImageTools si disponible)
        ImageIcon loginIcon = null;
        try {
            // Essayer de charger une icône depuis les ressources
            loginIcon = ImageTools.loadIcon("/static/icons/connect.png", 40, 40);
        } catch (Exception e) {
            // Si l'icône n'existe pas, créer une icône simple
            BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(52, 152, 219));
            g2.fillOval(5, 5, 30, 30);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            g2.drawString("✓", 12, 28);
            g2.dispose();
            loginIcon = new ImageIcon(img);
        }

        // Utiliser MyButton comme dans la logique du prof
        var buttonFont = new Font("Segoe UI", Font.BOLD, 16);
        btLogin = new MyButton("Connexion", loginIcon, buttonFont);
        btLogin.addActionListener(this::loginAction);
        btLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btLogin.setPreferredSize(new Dimension(300, 50));

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(10, 40, 30, 40));
        footer.add(btLogin);

        return footer;
    }

    // Root Panel Builder —————————————————————————————————————————————————————————————————
    private JPanel buildRoot() {
        // Fond sombre comme dans la maquette (#2c3e50)
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(44, 62, 80)); // #2c3e50

        // Carte blanche centrée (comme .login-card dans la maquette)
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(380, Integer.MAX_VALUE));

        // Ajouter les sections
        card.add(buildHeader());
        card.add(buildCenterForm());
        card.add(buildFooterButtons());

        // Centrer la carte dans le root
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);

        root.add(wrapper, BorderLayout.CENTER);

        return root;
    }

    // —————————————— KEY BINDINGS (BONNE PRATIQUE SWING)
    private void installKeyBindings() {
        // InputMap du RootPane → actif partout dans la fenêtre
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        // Association de la touche ENTER à l'action "LOGIN"
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "LOGIN");
        // Association de la touche Escape à l'action "CANCEL"
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CANCEL");

        // Action exécutée quand ENTER est pressée
        actionMap.put("LOGIN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction(e);
            }
        });
        // Action exécutée quand ESCAPE est pressée
        actionMap.put("CANCEL", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fermer l'application
            }
        });
    }
}
