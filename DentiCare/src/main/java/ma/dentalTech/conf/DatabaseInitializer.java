package ma.dentalTech.conf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Utilitaire pour initialiser automatiquement la base de données
 * lors du premier lancement de l'application
 */
public class DatabaseInitializer {

    private static final String SCHEMA_FILE = "dataBase/schema.sql";
    private static final String SEED_FILE = "dataBase/seed.sql";

    /**
     * Vérifie si la base de données est initialisée et l'initialise si nécessaire
     */
    public static void initializeIfNeeded(Connection conn) {
        try {
            System.out.println("=== VÉRIFICATION BASE DE DONNÉES ===");

            // Vérifier si les tables existent
            if (!tablesExist(conn)) {
                System.out.println("Tables non trouvées, initialisation de la base de données...");

                // Créer les tables
                createTables(conn);

                // Insérer les données de base
                seedDatabase(conn);

                System.out.println("✓ Base de données initialisée avec succès !");
            } else {
                System.out.println("✓ Base de données déjà initialisée");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation de la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Vérifie si les tables principales existent
     */
    private static boolean tablesExist(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Tester quelques tables principales
            stmt.executeQuery("SELECT 1 FROM cabinet_medical LIMIT 1");
            stmt.executeQuery("SELECT 1 FROM patient LIMIT 1");
            stmt.executeQuery("SELECT 1 FROM utilisateur LIMIT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Crée les tables à partir du fichier schema.sql
     */
    private static void createTables(Connection conn) throws Exception {
        System.out.println("Création des tables...");
        String schemaSql = loadSqlFile(SCHEMA_FILE);

        try (Statement stmt = conn.createStatement()) {
            // Diviser le fichier SQL en statements individuels
            String[] statements = schemaSql.split(";");

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    try {
                        stmt.execute(statement);
                    } catch (Exception e) {
                        // Ignorer les erreurs sur les tables qui existent déjà
                        if (!e.getMessage().contains("already exists")) {
                            System.err.println("Erreur SQL: " + statement);
                            throw e;
                        }
                    }
                }
            }
        }
        System.out.println("✓ Tables créées");
    }

    /**
     * Insère les données de base à partir du fichier seed.sql
     */
    private static void seedDatabase(Connection conn) throws Exception {
        System.out.println("Insertion des données de base...");
        String seedSql = loadSqlFile(SEED_FILE);

        try (Statement stmt = conn.createStatement()) {
            // Diviser le fichier SQL en statements individuels
            String[] statements = seedSql.split(";");

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    try {
                        stmt.execute(statement);
                    } catch (Exception e) {
                        // Certaines instructions peuvent échouer si les données existent déjà
                        System.out.println("Note: " + e.getMessage().split("\n")[0]);
                    }
                }
            }
        }
        System.out.println("✓ Données insérées");
    }

    /**
     * Charge un fichier SQL depuis les ressources
     */
    private static String loadSqlFile(String fileName) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    DatabaseInitializer.class.getClassLoader().getResourceAsStream(fileName),
                    StandardCharsets.UTF_8))) {

            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}