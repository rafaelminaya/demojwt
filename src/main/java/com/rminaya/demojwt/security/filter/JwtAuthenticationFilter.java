package com.rminaya.demojwt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rminaya.demojwt.security.model.AuthCredentials;
import com.rminaya.demojwt.security.service.JwtService;
import com.rminaya.demojwt.security.service.JwtServiceImpl;
import lombok.AllArgsConstructor;
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
import java.util.HashMap;
import java.util.Map;

// Primer "filtro" que se encargará del proceso de "autenticación".
@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;

    /*
     * Sobre escribimos, por medio del IDE, el método "attemptAuthentication".
     * Este será el método encargado de hacer un intento de "autenticación" para este filtro "JwtAuthenticationFilter".
     * Recibe dos parámetros: "request" que es la solicitud y "response" que es la respuesta.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        System.out.println("attemptAuthentication");
        // Implementamos el método antes de retornarlo.
        // Creamos un objeto de tipo "AuthCredentials" que contendrá las "credenciales de autenticación"/"Authentication"
        AuthCredentials authCredentials;
        try {
            /*
             * ObjectMapper() : Permite leer a partir de un formato json.
             * readValue() : Lee los valores.
             * request.getReader() : Obtenemos un "reader" a partir del "request". Lanza la excepción "IOException"
             * AuthCredentials.class: Indicamos la clase del tipo al cual será parseado lo que se mapee.
             */
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Creamos el "authToken", el cual es un contenedor que almacena las
        // creadenciales, necesita recibir de argumentos el usuario y contraseña.
        // UsernamePasswordAuthenticationToken: Es una clase que contiene credenciales.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authCredentials.getEmail(),
                authCredentials.getPassword(),
                Collections.emptyList()
        );

        /*
         * Retornamos la autenticación. Esta se realiza con el token (por medio de
         * usuario y contraseña) a través del autenticador "authenticationManager"
         * authenticate() : Función que atentica
         */
        return getAuthenticationManager().authenticate(authToken);
    }

    /*
     * successfulAuthentication():
     * Este método obtiene la autenticación, es decir, los datos del usuario ya logueado para poder generar el JWT.
     * Método que completa la "autenticación", en caso de que se haya realizado correctamente.
     * Sobre escribimos, por medio del IDE, el método  "successfulAuthentication".
     * Recibe como parámetros : "request" que es la solicitud y "response" que es la respuesta, un "FilterChain" y
     * un "authResult" que es el resultado de la "autenticación". */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication");
        System.out.println("authResult = " + authResult);

        // Implementamos antes de retornar.
        // Creamos el token
        String token = this.jwtService.create(authResult);

        /*
         * Enviamos el token en la cabecera del response. Importante enviar con la
         * palabra "Authorization" (HEADER_STRING) y el token con el prefijo "Bearer " ya que es un
         * estándar.
         */
        response.addHeader(JwtServiceImpl.HEADER_STRING, JwtServiceImpl.TOKEN_PREFIX + token);
        // getWriter().flush() : Permite confirmar los cambios
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }

    /*
     * unsuccessfulAuthentication(): Método que maneja la falla del intento de
     * autenticación.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        System.out.println("fallando");
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Error de atenticación: username o password incorrecto!");

        // failed: Parámetro propio del método que contiene más información de la falla lanzada.
        body.put("error", failed.getMessage());

        // Enviamos los mensajes de error.
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));

        // Enviamos el código de respuesta al cliente.
        // 403 : Acceso no prohibido, es más para roles.
        // 401 : Acceso no autorizado.
        response.setStatus(401);

        // Configuramos el "content type" para esta respuesa.
        response.setContentType("application/json");

//        super.unsuccessfulAuthentication(request, response, failed);
    }
}
