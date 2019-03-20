package com.projet2.api.Controllers;

import com.projet2.api.Entities.ParametresapplicationEntity;
import com.projet2.api.Enums.RoleEnum;
import com.projet2.api.Helpers.JwtHelper;
import com.projet2.api.Services.IParametresApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/parameters")
public class ParametresApplicationController {

    @Autowired
    private IParametresApplicationService parametresApplicationService;

    @PutMapping("/choices/{valueChoice}")
    public ResponseEntity<?> updateChoicesParameters(@RequestHeader HashMap<String, String> header, @PathVariable Integer valueChoice){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                ParametresapplicationEntity parametres = parametresApplicationService.findFirst();
                parametres.setChoixFerme(valueChoice);
                parametres = parametresApplicationService.save(parametres);
                return new ResponseEntity<>(parametres, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/schedules/{valueSchedule}")
    public ResponseEntity<?> updateSchedulesParameters(@RequestHeader HashMap<String, String> header, @PathVariable Integer valueSchedule){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                ParametresapplicationEntity parametres = parametresApplicationService.findFirst();
                parametres.setPlanningFerme(valueSchedule);
                parametres = parametresApplicationService.save(parametres);
                return new ResponseEntity<>(parametres, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/times")
    public ResponseEntity<?> updateTimeParameters(@RequestHeader HashMap<String, String> header,
                                                       @RequestParam(value = "startTime", defaultValue = "null") String rawStartTime,
                                                       @RequestParam(value = "endTime", defaultValue = "null") String rawEndTime){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                ParametresapplicationEntity parametres = parametresApplicationService.findFirst();
                Time startTime;
                Time endTime;
                if(rawStartTime.equals("null")) {
                    startTime = Time.valueOf("14:00:00");
                } else {
                    startTime = Time.valueOf(rawStartTime + ":00");
                }

                if(rawEndTime.equals("null")) {
                    endTime = Time.valueOf("18:00:00");
                } else {
                    endTime = Time.valueOf(rawEndTime + ":00");
                }
                parametres.setDebutEntretiens(startTime);
                parametres.setFinEntretiens(endTime);
                parametres = parametresApplicationService.save(parametres);
                return new ResponseEntity<>(parametres, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/slotDuration/{duration}")
    public ResponseEntity<?> updateDurationSlotParameters(@RequestHeader HashMap<String, String> header,
                                                  @PathVariable Integer duration){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Collections.singletonList(RoleEnum.ADMIN), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                ParametresapplicationEntity parametres = parametresApplicationService.findFirst();

                parametres.setDureeEntretiens(duration);
                parametres = parametresApplicationService.save(parametres);
                return new ResponseEntity<>(parametres, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getParameters(@RequestHeader HashMap<String, String> header){
        try{
            Map<String, Object> res = JwtHelper.checkTokenInformations(Arrays.asList(RoleEnum.ADMIN, RoleEnum.ENTREPRISE, RoleEnum.ETUDIANT), header.get("identificationtoken"));
            if(res.get("responseError") == null){
                ParametresapplicationEntity parametres = parametresApplicationService.findFirst();
                return new ResponseEntity<>(parametres, HttpStatus.OK);
            }
            else{
                return (ResponseEntity<?>) res.get("responseError");
            }
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
