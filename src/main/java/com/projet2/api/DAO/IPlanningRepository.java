package com.projet2.api.DAO;

import com.projet2.api.Entities.PlanningEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlanningRepository extends JpaRepository<PlanningEntity, Integer> {
}
