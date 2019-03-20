package com.projet2.api.Services;

import com.projet2.api.Entities.EntrepriseEntity;

import java.util.List;

public interface IEntrepriseService {
    List<EntrepriseEntity> findAll();
    EntrepriseEntity findById(int idEtudiant) throws Exception;
    EntrepriseEntity save(EntrepriseEntity entreprise);
    List<EntrepriseEntity> saveAll(List<EntrepriseEntity> entreprises);
    void deleteByIdCompany(Integer idEntreprise);
}
