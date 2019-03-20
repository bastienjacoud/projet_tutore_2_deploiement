package com.projet2.api.Controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projet2.api.Entities.EntrepriseEntity;
import com.projet2.api.Entities.UtilisateurEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IEntrepriseService;
import com.projet2.api.Services.IUtilisateurService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.*;

@RestController
@RequestMapping("/api/companies")
public class EntrepriseController {

    @Autowired
    private IEntrepriseService entrepriseService;

    @Autowired
    private IUtilisateurService utilisateurService;

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
    public ResponseEntity<?> getByIdCompany(@PathVariable("idCompany") Integer idCompany,@RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ETUDIANT, RoleEnum.ENTREPRISE), header.get("identificationtoken"));
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
                entreprise = entrepriseService.save(entreprise);
                //TODO vérifier que l'on ne renvoie pas les fichiers
                entreprise.setAvatar(null);
                return new ResponseEntity<>(entreprise, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> addCompanies(@RequestHeader HashMap<String, String> header, @RequestBody HashMap<String, Object> body){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                Type entreprisesType = new TypeToken<ArrayList<EntrepriseEntity>>(){}.getType();
                List<EntrepriseEntity> entreprises = modelMapper.map(body.get("companies"), entreprisesType);
                entreprises = entrepriseService.saveAll(entreprises);

                Type utilisateursType = new TypeToken<ArrayList<UtilisateurEntity>>(){}.getType();
                List<UtilisateurEntity> utilisateurs = modelMapper.map(body.get("companies"), utilisateursType);

                for (int i=0;i<entreprises.size();i++){
                    utilisateurs.get(i).setEntrepriseIdEntreprise(entreprises.get(i).getIdEntreprise());
                }
                utilisateurService.saveAll(utilisateurs);

                return new ResponseEntity<>(entreprises, HttpStatus.OK);
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
                        return new ResponseEntity<>("Entreprise non trouvée.", HttpStatus.NO_CONTENT);
                    }
                }
                else {
                    return new ResponseEntity<>("Erreur lors de la récupération des données JSON.", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{idCompany}")
    public ResponseEntity<?> deleteById(@PathVariable("idCompany") Integer idCompany, @RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                entrepriseService.deleteByIdCompany(idCompany);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}