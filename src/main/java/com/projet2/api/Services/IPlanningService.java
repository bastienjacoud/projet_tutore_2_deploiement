package com.projet2.api.Services;

import com.projet2.api.Entities.ChoixEntity;

import java.util.List;

public interface IPlanningService {
    List<Object[]> findAll();
    List<ChoixEntity> generate(List<ChoixEntity> choix);
    List<Object[]> findAllDetailed();
    List<Object[]> findAllByStudentDetailed(int id);
    List<Object[]> findAllByCompanyDetailed(int id);
}
