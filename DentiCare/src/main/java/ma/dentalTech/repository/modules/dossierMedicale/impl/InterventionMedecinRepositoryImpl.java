package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.entities.Consultation.Consultation;
import ma.dentalTech.entities.InterventionMedecin.InterventionMedecin;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.dossierMedicale.api.InterventionMedecinRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InterventionMedecinRepositoryImpl implements InterventionMedecinRepo {

    @Override
    public Optional<InterventionMedecin> findById(Long id) throws InterruptedException {
        String sql = "SELECT * FROM InterventionMedecin WHERE idIM = ?";

        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(RowMappers.mapInterventionMedecin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la consultation", e);
        }
        return Optional.empty();
    }

    @Override
    public List<InterventionMedecin> findAll() throws SQLException {
        List<InterventionMedecin> interventionList = new ArrayList<>();
        String sql = "SELECT * FROM InterventionMedecin";

        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) interventionList.add(RowMappers.mapInterventionMedecin(rs));
        }
        return interventionList;
        }

    private InterventionMedecin insert(InterventionMedecin interventionMedecin) {
        throw new UnsupportedOperationException("A faire");
    }

    private InterventionMedecin update(InterventionMedecin interventionMedecin) {
        throw new UnsupportedOperationException("A faire");
    }
    @Override
    public InterventionMedecin save(InterventionMedecin interventionMedecin) {
        if (InterventionMedecin.getIdEntite() == null) return insert(interventionMedecin);
        else return update(interventionMedecin);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM InterventionMedecin WHERE idEntite = ?";

        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la consultation", e);
        }
    }
}
