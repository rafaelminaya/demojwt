package com.rminaya.demojwt.service;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {
    UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return this.usuarioRepository.save(usuario);
    }
}
