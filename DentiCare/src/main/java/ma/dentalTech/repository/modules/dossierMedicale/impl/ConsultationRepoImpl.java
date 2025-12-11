package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.repository.modules.dossierMedicale.api.ConsultationRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultationRepoImpl implements ConsultationRepo {
    @Override
    public Optional<Consultation> findById(Long id) throws InterruptedException {
        String sql = "SELECT * FROM Consultation WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(RowMappers.mapConsultation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la consultation", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Consultation> findAll() {
        String sql = "SELECT * FROM Consultation";
        List<Consultation> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapConsultation(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des consultations", e);
        }
        return list;
    }

    @Override
    public Consultation save(Consultation consultation) {
        if (consultation.getIdEntite() == null) return insert(consultation);
        else return update(consultation);
    }

    private Consultation insert(Consultation c) {
        throw new UnsupportedOperationException("A faire");
    }

    private Consultation update(Consultation c) {
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
