package org.example.gastos.application.ports.in;

import org.example.gastos.domain.models.Gasto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RegistrarGastoUseCase {

    Gasto registrar(String descripcion, BigDecimal monto, String moneda, LocalDateTime fecha, Long categoriaId);

    Gasto registrar(Gasto gasto);

    Gasto buscarPorId(Long id);

    List<Gasto> listarTodos();

    List<Gasto> listarPorCategoria(Long categoriaId);
}

