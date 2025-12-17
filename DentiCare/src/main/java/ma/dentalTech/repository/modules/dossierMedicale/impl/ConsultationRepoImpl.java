package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.repository.modules.dossierMedicale.api.ConsultationRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ma.dentalTech.repository.common.RowMappers.mapCertificat;
import static ma.dentalTech.repository.common.RowMappers.mapConsultation;

public class ConsultationRepoImpl implements ConsultationRepo {
    @Override
    public List<Consultation> findAll() throws SQLException{
        String sql = "SELECT * FROM certificat";
        List<Consultation> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) list.add(mapConsultation(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Consultation> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM consultation  WHERE id_consultation = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapConsultation(rs));
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }


    @Override
    public void create(Consultation c) throws SQLException {
        String sql = "INSERT INTO consultation (date_consultation, heure_consultation, statut, observationMedecin) VALUES (?, ?, ?, ?)";

        try (Connection con = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(c.getDateConsultation()));
            ps.setTime(2, Time.valueOf(c.getHeureConsultation()));
            ps.setString(3, c.getStatut().name());
            ps.setString(4, c.getObservationMedecin());

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdConsultation(rs.getLong(1));
                }
            }
        }
    }


    @Override
    public void  update(Consultation c) {
        throw new UnsupportedOperationException("A faire");
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Consultation WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la consultation", e);
        }

    }
}
