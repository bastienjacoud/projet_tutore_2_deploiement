package com.projet2.api.Services.Implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.projet2.api.DAO.IPlanningRepository;
import com.projet2.api.Entities.ChoixEntity;
import com.projet2.api.Entities.PlanningEntity;
import com.projet2.api.Services.IPlanningService;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanningServiceImpl implements IPlanningService {

    @Autowired
    IPlanningRepository planningRepository;
    private int nbCreneaux = 8; // 4h d'entretiens de 30 min

    @Override
    public List<PlanningEntity> findAll() {
        return planningRepository.findAll();
    }

    @Override
    public void generate(List<ChoixEntity> choix) {
        int nbGenerations = 100;
        double scoreMax = Double.NEGATIVE_INFINITY;
        List<ChoixEntity> planningOptimum = new ArrayList<>();

        // 1. Préparation
        Map<Integer, List<ChoixEntity>> choixScore = getChoixScore(choix);

        // 2. Génération des différentes possibilités
        // et sélection de la meilleure
        for (int i = 0; i < nbGenerations; i++) {
            // Copie de la map
            List<ChoixEntity> planningPossible = new ArrayList<ChoixEntity>();

            double scorePlanning = generatePlanning(choixScore, planningPossible);

            // On vérifie si c'est un meilleur planning
            if (scoreMax < scorePlanning) {
                scoreMax = scorePlanning;
                planningOptimum = planningPossible;
            }
        }

        insertCreneaux(choix);

    }

    /* Méthodes privées */
    private Map<Integer, List<ChoixEntity>> getChoixScore(List<ChoixEntity> choix) {
        Map<Integer, List<ChoixEntity>> choixScore = new TreeMap<>(Collections.reverseOrder());

        for (ChoixEntity c : choix) {
            int score = calculateScore(c);
            if (choixScore.get(score) == null) {
                choixScore.put(score, new ArrayList<>());
            }
            List<ChoixEntity> listeChoix = choixScore.get(score);
            listeChoix.add(c);
            choixScore.replace(score, listeChoix);
        }

        return choixScore;
    }

    private int calculateScore(ChoixEntity choix) {
        int multiplicateur = 1;
        int score = 0;

        if (choix.getSuperMatchEntreprise() == 1)
            multiplicateur *= 2;
        if (choix.getSuperMatchEtudiant() == 1)
            multiplicateur *= 2;
        if (choix.getChoixEntreprise() == 1)
            score += 1;
        if (choix.getChoixEtudiant() == 1)
            score += 1;

        // Si les deux parties veulent se rencontrer
        if (score == 2) {
            score = 3;
        }

        return score * multiplicateur;
    }

    private double generatePlanning(Map<Integer, List<ChoixEntity>> choixScore, List<ChoixEntity> planningPossible) {
        Map<Integer, Integer> nombreCreneauxUtilises = new HashMap<>();
        double score = 0;

        for (Entry<Integer, List<ChoixEntity>> elt : choixScore.entrySet()) {
            Collections.shuffle(elt.getValue());
            for(ChoixEntity choix : elt.getValue()) {
                if(nombreCreneauxUtilises.get(choix.getIdEntreprise()) == null || nombreCreneauxUtilises.get(choix.getIdEntreprise()) < nbCreneaux) {
                    planningPossible.add(choix);

                    if(nombreCreneauxUtilises.get(choix.getIdEntreprise()) == null) {
                        nombreCreneauxUtilises.put(choix.getIdEntreprise(), 0);
                    }
                    int nbCreneauxUtilisesEnt = nombreCreneauxUtilises.get(choix.getIdEntreprise());
                    nbCreneauxUtilisesEnt += 1;
                    nombreCreneauxUtilises.replace(choix.getIdEntreprise(), nbCreneauxUtilisesEnt);

                    score += elt.getKey();
                }
                

            }
        }

        // Ajout du malus
        score += addMalusScore(nombreCreneauxUtilises);

        return score;
    }

    private double addMalusScore(Map<Integer,Integer> nombreCreneauxUtilises) {
        int malus = -5;
        int nbCreneauxInutilises = 0;

        for(Entry<Integer,Integer> elt : nombreCreneauxUtilises.entrySet()) {
            nbCreneauxInutilises = nbCreneaux - elt.getValue();
        }

        return nbCreneauxInutilises * malus;
    }

    private void insertCreneaux(List<ChoixEntity> choix) {
        Map<Integer,Integer> sauvegardeOrdreCreneaux = new HashMap<>();
        planningRepository.deleteAll();

        for(ChoixEntity c : choix) {
            int creneau = 0;
            if(sauvegardeOrdreCreneaux.get(c.getIdEntreprise()) == null) {
                sauvegardeOrdreCreneaux.put(c.getIdEntreprise(), 1);
            }
            creneau = sauvegardeOrdreCreneaux.get(c.getIdEntreprise());
            PlanningEntity p = new PlanningEntity();
            p.setOrdre(creneau);
            p.setIdChoix(c.getIdChoix());           
            planningRepository.save(p);

            sauvegardeOrdreCreneaux.put(c.getIdEntreprise(),creneau + 1);
        }
    }
}
