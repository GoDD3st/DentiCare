package ma.dentalTech.repository.modules.consultation.impl;
import ma.dentalTech.entities.Consultation.Consultation;

import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultationRepositoryImpl {
    public Consultation findByRdvId(Long rdvId) {
        String sql = "SELECT * FROM consultation WHERE rdv_id = ?";

        try (Connection conn = SessionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, rdvId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return RowMappers.mapConsultation(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
