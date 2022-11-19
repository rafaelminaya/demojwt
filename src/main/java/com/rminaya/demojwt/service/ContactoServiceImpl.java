package com.rminaya.demojwt.service;

import com.rminaya.demojwt.model.Contacto;
import com.rminaya.demojwt.repository.ContactoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactoServiceImpl implements IContactoService {

    ContactoRepository contactoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Contacto> findAll() {
        return this.contactoRepository.findAll();
    }
}
