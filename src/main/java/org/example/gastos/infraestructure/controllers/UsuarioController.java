package org.example.gastos.infraestructure.controllers;

import jakarta.validation.Valid;
import org.example.gastos.application.ports.in.UsuarioIn;
import org.example.gastos.domain.models.Usuario;
import org.example.gastos.infraestructure.controllers.dtos.ActionResponse;
import org.example.gastos.infraestructure.controllers.dtos.UsuarioRequest;
import org.example.gastos.infraestructure.controllers.dtos.UsuarioResponse;
import org.example.gastos.infraestructure.mappers.UsuarioMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private final UsuarioIn usuarioIn;

    public UsuarioController(UsuarioIn usuarioIn) {
        this.usuarioIn = usuarioIn;
    }

    @GetMapping
    public ResponseEntity<ActionResponse<List<UsuarioResponse>>> listar() {
        List<UsuarioResponse> list = usuarioIn.listar().stream()
                .map(UsuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(new ActionResponse<>("Usuarios recuperados: " + list.size(), list));
    }

    @PostMapping
    public ResponseEntity<ActionResponse<UsuarioResponse>> crear(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = usuarioIn.crear(UsuarioMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ActionResponse<>("Usuario creado exitosamente", UsuarioMapper.toResponse(usuario)));
    }
}

