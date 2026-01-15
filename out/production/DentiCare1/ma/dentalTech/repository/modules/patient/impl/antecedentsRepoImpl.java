package ma.dentalTech.repository.modules.patient.impl;

import ma.dentalTech.entities.Antecedents.Antecedents;
import ma.dentalTech.repository.modules.patient.api.antecedentsRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class antecedentsRepoImpl implements antecedentsRepo {

    @Override
    public List<Antecedents> findAll() throws SQLException {
        String sql = "SELECT * FROM antecedents ORDER BY id_antecedent";
        List<Antecedents> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapAntecedents(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Antecedents> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM antecedents WHERE id_antecedent = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapAntecedents(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Antecedents an) throws SQLException {
        String sql = "INSERT INTO antecedents (nom, categorie, description, niveau_de_risque, cree_par) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, an.getNom());
            ps.setString(2, an.getCategorie());
            ps.setString(3, an.getDescription());
            ps.setString(4, an.getNiveauDeRisque() != null ? an.getNiveauDeRisque().name() : null);
            ps.setString(5, an.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    an.setIdAntecedent(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Antecedents an) throws SQLException {
        String sql = "UPDATE antecedents SET nom = ?, categorie = ?, description = ?, niveau_de_risque = ?, modifie_par = ? WHERE id_antecedent = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, an.getNom());
            ps.setString(2, an.getCategorie());
            ps.setString(3, an.getDescription());
            ps.setString(4, an.getNiveauDeRisque() != null ? an.getNiveauDeRisque().name() : null);
            ps.setString(5, an.getModifiePar());
            ps.setLong(6, an.getIdAntecedent());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Antecedents an) throws SQLException {
        if (an != null && an.getIdAntecedent() != null) {
            deleteById(an.getIdAntecedent());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM antecedents WHERE id_antecedent = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}