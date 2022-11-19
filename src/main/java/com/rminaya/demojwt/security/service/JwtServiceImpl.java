package com.rminaya.demojwt.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rminaya.demojwt.security.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtServiceImpl implements JwtService {
    // Llave secreta
    private final static String ACCESS_TOKEN_SECRET = "8206d5ed98a0a19424cf2ffcb6a2e44e29024bc1325fd54bae01ed880d92609a";
    // Tiempo de vida util del token (30 días en segundos)
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2592000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @Override
    public String create(Authentication auth) throws JsonProcessingException {
        // Convertirmos 2592000L en los milisegundos multiplicando por 1000, ya que lo usaremos de esta forma.
        long expirationtime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        // Asignamos como "fecha de expiración" a la "fecha actual" en milisegundos sumados con la "fecha de expiración" del token en milisegundos
        Date expirationDate = new Date(System.currentTimeMillis() + expirationtime);
//        String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        // Mapa con el nombre del usuario para enviar al token
        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", userDetails.getNombre());
        String token = Jwts
                .builder() // Método principal para construir un token
                .setSubject(userDetails.getUsername()) // Establecemos a quién está dirigido este token
                .setExpiration(expirationDate) // Establecemos el tiempo de expiración del token.
                .addClaims(extra) // Agregamos "data" adicional al token
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes())) // Firmamos el token, enviando la palabra secreta en tipo "byte".
                .compact(); // Compactamos toda.
        return token;
    }

    @Override
    public Claims getClaims(String token) {
        Claims claims = Jwts
                .parserBuilder() // Es como el proceso inverso al crear un token
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes()) // establecemos la "llave secreta" que se usó para generar el token.
                .build() // Construimos toda lo anterior.
                .parseClaimsJws(resolve(token)) // Enviamos el "token" que estamos recibiendo.
                .getBody(); // Obtenemos el cuerpo del "token"
        return claims;
    }

    @Override
    public boolean validate(String token) {
        // Validamos el token
        // Variable que asignará "true" si el token se ha verificado correctamente o
        // "false" en caso contrario.
        boolean tokenValido;

        /*
         * try/catch Para controlar las posibles excepciones de la función
         * "parseClaimsJws()" de cualquier problema con la firma. JwtException : Todas
         * las funciones usadas en este ámbito usarán esta excepción
         * IllegalArgumentException : Posible excepción lanzada por "parseClaimsJws()"
         *
         *
         */
        try {

            getClaims(token);

            tokenValido = true;

        } catch (JwtException | IllegalArgumentException ex) {
            tokenValido = false;
        }

        return tokenValido;
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws JsonMappingException, JsonProcessingException {
        return Collections.emptyList();
    }

    @Override
    public String resolve(String token) {
        //Validamos que el token sea distinto de nulo y que cumpla el formato bearer
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }

        return null;
    }
}
