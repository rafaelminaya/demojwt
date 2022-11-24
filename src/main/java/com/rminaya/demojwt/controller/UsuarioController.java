package com.rminaya.demojwt.controller;

import com.rminaya.demojwt.model.Usuario;
import com.rminaya.demojwt.security.service.JwtService;
import com.rminaya.demojwt.service.IUsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/usuario")
@AllArgsConstructor
public class UsuarioController {
    private final IUsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario, BindingResult result) {

        Map<String, Object> response = new HashMap<>();
        Usuario nuevoUsuario = null;
        String token = "";

        // Validación seteando el ID con nulo o cero para  que no haga un "update" en vez que "insert"
        if (usuario.getId() != null && usuario.getId() > 0) {
            usuario.setId(0);
        }
        // Validación de errores del "entity Cliente".
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(fieldError -> "El campo '" + fieldError.getField() + "' " + fieldError.getDefaultMessage())
                    .toList();

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {

            nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(usuario.getNombre());
            nuevoUsuario.setEmail(usuario.getEmail());
            //Encriptamos el password
            nuevoUsuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            nuevoUsuario = this.usuarioService.save(nuevoUsuario);
            //Obtenemos y devolvemos un token al nuevo usuario
            token = jwtService.create(nuevoUsuario.getNombre(), nuevoUsuario.getEmail());

        } catch (DataAccessException exception) {
            response.put("mensaje", "Error al registrar a la base de datos.");
            response.put("error", exception.getMessage() + " : " + exception.getMostSpecificCause().getMessage());
        }

        response.put("mensaje", "Usuario registrado correctamente.");
        response.put("usuario", nuevoUsuario);
        response.put("token", "Bearer " + token);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return this.usuarioService.listAll();
    }
}
