package com.projet2.api.Controllers;

import com.projet2.api.Entities.ChoixEntity;
import com.projet2.api.Entities.PlanningEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IChoixService;
import com.projet2.api.Services.IPlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ChoixPlanningController {

    @Autowired
    IPlanningService planningService;

    @Autowired
    private IChoixService choixService;

    /**
     *
     * PARTIE CHOIX
     *
     */

    @PostMapping("/choices")
    public ResponseEntity<?> addChoice(@RequestHeader HashMap<String, String> header, @RequestBody HashMap<String, String> body){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ENTREPRISE, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                if(res.get("role") == RoleEnum.ETUDIANT){
                    int idEtudiant = (int)res.get("id");
                    List<Integer> idMatchList = new ArrayList<>(), idSuperMatchList = new ArrayList<>();
                    if(body.get("match") != null){
                        idMatchList = Arrays.stream(body.get("match").split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    }
                    if(body.get("superMatch") != null){
                        idSuperMatchList = getValidSuperMatchs((RoleEnum)res.get("role"), idEtudiant, body.get("superMatch"));
                    }
                    List<ChoixEntity> choix = choixService.addChoixEtudiant(idEtudiant, idMatchList, idSuperMatchList);
                    return new ResponseEntity<>(choix, HttpStatus.OK);
                }
                else if(res.get("role") == RoleEnum.ENTREPRISE){
                    int idEntreprise = (int)res.get("id");
                    List<Integer> idMatchList = new ArrayList<>(), idSuperMatchList = new ArrayList<>();
                    if(body.get("match") != null){
                        idMatchList = Arrays.stream(body.get("match").split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    }
                    if(body.get("superMatch") != null){
                        idSuperMatchList = getValidSuperMatchs((RoleEnum)res.get("role"), idEntreprise, body.get("superMatch"));
                    }
                    List<ChoixEntity> choix = choixService.addChoixEntreprise(idEntreprise, idMatchList, idSuperMatchList);
                    return new ResponseEntity<>(choix, HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new Exception("Votre rôle ne vous permet pas d'accéder à cette URL."), HttpStatus.FORBIDDEN);
                }
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Integer> getValidSuperMatchs(RoleEnum role, Integer id, String list){
        //Récupération du nombre de super matchs restants
        int superMatchNumber = choixService.getSuperMatchNumber(id, role);
        List<Integer> idSuperMatchList = Arrays.stream(list.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        if(idSuperMatchList.size() > superMatchNumber){
            idSuperMatchList.subList(0, superMatchNumber);
        }
        return idSuperMatchList;
    }

    @GetMapping("/choices")
    public ResponseEntity<?> getAllChoices(@RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ENTREPRISE, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                if(res.get("role") == RoleEnum.ETUDIANT){
                    int idEtudiant = (int)res.get("id");
                    List<ChoixEntity> choix = choixService.findByIdEtudiant(idEtudiant);
                    return new ResponseEntity<>(choix, HttpStatus.OK);
                }
                else if(res.get("role") == RoleEnum.ENTREPRISE){
                    int idEntreprise = (int)res.get("id");
                    List<ChoixEntity> choix = choixService.findByIdEntreprise(idEntreprise);
                    return new ResponseEntity<>(choix, HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new Exception("Votre rôle ne vous permet pas d'accéder à cette URL."), HttpStatus.FORBIDDEN);
                }
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * PARTIE PLANNING
     *
     */

    @GetMapping("/schedules")
    public ResponseEntity<?> getAllSchedules(@RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ENTREPRISE, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                List<PlanningEntity> creneaux = planningService.findAll();
                return new ResponseEntity<>(creneaux, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/schedules/generate/{forced}")
    public ResponseEntity<?> generate(@RequestHeader HashMap<String, String> header, @PathVariable("forced") Boolean isForced) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                // On vérifie si le planning n'est pas déjà généré
                List<PlanningEntity> creneaux = planningService.findAll();
                if(!creneaux.isEmpty() && !isForced)
                    return new ResponseEntity<>("Le planning est déjà généré", HttpStatus.OK);
                else {
                    List<ChoixEntity> choix = choixService.findAll();
                    planningService.generate(choix);

                    return new ResponseEntity<>(planningService.findAll(), HttpStatus.OK);
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
