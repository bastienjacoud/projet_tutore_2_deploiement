package com.projet2.api.Controllers;

import com.google.gson.Gson;
import com.projet2.api.Entities.EntrepriseEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IEntrepriseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/companies")
public class EntrepriseController {

    @Autowired
    private IEntrepriseService entrepriseService;

    private ModelMapper modelMapper = new ModelMapper();

    private Gson g = new Gson();

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                List<EntrepriseEntity> entreprises = entrepriseService.findAll();
                return new ResponseEntity<>(entreprises, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{idCompany}")
    public ResponseEntity<?> getById(@PathVariable("idCompany") Integer idCompany, @RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                EntrepriseEntity entreprise = entrepriseService.findById(idCompany);
                return new ResponseEntity<>(entreprise, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addCompany(@RequestHeader HashMap<String, String> header, @RequestParam("company") String company, @RequestParam(value = "avatar", required = false) MultipartFile avatar){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                EntrepriseEntity entreprise = g.fromJson(company, EntrepriseEntity.class);
                // Ajout de l'avatar et du cv à l'étudiant
                if(avatar != null){
                    entreprise.setAvatar(avatar.getBytes());
                }
                entrepriseService.save(entreprise);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateCompany(@RequestHeader HashMap<String, String> header, @RequestParam("company") String company, @RequestParam(value = "avatar", required = false) MultipartFile avatar){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ENTREPRISE), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                EntrepriseEntity newEntreprise = g.fromJson(company, EntrepriseEntity.class);
                if(newEntreprise != null && newEntreprise.getIdEntreprise() != 0){
                    // Récupération de l'instance en base
                    EntrepriseEntity entreprise = entrepriseService.findById(newEntreprise.getIdEntreprise());

                    if(entreprise != null){
                        // Ajout prenom, nom et description
                        entreprise.setNom(newEntreprise.getNom());
                        entreprise.setPresentation(newEntreprise.getPresentation());
                        entreprise.setNbEntreprise(newEntreprise.getNbEntreprise());

                        // Ajout de l'avatar et du cv à l'étudiant
                        if(avatar != null){
                            entreprise.setAvatar(avatar.getBytes());
                        }
                        entrepriseService.save(entreprise);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>("Etudiant non trouvé.", HttpStatus.NO_CONTENT);
                    }
                }
                else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
