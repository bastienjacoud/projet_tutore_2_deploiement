package com.projet2.api.DAO;

import com.projet2.api.Entities.EtudiantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEtudiantRepository extends JpaRepository<EtudiantEntity, Integer> {
}
