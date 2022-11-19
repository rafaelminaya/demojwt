package com.rminaya.demojwt.security.filter2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rminaya.demojwt.security.model.AuthCredentials;
import com.rminaya.demojwt.security.TokenUtils;
import com.rminaya.demojwt.security.model.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

// Primer "filtro" que se encargará del proceso de "autenticación".
public class JwtAutenticationFilter extends UsernamePasswordAuthenticationFilter {

    // Sobre escribimos manualmente el método "attemptAuthentication".
    // Este será el método que hace un intento de "autenticación" en este filtro "JwtAuthenticationFilter".
    // Recibe dos parámetros: "request" que es la solicitud y "response" que es la respuesta.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // Implementamos antes de retornar.
        // Cremamos un objeto de tipo "AuthCredentials"
        AuthCredentials authCredentials = new AuthCredentials();
        try {
            // ObjectMapper() : Permite leer a partir de un formato json.
            // readValue() : Lee los valores
            // request.getReader() : Obtenemos un "reader" a partir del "request". Lanza la excepción "IOException"
            // AuthCredentials.class: Indicamos la clase del tipo al que será parseado.
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
        } catch (IOException e) {
//            throw new RuntimeException(e);
        }

        // Recordando de que estamos en un proceso de autenticación.
        // Construimos un "token", a partir de las credenciales.
        UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
                authCredentials.getEmail(),
                authCredentials.getPassword(),
                Collections.emptyList()
        );

        return getAuthenticationManager().authenticate(usernamePAT);
    }

    // Método que completa la autenticación, en caso de que se haya realizado correctamente la "autenticación".
    // Sobre escribimos manualmente el método "successfulAuthentication".
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // Implementamos antes de retornar.
        // Capturamos el "authResult.getPrincipal()" y lo parseamos a nuestra clase "UserDetailsImpl" para poder crear el token.
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        // Cremamos el token
        // "getNombre()" y "getUsername()" son métodos de la clase "UserDetailsImpl", que implementó la interfaz "UserDetails" propia de "Spring security".
        String token = TokenUtils.createToken(userDetails.getNombre(), userDetails.getUsername());

        // Modificamos la "respuesta" a la solicitud http, enviando como argumentos en el encabezado el token recien creado.
        // "Authorization" : Nombre del encabezado.
        // "Bearer " : Valor del encabezado.
        response.addHeader("Authorization", "Bearer " + token);
        // getWriter().flush() : Permite confirmar los cambios
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
