package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IChoixRepository;
import com.projet2.api.Entities.ChoixEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Services.IChoixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChoixServiceImpl implements IChoixService {

    private static final int MAX_SUPERMACTH_NUMBER = 3;

    @Autowired
    IChoixRepository choixRepository;

    @Override
    public List<ChoixEntity> addChoixEtudiant(Integer idEtudiant, List<Integer> idEntreprises, List<Integer> idSuperMatchEntreprise){
        // Reset (remise à 0) des valeurs des anciennes données
        List<ChoixEntity> oldValues = getOldChoixList(idEtudiant, idEntreprises, idSuperMatchEntreprise, RoleEnum.ETUDIANT);
        choixRepository.saveAll(oldValues);

        // Mise à jour des données existantes et insertion des nouvelles données
        List<ChoixEntity> values = getChoixList(idEtudiant, idEntreprises, idSuperMatchEntreprise, RoleEnum.ETUDIANT);
        choixRepository.saveAll(values);

        return values;
    }

    @Override
    public List<ChoixEntity> addChoixEntreprise(Integer idEntreprise, List<Integer> idEtudiants, List<Integer> idSuperMatchEtudiant){
        // Reset (remise à 0) des valeurs des anciennes données
        List<ChoixEntity> oldValues = getOldChoixList(idEntreprise, idEtudiants, idSuperMatchEtudiant, RoleEnum.ENTREPRISE);
        choixRepository.saveAll(oldValues);

        // Mise à jour des données existantes et insertion des nouvelles données
        List<ChoixEntity> values = getChoixList(idEntreprise, idEtudiants, idSuperMatchEtudiant, RoleEnum.ENTREPRISE);
        choixRepository.saveAll(values);

        return values;
    }

    @Override
    public List<ChoixEntity> findByIdEntreprise(Integer idEntreprise) {
        List<ChoixEntity> res = choixRepository.findAllByIdEntrepriseAndChoixEntreprise(idEntreprise, 1);
        res.addAll(choixRepository.findAllByIdEntrepriseAndSuperMatchEntreprise(idEntreprise, 1));
        return res;
    }

    @Override
    public List<ChoixEntity> findByIdEtudiant(Integer idEtudiant) {
        List<ChoixEntity> res = choixRepository.findAllByIdEtudiantAndChoixEtudiant(idEtudiant, 1);
        res.addAll(choixRepository.findAllByIdEtudiantAndSuperMatchEtudiant(idEtudiant, 1));
        return res;
    }

    @Override
    public int getSuperMatchNumber(Integer id, RoleEnum role){
        if(role == RoleEnum.ETUDIANT){
            return MAX_SUPERMACTH_NUMBER - choixRepository.findAllByIdEtudiantAndSuperMatchEtudiant(id, 1).size();
        }
        else if(role == RoleEnum.ENTREPRISE){
            return MAX_SUPERMACTH_NUMBER - choixRepository.findAllByIdEntrepriseAndSuperMatchEntreprise(id, 1).size();
        }
        else{
            return 0;
        }
    }

    private List<ChoixEntity> getChoixList(Integer id, List<Integer> idList, List<Integer> idSuperMatchList, RoleEnum role){
        List<ChoixEntity> matchs = getChoixList(id, idList, false, role);
        List<ChoixEntity> supermatchs = getChoixList(id, idSuperMatchList, true, role);

        matchs.addAll(supermatchs);
        return matchs;
    }

    private List<ChoixEntity> getChoixList(Integer id, List<Integer> idMatch, boolean isSuperMatch, RoleEnum role){
        return idMatch.stream()
                .map(id1 -> {
                    ChoixEntity res = new ChoixEntity();
                    if(role == RoleEnum.ENTREPRISE){
                        ChoixEntity value = choixRepository.findByIdEntrepriseAndIdEtudiant(id, id1);
                        if(value == null){
                            res.setIdEntreprise(id);
                            res.setIdEtudiant(id1);
                            res.setChoixEntreprise(1);
                            res.setChoixEtudiant(0);
                            if(isSuperMatch){
                                res.setSuperMatchEntreprise(1);
                            }
                            else {
                                res.setSuperMatchEntreprise(0);
                            }
                            res.setSuperMatchEtudiant(0);
                        }
                        else{
                            res = value;
                            res.setChoixEntreprise(1);
                            if(isSuperMatch){
                                res.setSuperMatchEntreprise(1);
                            }
                        }
                    }
                    else if(role == RoleEnum.ETUDIANT){
                        ChoixEntity value = choixRepository.findByIdEntrepriseAndIdEtudiant(id1, id);
                        if(value == null){
                            res.setIdEntreprise(id1);
                            res.setIdEtudiant(id);
                            res.setChoixEntreprise(0);
                            res.setChoixEtudiant(1);
                            if(isSuperMatch){
                                res.setSuperMatchEtudiant(1);
                            }
                            else{
                                res.setSuperMatchEtudiant(0);
                            }
                            res.setSuperMatchEntreprise(0);
                        }
                        else{
                            res = value;
                            res.setChoixEtudiant(1);
                            if(isSuperMatch){
                                res.setSuperMatchEtudiant(1);
                            }
                        }
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }

    private List<ChoixEntity> getOldChoixList(Integer id, List<Integer> idList, List<Integer> idSuperMatchList, RoleEnum role){
        List<ChoixEntity> oldValues = new ArrayList<>();
        if(role == RoleEnum.ETUDIANT){
            oldValues = choixRepository.findAllByIdEtudiant(id);
            oldValues = oldValues.stream().map(v -> {
                if(!idList.contains(v.getIdEntreprise())){
                    v.setChoixEtudiant(0);
                    v.setChoixEntreprise(0);
                    v.setSuperMatchEtudiant(0);
                    v.setSuperMatchEntreprise(0);
                }
                else if(!idSuperMatchList.contains(v.getIdEntreprise())){
                    v.setChoixEtudiant(0);
                    v.setChoixEntreprise(0);
                    v.setSuperMatchEtudiant(0);
                    v.setSuperMatchEntreprise(0);
                }
                return v;
            }).collect(Collectors.toList());
        }
        else if(role == RoleEnum.ENTREPRISE){
            oldValues = choixRepository.findAllByIdEntreprise(id);
            oldValues = oldValues.stream().map(v -> {
                if(!idList.contains(v.getIdEtudiant())){
                    v.setChoixEtudiant(0);
                    v.setChoixEntreprise(0);
                    v.setSuperMatchEtudiant(0);
                    v.setSuperMatchEntreprise(0);
                }
                else if(!idSuperMatchList.contains(v.getIdEtudiant())){
                    v.setChoixEtudiant(0);
                    v.setChoixEntreprise(0);
                    v.setSuperMatchEtudiant(0);
                    v.setSuperMatchEntreprise(0);
                }
                return v;
            }).collect(Collectors.toList());
        }
        return oldValues;
    }

    @Override
    public List<ChoixEntity> findAll() {
        return choixRepository.findAll();
    }
}
