package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.DossierMedicale.DossierMedicale;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DossierMedicalRepositoryImpl {
    public DossierMedicale findById(Long id) {
        String sql = "SELECT * FROM dossiermedicale WHERE id = ?";

        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return RowMappers.mapDossierMedical(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
