package ma.dentalTech.repository.common;

import ma.dentalTech.entities.Acte.Acte;
import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.entities.enums.*;
import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.repository.modules.agenda.AgendaUtils;
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
        Medecin medecin = Medecin.builder()
                .idMedecin(rs.getLong("idMedecin"))
                .specialite(rs.getString("specialite"))
                .build();
        medecin.setIdEntite(rs.getLong("idEntite"));

        return medecin;
    }

    public static Certificat mapCertificat(ResultSet rs) throws SQLException {
        Certificat certificat = Certificat.builder()
                .idCertificat(rs.getLong("idCertificat"))
                .idConsultation(rs.getLong("idConsultation"))
                .idDossier(rs.getLong("idDossier"))
                .dateDebut(rs.getDate("dateDebut")!= null  ? rs.getDate("dateDebut").toLocalDate()  : null)
                .dateFin(rs.getDate("dateFin") !=  null ? rs.getDate("dateFin").toLocalDate()  : null)
                .duree(rs.getInt("duree"))
                .noteMedecin(rs.getString("noteMedecin"))
                .build();

        certificat.setIdEntite(rs.getLong("idEntite"));
        return certificat;
    }

    public static Acte mapActe(ResultSet rs) throws SQLException{
        Acte acte = Acte.builder()
                .idActe(rs.getLong("idActe"))
                .libelle(rs.getString("Libelle"))
                .categorie(rs.getString("categorie"))
                .prixDeBase(rs.getDouble("prixDeBase"))
                .build();
        acte.setIdEntite(rs.getLong("idEntite"));
        return acte;
    }

    public static Ordonnance mapOrdonnance(ResultSet rs) throws SQLException {
        Ordonnance ordonnance = Ordonnance.builder()
                .idOrdonnance(rs.getLong("idOrdonnance"))
                .date(rs.getDate("date").toLocalDate())
                .build();
        ordonnance.setIdEntite(rs.getLong("idEntite"));
        return ordonnance;
    }

    public static Prescription mapPrescription(ResultSet rs) throws SQLException {
        Prescription p = Prescription.builder()
                .idPrescription(rs.getLong("idPrescription"))
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
    public static Facture mapFacture(ResultSet rs) throws SQLException {
         Facture facture = Facture.builder()
                .idFacture(rs.getLong("idFacture"))
                .totaleFacture(rs.getDouble("totaleFacture"))
                .totalePaye(rs.getDouble("totalePaye"))
                .reste(rs.getDouble("reste"))
                .statut(FactureStatutEnum.valueOf(rs.getString("statut")))
                .dateFacture(rs.getTimestamp("dateFacture").toLocalDateTime())
                .build();
         facture.setIdEntite(rs.getLong("idEntite"));
        return facture;
    }

    public static AgendaMensuel mapAgenda(ResultSet rs) throws SQLException {
        AgendaMensuel agenda = AgendaMensuel.builder()
                .mois(MoisEnum.valueOf(rs.getString("mois")))
                .joursNonDisponible(AgendaUtils.deserializeJours(rs.getString("joursNonDisponible")))
                .build();
        agenda.setIdEntite(rs.getLong("idEntite"));

        var dc = rs.getTimestamp("dateCreation");
        if (dc != null) agenda.setDateCreation(LocalDate.from(dc.toLocalDateTime()));

        var ddm = rs.getTimestamp("dateDerniereModification");
        if (ddm != null) agenda.setDateDerniereModification(ddm.toLocalDateTime());

        agenda.setModifiePar(rs.getString("modifiePar"));
        agenda.setCreePar(rs.getString("creePar"));

        long medecinId = rs.getLong("medecin_id");
        if (!rs.wasNull()) {
            Medecin medecin = new Medecin();
            medecin.setIdEntite(medecinId);
            agenda.setMedecin(medecin);
        }

        return agenda;
    }
}
