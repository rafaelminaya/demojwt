package com.rminaya.demojwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.*;

// Clase que ayuda a crear los tokens
public class TokenUtils {
    // Llave secreta
    private final static String ACCESS_TOKEN_SECRET = "8206d5ed98a0a19424cf2ffcb6a2e44e29024bc1325fd54bae01ed880d92609a";
    // Tiempo de vida util del token (30 días en segundos)
    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2592000L;

    // Creamos el token que será enviado al cliente. Guardaremos el nombre de usuario y email en el token.
    public static String createToken(String nombre, String email) {
        // Convertirmos 2592000L en los milisegundos multiplicando por 1000, ya que lo usaremos de esta forma.
        long expirationtime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;
        // Asignamos como "fecha de expiración" a la "fecha actual" en milisegundos sumados con la "fecha de expiración" del token en milisegundos
        Date expirationDate = new Date(System.currentTimeMillis() + expirationtime);

        // Mapa con el nombre del usuario para enviar al token
        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", nombre);

        return Jwts
                .builder() // Método principal para construir un token
                .setSubject(email) // Establecemos a quién está dirigido este token
                .setExpiration(expirationDate) // Establecemos el tiempo de expiración del token.
                .addClaims(extra) // Agregamos "data" adicional al token
                .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes())) // Firmamos el token, enviando la palabra secreta en tipo "byte".
                .compact(); // Compactamos toda.
    }

    // Método que devuelve las credenciales del usuario según el token recibido y así pasar al proceso de "autorización".
    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
        // En caso de que el token sea de formato incorrecto, inválido o expirado.
        try {
            Claims claims = Jwts
                    .parserBuilder() // Es como el proceso inverso al crear un token
                    .setSigningKey(ACCESS_TOKEN_SECRET.getBytes()) // establecemos la "llave secreta" que se usó para generar el token.
                    .build() // Construimos toda lo anterior.
                    .parseClaimsJws(token) // Enviamos el "token" que estamos recibiendo.
                    .getBody(); // Obtenemos el cuerpo del "token"

            // Extramos el email dentro del token
            String email = claims.getSubject();
            // Retornamos una intancia del tipo del método, que incluye el "email" extraído del token,
            // necesario para que el usuario pueda autenticarse.
            // Collections.emptyList() : Aqui indicamos que no trabajaremos con permisos, enviando una lista vacia.
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }

    }
}
