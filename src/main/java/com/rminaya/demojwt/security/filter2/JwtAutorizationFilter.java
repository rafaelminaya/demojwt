package com.rminaya.demojwt.security.filter2;

import com.rminaya.demojwt.security.TokenUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// - Segundo "filtro" para el proceso de autorización, para cuando el cliente desee usar el token, adjuntado a las solicitudes para ingresar a los enpoints
// - Anotado como componente de Spring, para poder gestionarse como un bean de Spring e inyectarse como dependencia.
// Y así poder cargar datos del usuario como roles, permisos o cualquier otro que necesite verificar antes de que pueda
// consultar datos por medio de algún enpoint de la API.
@Component
public class JwtAutorizationFilter extends OncePerRequestFilter {

    // MÉTODOS SOBRE ESCRITOS
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        // Capturamos el token con toda y el prefijo "Bearer" por medio del encabezado "Authorization"
        String bearerToken = request.getHeader("Authorization");

        // Verificamos que exista el token y que cumpla con el formato correcto.
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Extraemos el token del brearer token
            String jwt = bearerToken.replace("Bearer ", "");

            // Extraemos las credenciales usando nuestro método "getAuthentication()"
            UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(jwt);

            // Establecemos la autenticación.
            SecurityContextHolder.getContext().setAuthentication(usernamePAT);
        }
        // Finalizamos el proceso invocando al método "doFilter()", desconocido para nosotros.
        filterChain.doFilter(request, response);
    }
    // Realizamos la configuración pertinente dentro de "WebSecurityConfig"
}
