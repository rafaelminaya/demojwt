package com.rminaya.demojwt.security.service;

import com.rminaya.demojwt.security.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtServiceImpl implements JwtService {
    // Llave secreta
    private final static String ACCESS_TOKEN_SECRET = "8206d5ed98a0a19424cf2ffcb6a2e44e29024bc1325fd54bae01ed880d92609a";
    // Tiempo de vida util del token (30 días en segundos)
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2592000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @Override
    public String create(String nombre, String email) {
        System.out.println("creando");
        // Convertirmos 2592000L en los milisegundos multiplicando por 1000, ya que lo usaremos de esta forma.
        long expirationtime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        // Asignamos como "fecha de expiración" a la "fecha actual" en milisegundos sumados con la "fecha de expiración" del token en milisegundos
        Date expirationDate = new Date(System.currentTimeMillis() + expirationtime);
        // Mapa con el nombre del usuario para enviar al token
        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", nombre);

        // Creamos el token a partir de los datos del usuario
        String token = Jwts
                .builder() // Método principal para construir un token
                .setSubject(email) // Establecemos a quién está dirigido este token
                .setExpiration(expirationDate) // Establecemos el tiempo de expiración del token.
                .addClaims(extra) // Agregamos "data" adicional al token
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes())) // Firmamos el token, enviando la palabra secreta en tipo "byte".
                .compact(); // Compactamos toda.
        return token;
    }

    @Override
    public String create(Authentication auth) {
        System.out.println("creando");
        // Convertirmos 2592000L en los milisegundos multiplicando por 1000, ya que lo usaremos de esta forma.
        long expirationtime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        // Asignamos como "fecha de expiración" a la "fecha actual" en milisegundos sumados con la "fecha de expiración" del token en milisegundos
        Date expirationDate = new Date(System.currentTimeMillis() + expirationtime);
//        String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();

        // Capturamos el "authResult.getPrincipal()" y lo parseamos a nuestra clase "UserDetailsImpl" para poder crear el token.
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        // Mapa con el nombre del usuario para enviar al token
        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", userDetails.getNombre());

        // Creamos el token a partir de los datos del usuario
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
        System.out.println("reclamando");
        /*
         * Claims : Interfaz necesaria ya que el ".getBody()" retorna de este tipo de
         * interfaz. Contendrá el contenido del token ya desencriptado.
         *
         * parserBuilder() : Permite analizar el JWT.
         * setSigningKey() : Indica cuál será la clave secreta a analizar en la firma del token JWT.
         * parseClaimsJws() : Permite analizar el token obtenido del cliente.
         * this.resolve : Método para resolver(quitar la palabra Bearer) del token.
         * getBody() : Método de cierre que permite
         * obtener el cuerpo del token por medio de un "Claim" que es una interfaz. */
        Claims claims = Jwts
                .parserBuilder() // Es como el proceso inverso al crear un token
                .setSigningKey(ACCESS_TOKEN_SECRET.getBytes()) // establecemos la "llave secreta" que se usó para generar el token.
                .build() // Construimos toda lo anterior.
                .parseClaimsJws(this.resolve(token)) // Enviamos el "token" que estamos recibiendo.
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
         * "parseClaimsJws()" de cualquier problema con la firma.
         * JwtException : Todas las funciones usadas en este ámbito usarán esta excepción,
         * por ejemplo que el JWT sea inválido o haya expirado.
         * IllegalArgumentException : Posible excepción lanzada por "parseClaimsJws()" */
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
    public Collection<? extends GrantedAuthority> getRoles(String token) {
        return Collections.emptyList();
    }

    @Override
    public String resolve(String token) {
        /*
         * Validamos que el token sea distinto de nulo y que cumpla el formato bearer.
         * replace("Bearer ", "") : Reemplaza la palabra "Bearer " por un espacio " " para obtener únicamente el token.
         * getBody() : Método de cierre que permite obtener el cuerpo del token por medio de un "Claim" que es una interfaz. */
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }

        return null;
    }
}
