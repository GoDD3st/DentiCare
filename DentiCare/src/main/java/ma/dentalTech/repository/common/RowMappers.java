package ma.dentalTech.repository.common;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.entities.Patient.Patient;
import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.entities.enums.*;
import ma.dentalTech.entities.Antecedents.Antecedents;
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

        RDV rdv = new RDV();

        rdv.setIdRDV(rs.getLong("id"));
        rdv.setMotif(rs.getString("motif"));
        rdv.setNoteMedecin(rs.getString("noteMedecin"));

        var d = rs.getDate("date");
        if (d != null) rdv.setDate(d.toLocalDate());

        var h = rs.getTime("heure");
        if (h != null) rdv.setHeure(h.toLocalTime());

        rdv.setStatut(RDVStatutEnum.valueOf(rs.getString("statut")));

        var dc = rs.getTimestamp("dateCreation");
        if (dc != null) rdv.setDateCreation(LocalDate.from(dc.toLocalDateTime()));

        // ❗ patient_id et medecin_id récupérés mais NON stockés dans l'entité
        // ils seront utilisés ensuite dans le Repository pour fetch Patient/Medecin
        // long patientId = rs.getLong("patient_id");
        // long medecinId = rs.getLong("medecin_id");

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
}
