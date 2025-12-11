package ma.dentalTech.repository.common;

import ma.dentalTech.entities.Acte.Acte;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.entities.enums.*;
import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.repository.modules.dossierMedicale.api.InterventionMedecinRepo;

import java.sql.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class RowMappers {

    private RowMappers(){}

    public static Patient mapPatient(ResultSet rs) throws SQLException {

        Patient patientRow = new Patient();

        patientRow.setIdPatient(rs.getLong("id"));
        patientRow.setNom(rs.getString("nom"));
        patientRow.setAdresse(rs.getString("adresse"));
        patientRow.setTelephone(rs.getString("telephone"));
        var dn = rs.getDate("dateNaissance");
        if (dn != null) patientRow.setDateNaissance(dn.toLocalDate());
        var dc = rs.getTimestamp("dateCreation");
        if (dc != null) patientRow.setDateCreation(LocalDate.from(dc.toLocalDateTime()));
        patientRow.setSexe(SexeEnum.valueOf(rs.getString("sexe")));
        patientRow.setAssurance(AssuranceEnum.valueOf(rs.getString("assurance")));

        return patientRow;
    }


    public static Antecedents mapAntecedent(ResultSet rs) throws SQLException {
        Antecedents antecedentRow = new Antecedents();

        antecedentRow.setIdAntecedent(rs.getLong("id"));
        antecedentRow.setNom(rs.getString("nom"));
        antecedentRow.setCategorie(rs.getString("categorie"));
        antecedentRow.setNiveauDeRisque(RisqueEnum.valueOf(rs.getString("niveauDeRisque")));

        return antecedentRow;
    }

    public static RDV mapRDV(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setIdEntite(rs.getLong("patient_id"));
        
        Medecin medecin = new Medecin();
        medecin.setIdEntite(rs.getLong("medecin_id"));

        RDV rdv = RDV.builder()
                .idRDV(rs.getLong("id"))
                .motif(rs.getString("motif"))
                .noteMedecin(rs.getString("noteMedecin"))
                .date(rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null)
                .heure(rs.getTime("heure") != null ? rs.getTime("heure").toLocalTime() : null)
                .statut(RDVStatutEnum.valueOf(rs.getString("statut")))
                .patient(patient)
                .medecin(medecin)
                .build();
        
        var dc = rs.getTimestamp("dateCreation");
        if (dc != null) rdv.setDateCreation(LocalDate.from(dc.toLocalDateTime()));

        return rdv;
    }

    public static Consultation mapConsultation(ResultSet rs) throws SQLException {
        Consultation consultation = new Consultation();
        consultation.setIdEntite(rs.getLong("id"));
        consultation.setDate(rs.getDate("date").toLocalDate());
        consultation.setStatut(ConsultationStatutEnum.valueOf(rs.getString("statut")));
        consultation.setObservationMedecin(rs.getString("observationMedecin"));
        return consultation;
    }

    public static DossierMedicale mapDossierMedical(ResultSet rs) throws SQLException {
        DossierMedicale dossierMedical = new DossierMedicale();
        dossierMedical.setIdEntite(rs.getLong("id"));
        var dc = rs.getTimestamp("dateCreation");
        if (dc != null) dossierMedical.setDateCreation(LocalDate.from(dc.toLocalDateTime()));
        var ddm = rs.getTimestamp("dateDerniereModification");
        if (ddm != null) dossierMedical.setDateDerniereModification(ddm.toLocalDateTime());
        dossierMedical.setModifiePar(rs.getString("modifiePar"));
        dossierMedical.setCreePar(rs.getString("creePar"));
        return dossierMedical;
    }

    public static Medecin mapMedecin(ResultSet rs) throws SQLException {
        Medecin medecin = new Medecin();
        medecin.setIdEntite(rs.getLong("idEntite"));
        medecin.setIdMedecin(rs.getLong("idMedecin"));
        medecin.setSpecialite(rs.getString("specialite"));
        return medecin;
    }

    public static Certificat mapCertificat(ResultSet rs) throws SQLException {
        Certificat certificat = new Certificat();
        certificat.setIdEntite(rs.getLong("idEntite"));
        certificat.setIdCertificat(rs.getLong("idCertificat"));

        Date dateDebutSql = rs.getDate("dateDebut");
        if (dateDebutSql != null)
            certificat.setDateDebut(dateDebutSql.toLocalDate());

        Date dateFinSql = rs.getDate("dateFin");
        if (dateFinSql != null)
            certificat.setDateFin(dateFinSql.toLocalDate());

        certificat.setDuree(rs.getInt("Duree"));
        certificat.setNoteMedecin(rs.getString("noteMedecin"));

        Long idConsult = rs.getLong("idConsultation");
        Consultation cons = new Consultation();
        cons.setIdConsultation(idConsult);
        certificat.setConsultation(cons);

        Long idDossier = rs.getLong("idDossier");
        DossierMedicale dossier = new DossierMedicale();
        dossier.setIdDossier(idDossier);
        certificat.setDossierMedicale(dossier);

        return certificat;
    }

    public static Acte mapActe(ResultSet rs) throws SQLException{
        Acte acte = new Acte();
        acte.setIdEntite(rs.getLong("idEntite"));
        acte.setIdActe(rs.getLong("idActe"));
        acte.setLibelle(rs.getString("Libelle"));
        acte.setCategorie(rs.getString("categorie"));
        acte.setPrixDeBase(rs.getDouble("prixDeBase"));
        return acte;
    }

    public static Ordonnance mapOrdonnance(ResultSet rs) throws SQLException {
        Ordonnance ordonnance = Ordonnance.builder()
                .date(rs.getDate("date").toLocalDate())
                .build();
        ordonnance.setIdEntite(rs.getLong("idEntite"));
        return ordonnance;
    }

    public static Prescription mapPrescription(ResultSet rs) throws SQLException {
        Prescription p = Prescription.builder()
                .quantite(rs.getInt("quantite"))
                .frequence(rs.getString("frequence"))
                .dureeEnJours(rs.getInt("dureeEnJours"))
                .build();
        p.setIdEntite(rs.getLong("idEntite"));
        return p;
    }

    public static InterventionMedecin mapInterventionMedecin(ResultSet rs) throws SQLException {
        InterventionMedecin interventionMedecin = new InterventionMedecin();
        interventionMedecin.setIdEntite(rs.getLong("idEntite"));
        interventionMedecin.setIdIntervention(rs.getLong("idIntervention"));
        interventionMedecin.setPrixDePatient(rs.getDouble("prixDePatient"));
        interventionMedecin.setNumDent(rs.getInt("numDent"));

        return interventionMedecin;
    }

}
