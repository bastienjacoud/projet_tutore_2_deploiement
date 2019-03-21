package com.projet2.api.Services;

import com.projet2.api.Entities.ChoixEntity;
import com.projet2.api.Entities.EntrepriseEntity;
import com.projet2.api.Entities.EtudiantEntity;
import com.projet2.api.Enums.RoleEnum;

import java.util.List;

public interface IChoixService {
    List<ChoixEntity> addChoixEtudiant(Integer idEtudiant, List<Integer> idEntreprises, List<Integer> idSuperMatchEntreprise);
    List<ChoixEntity> addChoixEntreprise(Integer idEntreprise, List<Integer> idEtudiants, List<Integer> idSuperMatchEntreprise);
    List<ChoixEntity> findByIdEntreprise(Integer idEntreprise);
    List<ChoixEntity> findByIdEtudiant(Integer idEtudiant);
    List<ChoixEntity> findAll();
    int getSuperMatchNumber(Integer id, RoleEnum role);
    void insertDefaultChoice(EtudiantEntity etu, EntrepriseEntity ent);
}
