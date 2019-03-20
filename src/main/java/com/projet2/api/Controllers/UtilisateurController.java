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

import java.util.*;

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
                    return new ResponseEntity<>("Cette adresse email est déjà utilisée", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                List<UtilisateurEntity> utilisateurs = utilisateurService.findAll();
                return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@RequestHeader HashMap<String, String> header,
                                     @PathVariable Integer id) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ENTREPRISE, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                UtilisateurEntity utilisateur;
                switch ((RoleEnum)res.get("role")){
                    case ADMIN:
                        utilisateur = utilisateurService.findByIdAdmin(id);
                        break;
                    case ENTREPRISE:
                        utilisateur = utilisateurService.findByIdEntreprise(id);
                        break;
                    case ETUDIANT:
                        utilisateur = utilisateurService.findByIdEtudiant(id);
                        break;
                    default:
                        utilisateur = new UtilisateurEntity();
                        break;
                }
                return new ResponseEntity<>(utilisateur, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestHeader HashMap<String, String> header, @RequestBody HashMap<String, Object> body){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ETUDIANT, RoleEnum.ENTREPRISE), header.get("identificationtoken"));

            if(res.get("responseError") == null){
                UtilisateurEntity utilisateur = modelMapper.map(body.get("user"), UtilisateurEntity.class);
                utilisateurService.save(utilisateur);
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
