package com.projet2.api.Services;

import com.projet2.api.Entities.UtilisateurEntity;

import java.util.List;

public interface IUtilisateurService {
    UtilisateurEntity findByLoginAndMotPasse(String login, String motPasse);
    void save(UtilisateurEntity utilisateur);
    void saveAll(List<UtilisateurEntity> utilisateurs);
    boolean existByLogin(String login);
    List<UtilisateurEntity> findAll();
    UtilisateurEntity findByIdEtudiant(int idEtudiant) throws Exception;
    UtilisateurEntity findByIdEntreprise(int idEntreprise) throws Exception;
    UtilisateurEntity findByIdAdmin(int idAdmin) throws Exception;
}
