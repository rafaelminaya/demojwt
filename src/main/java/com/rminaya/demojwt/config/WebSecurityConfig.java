package com.rminaya.demojwt.config;

import com.rminaya.demojwt.security.filter.JwtAuthenticationFilter;
import com.rminaya.demojwt.security.filter.JwtAuthorizationFilter;
import com.rminaya.demojwt.security.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
// Inyectaremos las dependencias por constructor, mediante la anotación "AllArgsConstructor" de "lombok"
@Configuration
@AllArgsConstructor
public class WebSecurityConfig {
    //UserDetailsService : Interfaz propia de "Spring security", que implementamos por medio de nuestra clase "UserDetailsServiceImpl".
    private final UserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    private JwtService jwtService;

    // Producimos un "bean"
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        System.out.println("WebSecurityConfig - filterChain");
        // Instanciamos nuestro filtro "JwtAuthenticationFilter", ya que ni siquiera fue anotada con "@Component",
        // esto fue necesario ya que, su instancia necesita asignar un atributo de la interfaz "AuthenticationManager"
        // el cual disponemos como parámetro en el método actual, para poder establecer el "AuthenticationManager"
        // o también conocido como "Gestor de autenticacion".

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);
        // Establecemos el "Gestor de autenticacion" que es necesario
        jwtAuthenticationFilter.setAuthenticationManager(authManager);

        // Establecemos la URL para el inicio de sesión. Aunque por defecto es "/login"
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");

        return http
                .csrf().disable() // Deshabilitamos el "csrf" que es el "Cross-site request forgery". Lanza una excepción.
                .authorizeRequests() // Entramos a las reglas de las solicitudes
                .antMatchers("/api/usuario",
                         "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll() // Indicamos las rutas que no tendrán autenticación. A partir del segundo son las rutas de Swagger 2
                .anyRequest().authenticated() // Indicamos que cualquier solicitud que ingrese a la API debe estar autenticada
//                .and() // y
//                .httpBasic() // Habilitamos la "autenticación básica", el cual requiere un usuario y contraseña, luego será deshabilitado
                .and() // y
                .sessionManagement() // Entramos a la gestión de las sesiones
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Establecemos la politíca de creación de sesiones como "STATELES"(sin estado)
                .and() // y
                .addFilter(jwtAuthenticationFilter) // Agregamos un filtro, en este caso el de "autenticación"
                .addFilterBefore(this.jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // Agregamos un filtro, en este caso el de "autorización". Establecemos el orden, en este caso será antes de la clase "UsernamePasswordAuthenticationFilter"
                .build(); // construimos nuestro "SecurityFilterChain"
    }

    // Para probar la implementación del "filterChain", producimos el sgte. bean y gestionar el usuario pero en "memoria"
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
        System.out.println("WebSecurityConfig - passwordEncoder");
        return new BCryptPasswordEncoder();
    }

    // Para que toda esta configuración previa tenga efecto, produciremos el "gestor de autenticación"
    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        System.out.println("WebSecurityConfig - authManager");
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService()) // Toma efecto del usuario creado en memoria, enviando por argumento el método "userDetailsService()" creado previamente.
                .userDetailsService(this.userDetailsService) // Toma efecto del usuario creado, por medio de la dependencia "this.userDetailsService"
                .passwordEncoder(this.passwordEncoder()) // Reutilizamos el método "passwordEncoder()" para establecer la encriptación de la contraseña
                .and() // y
                .build(); // Construimos el "AuthenticationManager"
    }
}
