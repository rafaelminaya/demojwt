package com.rminaya.demojwt.service;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.repository.UsuarioRepository;
import com.rminaya.demojwt.security.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// Servicio que se va a encargar de poder cargar un usuario desde la base de datos, a partir de lo que lo identifica, es decir, el email
// Lo llamamos "UserDetailServiceImpl", ya que el nombre "UserDetailsService" ya existe.
// "UserDetailsService" interfaz propia de "spring security", requiere implementar el método "loadUserByUsername()"
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Obtenemos el usuario según su email,lanzamos una excepción en caso no lo encuentre.
        Usuario usuario = usuarioRepository
                .findOneByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con email:  " + email + " no existe."));
        //Retornamos una implementación de "UserDetails", apoyada por la clase que creamos llamada "UserDetailsImpl"
        // Le enviamos el usuario obtenido por la base de datos.
        // Luego crearemos los filtros necesarios que se van a agregar a la configuración dentro del WebSecurityConfig
        return new UserDetailsImpl(usuario);
//        return new User(usuario.getEmail(), usuario.getPassword(), true, true, true, true, Collections.emptyList());

    }
}
