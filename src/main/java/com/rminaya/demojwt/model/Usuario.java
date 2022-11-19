package com.rminaya.demojwt.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    private String nombre;
    @Column(unique = true)
    private String email;
    private String password;
}
