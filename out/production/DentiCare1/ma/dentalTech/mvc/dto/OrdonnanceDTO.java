package ma.dentalTech.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

// ====================================
//
// Author : Marouane
//
// ====================================

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdonnanceDTO {
    private Long id;
    private LocalDate date;
    private Long consultationId;
    private String consultationObservation;
    private List<PrescriptionDTO> prescriptions;
}
