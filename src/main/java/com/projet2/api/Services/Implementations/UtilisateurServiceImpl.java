package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IUtilisateurRepository;
import com.projet2.api.Entities.UtilisateurEntity;
import com.projet2.api.Services.IUtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public void saveAll(List<UtilisateurEntity> utilisateurs) {
        utilisateurRepository.saveAll(utilisateurs);
    }

    @Override
    public boolean existByLogin(String login){
        return utilisateurRepository.existsByLogin(login);
    }

    @Override
    public List<UtilisateurEntity> findAll() {
        return utilisateurRepository.findAll();
    }

    @Override
    public UtilisateurEntity findByIdEtudiant(int idEtudiant) throws Exception {
        return utilisateurRepository.findByEtudiantIdEtudiant(idEtudiant).orElseThrow(() ->
                new Exception("L'utilisateur sélectionné n'existe pas."));
    }

    @Override
    public UtilisateurEntity findByIdEntreprise(int idEntreprise) throws Exception {
        return utilisateurRepository.findByEntrepriseIdEntreprise(idEntreprise).orElseThrow(() ->
                new Exception("L'utilisateur sélectionné n'existe pas."));
    }

    @Override
    public UtilisateurEntity findByIdAdmin(int idAdmin) throws Exception {
        return utilisateurRepository.findByAdministrateurIdAdministrateur(idAdmin).orElseThrow(() ->
                new Exception("L'utilisateur sélectionné n'existe pas."));
    }
}

