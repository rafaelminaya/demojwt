package com.rminaya.demojwt.config;

import com.rminaya.demojwt.security.filter.JwtAuthenticationFilter;
import com.rminaya.demojwt.security.filter.JwtAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Usaremos @Configuration, ya que la clase "WebSecurityConfigurerAdapter" a extender está "deprecated"
@Configuration
@AllArgsConstructor
public class WebSecurityConfig {
    // Inyectamos estas dependencias por constructor, mediante la anotación "AllArgsConstructor" de "lombok"
    private final UserDetailsService userDetailsService; //Esta interfaz es propia de "Spring security", pero es implementada por nuestra clase "UserDetailServiceImpl".
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    // Producimos un "bean"
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        // Instanciaremos la clase "JwtAuthenticationFilter", ya que  ni siquiera fue anotada con "@Component",
        // esto fue necesario ya que su instancia necesita de una interfaz "AuthenticationManager"
        // el cual disponemos como parámetro en el método actual.

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        // Establecemos el "Gestor de autenticacion" que es necesario
        jwtAuthenticationFilter.setAuthenticationManager(authManager);

        // Establecemos la URL para el inicio de sesión. Aunque por defecto es "/login"
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");


        return http
                .csrf().disable() // Deshabilitamos el "csrf" que es el "Cross-site request forgery". Lanza una excepción.
                .authorizeRequests() // Entramos a las reglas de las solicitudes
                .antMatchers("/api/usuario", "/login").permitAll() // Indicamos las rutas que no tendrán autenticación
                .anyRequest().authenticated() // Indicamos que cualquier solicitud que ingrese a la API debe estar autenticada
//                .and() // y
//                .httpBasic() // Habilitamos la "autenticación básica", el cual requiere un usuario y contraseña, luego será deshabilitado
                .and() // y
                .sessionManagement() // Entramos a la gestión de las sesiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Establecemos la politíca de creación de sesiones como "STATELES"(sin estado)
                .and() // y
                .addFilter(jwtAuthenticationFilter) // Agregamos un filtro, en este caso el de "autenticación"
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // Agregamos un filtro, en este caso el de "autorización". Establecemos el orden, en este caso será antes de la clase "UsernamePasswordAuthenticationFilter"
                .build(); // construimos nuestro "SecurityFilterChain"
    }

    // Para probar la implementación del "filterChain", producimos el sgte. bean
    /*
    @Bean
    UserDetailsService userDetailsService() {
        // D momento cargaremos usuarios en memoria, solo para probar el funcionamiento
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // Creamos un usuario
        manager.createUser(User.withUsername("admin") // Establecemos el nombre de usuario como "admin2
                .password(passwordEncoder().encode("admin")) // Contraseña "admin" encriptada
                .roles() // Indicamos que no hay roles
                .build()); // Construimos el usuario

        return manager;
    }
    */

    // Implementación de la encriptacion de una contraseña.
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Para que toda esta configuración previa tenga efecto, produciremos el "gestor de autenticación"
    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService()) // Toma efecto del usuario creado en memoria, enviando por argumento el método "userDetailsService()" creado previamente.
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()) // Reutilizamos el método "passwordEncoder()"
                .and() // y
                .build(); // Construimos el "AuthenticationManager"
    }
}
