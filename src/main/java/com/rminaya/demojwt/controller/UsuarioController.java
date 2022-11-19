package com.rminaya.demojwt.controller;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.security.TokenUtils;
import com.rminaya.demojwt.service.IUsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/usuario")
@AllArgsConstructor
public class UsuarioController {
    IUsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {

        Map<String, String> response = new HashMap<>();
        if (usuario.getId() != null && usuario.getId() > 0) {
            usuario.setId(0);
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setEmail(usuario.getEmail());
        //Encriptamos el password
        nuevoUsuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        nuevoUsuario = usuarioService.save(nuevoUsuario);

        //Obtenemos y devolvemos un token al nuevo usuario
        String token = TokenUtils.createToken(nuevoUsuario.getNombre(), nuevoUsuario.getEmail());

        response.put("token", "Bearer " + token);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
