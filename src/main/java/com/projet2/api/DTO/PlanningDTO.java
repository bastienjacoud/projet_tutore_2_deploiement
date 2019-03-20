
package com.projet2.api.DTO;

import com.projet2.api.Entities.EntrepriseEntity;
import com.projet2.api.Entities.EtudiantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlanningDTO {
    private Integer ordre;
    private EntrepriseEntity entreprise;
    private EtudiantEntity etudiant;
}