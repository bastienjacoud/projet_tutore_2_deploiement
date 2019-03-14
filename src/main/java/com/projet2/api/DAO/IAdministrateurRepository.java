package com.projet2.api.DAO;

import com.projet2.api.Entities.AdministrateurEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdministrateurRepository extends JpaRepository<AdministrateurEntity, Integer> {
}
