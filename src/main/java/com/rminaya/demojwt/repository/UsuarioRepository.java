package com.rminaya.demojwt.repository;

import com.rminaya.demojwt.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //"Query method" que obtiene un único usuario según el parámetro email
    Optional<Usuario> findOneByEmail(String email);
}
