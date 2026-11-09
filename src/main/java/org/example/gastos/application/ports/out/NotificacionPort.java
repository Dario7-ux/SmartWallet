package org.example.gastos.application.ports.out;

import org.example.gastos.domain.models.Gasto;
import org.example.gastos.domain.models.Presupuesto;

import java.math.BigDecimal;

public interface NotificacionPort {

    void enviarAlertaPresupuesto(String mensaje, Presupuesto presupuesto);

    void enviarAlertaPresupuestoExcedido(Presupuesto presupuesto, BigDecimal montoIntento);

    void enviarConfirmacionGasto(Gasto gasto);
}

