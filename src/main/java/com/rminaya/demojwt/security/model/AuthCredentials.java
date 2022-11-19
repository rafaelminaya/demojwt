package com.rminaya.demojwt.security.model;

import lombok.Data;

// Clase que recibirá el email y contraseña del cliente
@Data
public class AuthCredentials {
    private String email;
    private String password;
}
