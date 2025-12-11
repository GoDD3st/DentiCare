package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.DossierMedicale.DossierMedicale;
import ma.dentalTech.repository.modules.dossierMedicale.api.DossierMedicaleRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DossierMedicalRepoImpl implements DossierMedicaleRepo {
    @Override
    public Optional<DossierMedicale> findById(Long id) throws InterruptedException {
        String sql = "SELECT * FROM DossierMedicale WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(RowMappers.mapDossierMedical(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du dossier médical", e);
        }
        return Optional.empty();
    }

    @Override
    public List<DossierMedicale> findAll() {
        String sql = "SELECT * FROM DossierMedicale";
        List<DossierMedicale> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapDossierMedical(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des dossiers médicaux", e);
        }
        return list;
    }

    @Override
    public DossierMedicale save(DossierMedicale d) {
        if (d.getIdEntite() == null) return insert(d);
        else return update(d);
    }

    private DossierMedicale insert(DossierMedicale d) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private DossierMedicale update(DossierMedicale d) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM DossierMedicale WHERE idEntite = ?";
        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du dossier médical", e);
        }
    }
}
