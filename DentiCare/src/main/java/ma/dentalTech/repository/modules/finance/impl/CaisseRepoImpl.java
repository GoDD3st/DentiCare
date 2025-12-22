package ma.dentalTech.repository.modules.finance.impl;

import ma.dentalTech.entities.Caisse.Caisse;
import ma.dentalTech.repository.modules.finance.api.CaisseRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CaisseRepoImpl implements CaisseRepo {

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
    public void create(Caisse ca) throws SQLException {
        String sql = "INSERT INTO caisse (montant, date_encaissement, mode_encaissement, reference) VALUES (?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, ca.getMontant());
            ps.setTimestamp(2, ca.getDateEncassement() != null ? Timestamp.valueOf(ca.getDateEncassement()) : null);
            ps.setString(3, ca.getModeEncaissement() != null ? ca.getModeEncaissement().name() : null);
            ps.setString(4, ca.getReference());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    ca.setIdCaisse(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Caisse ca) throws SQLException {
        String sql = "UPDATE caisse SET montant = ?, date_encaissement = ?, mode_encaissement = ?, reference = ? WHERE id_caisse = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, ca.getMontant());
            ps.setTimestamp(2, ca.getDateEncassement() != null ? Timestamp.valueOf(ca.getDateEncassement()) : null);
            ps.setString(3, ca.getModeEncaissement() != null ? ca.getModeEncaissement().name() : null);
            ps.setString(4, ca.getReference());
            ps.setLong(5, ca.getIdCaisse());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Caisse ca) throws SQLException {
        if (ca != null && ca.getIdCaisse() != null) {
            deleteById(ca.getIdCaisse());
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