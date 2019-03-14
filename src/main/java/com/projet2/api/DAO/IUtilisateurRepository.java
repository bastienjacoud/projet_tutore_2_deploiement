package com.projet2.api.DAO;

import com.projet2.api.Entities.UtilisateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUtilisateurRepository extends JpaRepository<UtilisateurEntity, Integer> {
    UtilisateurEntity findByLoginAndMotPasse(String login, String motPasse);
    boolean existsByLogin(String login);
}