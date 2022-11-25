package com.rminaya.demojwt.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    @NotEmpty(message = "no puede estar vacio.")
    @Column(nullable = false)
    @Size(min = 3, max = 255, message = "el tamaño tiene que estar entre 3 y 255")
    private String nombre;

    @NotEmpty(message = "no puede estar vacio")
    @Email(message = "no es una direccion de correo bien formada.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "no puede estar vacio.")
    @Column(nullable = false)
    private String password;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
