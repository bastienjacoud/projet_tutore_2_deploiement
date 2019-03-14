package com.projet2.api.Services;

import com.projet2.api.Entities.ChoixEntity;
import com.projet2.api.Entities.PlanningEntity;

import java.util.List;

public interface IPlanningService {
    List<PlanningEntity> findAll();
    void generate(List<ChoixEntity> choix);
}
