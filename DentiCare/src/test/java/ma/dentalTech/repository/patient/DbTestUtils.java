package ma.dentalTech.repository.patient;

import ma.dentalTech.conf.SessionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DbTestUtils {
    private DbTestUtils(){}

    /** Nettoie les 3 tables dans l'ordre FK (jointure -> feuilles) */
    public static void cleanAll() {
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement()) {

            // On coupe temporairement les FK pour pouvoir vider tranquillement
            st.execute("SET FOREIGN_KEY_CHECKS = 0");

            st.executeUpdate("DELETE FROM Patient_Antecedents");
            st.executeUpdate("DELETE FROM Patients");
            st.executeUpdate("DELETE FROM Antecedents");

            // On remet les compteurs à 1 pour garantir les IDs (1..12) sur Antecedents
            st.execute("ALTER TABLE Patients AUTO_INCREMENT = 1");
            st.execute("ALTER TABLE Antecedents AUTO_INCREMENT = 1");

            st.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException("Nettoyage BD échoué", e);
        }
    }

    /**
     * Seed complet en une passe : Antecedents (IDs 1..12 par ordre d'insertion),
     * Patients (IDs fixés 1..6), puis liaisons dans Patient_Antecedents.
     */
    public static void seedFullDataset() {
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement st = c.createStatement()) {

            // ====== Antecedents : SANS id explicite (idempotent si cleanAll() a été appelé) ======
            st.executeUpdate("""
                INSERT INTO Antecedents (nom, categorie, niveauRisque) VALUES
                -- Allergies
                ('Allergie à la pénicilline', 'ALLERGIE', 'CRITIQUE'),
                ('Allergie au latex', 'ALLERGIE', 'ELEVE'),
                ('Allergie aux anesthésiques locaux', 'ALLERGIE', 'CRITIQUE'),

                -- Maladies chroniques
                ('Diabète de type 2', 'MALADIE_CHRONIQUE', 'MODERE'),
                ('Hypertension artérielle', 'MALADIE_CHRONIQUE', 'MODERE'),
                ('Asthme', 'MALADIE_CHRONIQUE', 'ELEVE'),

                -- Contre-indications ou traitements
                ('Sous traitement anticoagulant', 'CONTRE_INDICATION', 'ELEVE'),
                ('Grossesse', 'CONTRE_INDICATION', 'MODERE'),

                -- Antécédents chirurgicaux ou infectieux
                ('Prothèse valvulaire cardiaque', 'ANTECEDENT_CHIRURGICAL', 'ELEVE'),
                ('Hépatite B ancienne', 'ANTECEDENT_INFECTIEUX', 'MODERE'),

                -- Habitudes de vie
                ('Tabagisme chronique', 'HABITUDE_DE_VIE', 'MODERE'),
                ('Alcoolisme', 'HABITUDE_DE_VIE', 'ELEVE');
            """);

            // ====== Patients : IDs fixés ======
            st.executeUpdate("""
                INSERT INTO Patients
                (id, nom, prenom, adresse, telephone, email, dateNaissance, dateCreation, sexe, assurance)
                VALUES
                    (1, 'Amal',    'Zahra',    'Rabat',       '0611111111', 'amal@example.com',    '1995-05-12', '2025-10-25 21:35:39', 'Femme', 'CNSS'),
                    (2, 'Omar',    'Badr',     'Salé',        '0622222222', 'omar@example.com',    '1989-09-23', '2025-10-25 20:40:39', 'Homme', 'CNOPS'),
                    (3, 'Nour',    'Chafi',    'Témara',      '0633333333', 'nour@example.com',    '2000-02-02', '2025-10-24 21:10:39', 'Femme', 'Autre'),
                    (4, 'Youssef', 'Dari',     'Kénitra',     '0644444444', 'youssef@example.com', '1992-11-01', '2025-10-23 21:40:39', 'Homme', 'Aucune'),
                    (5, 'Hiba',    'Zerouali', 'Rabat',       '0655555555', 'hiba@example.com',    '2001-03-14', '2025-10-26 10:00:00', 'Femme', 'CNSS'),
                    (6, 'Mahdi',   'ElMidaoui','Casablanca',  '0666666666', 'mahdi@example.com',   '1990-07-18', '2025-10-26 10:05:00', 'Homme', 'Autre');
            """);

            // ====== Liaisons (IDs d'antécédents supposés 1..12 selon l'ordre d'insertion ci-dessus) ======
            // Patient 1 : Amal (diabète id=4, allergie latex id=2)
            st.executeUpdate("INSERT INTO Patient_Antecedents (patient_id, antecedent_id) VALUES (1, 2), (1, 4)");

            // Patient 2 : Omar (HTA id=5, tabagisme id=11, allergie latex id=2)
            st.executeUpdate("INSERT INTO Patient_Antecedents (patient_id, antecedent_id) VALUES (2, 5), (2, 11), (2, 2)");

            // Patient 3 : Nour (grossesse id=8, allergie pénicilline id=1)
            st.executeUpdate("INSERT INTO Patient_Antecedents (patient_id, antecedent_id) VALUES (3, 1), (3, 8)");

            // Patient 4 : Youssef (prothèse valvulaire cardiaque id=9)
            st.executeUpdate("INSERT INTO Patient_Antecedents (patient_id, antecedent_id) VALUES (4, 9)");

        } catch (SQLException e) {
            throw new RuntimeException("Seed complet échoué", e);
        }
    }
}
