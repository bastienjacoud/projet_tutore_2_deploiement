package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IParametresApplicationRepository;
import com.projet2.api.Entities.ParametresapplicationEntity;
import com.projet2.api.Services.IParametresApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParametresApplicationServiceImpl implements IParametresApplicationService {

    @Autowired
    private IParametresApplicationRepository parametresApplicationRepository;

    /**
     * Récupère la première et seule instance de paramètres de l'appli
     * @return
     */
    @Override
    public ParametresapplicationEntity findFirst() throws Exception {
        List<ParametresapplicationEntity> res = parametresApplicationRepository.findAll();
        if(res.size() == 0){
            throw new Exception("Il n'existe pas de paramètres d'application de définis");
        }
        else {
            return res.get(0);
        }
    }

    @Override
    public ParametresapplicationEntity save(ParametresapplicationEntity parametres) {
        parametresApplicationRepository.save(parametres);
        return parametres;
    }

    @Override
    public boolean choicesAvailable() throws Exception {
        List<ParametresapplicationEntity> res = parametresApplicationRepository.findAll();
        if(res.size() == 0){
            throw new Exception("Il n'existe pas de paramètres d'application de définis");
        }
        else {
            return res.get(0).getChoixFerme() == 0;
        }
    }

    @Override
    public boolean schedulesAvailable() throws Exception {
        List<ParametresapplicationEntity> res = parametresApplicationRepository.findAll();
        if(res.size() == 0){
            throw new Exception("Il n'existe pas de paramètres d'application de définis");
        }
        else {
            return res.get(0).getPlanningFerme() == 0;
        }
    }
}
