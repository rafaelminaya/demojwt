package com.rminaya.demojwt.controller;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.security.service.JwtService;
import com.rminaya.demojwt.service.IUsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/usuario")
@AllArgsConstructor
public class UsuarioController {
    private final IUsuarioService usuarioService;
    private final JwtService jwtService;

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
        nuevoUsuario = this.usuarioService.save(nuevoUsuario);

        //Obtenemos y devolvemos un token al nuevo usuario
        String token = jwtService.create(nuevoUsuario.getNombre(), nuevoUsuario.getEmail());

        response.put("mensaje", "Usuario creado con éxito. ");
        response.put("token", "Bearer " + token);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return this.usuarioService.listAll();
    }
}
