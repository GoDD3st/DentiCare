package ma.dentalTech.entities.Prescription;

import ma.dentalTech.entities.BaseEntity.BaseEntity;
import ma.dentalTech.entities.Ordonnance.Ordonnance;
import ma.dentalTech.entities.Medicament.Medicament;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prescription extends BaseEntity {
    private Long idPrescription;
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;

    private Ordonnance ordonnance;
    private Medicament medicament;
}