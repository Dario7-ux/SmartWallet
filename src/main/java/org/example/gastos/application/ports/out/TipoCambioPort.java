package org.example.gastos.application.ports.out;

import java.math.BigDecimal;

public interface TipoCambioPort {

    BigDecimal obtenerTasaDeCambio(String monedaOrigen, String monedaDestino);

    BigDecimal convertir(BigDecimal monto, String monedaOrigen, String monedaDestino);
}

