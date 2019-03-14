package com.projet2.api.DAO;

import com.projet2.api.Entities.ChoixEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IChoixRepository extends JpaRepository<ChoixEntity, Integer> {
    List<ChoixEntity> findAllByIdEntrepriseAndChoixEntreprise(Integer idEntreprise, Integer choixEntreprise);
    List<ChoixEntity> findAllByIdEtudiantAndChoixEtudiant(Integer idEtudiant, Integer choixEtudiant);
    List<ChoixEntity> findAllByIdEntrepriseAndSuperMatchEntreprise(Integer idEntreprise, Integer superMatchEntreprise);
    List<ChoixEntity> findAllByIdEtudiantAndSuperMatchEtudiant(Integer idEtudiant, Integer superMatchEtudiant);
    ChoixEntity findByIdEntrepriseAndIdEtudiant(Integer idEntreprise, Integer IdEtudiant);
    List<ChoixEntity> findAll();
    List<ChoixEntity> findAllByIdEtudiant(Integer idEtudiant);
    List<ChoixEntity> findAllByIdEntreprise(Integer idEntreprise);
}
