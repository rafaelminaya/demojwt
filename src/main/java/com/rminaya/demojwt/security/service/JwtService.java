package com.rminaya.demojwt.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {

    // Método que se encarga de crear el token, a partir de un usuario logueado
    String create(String nombre, String email);

    // Método que se encarga de crear el token, a partir de un usuario nuevo
    String create(Authentication auth);

    // Método para obtener los claims/payload del JWT
    Claims getClaims(String token);

    // Método para validar el token
    boolean validate(String token);

    // Método para obtener el username y roles a partir del JWT
    String getUsername(String token);

    // Método para obtener los roles
//    List<?> getRoles(String token);
    Collection<? extends GrantedAuthority> getRoles(String token);

    // Método para resolver(quitar la palabra Bearer) del token
    String resolve(String token);
}
