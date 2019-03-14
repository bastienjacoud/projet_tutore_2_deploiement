package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IUtilisateurRepository;
import com.projet2.api.Entities.UtilisateurEntity;
import com.projet2.api.Services.IUtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements IUtilisateurService {

    @Autowired
    private IUtilisateurRepository utilisateurRepository;

    @Override
    public UtilisateurEntity findByLoginAndMotPasse(String login, String motPasse) {
        return utilisateurRepository.findByLoginAndMotPasse(login, motPasse);
    }

    @Override
    public void save(UtilisateurEntity utilisateur) {
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public boolean existByLogin(String login){
        return utilisateurRepository.existsByLogin(login);
    }
}

