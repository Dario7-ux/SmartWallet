package org.example.gastos.infraestructure.controllers;

import jakarta.validation.Valid;
import org.example.gastos.application.ports.out.CategoriaRepositoryPort;
import org.example.gastos.domain.exception.CategoriaNoEncontradaException;
import org.example.gastos.domain.models.Categoria;
import org.example.gastos.infraestructure.controllers.dtos.ActionResponse;
import org.example.gastos.infraestructure.controllers.dtos.CategoriaRequest;
import org.example.gastos.infraestructure.controllers.dtos.CategoriaResponse;
import org.example.gastos.infraestructure.mappers.CategoriaMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaRepositoryPort categoriaRepositoryPort;

    public CategoriaController(CategoriaRepositoryPort categoriaRepositoryPort) {
        this.categoriaRepositoryPort = categoriaRepositoryPort;
    }

    @GetMapping
    public ResponseEntity<ActionResponse<List<CategoriaResponse>>> listar() {
        List<CategoriaResponse> list = categoriaRepositoryPort.listar().stream()
                .map(CategoriaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(new ActionResponse<>("Categorías recuperadas: " + list.size(), list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponse<CategoriaResponse>> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con id " + id + " no encontrada"));
        return ResponseEntity.ok(new ActionResponse<>("Categoría encontrada", CategoriaMapper.toResponse(categoria)));
    }

    @PostMapping
    public ResponseEntity<ActionResponse<CategoriaResponse>> crear(@Valid @RequestBody CategoriaRequest request) {
        Categoria nueva = Categoria.nueva(request.nombre(), request.descripcion());
        Categoria guardada = categoriaRepositoryPort.guardar(nueva);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ActionResponse<>("Categoría creada exitosamente", CategoriaMapper.toResponse(guardada)));
    }
}

