package com.projet2.api.Services.Implementations;

import java.sql.Time;
import java.util.*;
import java.util.Map.Entry;

import com.projet2.api.DAO.IPlanningRepository;
import com.projet2.api.Entities.ChoixEntity;
import com.projet2.api.Entities.ParametresapplicationEntity;
import com.projet2.api.Entities.PlanningEntity;
import com.projet2.api.Services.IParametresApplicationService;
import com.projet2.api.Services.IPlanningService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanningServiceImpl implements IPlanningService {

    @Autowired
    IPlanningRepository planningRepository;

    // Voir : https://stackoverflow.com/questions/32970873/questions-regarding-design-call-a-service-from-another-service
    @Autowired
    IParametresApplicationService parametresApplicationService;

    private int nbCreneaux = 8; // 4h d'entretiens de 30 min (défaut)

    @Override
    public List<Object[]> findAll() {
        return planningRepository.getDetailedPlannings();
    }

    @Override
    public List<ChoixEntity> generate(List<ChoixEntity> choix) {
        int nbGenerations = 100;

        // Calcul du nombres de créneaux via les paramètres
        nbCreneaux = getNbSlots();

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

        insertCreneaux(planningOptimum);
        return planningOptimum;
    }

    /* Méthodes privées */
    private int getNbSlots() {
        int nbDefaut = 8;
        try {
            ParametresapplicationEntity parametresapplicationEntity = parametresApplicationService.findFirst();
            if(parametresapplicationEntity == null) {
                return nbDefaut;
            } else {
                Time startTime = parametresapplicationEntity.getDebutEntretiens();
                Time endTime = parametresapplicationEntity.getFinEntretiens();
                int dureeEntretiens = parametresapplicationEntity.getDureeEntretiens();

                return Math.toIntExact((endTime.toLocalTime().toSecondOfDay() - startTime.toLocalTime().toSecondOfDay()) / (dureeEntretiens * 60));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return nbDefaut; // Nombre par défaut
        }
    }

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
        Map<Integer, List<Integer>> listeEtudiantsParCreneaux = new HashMap<>();
        List<ChoixEntity> choixNonPlaces = new ArrayList<>();
        double score = 0;

        // Initialisation liste des étudiants par créneaux
        // Cette liste va permettre de connaître les étudiants pour un créneau (1,2,3, ...) donné
        for(int i = 0; i < nbCreneaux; i++) {
            listeEtudiantsParCreneaux.put(i, new ArrayList<>());
        }

        for (Entry<Integer, List<ChoixEntity>> elt : choixScore.entrySet()) {
            Collections.shuffle(elt.getValue());
            for(ChoixEntity choix : elt.getValue()) {
                // On va d'abord placer les choix plus élevés non placés
                ArrayList<ChoixEntity> choixNonPlacesNew = new ArrayList<>();
                for(ChoixEntity choixNonPlace : choixNonPlaces) {
                    if (propositionCreneauPossible(nombreCreneauxUtilises, choixNonPlace, choixNonPlacesNew, listeEtudiantsParCreneaux, planningPossible)) {
                        score += elt.getKey();

                    }
                }

                choixNonPlaces = choixNonPlacesNew;

                if (propositionCreneauPossible(nombreCreneauxUtilises, choix, choixNonPlaces, listeEtudiantsParCreneaux, planningPossible)) {
                    score += elt.getKey();
                }
            }
        }

        // Ajout du malus
        score += addMalusScore(nombreCreneauxUtilises);

        return score;
    }

    private boolean propositionCreneauPossible(Map<Integer, Integer> nombreCreneauxUtilises, ChoixEntity choix, List<ChoixEntity> choixNonPlaces, Map<Integer, List<Integer>> listeEtudiantsParCreneaux, List<ChoixEntity> planningPossible) {
        if(nombreCreneauxUtilises.get(choix.getIdEntreprise()) == null || nombreCreneauxUtilises.get(choix.getIdEntreprise()) < nbCreneaux) {

            if(nombreCreneauxUtilises.get(choix.getIdEntreprise()) == null) {
                nombreCreneauxUtilises.put(choix.getIdEntreprise(), 0);
            }
            int nbCreneauxUtilisesEnt = nombreCreneauxUtilises.get(choix.getIdEntreprise());

            List<Integer> listeEtudiantsOld = listeEtudiantsParCreneaux.get(nbCreneauxUtilisesEnt);
            List<Integer> listeEtudiantsNew = new ArrayList<>(listeEtudiantsParCreneaux.get(nbCreneauxUtilisesEnt));
            listeEtudiantsNew.add(choix.getIdEtudiant());
            listeEtudiantsParCreneaux.replace(nbCreneauxUtilisesEnt,listeEtudiantsNew);

            if(!verificationDoublonsCreneau(listeEtudiantsParCreneaux)) { // S'il y un risque d'incohérence (un étudiant à deux créneaux au même moment)
                listeEtudiantsParCreneaux.replace(nbCreneauxUtilisesEnt,listeEtudiantsOld);
                choixNonPlaces.add(choix);
                return false;
            } else {

                planningPossible.add(choix);

                nbCreneauxUtilisesEnt += 1;
                nombreCreneauxUtilises.replace(choix.getIdEntreprise(), nbCreneauxUtilisesEnt);

                return true;
            }
        }
        return false;
    }

    private boolean verificationDoublonsCreneau(Map<Integer, List<Integer>> listeEtudiantParCreneaux) {
        int malus = -100000;

        for(List<Integer> listeEtudiants : listeEtudiantParCreneaux.values()) {
            if(!listeEtudiants.stream().sequential().allMatch(new HashSet<>()::add)) { // Vérification des doublons
                return false;
            }
        }

        return true;

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

    @Override
    public List<Object[]> findAllDetailed() {
        return planningRepository.getDetailedPlannings();
    }

    @Override
    public List<Object[]> findAllByStudentDetailed(int id) {
        return planningRepository.getDetailedPlanningsByStudentId(id);
    }

    @Override
    public List<Object[]> findAllByCompanyDetailed(int id) {
        return planningRepository.getDetailedPlanningsByCompanyId(id);
    }
}
