package com.projet2.api.Services;

import com.projet2.api.Entities.EtudiantEntity;

import java.util.List;

public interface IEtudiantService {
    List<EtudiantEntity> findAll();
    EtudiantEntity findById(int idEtudiant) throws Exception;
    EtudiantEntity save(EtudiantEntity etudiant);
    List<EtudiantEntity> saveAll(List<EtudiantEntity> etudiants);
    void deleteByIdStudent(Integer idEtudiant);
}
