package com.rminaya.demojwt.service;

import com.rminaya.demojwt.mapper.UsuarioToUsuarioDTO;
import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.repository.UsuarioRepository;
import com.rminaya.demojwt.service.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioToUsuarioDTO mapperUsuarioToUsuarioDTO;


    @Override
    @Transactional
    public UsuarioDTO save(Usuario usuario) {

        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setNombre(usuario.getNombre());
        usuarioNuevo.setEmail(usuario.getEmail());
        usuarioNuevo.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        usuarioNuevo.setFechaRegistro(LocalDateTime.now());
        usuarioNuevo = this.usuarioRepository.save(usuarioNuevo);

        // Retornamos como tipo "UsuarioDTO" para no mostrar la contaseña y fecha con formato personalizado.
        return mapperUsuarioToUsuarioDTO.map(usuarioNuevo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listAll() {
        //Retornamos como tipo "UsuarioDTO" para no mostrar las contaseñas y fecha con formato personalizado.
        return this.usuarioRepository.findAll().stream()
                .map(usuario -> mapperUsuarioToUsuarioDTO.map(usuario))
                .collect(Collectors.toList());
    }
}
