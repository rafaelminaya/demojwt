package com.rminaya.demojwt.mapper;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.service.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class UsuarioToUsuarioDTO implements IMapper<Usuario, UsuarioDTO> {
    @Override
    public UsuarioDTO map(Usuario in) {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(in.getId());
        usuario.setNombre(in.getNombre());
        usuario.setEmail(in.getEmail());

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        usuario.setFechaRegistro(formato.format(in.getFechaRegistro()));
        return usuario;
    }
}
