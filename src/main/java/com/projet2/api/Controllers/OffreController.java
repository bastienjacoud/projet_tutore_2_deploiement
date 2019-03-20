package com.projet2.api.Controllers;

import com.projet2.api.Entities.OffreEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IOffreService;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/offers")
public class OffreController {

    @Autowired
    private IOffreService offreService;

    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/companies/{idCompany}")
    public ResponseEntity<?> getOffersByIdCompany(@PathVariable("idCompany") Integer idCompany, @RequestHeader HashMap<String, String> header){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ENTREPRISE, RoleEnum.ETUDIANT), header.get("identificationtoken"));
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
    public ResponseEntity<?> addOffer(@RequestHeader HashMap<String, String> header, @RequestParam("idCompany") Integer idCompany, @RequestParam(value = "offer", required = false) MultipartFile offer){
        return addOrUpdateOffre(header, idCompany, offer);
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
    public ResponseEntity<?> updateOffer(@RequestHeader HashMap<String, String> header, @RequestParam("idCompany") Integer idCompany, @RequestParam(value = "offer", required = false) MultipartFile offer){
        return addOrUpdateOffre(header, idCompany, offer);
    }

    /**
     * Méthode commune pour l'ajout et l'update
     * @param header
     */
    private ResponseEntity<?> addOrUpdateOffre(HashMap<String, String> header, Integer idCompany, MultipartFile offer){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ENTREPRISE), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                OffreEntity offre = new OffreEntity();
                offre.setIdEntreprise(idCompany);
                offre.setOffre(offer.getBytes());
                offre = offreService.save(offre);
                //TODO vérifier que l'on ne renvoie pas l'offre
                offre.setOffre(null);
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
