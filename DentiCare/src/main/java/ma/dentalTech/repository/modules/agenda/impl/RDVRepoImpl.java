package ma.dentalTech.repository.modules.agenda.impl;

import ma.dentalTech.entities.RDV.RDV;
import ma.dentalTech.repository.modules.agenda.api.RDVRepository;
import ma.dentalTech.conf.SessionFactory;
import ma.dentalTech.repository.common.RowMappers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RDVRepoImpl implements RDVRepository {

    @Override
    public List<RDV> findAll() throws SQLException {
        String sql = "SELECT * FROM rdv ORDER BY id_rdv";
        List<RDV> list = new ArrayList<>();
        try (Connection c = SessionFactory.getInstance().getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(RowMappers.mapRDV(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<RDV> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM rdv WHERE id_rdv = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(RowMappers.mapRDV(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void create(RDV r) throws SQLException {
        String sql = "INSERT INTO rdv (id_entite, id_dossier_medical, id_consultation, date_rdv, heure, motif, statut, noteMedecin, cree_par) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            Long idDossier = (r.getDossierMedicale() != null) ? r.getDossierMedicale().getIdDossier() : null;
            Long idCons = (r.getConsultation() != null) ? r.getConsultation().getIdConsultation() : null;

            if (r.getIdEntite() != null) {
                ps.setLong(1, r.getIdEntite());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }
            ps.setLong(2, idDossier);
            ps.setLong(3, idCons);
            ps.setDate(4, r.getDate() != null ? Date.valueOf(r.getDate()) : null);
            ps.setTime(5, r.getHeure() != null ? Time.valueOf(r.getHeure()) : null);
            ps.setString(6, r.getMotif());
            ps.setString(7, r.getStatut() != null ? r.getStatut().name() : null);
            ps.setString(8, r.getNoteMedecin());
            ps.setString(9, r.getCreePar());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setIdRDV(keys.getLong(1));
                }
            }
        }
    }

    @Override
    public void update(RDV r) throws SQLException {
        String sql = "UPDATE rdv SET id_dossier_medical = ?, id_consultation = ?, date_rdv = ?, heure = ?, motif = ?, statut = ?, noteMedecin = ?, modifie_par = ?, date_derniere_modification = CURRENT_TIMESTAMP WHERE id_rdv = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Long idDossier = (r.getDossierMedicale() != null) ? r.getDossierMedicale().getIdDossier() : null;
            Long idCons = (r.getConsultation() != null) ? r.getConsultation().getIdConsultation() : null;

            ps.setLong(1, idDossier);
            ps.setLong(2, idCons);
            ps.setDate(3, r.getDate() != null ? Date.valueOf(r.getDate()) : null);
            ps.setTime(4, r.getHeure() != null ? Time.valueOf(r.getHeure()) : null);
            ps.setString(5, r.getMotif());
            ps.setString(6, r.getStatut() != null ? r.getStatut().name() : null);
            ps.setString(7, r.getNoteMedecin());
            ps.setString(8, r.getModifiePar());
            ps.setLong(9, r.getIdRDV());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(RDV r) throws SQLException {
        if (r != null && r.getIdRDV() != null) {
            deleteById(r.getIdRDV());
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM rdv WHERE id_rdv = ?";
        try (Connection c = SessionFactory.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}