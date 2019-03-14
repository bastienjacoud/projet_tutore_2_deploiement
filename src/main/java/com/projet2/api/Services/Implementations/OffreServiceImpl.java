package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IOffreRepository;
import com.projet2.api.Entities.OffreEntity;
import com.projet2.api.Services.IOffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffreServiceImpl implements IOffreService {

    @Autowired
    IOffreRepository offreRepository;

    @Override
    public List<OffreEntity> findByIdEntreprise(Integer idEntreprise) {
        return offreRepository.findAllByIdEntreprise(idEntreprise);
    }

    @Override
    public OffreEntity save(OffreEntity offre) {
        offreRepository.save(offre);
        return offre;
    }

    @Override
    public void remove(Integer idOffer) {
        offreRepository.deleteById(idOffer);
    }
}
