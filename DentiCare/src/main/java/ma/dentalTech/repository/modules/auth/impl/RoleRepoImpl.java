package ma.dentalTech.repository.modules.auth.impl;

import ma.dentalTech.entities.Role.Role;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.auth.api.RoleRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepoImpl implements RoleRepo{
    @Override
    public List<Role> findAll() throws SQLException {
        String sql = "SELECT * FROM role ORDER BY id_role";
        List<Role> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement ps = c.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapRole(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Role> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM role WHERE id_role = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapRole(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Role r) throws SQLException {
        String sql = "INSERT INTO role (libelle, privileges, cree_par) VALUES (?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getLibelle().name());
            ps.setString(2, String.valueOf(r.getPrivileges()));
            ps.setString(3, r.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setIdRole(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Role r) throws SQLException {
        String sql = "UPDATE role SET libelle = ?, privileges = ?, modifie_par = ? WHERE id_role = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, r.getLibelle().name());
            ps.setString(2, String.valueOf(r.getPrivileges()));
            ps.setString(3, r.getModifiePar());
            ps.setLong(4, r.getIdRole());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Role r) throws SQLException {
        if (r != null && r.getIdRole() != null) {
            deleteById(r.getIdRole());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM role WHERE id_role = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
