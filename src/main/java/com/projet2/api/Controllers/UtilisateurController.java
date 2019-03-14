package com.projet2.api.Controllers;

import com.projet2.api.Entities.UtilisateurEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IUtilisateurService;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UtilisateurController {

    @Autowired
    IUtilisateurService utilisateurService;

    private ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody HashMap<String, String> body){
        try{
            UtilisateurEntity utilisateurEntity = utilisateurService.findByLoginAndMotPasse(body.get("email"), body.get("password"));

            if( utilisateurEntity != null && utilisateurEntity.getIdUtilisateur() != 0){
                RoleEnum role = RoleEnum.NONE;
                int id = 0;

                if(utilisateurEntity.getAdministrateurIdAdministrateur() != null){
                    role = RoleEnum.ADMIN;
                    id = utilisateurEntity.getAdministrateurIdAdministrateur();
                } else if(utilisateurEntity.getEntrepriseIdEntreprise() != null){
                    role = RoleEnum.ENTREPRISE;
                    id = utilisateurEntity.getEntrepriseIdEntreprise();
                } else if(utilisateurEntity.getEtudiantIdEtudiant() != null){
                    role = RoleEnum.ETUDIANT;
                    id = utilisateurEntity.getEtudiantIdEtudiant();
                }

                JSONObject jsonObject = new JSONObject();
                Date dateExpiration = new Date(new Date().getTime() + new Date(3*3600*1000).getTime());
                jsonObject.put("token", JwtHelper.getToken(role, id, dateExpiration));
                return new ResponseEntity<>(jsonObject.toMap(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Exception("Utilisateur ou mot de passe incorrect !"), HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addUser(@RequestHeader HashMap<String, String> header, @RequestBody HashMap<String, String> body){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                UtilisateurEntity utilisateur = new UtilisateurEntity();
                utilisateur.setLogin(body.get("email"));

                //Vérification de l'unicité de l'email
                if(!utilisateurService.existByLogin(utilisateur.getLogin())){
                    utilisateur.setMotPasse(body.get("password"));
                    RoleEnum role = modelMapper.map(body.get("role"), RoleEnum.class);
                    if(role == RoleEnum.ENTREPRISE){
                        utilisateur.setEntrepriseIdEntreprise(Integer.parseInt(body.get("id")));
                    }
                    else if(role == RoleEnum.ETUDIANT){
                        utilisateur.setEtudiantIdEtudiant(Integer.parseInt(body.get("id")));
                    }
                    else if(role == RoleEnum.ADMIN){
                        utilisateur.setAdministrateurIdAdministrateur(Integer.parseInt(body.get("id")));
                    }
                    utilisateurService.save(utilisateur);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(new Exception("Cette adresse email est déjà utilisée"), HttpStatus.BAD_REQUEST);
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
