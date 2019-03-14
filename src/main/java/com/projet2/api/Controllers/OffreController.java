package com.projet2.api.Controllers;

import com.projet2.api.Entities.OffreEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IOffreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/offers")
public class OffreController {

    @Autowired
    private IOffreService offreService;

    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/companies/{idCompany}")
    public ResponseEntity<?> getOffersByIdCompany(@PathVariable("idCompany") Integer idCompany, @RequestHeader HashMap<String, String> header){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ENTREPRISE), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                List<OffreEntity> offres = offreService.findByIdEntreprise(idCompany);
                return new ResponseEntity<>(offres, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> addOffer(@RequestHeader HashMap<String, String> header, @RequestBody HashMap<String, Object> body){
        return addOrUpdateOffre(header, body);
    }

    @DeleteMapping("/{idOffer}")
    public ResponseEntity<?> removeOffer(@RequestHeader HashMap<String, String> header, @PathVariable("idOffer") Integer idOffer){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ENTREPRISE), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                offreService.remove(idOffer);
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
    public ResponseEntity<?> updateOffer(@RequestHeader HashMap<String, String> header, @RequestBody HashMap<String, Object> body){
        return addOrUpdateOffre(header, body);
    }

    /**
     * Méthode commune pour l'ajout et l'update
     * @param header
     * @param body
     * @return
     */
    private ResponseEntity<?> addOrUpdateOffre(@RequestHeader HashMap<String, String> header, @RequestBody HashMap<String, Object> body){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ENTREPRISE), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                OffreEntity offre = modelMapper.map(body.get("offer"), OffreEntity.class);
                offre = offreService.save(offre);
                return new ResponseEntity<>(offre, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
