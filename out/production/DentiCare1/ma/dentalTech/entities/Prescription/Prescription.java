package ma.dentalTech.entities.Prescription;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Medicament.Medicament;
import lombok.experimental.SuperBuilder;
import lombok.Data;

@Data
@SuperBuilder

public class Prescription extends BaseEntity {
    private Long idPrescription;
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;

    private Ordonnance ordonnance; // Relation (Prescription) n--1 (Ordonnance)
    private Medicament medicament;
    public static Prescription createTestInstance() {
        return Prescription.builder()
                .quantite(10)
                .frequence("1 fois par jour")
                .dureeEnJours(10)
                .build();
    }
}