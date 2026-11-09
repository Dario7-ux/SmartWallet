package org.example.gastos.infraestructure.controllers;

import jakarta.validation.Valid;
import org.example.gastos.application.ports.in.RegistrarGastoUseCase;
import org.example.gastos.domain.models.Gasto;
import org.example.gastos.infraestructure.controllers.dtos.ActionResponse;
import org.example.gastos.infraestructure.controllers.dtos.GastoRequest;
import org.example.gastos.infraestructure.controllers.dtos.GastoResponse;
import org.example.gastos.infraestructure.mappers.GastoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gastos")
public class GastoController {

    private final RegistrarGastoUseCase registrarGastoUseCase;

    public GastoController(RegistrarGastoUseCase registrarGastoUseCase) {
        this.registrarGastoUseCase = registrarGastoUseCase;
    }

    @PostMapping
    public ResponseEntity<ActionResponse<GastoResponse>> registrar(@Valid @RequestBody GastoRequest request) {
        Gasto guardado = registrarGastoUseCase.registrar(
                request.descripcion(),
                request.monto(),
                request.moneda(),
                request.fecha(),
                request.categoriaId()
        );
        GastoResponse response = GastoMapper.toResponse(guardado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ActionResponse<>("Gasto registrado exitosamente", response));
    }

    @GetMapping
    public ResponseEntity<ActionResponse<List<GastoResponse>>> listar(@RequestParam(required = false) Long categoriaId) {
        List<Gasto> lista = (categoriaId != null)
                ? registrarGastoUseCase.listarPorCategoria(categoriaId)
                : registrarGastoUseCase.listarTodos();

        List<GastoResponse> responseList = lista.stream()
                .map(GastoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(new ActionResponse<>("Gastos recuperados: " + responseList.size(), responseList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponse<GastoResponse>> buscarPorId(@PathVariable Long id) {
        Gasto gasto = registrarGastoUseCase.buscarPorId(id);
        return ResponseEntity.ok(new ActionResponse<>("Gasto encontrado", GastoMapper.toResponse(gasto)));
    }
}

