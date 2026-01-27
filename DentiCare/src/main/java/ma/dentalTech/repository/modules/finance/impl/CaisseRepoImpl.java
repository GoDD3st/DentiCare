package ma.dentalTech.repository.modules.finance.impl;

import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.repository.modules.finance.api.CaisseRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CaisseRepoImpl implements CaisseRepository {

    @Override
    public List<Caisse> findAll() throws SQLException {
        String sql = "SELECT * FROM caisse ORDER BY id_caisse";
        List<Caisse> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapCaisse(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Caisse> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM caisse WHERE id_caisse = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapCaisse(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Caisse caisse) throws SQLException {
        String sql = "INSERT INTO caisse (montant, date_encaissement, mode_encaissement, reference) VALUES (?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, caisse.getMontant());
            ps.setTimestamp(2, Timestamp.valueOf(caisse.getDateEncassement()));
            ps.setString(3, caisse.getModeEncaissement() != null ? caisse.getModeEncaissement().name() : null);
            ps.setString(4, caisse.getReference());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    caisse.setIdCaisse(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Caisse caisse) throws SQLException {
        String sql = "UPDATE caisse SET montant = ?, date_encaissement = ?, mode_encaissement = ?, reference = ? WHERE id_caisse = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDouble(1, caisse.getMontant());
            ps.setTimestamp(2, Timestamp.valueOf(caisse.getDateEncassement()));
            ps.setString(3, caisse.getModeEncaissement() != null ? caisse.getModeEncaissement().name() : null);
            ps.setString(4, caisse.getReference());
            ps.setLong(5, caisse.getIdCaisse());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Caisse caisse) throws SQLException {
        if (caisse != null && caisse.getIdCaisse() != null) {
            deleteById(caisse.getIdCaisse());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM caisse WHERE id_caisse = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}