package ma.dentalTech.repository.modules.auth.impl;

import ma.dentalTech.entities.Secretaire.Secretaire;
import ma.dentalTech.repository.modules.auth.api.SecretaireRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SecretaireRepoImpl implements SecretaireRepo {

    @Override
    public List<Secretaire> findAll() throws SQLException {
        String sql = "SELECT * FROM secretaire ORDER BY id_secretaire";
        List<Secretaire> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapSecretaire(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Secretaire> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM secretaire WHERE id_secretaire = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapSecretaire(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Secretaire sec) throws SQLException {
        String sql = "INSERT INTO secretaire (id_staff, num_cnss, commission, cree_par) VALUES (?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, sec.getIdStaff());
            ps.setString(2, sec.getNumCNSS());
            ps.setObject(3, sec.getCommission());
            ps.setString(4, sec.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    sec.setIdSecretaire(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Secretaire sec) throws SQLException {
        String sql = "UPDATE secretaire SET num_cnss = ?, commission = ?, modifie_par = ? WHERE id_secretaire = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sec.getNumCNSS());
            ps.setObject(2, sec.getCommission());
            ps.setString(3, sec.getModifiePar());
            ps.setLong(4, sec.getIdSecretaire());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Secretaire sec) throws SQLException {
        if (sec != null && sec.getIdSecretaire() != null) {
            deleteById(sec.getIdSecretaire());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM secretaire WHERE id_secretaire = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}