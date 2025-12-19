package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Prescription.Prescription;
import ma.dentalTech.repository.modules.dossierMedicale.api.PrescriptionRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrescriptionRepoImpl implements PrescriptionRepo {

    @Override
    public List<Prescription> findAll() throws SQLException {
        String sql = "SELECT * FROM prescription ORDER BY id_prescription";
        List<Prescription> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapPrescription(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Prescription> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM prescription WHERE id_prescription = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapPrescription(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Prescription p) throws SQLException {
        String sql = "INSERT INTO prescription (quantite, frequence, dureeEnJours, id_ordonnance, id_medicament, cree_par) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Extraction des IDs des objets liés
            Long idOrd = (p.getOrdonnance() != null) ? p.getOrdonnance().getIdOrdonnance() : null;
            Long idMed = (p.getMedicament() != null) ? p.getMedicament().getIdMedicament() : null;

            ps.setObject(1, p.getQuantite());
            ps.setString(2, p.getFrequence());
            ps.setObject(3, p.getDureeEnJours());
            ps.setObject(4, idOrd);
            ps.setObject(5, idMed);
            ps.setString(6, p.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setIdPrescription(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Prescription p) throws SQLException {
        String sql = "UPDATE prescription SET quantite = ?, frequence = ?, dureeEnJours = ?, id_ordonnance = ?, id_medicament = ?, modifie_par = ? WHERE id_prescription = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idOrd = (p.getOrdonnance() != null) ? p.getOrdonnance().getIdOrdonnance() : null;
            Long idMed = (p.getMedicament() != null) ? p.getMedicament().getIdMedicament() : null;

            ps.setObject(1, p.getQuantite());
            ps.setString(2, p.getFrequence());
            ps.setObject(3, p.getDureeEnJours());
            ps.setObject(4, idOrd);
            ps.setObject(5, idMed);
            ps.setString(6, p.getModifiePar());
            ps.setLong(7, p.getIdPrescription());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Prescription p) throws SQLException {
        if (p != null && p.getIdPrescription() != null) {
            deleteById(p.getIdPrescription());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM prescription WHERE id_prescription = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}