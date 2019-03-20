package com.projet2.api.DAO;

import java.util.List;

import com.projet2.api.DTO.PlanningDTO;
import com.projet2.api.Entities.PlanningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPlanningRepository extends JpaRepository<PlanningEntity, Integer> {

    @Query("SELECT p.ordre, ent, etu FROM PlanningEntity p, ChoixEntity c, EtudiantEntity etu, EntrepriseEntity ent WHERE p.idChoix = c.idChoix AND etu.idEtudiant = c.idEtudiant AND ent.idEntreprise = c.idEntreprise")
    public List<Object[]> getDetailedPlannings();

    @Query("SELECT p.ordre, ent, etu FROM PlanningEntity p, ChoixEntity c, EtudiantEntity etu, EntrepriseEntity ent WHERE p.idChoix = c.idChoix AND etu.idEtudiant = c.idEtudiant AND ent.idEntreprise = c.idEntreprise AND etu.idEtudiant = ?1")
    public List<Object[]> getDetailedPlanningsByStudentId(int id);

    @Query("SELECT p.ordre, ent, etu FROM PlanningEntity p, ChoixEntity c, EtudiantEntity etu, EntrepriseEntity ent WHERE p.idChoix = c.idChoix AND etu.idEtudiant = c.idEtudiant AND ent.idEntreprise = c.idEntreprise AND ent.idEntreprise = ?1")
    public List<Object[]> getDetailedPlanningsByCompanyId(int id);
}
