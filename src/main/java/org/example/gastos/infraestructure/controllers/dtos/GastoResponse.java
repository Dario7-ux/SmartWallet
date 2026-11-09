package org.example.gastos.infraestructure.controllers.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GastoResponse(
        Long id,
        String descripcion,
        BigDecimal monto,
        String moneda,
        LocalDateTime fecha,
        CategoriaResponse categoria,
        LocalDateTime creado,
        LocalDateTime modificado
) {
}

