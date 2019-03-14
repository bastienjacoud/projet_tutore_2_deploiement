package com.projet2.api.DTO;

import lombok.*;

@Data
public class EtudiantDTO {
    private Integer idEtudiant;
    private String prenom;
    private String nom;
    private String presentation;
    private byte[] avatar;
}
