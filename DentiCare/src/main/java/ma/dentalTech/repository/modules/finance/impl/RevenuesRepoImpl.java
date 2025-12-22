package ma.dentalTech.repository.modules.finance.impl;

import ma.dentalTech.entities.Revenues.Revenues;
import ma.dentalTech.repository.modules.finance.api.RevenuesRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RevenuesRepoImpl implements RevenuesRepo {

    @Override
    public List<Revenues> findAll() throws SQLException {
        String sql = "SELECT * FROM revenues ORDER BY id_revenue";
        List<Revenues> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapRevenues(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Revenues> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM revenues WHERE id_revenue = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapRevenues(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Revenues r) throws SQLException {
        String sql = "INSERT INTO revenues (titre, description, montant, date_revenue, id_cabinet, cree_par) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idCab = (r.getCabinetMedicale() != null) ? r.getCabinetMedicale().getIdCabinet() : null;

            ps.setString(1, r.getTitre());
            ps.setString(2, r.getDescription());
            ps.setObject(3, r.getMontant());
            ps.setTimestamp(4, r.getDate() != null ? Timestamp.valueOf(r.getDate()) : null);
            ps.setObject(5, idCab);
            ps.setString(6, r.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setIdRevenue(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Revenues r) throws SQLException {
        String sql = "UPDATE revenues SET titre = ?, description = ?, montant = ?, date_revenue = ?, id_cabinet = ?, modifie_par = ? WHERE id_revenue = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idCab = (r.getCabinetMedicale() != null) ? r.getCabinetMedicale().getIdCabinet() : null;

            ps.setString(1, r.getTitre());
            ps.setString(2, r.getDescription());
            ps.setObject(3, r.getMontant());
            ps.setTimestamp(4, r.getDate() != null ? Timestamp.valueOf(r.getDate()) : null);
            ps.setObject(5, idCab);
            ps.setString(6, r.getModifiePar());
            ps.setLong(7, r.getIdRevenue());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Revenues r) throws SQLException {
        if (r != null && r.getIdRevenue() != null) {
            deleteById(r.getIdRevenue());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM revenues WHERE id_revenue = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}