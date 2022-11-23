package com.rminaya.demojwt.service;

import com.rminaya.demojwt.model.Usuario;

import java.util.List;

public interface IUsuarioService {
    Usuario save(Usuario usuario);

    List<Usuario> listAll();
}
