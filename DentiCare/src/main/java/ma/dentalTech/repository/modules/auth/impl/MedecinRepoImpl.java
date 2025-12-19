package ma.dentalTech.repository.modules.auth.impl;

import ma.dentalTech.entities.Medecin.Medecin;
import ma.dentalTech.repository.modules.auth.api.MedecinRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedecinRepoImpl implements MedecinRepo {

    @Override
    public List<Medecin> findAll() throws SQLException {
        String sql = "SELECT * FROM medecin ORDER BY id_medecin";
        List<Medecin> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapMedecin(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Medecin> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM medecin WHERE id_medecin = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapMedecin(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Medecin m) throws SQLException {
        // Note: id_medecin n'est pas AUTO_INCREMENT dans votre SQL,
        // il doit être fourni (souvent le même que id_staff)
        String sql = "INSERT INTO medecin (id_medecin, id_staff, specialite, jours_non_disponible, cree_par) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, m.getIdMedecin());
            ps.setLong(2, m.getIdStaff());
            ps.setString(3, m.getSpecialite());
            ps.setString(4, String.valueOf(m.getJoursNonDisponible())); // Stocké en TEXT dans la BD
            ps.setString(5, m.getCreePar());

            ps.executeUpdate();
        }
    }

    @Override
    public void update(Medecin m) throws SQLException {
        String sql = "UPDATE medecin SET id_staff = ?, specialite = ?, jours_non_disponible = ?, modifie_par = ? WHERE id_medecin = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, m.getIdStaff());
            ps.setString(2, m.getSpecialite());
            ps.setString(3, String.valueOf(m.getJoursNonDisponible()));
            ps.setString(4, m.getModifiePar());
            ps.setLong(5, m.getIdMedecin());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Medecin m) throws SQLException {
        if (m != null && m.getIdMedecin() != null) {
            deleteById(m.getIdMedecin());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM medecin WHERE id_medecin = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}