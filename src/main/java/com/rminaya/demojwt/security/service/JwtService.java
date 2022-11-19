package com.rminaya.demojwt.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface JwtService {

    // Método que se encarga de crear el token
//    String create(String nombre, String email);
    String create(Authentication auth) throws JsonProcessingException;

    // Método para obtener los claims/payload del JWT
    Claims getClaims(String token);

    boolean validate(String token);

    // Método para obtener el username y roles a partir del JWT
    String getUsername(String token);

    // Método para obtener los roles
//    List<?> getRoles(String token);
    public Collection<? extends GrantedAuthority> getRoles(String token) throws JsonMappingException, JsonProcessingException;
    // Método para resolver(quitar la palabra Bearer) el token
    String resolve(String token);
}
