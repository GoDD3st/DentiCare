package ma.dentalTech.repository.modules.auth.impl;

import ma.dentalTech.entities.Role.Role;
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
        String sql = "INSERT INTO utilisateur (id_entite, nom, email, adresse, cin, tel, sexe, login, motDePass, dateNaissance, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (u.getIdEntite() != null) {
                ps.setLong(1, u.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setString(2, u.getNom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getAdresse());
            ps.setString(5, u.getCin());
            ps.setString(6, u.getTel());
            ps.setString(7, String.valueOf(u.getSexe()));
            ps.setString(8, u.getLogin());
            ps.setString(9, u.getMotDePass());
            ps.setDate(10, u.getDateNaissance() != null ? Date.valueOf(u.getDateNaissance()) : null);
            ps.setString(11, u.getCreePar());

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setIdUser(keys.getLong(1));
            }
        }
    }

    @Override
    public void update(Utilisateur u) throws SQLException {
        String sql = "UPDATE utilisateur SET nom = ?, email = ?, adresse = ?, cin = ?, tel = ?, sexe = ?, login = ?, motDePass = ?, dateNaissance = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_user = ?";
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

    @Override
    public Optional<Utilisateur> findByLogin(String login) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE login = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapUtilisateur(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapUtilisateur(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) throws SQLException {
        String sql = "SELECT r.* FROM role r " +
                     "INNER JOIN utilisateur_role ur ON r.id_role = ur.id_role " +
                     "WHERE ur.id_user = ?";
        List<Role> roles = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(RowMappers.mapRole(rs));
                }
            }
        }
        return roles;
    }
}
