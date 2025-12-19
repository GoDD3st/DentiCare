package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Medicament.Medicament;
import ma.dentalTech.repository.modules.dossierMedicale.api.MedicamentRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicamentRepoImpl implements MedicamentRepo {

    @Override
    public List<Medicament> findAll() throws SQLException {
        String sql = "SELECT * FROM medicament ORDER BY id_medicament";
        List<Medicament> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapMedicament(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Medicament> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM medicament WHERE id_medicament = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapMedicament(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Medicament m) throws SQLException {
        String sql = "INSERT INTO medicament (nom, laboratoire, type, forme, remboursable, prix_unitaire, description, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getLaboratoire());
            ps.setString(3, m.getType());
            ps.setString(4, m.getForme() != null ? m.getForme().name() : null);
            ps.setBoolean(5, m.isRemboursable());
            ps.setObject(6, m.getPrixUnitaire());
            ps.setString(7, m.getDescription());
            ps.setString(8, m.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    m.setIdMedicament(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Medicament m) throws SQLException {
        String sql = "UPDATE medicament SET nom = ?, laboratoire = ?, type = ?, forme = ?, remboursable = ?, prix_unitaire = ?, description = ?, modifie_par = ? WHERE id_medicament = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getLaboratoire());
            ps.setString(3, m.getType());
            ps.setString(4, m.getForme() != null ? m.getForme().name() : null);
            ps.setBoolean(5, m.isRemboursable());
            ps.setObject(6, m.getPrixUnitaire());
            ps.setString(7, m.getDescription());
            ps.setString(8, m.getModifiePar());
            ps.setLong(9, m.getIdMedicament());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Medicament m) throws SQLException {
        if (m != null && m.getIdMedicament() != null) {
            deleteById(m.getIdMedicament());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM medicament WHERE id_medicament = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}