package com.projet2.api.DAO;

import com.projet2.api.Entities.EntrepriseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEntrepriseRepository extends JpaRepository<EntrepriseEntity, Integer> {
}
