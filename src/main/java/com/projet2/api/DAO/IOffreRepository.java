package com.projet2.api.DAO;

import com.projet2.api.Entities.OffreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOffreRepository extends JpaRepository<OffreEntity, Integer> {
    List<OffreEntity> findAllByIdEntreprise(Integer idEntreprise);
}
