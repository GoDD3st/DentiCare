package ma.dentalTech.repository.testRepo;

import ma.dentalTech.entities.Acte.Acte;
import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Admin.Admin;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.Secretaire.Secretaire;
import ma.dentalTech.entities.SituationFinanciere.SituationFinanciere;
import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Notification.Notification;
import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.entities.Medicament.Medicament;
import ma.dentalTech.entities.Staff.Staff;
import ma.dentalTech.entities.Statistique.Statistique;
public class MasterTestRunner {

    public static void main(String[] args) {
        System.out.println("=== DENTICARE SYSTEM TEST RUNNER ===");

        testEntites();
    }

    public static void testEntites() {
        try {
            // 1. Test Patient
            Patient patient = Patient.createTestInstance();
            Acte acte = Acte.createTestInstance();
            Admin admin = Admin.createTestInstance();
            AgendaMensuel agenda = AgendaMensuel.createTestInstance();
            Antecedents antecedents = Antecedents.createTestInstance();

            System.out.println("✅ Entité Patient créée : " + patient.getNom());
            System.out.println("✅ Entité Acte créée : " + acte.getLibelle());
            System.out.println("✅ Entité Antecedents créée : " + antecedents.getNom());
            System.out.println("✅ Agenda mensuel créé pour mois : " + agenda.getMois());

            // 2. Test Medecin
            Medecin medecin = Medecin.createTestInstance();
            System.out.println("✅ Entité Medecin créée : " + medecin.getSpecialite());

            // 3. Test Dossier lié
            DossierMedicale dm = DossierMedicale.createTestInstance(patient);
            System.out.println("✅ Entité Dossier créée pour : " + dm.getPatient().getNom());

            // 4. Test Admin
            System.out.println("✅ Entité Admin créée avec login : " + admin.getLogin());

            // 3. Création du Dossier lié au Patient et au Médecin
            DossierMedicale dossier = DossierMedicale.createTestInstance(patient);
            dossier.setMedecin(medecin);
            System.out.println("✅ Dossier créé pour " + patient.getNom() + " avec Dr. " + medecin.getNom());

            // 4. Création d'une Consultation
            Consultation consultation = Consultation.createTestInstance(dossier);
            System.out.println("✅ Consultation enregistrée le " + consultation.getDateConsultation());

            // 5. Certificat et Ordonnance
            Certificat certificat = Certificat.createTestInstance(dossier, consultation);
            Ordonnance ordonnance = Ordonnance.createTestInstance();
            System.out.println("✅ Certificat créé, durée : " + certificat.getDuree() + " jours");
            System.out.println("✅ Ordonnance créée le : " + ordonnance.getDate());

            // 6. Situation financière et Facture
            SituationFinanciere situation = SituationFinanciere.createTestInstance();
            dossier.setSituationFinanciere(situation);
            Facture facture = Facture.createTestInstance();
            facture.setConsultation(consultation);
            facture.setSituationFinanciere(situation);
            System.out.println("✅ Situation financière créée avec statut : " + situation.getStatut());
            System.out.println("✅ Facture créée, montant : " + facture.getTotaleFacture());

            // 7. Personnel et rôles associés
            Secretaire secretaire = Secretaire.createTestInstance();
            Staff staff = Staff.createTestInstance();
            System.out.println("✅ Secrétaire créée : " + secretaire.getNom());
            System.out.println("✅ Staff créé, salaire : " + staff.getSalaire());

            // 8. Notifications et statistiques
            Notification notification = Notification.createTestInstance();
            Statistique statistique = Statistique.createTestInstance();
            System.out.println("✅ Notification créée de type : " + notification.getType());
            System.out.println("✅ Statistique créée : " + statistique.getNom());

            // 9. Caisse, médicaments
            Caisse caisse = Caisse.createTestInstance();
            Medicament medicament = Medicament.createTestInstance();
            System.out.println("✅ Caisse créée avec référence : " + caisse.getReference());
            System.out.println("✅ Médicament créé : " + medicament.getNom());

        } catch (Exception e) {
            System.err.println("❌ Erreur lors des tests : " + e.getMessage());
            e.printStackTrace();
        }
    }
}