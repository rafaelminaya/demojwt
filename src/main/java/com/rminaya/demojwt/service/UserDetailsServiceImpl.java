package com.rminaya.demojwt.service;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.repository.UsuarioRepository;
import com.rminaya.demojwt.security.model.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 * Servicio que se va a encargar de poder cargar un usuario desde la base de datos, a partir de lo que lo identifica, es decir, el email.
 * Lo llamamos "UserDetailsServiceImpl", ya que el nombre "UserDetailsService" ya existe.
 * "UserDetailsService" interfaz propia de "spring security", requiere implementar el método "loadUserByUsername()".
 * NOTA: Esta clase será usada en la clase "WebSecurityConfig" para cargar el/los usuarios en la configuración del "Spring Security"
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Obtenemos el usuario según su email, lanzando una excepción en caso no lo encuentre.
        Usuario usuario = this.usuarioRepository
                .findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email:  " + email + " no existe."));
        // Retornamos "UserDetailsImpl" que es una implementación propia de la interfaz "UserDetails" a devolver.
        // Le enviamos el usuario obtenido de la base de datos.

        // Opcion 1 : Retornar una implementación propia de "UserDetails"
        return new UserDetailsImpl(usuario);

        //Opcion 2:
        // Retornar la clase "User" propia de "Spring security" que implementa "UserDetails" enviando
        // los valores necesarios por constructor. Sin embargo, esta clase solo trabaja con el atributo "email" y "passowrd".
//        return new User(usuario.getEmail(), usuario.getPassword(), true, true, true, true, Collections.emptyList());

    }
}
