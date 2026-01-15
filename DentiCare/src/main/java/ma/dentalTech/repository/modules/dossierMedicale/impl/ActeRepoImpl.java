package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Acte.Acte;
import ma.dentalTech.repository.modules.dossierMedicale.api.ActeRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActeRepoImpl implements ActeRepo {

    @Override
    public List<Acte> findAll() {
        String sql = "SELECT * FROM acte";
        List<Acte> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapActe(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all actes", e);
        }
        return list;
    }

    @Override
    public Optional<Acte> findById(Long id) {
        String sql = "SELECT * FROM acte WHERE id_acte = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapActe(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding acte with id: " + id, e);
        }
        return Optional.empty(); // Correct: Return empty instead of null
    }

    @Override
    public void create(Acte a) {
        String sql = "INSERT INTO acte (id_entite, libelle, categorie, prix_de_base, cree_par) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (a.getIdEntite() != null) {
                ps.setLong(1, a.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setString(2, a.getLibelle());
            ps.setString(3, a.getCategorie());
            ps.setDouble(4, a.getPrixDeBase());
            ps.setString(5, a.getCreePar());

            ps.executeUpdate(); // CRITICAL: Added this line

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setIdActe(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating acte", e);
        }
    }

    @Override
    public void update(Acte a) {
        String sql = "UPDATE acte SET libelle = ?, categorie = ?, prix_de_base = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_acte = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getLibelle());
            ps.setString(2, a.getCategorie());
            ps.setDouble(3, a.getPrixDeBase());
            ps.setString(4, a.getModifiePar());
            ps.setLong(5, a.getIdActe());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating acte", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM acte WHERE id_acte = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting acte", e);
        }
    }

    @Override
    public void delete(Acte a) {
        if (a != null && a.getIdActe() != null) {
            deleteById(a.getIdActe());
        }
    }
}