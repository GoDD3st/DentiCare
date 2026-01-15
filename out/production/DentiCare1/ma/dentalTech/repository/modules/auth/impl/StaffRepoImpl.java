package ma.dentalTech.repository.modules.auth.impl;

import ma.dentalTech.entities.Staff.Staff;
import ma.dentalTech.repository.modules.auth.api.StaffRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffRepoImpl implements StaffRepo {

    @Override
    public List<Staff> findAll() throws SQLException {
        String sql = "SELECT * FROM staff ORDER BY id_staff";
        List<Staff> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapStaff(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Staff> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM staff WHERE id_staff = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapStaff(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Staff s) throws SQLException {
        String sql = "INSERT INTO staff (id_utilisateur, salaire, prime, date_recrutement, solde_conge, id_cabinet, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idCab = (s.getCabinetMedicale() != null) ? s.getCabinetMedicale().getIdCabinet() : null;

            ps.setLong(1, s.getIdUser());
            ps.setDouble(2, s.getSalaire());
            ps.setDouble(3, s.getPrime());
            ps.setDate(4, s.getDateRecrutement() != null ? Date.valueOf(s.getDateRecrutement()) : null);
            ps.setObject(5, s.getSoldeConge());
            ps.setObject(6, idCab);
            ps.setString(7, s.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    s.setIdStaff(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Staff s) throws SQLException {
        String sql = "UPDATE staff SET salaire = ?, prime = ?, date_recrutement = ?, solde_conge = ?, id_cabinet = ?, modifie_par = ? WHERE id_staff = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idCab = (s.getCabinetMedicale() != null) ? s.getCabinetMedicale().getIdCabinet() : null;

            ps.setDouble(1, s.getSalaire());
            ps.setDouble(2, s.getPrime());
            ps.setDate(3, s.getDateRecrutement() != null ? Date.valueOf(s.getDateRecrutement()) : null);
            ps.setObject(4, s.getSoldeConge());
            ps.setObject(5, idCab);
            ps.setString(6, s.getModifiePar());
            ps.setLong(7, s.getIdStaff());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Staff s) throws SQLException {
        if (s != null && s.getIdStaff() != null) {
            deleteById(s.getIdStaff());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM staff WHERE id_staff = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}