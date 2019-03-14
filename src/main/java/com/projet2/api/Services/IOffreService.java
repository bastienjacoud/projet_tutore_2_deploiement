package com.projet2.api.Services;

import com.projet2.api.Entities.OffreEntity;

import java.util.List;

public interface IOffreService {
    List<OffreEntity> findByIdEntreprise(Integer idEntreprise);
    OffreEntity save(OffreEntity offre);
    void remove(Integer idOffer);
    //OffreEntity findById(Integer idOffre);
}
