package ma.dentalTech.repository.modules.auth.impl;

import ma.dentalTech.entities.Admin.Admin;
import ma.dentalTech.repository.modules.auth.api.AdminRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdminRepoImpl implements AdminRepo {

    @Override
    public List<Admin> findAll() throws SQLException {
        String sql = "SELECT * FROM admin ORDER BY id_utilisateur";
        List<Admin> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapAdmin(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Admin> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM admin WHERE id_utilisateur = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapAdmin(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Admin ad) throws SQLException {
        String sql = "INSERT INTO admin (id_utilisateur, cree_par) VALUES (?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, ad.getIdUser());
            ps.setString(2, ad.getCreePar());

            ps.executeUpdate();
        }
    }

    @Override
    public void update(Admin ad) throws SQLException {
        String sql = "UPDATE admin SET modifie_par = ? WHERE id_utilisateur = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ad.getModifiePar());
            ps.setLong(2, ad.getIdUser());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Admin ad) throws SQLException {
        if (ad != null && ad.getIdUser() != null) {
            deleteById(ad.getIdUser());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM admin WHERE id_utilisateur = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}