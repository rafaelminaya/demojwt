package com.rminaya.demojwt.service;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.service.dto.UsuarioDTO;

import java.util.List;

public interface IUsuarioService {
    UsuarioDTO save(Usuario usuario);

    List<UsuarioDTO> listAll();
}
