package com.projet2.api.Services;

import com.projet2.api.Entities.UtilisateurEntity;

public interface IUtilisateurService {
    UtilisateurEntity findByLoginAndMotPasse(String login, String motPasse);
    void save(UtilisateurEntity utilisateur);
    boolean existByLogin(String login);
}
