package ma.dentalTech.repository.modules.auth.impl;

import ma.dentalTech.entities.Utilisateur.Utilisateur;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.auth.api.UtilisateurRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilisateurRepoImpl implements UtilisateurRepo {

    @Override
    public List<Utilisateur> findAll() throws SQLException {
        String sql = "SELECT * FROM utilisateur";
        List<Utilisateur> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement ps = c.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapUtilisateur(rs));
        }
        return list;
    }

    @Override
    public Optional<Utilisateur> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE id_user = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapUtilisateur(rs));
            }
        }
        return Optional.empty();    }

    @Override
    public void create(Utilisateur u) throws SQLException {
        String sql = "INSERT INTO utilisateur (nom, email, adresse, cin, tel, sexe, login, motDePass, dateNaissance, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getAdresse());
            ps.setString(4, u.getCin());
            ps.setString(5, u.getTel());
            ps.setString(6, String.valueOf(u.getSexe()));
            ps.setString(7, u.getLogin());
            ps.setString(8, u.getMotDePass());
            ps.setDate(9, u.getDateNaissance() != null ? Date.valueOf(u.getDateNaissance()) : null);
            ps.setString(10, u.getCreePar());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setIdUser(keys.getLong(1));
            }
        }
    }

    @Override
    public void update(Utilisateur u) throws SQLException {
        String sql = "UPDATE utilisateur SET nom = ?, email = ?, adresse = ?, cin = ?, tel = ?, sexe = ?, login = ?, motDePass = ?, dateNaissance = ?, modifie_par = ? WHERE id_user = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getAdresse());
            ps.setString(4, u.getCin());
            ps.setString(5, u.getTel());
            ps.setString(6, String.valueOf(u.getSexe()));
            ps.setString(7, u.getLogin());
            ps.setString(8, u.getMotDePass());
            ps.setDate(9, u.getDateNaissance() != null ? Date.valueOf(u.getDateNaissance()) : null);
            ps.setString(10, u.getModifiePar());
            ps.setLong(11, u.getIdUser());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Utilisateur u) throws SQLException {
        if (u != null && u.getIdUser() != null) {
            deleteById(u.getIdUser());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM utilisateur WHERE id_user = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
