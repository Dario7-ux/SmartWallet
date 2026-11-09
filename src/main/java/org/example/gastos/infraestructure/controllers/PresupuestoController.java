package org.example.gastos.infraestructure.controllers;

import jakarta.validation.Valid;
import org.example.gastos.application.ports.in.EvaluarPresupuestoUseCase;
import org.example.gastos.domain.models.Presupuesto;
import org.example.gastos.infraestructure.controllers.dtos.ActionResponse;
import org.example.gastos.infraestructure.controllers.dtos.PresupuestoRequest;
import org.example.gastos.infraestructure.controllers.dtos.PresupuestoResponse;
import org.example.gastos.infraestructure.mappers.PresupuestoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/presupuestos")
public class PresupuestoController {

    private final EvaluarPresupuestoUseCase evaluarPresupuestoUseCase;

    public PresupuestoController(EvaluarPresupuestoUseCase evaluarPresupuestoUseCase) {
        this.evaluarPresupuestoUseCase = evaluarPresupuestoUseCase;
    }

    @PostMapping
    public ResponseEntity<ActionResponse<PresupuestoResponse>> crear(@Valid @RequestBody PresupuestoRequest request) {
        Presupuesto nuevo = evaluarPresupuestoUseCase.crearPresupuesto(
                request.categoriaId(),
                request.montoLimite(),
                request.fechaInicio(),
                request.fechaFin()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ActionResponse<>("Presupuesto creado exitosamente", PresupuestoMapper.toResponse(nuevo)));
    }

    @GetMapping
    public ResponseEntity<ActionResponse<List<PresupuestoResponse>>> listar() {
        List<PresupuestoResponse> list = evaluarPresupuestoUseCase.listarTodos().stream()
                .map(PresupuestoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(new ActionResponse<>("Presupuestos recuperados: " + list.size(), list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponse<PresupuestoResponse>> buscarPorId(@PathVariable Long id) {
        Presupuesto presupuesto = evaluarPresupuestoUseCase.buscarPorId(id);
        return ResponseEntity.ok(new ActionResponse<>("Presupuesto encontrado", PresupuestoMapper.toResponse(presupuesto)));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<ActionResponse<PresupuestoResponse>> obtenerPorCategoria(@PathVariable Long categoriaId) {
        Presupuesto presupuesto = evaluarPresupuestoUseCase.obtenerPorCategoria(categoriaId);
        return ResponseEntity.ok(new ActionResponse<>("Presupuesto de la categoría recuperado", PresupuestoMapper.toResponse(presupuesto)));
    }

    @PutMapping("/{id}/limite")
    public ResponseEntity<ActionResponse<PresupuestoResponse>> actualizarLimite(
            @PathVariable Long id,
            @RequestParam BigDecimal nuevoLimite) {
        Presupuesto actualizado = evaluarPresupuestoUseCase.actualizarLimite(id, nuevoLimite);
        return ResponseEntity.ok(new ActionResponse<>("Límite de presupuesto actualizado exitosamente", PresupuestoMapper.toResponse(actualizado)));
    }

    @PostMapping("/evaluar")
    public ResponseEntity<ActionResponse<String>> evaluarGasto(
            @RequestParam Long categoriaId,
            @RequestParam BigDecimal monto) {
        evaluarPresupuestoUseCase.evaluarPresupuesto(categoriaId, monto);
        return ResponseEntity.ok(new ActionResponse<>("El monto $" + monto + " está dentro del presupuesto permitido.", "DISPONIBLE"));
    }
}

