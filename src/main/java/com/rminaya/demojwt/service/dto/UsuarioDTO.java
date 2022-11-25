package com.rminaya.demojwt.service.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String email;
    private String fechaRegistro;
}
