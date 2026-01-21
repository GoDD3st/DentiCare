package ma.dentalTech.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import ma.dentalTech.conf.util.PropertiesExtractor;

public final class SessionFactory {


    private static volatile SessionFactory INSTANCE;

    /** Objet connexion JDBC unique */
    private Connection connection;

    /** Propriétés de configuration (fichier .properties) */
    private static final String PROPS_PATH = "config/db.properties"; // pour la mise a jour prochaine de BD
    private static final String URL_KEY    = "datasource.url";
    private static final String USER_KEY   = "datasource.user";
    private static final String PASS_KEY   = "datasource.password";
    private static final String DRIVER_KEY = "datasource.driver";

    /** Valeurs lues depuis le fichier de configuration */
    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    /**
     * Constructeur privé → empêche toute instanciation directe.
     * Initialise la configuration et le driver JDBC.
     */
    private SessionFactory() {
        var properties = PropertiesExtractor.loadConfigFile(PROPS_PATH);

        this.url      = PropertiesExtractor.getPropertyValue(URL_KEY, properties);
        this.user     = PropertiesExtractor.getPropertyValue(USER_KEY, properties);
        this.password = PropertiesExtractor.getPropertyValue(PASS_KEY, properties);
        this.driver   = PropertiesExtractor.getPropertyValue(DRIVER_KEY, properties);

        // Charger explicitement le driver si défini (bonne pratique)
        if (driver != null && !driver.isBlank()) {
            try {
                Class.forName(driver);
                System.out.println(" Driver JDBC chargé avec succès : " + driver);
            } catch (ClassNotFoundException e) {
                System.err.println(" Driver JDBC introuvable : " + driver);
            }
        }
    }



    /**
     * Retourne l'unique instance du Singleton.
     * Utilise le pattern "Double Checked Locking" pour être thread safe.
     * Car au moment de verrouillage, un autre Thread pourrait créer déjà l'instance
     */
    public static SessionFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (SessionFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionFactory();
                }
            }
        }
        return INSTANCE;
    }



    /**
     * Retourne une connexion JDBC active et valide.
     * Si la connexion n'existe pas, est fermée ou n'est plus valide, elle est recréée.
     *
     * @return une instance valide de {@link Connection}.
     * @throws SQLException en cas d'erreur de création.
     */
    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed() || !isValid(connection)) {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println(" Nouvelle connexion JDBC établie avec succès !");

            // Initialiser la base de données si nécessaire (premier lancement)
            initializeDatabaseIfNeeded();
        }
        return connection;
    }

    /**
     * Initialise automatiquement la base de données lors du premier accès
     */
    private void initializeDatabaseIfNeeded() {
        try {
            // Vérifier si c'est le premier lancement en testant une table
            try (Statement stmt = connection.createStatement()) {
                stmt.executeQuery("SELECT 1 FROM cabinet_medical LIMIT 1");
                // Si on arrive ici, les tables existent déjà
            } catch (Exception e) {
                // Les tables n'existent pas, on les crée
                System.out.println("Première utilisation détectée, initialisation de la base de données...");
                DatabaseInitializer.initializeIfNeeded(connection);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation automatique: " + e.getMessage());
        }
    }
    /**
     * Vérifie si la connexion est encore valide.
     *
     * @param conn la connexion à tester
     * @return true si la connexion est encore utilisable, sinon false
     */
    private boolean isValid(Connection conn) {
        try {
            return conn != null && conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }



    /**
     * Ferme proprement la connexion si elle est ouverte.
     * À appeler à la fermeture de l'application.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion JDBC fermée proprement.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }


}
