package com.projet2.api.Services.Implementations;

import com.projet2.api.DAO.IAdministrateurRepository;
import com.projet2.api.Services.IAdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministrateurServiceImpl implements IAdministrateurService {

    @Autowired
    private IAdministrateurRepository administrateurRepository;
}
