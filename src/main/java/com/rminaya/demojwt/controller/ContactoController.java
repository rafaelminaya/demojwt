package com.rminaya.demojwt.controller;

import com.rminaya.demojwt.model.Contacto;
import com.rminaya.demojwt.service.IContactoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/contacto")
@AllArgsConstructor
public class ContactoController {
    private final IContactoService contactoService;

    @GetMapping
    public List<Contacto> listContacto() {
        return this.contactoService.findAll();
    }
}
