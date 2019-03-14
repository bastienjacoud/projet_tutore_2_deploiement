package com.projet2.api.Controllers;

import com.google.gson.Gson;
import com.projet2.api.DTO.EtudiantDTO;
import com.projet2.api.Entities.EtudiantEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IEtudiantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins = "https://polymeetup.herokuapp.com", maxAge = 3600)
@RestController
@RequestMapping("/api/students")
public class EtudiantController {

    @Autowired
    private IEtudiantService etudiantService;

    private ModelMapper modelMapper = new ModelMapper();

    private Gson g = new Gson();

    @GetMapping("")
        public ResponseEntity<?> getAll(@RequestHeader HashMap<String, String> header) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ENTREPRISE), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                List<EtudiantEntity> etudiants = etudiantService.findAll();
                List<EtudiantDTO> result = new ArrayList<>();
                etudiants.forEach(e-> result.add(modelMapper.map(e, EtudiantDTO.class)));
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{idStudent}")
    public ResponseEntity<?> getById(@RequestHeader HashMap<String, String> header, @PathVariable("idStudent") Integer idStudent) {
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ENTREPRISE), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                EtudiantEntity etudiant = etudiantService.findById(idStudent);
                return new ResponseEntity<>(etudiant, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addStudent(@RequestHeader HashMap<String, String> header, @RequestParam("student") String student, @RequestParam(value = "avatar", required = false) MultipartFile avatar, @RequestParam(value = "cv", required = false) MultipartFile cv){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                EtudiantEntity etudiant = g.fromJson(student, EtudiantEntity.class);
                // Ajout de l'avatar et du cv à l'étudiant
                if(avatar != null){
                    etudiant.setAvatar(avatar.getBytes());
                }
                if(cv != null){
                    etudiant.setCv(cv.getBytes());
                }
                etudiantService.save(etudiant);
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
    public ResponseEntity<?> updateStudent(@RequestHeader HashMap<String, String> header, @RequestParam("student") String student, @RequestParam(value = "avatar", required = false) MultipartFile avatar, @RequestParam(value = "cv", required = false) MultipartFile cv){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                EtudiantEntity newEtudiant = g.fromJson(student, EtudiantEntity.class);
                if(newEtudiant != null && newEtudiant.getIdEtudiant() != 0){
                    // Récupération de l'instance en base
                    EtudiantEntity etudiant = etudiantService.findById(newEtudiant.getIdEtudiant());

                    if(etudiant != null){
                        // Ajout prenom, nom et description
                        etudiant.setNom(newEtudiant.getNom());
                        etudiant.setPrenom(newEtudiant.getPrenom());
                        etudiant.setPresentation(newEtudiant.getPresentation());

                        // Ajout de l'avatar et du cv à l'étudiant
                        if(avatar != null){
                            etudiant.setAvatar(avatar.getBytes());
                        }
                        if(cv != null){
                            etudiant.setCv(cv.getBytes());
                        }
                        etudiantService.save(etudiant);
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
