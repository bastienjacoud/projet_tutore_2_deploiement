package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IEtudiantRepository;
import com.projet2.api.Entities.EtudiantEntity;
import com.projet2.api.Services.IEtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtudiantServiceImpl implements IEtudiantService {

    @Autowired
    IEtudiantRepository etudiantRepository;

    @Override
    public List<EtudiantEntity> findAll() {
        return etudiantRepository.findAll();
    }

    @Override
    public EtudiantEntity findById(int idEtudiant) throws Exception {
        return etudiantRepository.findById(idEtudiant).orElseThrow(() ->
                new Exception("L'étudiant sélectionné n'existe pas."));
    }

    @Override
    public EtudiantEntity save(EtudiantEntity etudiant) {
        etudiantRepository.save(etudiant);
        return etudiant;
    }

    @Override
    public List<EtudiantEntity> saveAll(List<EtudiantEntity> etudiants) {
        etudiantRepository.saveAll(etudiants);
        return etudiants;
    }

    @Override
    public void deleteByIdStudent(Integer idEtudiant) {
        etudiantRepository.deleteById(idEtudiant);
    }
}
