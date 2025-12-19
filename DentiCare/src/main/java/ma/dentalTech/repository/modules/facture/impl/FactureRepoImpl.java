package ma.dentalTech.repository.modules.facturation.impl;

import ma.dentalTech.entities.Facture.Facture;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;
import ma.dentalTech.repository.modules.facture.api.FactureRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactureRepoImpl implements FactureRepository {

    @Override
    public List<Facture> findAll() throws SQLException {
        String sql = "SELECT * FROM facture ORDER BY id_facture";
        List<Facture> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapFacture(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Facture> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM facture WHERE id_facture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapFacture(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(Facture f) throws SQLException {
        String sql = "INSERT INTO facture (totaleFacture, totalePaye, reste, statut, dateFacture, id_situation_financiere, id_consultation, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idSit = (f.getSituationFinanciere() != null) ? f.getSituationFinanciere().getIdSituation() : null;
            Long idCons = (f.getConsultation() != null) ? f.getConsultation().getIdConsultation() : null;

            ps.setObject(1, f.getTotaleFacture());
            ps.setObject(2, f.getTotalePaye());
            ps.setObject(3, f.getReste());
            ps.setString(4, f.getStatut() != null ? f.getStatut().name() : null);
            ps.setTimestamp(5, f.getDateFacture() != null ? Timestamp.valueOf(f.getDateFacture()) : null);
            ps.setObject(6, idSit);
            ps.setObject(7, idCons);
            ps.setString(8, f.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    f.setIdFacture(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(Facture f) throws SQLException {
        String sql = "UPDATE facture SET totaleFacture = ?, totalePaye = ?, reste = ?, statut = ?, dateFacture = ?, id_situation_financiere = ?, id_consultation = ?, modifie_par = ? WHERE id_facture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idSit = (f.getSituationFinanciere() != null) ? f.getSituationFinanciere().getIdSituation() : null;
            Long idCons = (f.getConsultation() != null) ? f.getConsultation().getIdConsultation() : null;

            ps.setObject(1, f.getTotaleFacture());
            ps.setObject(2, f.getTotalePaye());
            ps.setObject(3, f.getReste());
            ps.setString(4, f.getStatut() != null ? f.getStatut().name() : null);
            ps.setTimestamp(5, f.getDateFacture() != null ? Timestamp.valueOf(f.getDateFacture()) : null);
            ps.setObject(6, idSit);
            ps.setObject(7, idCons);
            ps.setString(8, f.getModifiePar());
            ps.setLong(9, f.getIdFacture());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Facture f) throws SQLException {
        if (f != null && f.getIdFacture() != null) {
            deleteById(f.getIdFacture());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM facture WHERE id_facture = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}