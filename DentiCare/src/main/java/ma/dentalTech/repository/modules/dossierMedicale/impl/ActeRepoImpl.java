package ma.dentalTech.repository.modules.dossierMedicale.impl;

import ma.dentalTech.entities.Acte.Acte;
import ma.dentalTech.repository.modules.dossierMedicale.api.ActeRepo;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActeRepoImpl implements ActeRepo {


    @Override
    public List<Acte> findAll() throws SQLException{
        String sql = "SELECT * FROM acte";
        List<Acte> list = new ArrayList<>();
        try (Connection conn = SessionFactory.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(RowMappers.mapActe(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des actes", e);
        }
        return list;
    }

    @Override
    public Optional<Acte> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM acte WHERE id_acte = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(RowMappers.mapActe(rs));
                return null;
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void create(Acte a) throws SQLException{
        String sql = "INSERT INTO acte (libelle, categorie, prix_de_base, date_creation) VALUES (?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getLibelle());
            ps.setString(2,a.getCategorie());
            ps.setDouble(3,a.getPrixDeBase());
            // dateCreation (LocalDate)
            LocalDate dateCreation = a.getDateCreation() != null ? a.getDateCreation() : LocalDate.now();
            ps.setDate(4, Date.valueOf(dateCreation));
            // on met à jour l'objet en mémoire
            a.setDateCreation(dateCreation);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setIdActe(keys.getLong(1));}
            }

        } catch (SQLException e)
        { throw new RuntimeException(e); }
    }

    @Override
    public void update(Acte a) throws SQLException {
        String sql = "UPDATE acte SET libelle = ?, categorie = ?, prix_de_base = ?, date_derniere_modification = ?  WHERE id_acte = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getLibelle());
            ps.setString(2,a.getCategorie());
            ps.setDouble(3,a.getPrixDeBase());
            ps.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setLong(5, a.getIdActe());

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setIdActe(keys.getLong(1));}
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Acte a) throws SQLException {
        if (a != null) deleteById(a.getIdActe());
    }

    @Override
    public void deleteById(Long id) throws SQLException{
        String sql = "DELETE FROM acte WHERE id_acte = ?";
        try (Connection conn = SessionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
