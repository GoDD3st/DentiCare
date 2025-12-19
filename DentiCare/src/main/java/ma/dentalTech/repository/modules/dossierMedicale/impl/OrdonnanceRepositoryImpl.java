package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.repository.modules.dossierMedicale.api.OrdonnanceRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepo {

    @Override
    public List<Ordonnance> findAll() throws SQLException {
        String sql = "SELECT * FROM ordonnance ORDER BY id_ordonnance";
        List<Ordonnance> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapOrdonnance(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Ordonnance> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM ordonnance WHERE id_ordonnance = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapOrdonnance(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Ordonnance o) throws SQLException {
        String sql = "INSERT INTO ordonnance (date_ordonnance, cree_par) VALUES (?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, o.getDate() != null ? Date.valueOf(o.getDate()) : null);
            ps.setString(2, o.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    o.setIdOrdonnance(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Ordonnance o) throws SQLException {
        String sql = "UPDATE ordonnance SET date_ordonnance = ?, modifie_par = ? WHERE id_ordonnance = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, o.getDate() != null ? Date.valueOf(o.getDate()) : null);
            ps.setString(2, o.getModifiePar());
            ps.setLong(3, o.getIdOrdonnance());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Ordonnance o) throws SQLException {
        if (o != null && o.getIdOrdonnance() != null) {
            deleteById(o.getIdOrdonnance());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM ordonnance WHERE id_ordonnance = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}