package ma.dentalTech.repository.modules.agenda.impl;

import ma.dentalTech.entities.FileAttente.FileAttente;
import ma.dentalTech.repository.modules.agenda.api.fileAttenteRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class fileAttenteRepoImpl implements fileAttenteRepo {

    @Override
    public List<FileAttente> findAll() throws SQLException {
        String sql = "SELECT * FROM file_attente ORDER BY id_file_attente";
        List<FileAttente> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapFileAttente(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<FileAttente> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM file_attente WHERE id_file_attente = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapFileAttente(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(FileAttente fa) throws SQLException {
        String sql = "INSERT INTO file_attente (date_file, capacite, est_ouverte, cree_par, date_creation) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, fa.getDate() != null ? Date.valueOf(fa.getDate()) : null);
            ps.setObject(2, fa.getCapacite());
            ps.setBoolean(3, fa.getEstOuverte() != null ? fa.getEstOuverte() : false);
            ps.setString(4, fa.getCreePar());
            ps.setDate(5, fa.getDateCreation() != null ? Date.valueOf(fa.getDateCreation()) : null);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    fa.setIdFileAttente(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(FileAttente fa) throws SQLException {
        String sql = "UPDATE file_attente SET date_file = ?, capacite = ?, est_ouverte = ?, modifie_par = ?, date_derniere_modification = ? WHERE id_file_attente = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, fa.getDate() != null ? Date.valueOf(fa.getDate()) : null);
            ps.setObject(2, fa.getCapacite());
            ps.setBoolean(3, fa.getEstOuverte() != null ? fa.getEstOuverte() : false);
            ps.setString(4, fa.getModifiePar());
            ps.setTimestamp(5, fa.getDateDerniereModification() != null ? Timestamp.valueOf(fa.getDateDerniereModification()) : null);
            ps.setLong(6, fa.getIdFileAttente());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(FileAttente fa) throws SQLException {
        if (fa != null && fa.getIdFileAttente() != null) {
            deleteById(fa.getIdFileAttente());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM file_attente WHERE id_file_attente = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}