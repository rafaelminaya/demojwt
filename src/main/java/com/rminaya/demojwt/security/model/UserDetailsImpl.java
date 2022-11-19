package com.rminaya.demojwt.security.model;

import com.rminaya.demojwt.model.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Clase que implementa la interfaz "UserDetails", propia de "Spring security"
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    // Inyectamos esta dependencia por constructor, mediante la anotación "AllArgsConstructor" de "lombok"
    private final Usuario usuario;

    // Método personalizado, para poder saber el nombre
    public String getNombre() {
        return this.usuario.getNombre();
    }

    // MÉTODOS IMPLEMENTADOS DE LA INTERFAZ
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retornamos una lista vacía. Este método se usa para los permisos/roles.
        return Collections.emptyList();
    }

    // Retonarmos la contraseña
    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    // Retornamos el email que será considerado como nombre de usuario
    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    // Retornamos "true" para los siguientes métodos de controles de acceso.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
