package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IEntrepriseRepository;
import com.projet2.api.Entities.EntrepriseEntity;
import com.projet2.api.Services.IEntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrepriseServiceImpl implements IEntrepriseService {

    @Autowired
    IEntrepriseRepository entrepriseRepository;

    @Override
    public List<EntrepriseEntity> findAll() {
        return entrepriseRepository.findAll();
    }

    @Override
    public EntrepriseEntity findById(int idEtudiant) throws Exception {
        return entrepriseRepository.findById(idEtudiant).orElseThrow(() ->
                new Exception("L'entreprise sélectionnée n'existe pas."));
    }

    @Override
    public EntrepriseEntity save(EntrepriseEntity entreprise) {
        entrepriseRepository.save(entreprise);
        return entreprise;
    }

    @Override
    public List<EntrepriseEntity> saveAll(List<EntrepriseEntity> entreprises) {
        entrepriseRepository.saveAll(entreprises);
        return entreprises;
    }

    @Override
    public void deleteByIdCompany(Integer idEntreprise) {
        entrepriseRepository.deleteById(idEntreprise);
    }
}
