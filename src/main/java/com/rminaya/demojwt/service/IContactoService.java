package com.rminaya.demojwt.service;

import com.rminaya.demojwt.model.Contacto;

import java.util.List;

public interface IContactoService {
    List<Contacto> findAll();
}
