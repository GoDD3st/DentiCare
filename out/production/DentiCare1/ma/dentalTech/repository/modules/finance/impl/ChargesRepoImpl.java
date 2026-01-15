package ma.dentalTech.repository.modules.finance.impl;

import ma.dentalTech.entities.Charges.Charges;
import ma.dentalTech.repository.modules.finance.api.ChargesRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChargesRepoImpl implements ChargesRepo {

    @Override
    public List<Charges> findAll() throws SQLException {
        String sql = "SELECT * FROM charges ORDER BY id_charge";
        List<Charges> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapCharges(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Charges> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM charges WHERE id_charge = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapCharges(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Charges ch) throws SQLException {
        String sql = "INSERT INTO charges (titre, description, montant, date_charge, id_cabinet, cree_par) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idCab = (ch.getCabinetmedicale() != null) ? ch.getCabinetmedicale().getIdCabinet() : null;

            ps.setString(1, ch.getTitre());
            ps.setString(2, ch.getDescription());
            ps.setObject(3, ch.getMontant());
            ps.setTimestamp(4, ch.getDate() != null ? Timestamp.valueOf(ch.getDate()) : null);
            ps.setObject(5, idCab);
            ps.setString(6, ch.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    ch.setIdCharge(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Charges ch) throws SQLException {
        String sql = "UPDATE charges SET titre = ?, description = ?, montant = ?, date_charge = ?, id_cabinet = ?, modifie_par = ? WHERE id_charge = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idCab = (ch.getCabinetmedicale() != null) ? ch.getCabinetmedicale().getIdCabinet() : null;

            ps.setString(1, ch.getTitre());
            ps.setString(2, ch.getDescription());
            ps.setObject(3, ch.getMontant());
            ps.setTimestamp(4, ch.getDate() != null ? Timestamp.valueOf(ch.getDate()) : null);
            ps.setObject(5, idCab);
            ps.setString(6, ch.getModifiePar());
            ps.setLong(7, ch.getIdCharge());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Charges ch) throws SQLException {
        if (ch != null && ch.getIdCharge() != null) {
            deleteById(ch.getIdCharge());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM charges WHERE id_charge = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}