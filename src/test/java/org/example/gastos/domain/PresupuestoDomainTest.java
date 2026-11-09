package org.example.gastos.domain;

import org.example.gastos.domain.exception.PresupuestoExcedidoException;
import org.example.gastos.domain.models.Categoria;
import org.example.gastos.domain.models.Gasto;
import org.example.gastos.domain.models.Presupuesto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PresupuestoDomainTest {

    private Categoria categoria;
    private Presupuesto presupuesto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1L, "Alimentación", "Supermercado y comida", LocalDateTime.now(), LocalDateTime.now());
        presupuesto = Presupuesto.nuevo(new BigDecimal("300.00"), categoria, LocalDate.now(), LocalDate.now().plusMonths(1));
    }

    @Test
    @DisplayName("Debe evaluar correctamente un gasto dentro del límite presupuestario")
    void testGastoDentroDelLimite() {
        assertDoesNotThrow(() -> presupuesto.evaluarGasto(new BigDecimal("150.00")));
    }

    @Test
    @DisplayName("Debe lanzar PresupuestoExcedidoException cuando el gasto supera el límite")
    void testGastoExcedeLimite() {
        PresupuestoExcedidoException exception = assertThrows(
                PresupuestoExcedidoException.class,
                () -> presupuesto.evaluarGasto(new BigDecimal("350.00"))
        );

        assertTrue(exception.getMessage().contains("Presupuesto excedido"));
        assertTrue(exception.getMessage().contains("Alimentación"));
    }

    @Test
    @DisplayName("Debe acumular los gastos registrados y actualizar el monto disponible")
    void testRegistrarGastoYAcumular() {
        Gasto gasto1 = Gasto.nuevo("Almuerzo", new BigDecimal("50.00"), categoria);
        presupuesto.registrarGasto(gasto1);

        assertEquals(new BigDecimal("50.00"), presupuesto.getMontoActual());
        assertEquals(new BigDecimal("250.00"), presupuesto.getDisponible());

        Gasto gasto2 = Gasto.nuevo("Supermercado", new BigDecimal("100.00"), categoria);
        presupuesto.registrarGasto(gasto2);

        assertEquals(new BigDecimal("150.00"), presupuesto.getMontoActual());
        assertEquals(new BigDecimal("150.00"), presupuesto.getDisponible());
    }

    @Test
    @DisplayName("Debe fallar al intentar registrar un gasto que sobrepase el saldo acumulado restante")
    void testRegistrarGastoQueSuperaSaldoRestante() {
        Gasto gasto1 = Gasto.nuevo("Cena", new BigDecimal("200.00"), categoria);
        presupuesto.registrarGasto(gasto1);

        Gasto gastoExcedente = Gasto.nuevo("Compra extra", new BigDecimal("150.00"), categoria);
        assertThrows(PresupuestoExcedidoException.class, () -> presupuesto.registrarGasto(gastoExcedente));
    }
}

