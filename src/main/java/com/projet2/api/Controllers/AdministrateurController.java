package com.projet2.api.Controllers;

import com.projet2.api.Services.IAdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/administrators")
public class AdministrateurController {

    @Autowired
    IAdministrateurService administrateurService;
}
