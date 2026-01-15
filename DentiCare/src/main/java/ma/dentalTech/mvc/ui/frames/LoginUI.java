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
import ma.dentalTech.mvc.ui.palette.utils.HeaderWindowControls;
import ma.dentalTech.mvc.ui.palette.alert.Alert;

public class LoginUI extends JFrame {

    private final LoginController controller;

    private CustomTextField txt_lg;
    private CustomPasswordField txt_pass;

    private JLabel lbl_err_login, lbl_err_pass;
    private JTextArea lbl_err_global;

    // Bouton Login (référence nécessaire pour KeyBinding)
    private MyButton btLogin;

    public LoginUI(LoginController controller) {
        this.controller = controller;

        setSize(800, 900); // Optimisé pour résolution 3200x1800
        setLocationRelativeTo(null);
        setResizable(true); // Permettre le redimensionnement
        setUndecorated(true); // Garder undecorated pour la barre personnalisée

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
            lbl_err_global.setToolTipText(message);
        }
    }

    // API Controller -> View
    public void showFieldErrors(Map<String, String> errors) {
        if (errors == null || errors.isEmpty()) return;

        lbl_err_login.setText(errors.getOrDefault("login", ""));
        lbl_err_pass.setText(errors.getOrDefault("password", ""));
        String globalError = errors.getOrDefault("_global", "");
        lbl_err_global.setText(globalError);
        lbl_err_global.setToolTipText(globalError.isEmpty() ? null : globalError);

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
        lbl_err_global.setToolTipText(null);

        // Réinitialiser les bords des textFields
        txt_lg.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        txt_pass.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
    }

    public void showGlobalError(String message) {
        if (lbl_err_global != null) {
            lbl_err_global.setText(message);
            lbl_err_global.setToolTipText(message); // Tooltip pour voir le texte complet au survol
        }
    }

    // UI Builders ——————————————————————————————————————————————————————————————————————————
    // UI Header Builder (Logo + Texte) ——————————————————————————————————————————————————
    private JPanel buildHeader() {
        // Charger le logo depuis le chemin absolu
        ImageIcon logoIcon = loadLogoFromPath("C:\\Users\\marou\\Desktop\\New folder\\DentiCare logo png.png", 60, 60);

        // Logo
        JLabel lblLogo = new JLabel(logoIcon);
        lblLogo.setBorder(new EmptyBorder(0, 0, 0, 15)); // Espace à droite du logo

        // Texte DentiCare avec slogan
        JLabel lblTitle = new JLabel("<html><center>DentiCare<br/><span style='font-size: 10px; color: #7f8c8d;'>Le lien de votre sourire</span></center></html>");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80)); // #2c3e50

        // Panel horizontal pour logo + texte
        JPanel headerContent = new JPanel();
        headerContent.setOpaque(false);
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.X_AXIS));
        headerContent.add(lblLogo);
        headerContent.add(lblTitle);

        // Panel principal centré
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(20, 0, 15, 0));
        header.add(Box.createVerticalGlue());
        header.add(headerContent);
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
        lblLoginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLoginLabel.setBorder(new EmptyBorder(0, 0, 8, 0));

        txt_lg = new CustomTextField("Utilisateur");
        txt_lg.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt_lg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt_lg.setPreferredSize(new Dimension(500, 50));
        txt_lg.setAlignmentX(Component.CENTER_ALIGNMENT);
        // CustomTextField gère déjà le placeholder et le style

        // Erreur login
        lbl_err_login = new JLabel("");
        lbl_err_login.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_err_login.setForeground(new Color(222, 112, 112));
        lbl_err_login.setBorder(new EmptyBorder(4, 0, 0, 0));
        lbl_err_login.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ligne PASSWORD (label + champ)
        JLabel lblPassLabel = new JLabel("Mot de passe");
        lblPassLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassLabel.setForeground(new Color(85, 85, 85)); // #555
        lblPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPassLabel.setBorder(new EmptyBorder(20, 0, 8, 0));

        txt_pass = new CustomPasswordField("********");
        txt_pass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txt_pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt_pass.setPreferredSize(new Dimension(500, 50));
        txt_pass.setAlignmentX(Component.CENTER_ALIGNMENT);
        // CustomPasswordField gère déjà le placeholder et le style

        // Erreur pass
        lbl_err_pass = new JLabel("");
        lbl_err_pass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_err_pass.setForeground(new Color(222, 112, 112));
        lbl_err_pass.setBorder(new EmptyBorder(4, 0, 0, 0));
        lbl_err_pass.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Erreur globale (utiliser JTextArea pour permettre le texte multiligne)
        lbl_err_global = new JTextArea("");
        lbl_err_global.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl_err_global.setForeground(new Color(222, 112, 112));
        lbl_err_global.setBorder(new EmptyBorder(16, 0, 0, 0));
        lbl_err_global.setOpaque(false); // Fond transparent
        lbl_err_global.setEditable(false); // Non éditable
        lbl_err_global.setWrapStyleWord(true); // Coupure des mots
        lbl_err_global.setLineWrap(true); // Retour à la ligne automatique
        lbl_err_global.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl_err_global.setMaximumSize(new Dimension(300, 60)); // Limiter la hauteur maximale

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
        // Créer une icône pour le bouton de connexion
        ImageIcon loginIcon = createLoginIcon();

        // Utiliser MyButton comme dans la logique du prof
        var buttonFont = new Font("Segoe UI", Font.BOLD, 16);
        btLogin = new MyButton("Connexion", loginIcon, buttonFont);
        btLogin.addActionListener(this::loginAction);
        btLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btLogin.setPreferredSize(new Dimension(500, 60));
        btLogin.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer le bouton

        JPanel footer = new JPanel(new GridBagLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 15, 0); // Espacement entre les boutons
        footer.add(btLogin, gbc);

        // Bouton Quitter
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        MyButton btQuit = new MyButton("Quitter l'application", null, buttonFont);
        btQuit.addActionListener(e -> {
            boolean ok = Alert.confirm(this, "Voulez-vous vraiment quitter l'application ?");
            if (ok) {
                System.exit(0);
            }
        });
        btQuit.setPreferredSize(new Dimension(500, 45));
        btQuit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.add(btQuit, gbc);

        return footer;
    }

    // Créer une icône personnalisée pour le bouton de connexion
    private ImageIcon createLoginIcon() {
        BufferedImage img = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Flèche droite stylisée pour "connexion"
        g2.setColor(new Color(52, 152, 219)); // Bleu
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Corps de la flèche
        g2.drawLine(4, 12, 16, 12);
        // Pointe de la flèche
        g2.drawLine(13, 7, 16, 12);
        g2.drawLine(13, 17, 16, 12);

        g2.dispose();
        return new ImageIcon(img);
    }

    // Root Panel Builder —————————————————————————————————————————————————————————————————
    private JPanel buildRoot() {
        // Panel principal avec BorderLayout
        JPanel root = new JPanel(null); // Layout null pour positionnement absolu
        root.setBackground(new Color(44, 62, 80)); // #2c3e50

        // Pas de contrôles de fenêtre pour LoginUI - on ajoutera un bouton Quitter

        // Contenu principal (fond sombre comme dans la maquette #2c3e50)
        JPanel content = new JPanel(new BorderLayout(0, 0));
        content.setBackground(new Color(44, 62, 80)); // #2c3e50
        content.setBounds(0, 40, 800, 860); // Laisser de l'espace pour les contrôles

        // Carte blanche centrée (comme .login-card dans la maquette)
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(40, 40, 40, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Ajouter les sections
        card.add(buildHeader());
        card.add(buildCenterForm());
        card.add(buildFooterButtons());

        // Centrer la carte dans le contenu
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(card);

        content.add(wrapper, BorderLayout.CENTER);
        root.add(content);

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
