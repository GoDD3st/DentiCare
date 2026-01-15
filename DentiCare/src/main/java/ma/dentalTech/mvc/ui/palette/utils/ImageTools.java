package ma.dentalTech.mvc.ui.palette.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import ma.dentalTech.conf.ApplicationContext;

public class ImageTools {

    public static String[] getEnumNames(Class<? extends Enum<?>> enumClass) {
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            throw new IllegalArgumentException("La classe fournie n'est pas une énumération.");
        }

        String[] names = new String[enumConstants.length];
        for (int i = 0; i < enumConstants.length; i++) {
            names[i] = enumConstants[i].name();
        }

        return names;
    }

    private static final String ICONS_FOLDER = "C:\\Users\\marou\\Desktop\\New folder\\icons\\";

    public static ImageIcon loadIcon(String path, int w, int h) {
        // Essayer d'abord de charger depuis le dossier d'icônes personnalisé
        ImageIcon customIcon = loadFromCustomFolder(path, w, h);
        if (customIcon != null) {
            return customIcon;
        }

        // Sinon, essayer depuis les ressources du classpath
        var url = ImageTools.class.getResource(path);
        if (url != null) {
            Image img = new ImageIcon(url).getImage()
                    .getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }

        // Sinon, créer une icône intégrée spécifique
        ImageIcon builtInIcon = createBuiltInIcon(path, w, h);
        if (builtInIcon != null) {
            return builtInIcon;
        }

        // Enfin, créer une icône par défaut
        return createDefaultIcon(w, h);
    }

    /**
     * Crée une icône par défaut quand la ressource n'existe pas.
     * Crée un cercle avec un point d'interrogation.
     */
    private static ImageIcon createDefaultIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond circulaire gris
        g2.setColor(new Color(200, 200, 200));
        g2.fillOval(0, 0, w, h);

        // Bordure
        g2.setColor(new Color(150, 150, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, w - 2, h - 2);

        // Point d'interrogation blanc
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(8, w / 3)));
        FontMetrics fm = g2.getFontMetrics();
        String text = "?";
        int x = (w - fm.stringWidth(text)) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, x, y);

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Essaie de charger une icône depuis le dossier personnalisé.
     */
    private static ImageIcon loadFromCustomFolder(String path, int w, int h) {
        try {
            // Extraire le nom de fichier du path (sans les dossiers)
            String fileName = extractFileName(path);

            // Construire le chemin complet vers l'icône personnalisée
            String customPath = ICONS_FOLDER + fileName;

            // Vérifier si le fichier existe
            java.io.File iconFile = new java.io.File(customPath);
            if (iconFile.exists()) {
                ImageIcon icon = new ImageIcon(customPath);
                if (icon.getIconWidth() > 0) { // Vérifier que l'image est valide
                    Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    return new ImageIcon(img);
                }
            }
        } catch (Exception e) {
            // En cas d'erreur, continuer avec les autres méthodes
        }
        return null;
    }

    /**
     * Extrait le nom de fichier d'un chemin (gère les différents formats de path).
     */
    private static String extractFileName(String path) {
        if (path == null) return "";

        // Supprimer les barres obliques du début
        path = path.replaceAll("^/+", "");

        // Gérer les mappings personnalisés selon les types d'icônes
        if (path.contains("session") || path.contains("menu")) {
            return "menu.png";
        } else if (path.contains("logout") || path.contains("exit")) {
            return "menu.png"; // Réutiliser menu.png pour les actions menu
        } else if (path.contains("notification") || path.contains("notif")) {
            return "notif.png";
        } else if (path.contains("connect") || path.contains("login")) {
            return "user.png"; // Utiliser user.png pour login
        } else if (path.contains("dashboard")) {
            return "dashboard.png";
        } else if (path.contains("patient") || path.contains("profile") || path.contains("user")) {
            return "user.png";
        } else if (path.contains("calendar") || path.contains("agenda")) {
            return "menu.png"; // Fallback
        } else if (path.contains("cabinet")) {
            return "cabinet.png";
        } else if (path.contains("finance") || path.contains("caisse")) {
            return "menu.png"; // Fallback
        } else if (path.contains("consultation")) {
            return "cabinet.png"; // Fallback médical
        }

        // Par défaut, essayer d'extraire le dernier segment du path
        int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (lastSlash >= 0 && lastSlash < path.length() - 1) {
            return path.substring(lastSlash + 1);
        }

        return path;
    }


    public static ImageIcon resizeImageIcon(ImageIcon originalIcon, int newWidth, int newHeight) {

        Image originalImage = originalIcon.getImage(); // Récupérer l'image de l'ImageIcon
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH); // Redimensionner
        return new ImageIcon(resizedImage); // Retourner une nouvelle ImageIcon
    }

    public static ImageIcon loadAvatarFromProfilePath(String storedPath, int w, int h) {
        if (storedPath == null || storedPath.isBlank()) return null;

        // si absolu
        java.nio.file.Path p = java.nio.file.Paths.get(storedPath);
        if (p.isAbsolute() && java.nio.file.Files.exists(p)) {
            return new ImageIcon(new ImageIcon(p.toString()).getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH));
        }

        // relatif: on lit profile.avatars.dir via ApplicationContext
        String dir = ApplicationContext.getInstance()
                .getProperty("profile.avatars.dir", "data/avatars");

        String fn = storedPath.replace("\\", "/");
        if (fn.contains("/")) fn = fn.substring(fn.lastIndexOf('/') + 1);

        java.nio.file.Path file = java.nio.file.Paths.get(dir).resolve(fn);
        if (!java.nio.file.Files.exists(file)) return null;

        return new ImageIcon(new ImageIcon(file.toString()).getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH));
    }


    public static Image scaleImage(Image source, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, 0, 0, width, height, null);
        g.dispose();
        return img;
    }
    public static Image scaleImage(Image source, int size) {
        int width = source.getWidth(null);
        int height = source.getHeight(null);
        double f = 0;
        if (width < height) {//portrait
            f = (double) height / (double) width;
            width = (int) (size / f);
            height = size;
        } else {//paysage
            f = (double) width / (double) height;
            width = size;
            height = (int) (size / f);
        }
        return scaleImage(source, width, height);
    }

    //avec un facteur (<1 pour rétrécir, >1 pour agrandir):
    public static Image scaleImage(final Image source, final double factor) {
        int width = (int) (source.getWidth(null) * factor);
        int height = (int) (source.getHeight(null) * factor);
        return scaleImage(source, width, height);
    }



    public static Color getDominantColor(ImageIcon icon) {
        // Gérer le cas où l'icône est null
        if (icon == null) {
            return new Color(52, 152, 219); // Couleur bleue par défaut
        }

        // Convertir l'ImageIcon en BufferedImage
        Image img = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        // Analyser les pixels pour déterminer la couleur dominante
        int red = 0, green = 0, blue = 0, pixelCount = 0;

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color color = new Color(rgb);

                // Ignorer les pixels très sombres ou très clairs (optionnel)
                if (isSignificantColor(color)) {
                    red += color.getRed();
                    green += color.getGreen();
                    blue += color.getBlue();
                    pixelCount++;
                }
            }
        }

        // Éviter la division par zéro
        if (pixelCount == 0) return Color.GRAY;

        // Calculer la couleur moyenne
        red /= pixelCount;
        green /= pixelCount;
        blue /= pixelCount;

        // Augmenter la luminosité de la couleur (ajuster selon vos besoins)
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
        hsb[2] = Math.min(1.0f, hsb[2] + 0.2f); // Augmenter la luminosité (Value)
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    private static boolean isSignificantColor(Color color) {
        // Ignorer les couleurs trop sombres ou trop claires
        int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
        return brightness > 30 && brightness < 130;
    }

    public static ImageIcon applyColorOverlay(ImageIcon icon, Color overlayColor, float alpha) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) return icon;

        // Clamp alpha to [0..1]
        alpha = Math.max(0f, Math.min(1f, alpha));

        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        BufferedImage src = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = src.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(icon.getImage(), 0, 0, null);

        g.setComposite(AlphaComposite.SrcAtop.derive(alpha));
        g.setColor(overlayColor);
        g.fillRect(0, 0, w, h);

        g.dispose();
        return new ImageIcon(src);
    }

    /**
     * Crée une icône intégrée spécifique selon le chemin demandé.
     */
    private static ImageIcon createBuiltInIcon(String path, int w, int h) {
        if (path.contains("session")) {
            return createSessionIcon(w, h);
        } else if (path.contains("logout")) {
            return createLogoutIcon(w, h);
        } else if (path.contains("exit")) {
            return createExitIcon(w, h);
        } else if (path.contains("notification")) {
            return createNotificationIcon(w, h);
        } else if (path.contains("connect")) {
            return createLoginIcon(w, h);
        } else if (path.contains("dashboard")) {
            return createDashboardIcon(w, h);
        } else if (path.contains("patient")) {
            return createPatientIcon(w, h);
        } else if (path.contains("calendar") || path.contains("agenda")) {
            return createCalendarIcon(w, h);
        } else if (path.contains("cabinet")) {
            return createCabinetIcon(w, h);
        } else if (path.contains("finance") || path.contains("caisse")) {
            return createFinanceIcon(w, h);
        } else if (path.contains("consultation")) {
            return createConsultationIcon(w, h);
        } else if (path.contains("profile")) {
            return createProfileIcon(w, h);
        }
        return null;
    }

    // Icônes intégrées spécifiques
    private static ImageIcon createSessionIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(52, 152, 219));
        g2.setStroke(new BasicStroke(2.5f));

        // Symbole de session (personne stylisée)
        int centerX = w / 2;
        int centerY = h / 2;

        // Tête
        g2.drawOval(centerX - 4, centerY - 8, 8, 8);
        // Corps
        g2.drawLine(centerX, centerY, centerX, centerY + 6);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createLogoutIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(231, 76, 60));
        g2.setStroke(new BasicStroke(2.5f));

        // Flèche vers la gauche (déconnexion)
        g2.drawLine(w - 4, h/2, 4, h/2); // ligne horizontale
        g2.drawLine(7, h/2 - 3, 4, h/2); // pointe gauche haut
        g2.drawLine(7, h/2 + 3, 4, h/2); // pointe gauche bas

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createExitIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(149, 165, 166));
        g2.setStroke(new BasicStroke(2.5f));

        // Croix (X)
        g2.drawLine(4, 4, w-4, h-4);
        g2.drawLine(w-4, 4, 4, h-4);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createNotificationIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(52, 152, 219));

        // Cloche de notification
        int centerX = w / 2;
        g2.fillOval(centerX - 6, 4, 12, 12); // partie supérieure
        g2.fillRect(centerX - 6, 10, 12, 8); // partie inférieure
        g2.fillRect(centerX - 1, 14, 2, 4); // battant

        // Point rouge pour notification non lue
        g2.setColor(new Color(231, 76, 60));
        g2.fillOval(centerX + 2, 2, 4, 4);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createLoginIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Flèche droite stylisée pour "connexion"
        g2.setColor(new Color(52, 152, 219));
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // Corps de la flèche
        g2.drawLine(4, h/2, w-8, h/2);
        // Pointe de la flèche
        g2.drawLine(w-11, h/2-3, w-4, h/2);
        g2.drawLine(w-11, h/2+3, w-4, h/2);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createDashboardIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(46, 204, 113));
        g2.setStroke(new BasicStroke(2f));

        // Grille de dashboard (4 carrés)
        int size = Math.min(w, h) / 4;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                g2.drawRect(i * size + 2, j * size + 2, size - 4, size - 4);
            }
        }

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createPatientIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(155, 89, 182));
        g2.setStroke(new BasicStroke(2f));

        // Symbole patient (personne avec croix médicale)
        int centerX = w / 2;
        int centerY = h / 2;

        // Corps
        g2.drawOval(centerX - 3, centerY - 6, 6, 6);
        g2.drawLine(centerX, centerY, centerX, centerY + 4);

        // Croix médicale
        g2.setColor(new Color(231, 76, 60));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(centerX - 2, centerY + 6, centerX + 2, centerY + 6);
        g2.drawLine(centerX, centerY + 4, centerX, centerY + 8);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createCalendarIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(230, 126, 34));
        g2.setStroke(new BasicStroke(2f));

        // Calendrier
        g2.drawRect(2, 4, w-4, h-6);
        g2.drawLine(2, 10, w-2, 10); // ligne de séparation header

        // Points pour les jours
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                g2.fillOval(6 + i*6, 14 + j*6, 2, 2);
            }
        }

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createCabinetIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(44, 62, 80));
        g2.setStroke(new BasicStroke(2f));

        // Bâtiment stylisé
        g2.drawRect(3, 8, w-6, h-10);
        g2.drawLine(3, 12, w-3, 12); // toit

        // Fenêtres
        g2.setColor(new Color(52, 152, 219));
        g2.fillRect(5, 14, 4, 4);
        g2.fillRect(w-9, 14, 4, 4);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createFinanceIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(39, 174, 96));
        g2.setStroke(new BasicStroke(2f));

        // Symbole dollar/euro
        g2.drawLine(w/2, 4, w/2, h-4); // ligne verticale
        g2.drawOval(w/2 - 3, 4, 6, 6); // cercle haut
        g2.drawOval(w/2 - 3, h-10, 6, 6); // cercle bas

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createConsultationIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(142, 68, 173));
        g2.setStroke(new BasicStroke(2f));

        // Stéthoscope stylisé
        g2.drawOval(4, 6, 8, 8); // écouteur gauche
        g2.drawOval(w-12, 6, 8, 8); // écouteur droit
        g2.drawLine(8, 10, w-8, 10); // tube
        g2.drawOval(w/2 - 4, 12, 8, 8); // partie basse

        g2.dispose();
        return new ImageIcon(img);
    }

    private static ImageIcon createProfileIcon(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(52, 152, 219));
        g2.setStroke(new BasicStroke(2f));

        // Profil utilisateur (tête avec épaules)
        g2.drawOval(w/2 - 6, 2, 12, 12); // tête
        g2.drawLine(w/2 - 8, 10, w/2 + 8, 10); // épaules
        g2.drawLine(w/2 - 8, 10, w/2 - 4, 16); // épaule gauche
        g2.drawLine(w/2 + 8, 10, w/2 + 4, 16); // épaule droite

        g2.dispose();
        return new ImageIcon(img);
    }


}
