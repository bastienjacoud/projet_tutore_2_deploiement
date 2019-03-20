package com.projet2.api.DAO;

import com.projet2.api.Entities.UtilisateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;


public interface IUtilisateurRepository extends JpaRepository<UtilisateurEntity, Integer> {
    UtilisateurEntity findByLoginAndMotPasse(String login, String motPasse);
    boolean existsByLogin(String login);
    Optional<UtilisateurEntity> findByEntrepriseIdEntreprise(Integer idEntreprise);
    Optional<UtilisateurEntity> findByAdministrateurIdAdministrateur(Integer idAdmin);
    Optional<UtilisateurEntity> findByEtudiantIdEtudiant(Integer idEtudiant);
}