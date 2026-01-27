package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.repository.modules.dossierMedicale.api.CertificatRepo;
import ma.dentalTech.conf.SessionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.dentalTech.repository.common.RowMappers.mapCertificat;

public class CertificatRepoImpl implements CertificatRepo {
    @Override
    public List<Certificat> findAll() throws SQLException{
        String sql =
                "SELECT ctf.*, " +
                "dm.id_patient, p.nom AS patient_nom, p.telephone AS patient_telephone " +
                "FROM certificat ctf " +
                "LEFT JOIN dossier_medical dm ON ctf.id_dossier_medical = dm.id_dossier " +
                "LEFT JOIN patient p ON dm.id_patient = p.id_patient";
        List<Certificat> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) {
                Certificat cert = mapCertificat(rs);

                if (rs.getObject("id_dossier_medical") != null && rs.getObject("id_patient") != null) {
                    ma.dentalTech.entities.Patient.Patient patient = ma.dentalTech.entities.Patient.Patient.builder()
                            .idPatient(rs.getLong("id_patient"))
                            .nom(rs.getString("patient_nom"))
                            .telephone(rs.getString("patient_telephone"))
                            .build();

                    ma.dentalTech.entities.DossierMedicale.DossierMedicale dossier =
                            ma.dentalTech.entities.DossierMedicale.DossierMedicale.builder()
                                    .idDossier(rs.getLong("id_dossier_medical"))
                                    .patient(patient)
                                    .build();

                    cert.setDossierMedicale(dossier);
                }

                list.add(cert);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Certificat> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM certificat WHERE id_certificat = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapCertificat(rs));
                return Optional.empty();
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Certificat c) {
        String sql = "INSERT INTO certificat (id_entite, id_dossier_medical, id_consultation, dateDebut, dateFin, duree, noteMedecin, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (c.getIdEntite() != null) {
                ps.setLong(1, c.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setLong(2, c.getIdDossier());
            ps.setLong(3, c.getIdConsultation());
            ps.setDate(4, Date.valueOf(c.getDateDebut()));
            ps.setDate(5, Date.valueOf(c.getDateFin()));
            ps.setInt(6, c.getDuree());
            ps.setString(7, c.getNoteMedecin());
            ps.setString(8, c.getCreePar());
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setIdCertificat(keys.getLong(1));}
            }

        } catch (SQLException e)
        { throw new RuntimeException(e); }
    }

    @Override
    public void update(Certificat c) {
        String sql = "UPDATE certificat SET id_dossier_medical = ?, id_consultation = ?, dateDebut = ?, dateFin = ?, duree = ?, noteMedecin = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_certificat = ?";
        try (Connection con = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, c.getIdDossier());
            ps.setLong(2, c.getIdConsultation());
            ps.setDate(3, Date.valueOf(c.getDateDebut()));
            ps.setDate(4, Date.valueOf(c.getDateFin()));
            ps.setInt(5, c.getDuree());
            ps.setString(6, c.getNoteMedecin());
            ps.setString(7, c.getModifiePar());
            ps.setLong(8, c.getIdCertificat());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Certificat c) throws SQLException {
        if (c != null) deleteById(c.getIdCertificat());
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM Certificat WHERE id_certificat = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
