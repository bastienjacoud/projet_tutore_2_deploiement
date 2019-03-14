package com.projet2.api.Helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.projet2.api.Enums.RoleEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtHelper {

    //private static String secret  = "toto";
    private static Algorithm algorithm = Algorithm.HMAC256("Ma chaine de caractères");

    public static String getToken(@NotNull RoleEnum role, int id, Date dateExpiration){
        return JWT.create()
                .withIssuer("Polymeetup")
                .withClaim("role", role.toString())
                .withClaim("id", id)
                .withClaim("dateExpiration", dateExpiration)
                .sign(algorithm);
    }

    private static DecodedJWT verify(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Polymeetup")
                .build();
        return verifier.verify(token);
    }

    public static Map<String, Object> checkTokenInformations(List<RoleEnum> roleAvailable, String token){
        Map<String, Object> res = new HashMap<>();
        ResponseEntity responseError = null;

        try{
            if(token != null && !token.equals("")){
                DecodedJWT jwt = JwtHelper.verify(token);

                RoleEnum role = jwt.getClaim("role").as(RoleEnum.class);
                int id = jwt.getClaim("id").asInt();
                Date dateExpiration = jwt.getClaim("dateExpiration").asDate();

                res.put("role", role);
                res.put("id", id);

                // Vérification du role
                if(!roleAvailable.contains(role)){
                    responseError = new ResponseEntity<>(new Exception("Votre rôle ne vous permet pas d'accéder à cette URL."), HttpStatus.FORBIDDEN);
                }

                // Vérification de la date d'expiration
                else if(dateExpiration.before(new Date())){
                    responseError = new ResponseEntity<>(new Exception("Votre token a expiré."), HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                // Vérification de l'autorisation
                responseError = new ResponseEntity<>(new Exception("Vous n'êtes pas autorisé à accéder à cette URL."), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e){
            responseError = new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        res.put("responseError", responseError);
        return res;
    }
}
