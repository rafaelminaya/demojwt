package com.rminaya.demojwt.security.filter;

import com.rminaya.demojwt.security.service.JwtService;
import com.rminaya.demojwt.security.service.JwtServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

// - Segundo "filtro" para el proceso de autorización, para cuando el cliente desee usar el token, adjuntado a las solicitudes para ingresar a los enpoints
// - Anotado como componente de Spring, para poder gestionarse como un bean de Spring e inyectarse como dependencia.
// Y así poder cargar datos del usuario como roles, permisos o cualquier otro que necesite verificar antes de que pueda
// consultar datos por medio de algún enpoint de la API.
@Component
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtService jwtService;

    // MÉTODOS SOBRE ESCRITOS
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        // Capturamos el token con toda y el prefijo "Bearer" por medio del encabezado "Authorization"
//        String bearerToken = request.getHeader("Authorization");

        String header = request.getHeader(JwtServiceImpl.HEADER_STRING);

        // Verificamos que exista el token y que cumpla con el formato correcto.
        if (!requiresAuthentication(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;

        //Verificamos si el token es válido
        if (this.jwtService.validate(header)) {

//            authentication = new UsernamePasswordAuthenticationToken(this.jwtService.getUsername(header), null, this.jwtService.getRoles(header));
            authentication = new UsernamePasswordAuthenticationToken(this.jwtService.getUsername(header), null, this.jwtService.getRoles(header));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        /*
        // Verificamos que exista el token y que cumpla con el formato correcto.
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Extraemos el token del brearer token
            String jwt = bearerToken.replace("Bearer ", "");

            // Extraemos las credenciales usando nuestro método "getAuthentication()"
            UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(jwt);

            // Establecemos la autenticación.
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);
        }
        */
        // Finalizamos el proceso invocando al método "doFilter()", desconocido para nosotros.
        filterChain.doFilter(request, response);
    }

    // TODO Este comentario es de lo que se hará, no de lo que está abajo.Realizamos la configuración pertinente dentro de "WebSecurityConfig"
    protected boolean requiresAuthentication(String header) {

        if (header == null || !header.startsWith(JwtServiceImpl.TOKEN_PREFIX)) {

            return false;
        }
        return true;
    }
}
