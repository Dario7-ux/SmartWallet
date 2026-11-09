package org.example.gastos.application.ports.in;

import org.example.gastos.domain.models.Presupuesto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EvaluarPresupuestoUseCase {

    void evaluarPresupuesto(Long categoriaId, BigDecimal montoGasto);

    Presupuesto crearPresupuesto(Long categoriaId, BigDecimal montoLimite, LocalDate fechaInicio, LocalDate fechaFin);

    Presupuesto actualizarLimite(Long presupuestoId, BigDecimal nuevoLimite);

    Presupuesto buscarPorId(Long id);

    Presupuesto obtenerPorCategoria(Long categoriaId);

    List<Presupuesto> listarTodos();
}

