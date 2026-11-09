package org.example.gastos.infraestructure.adapters;

import org.example.gastos.application.ports.out.NotificacionPort;
import org.example.gastos.domain.models.Gasto;
import org.example.gastos.domain.models.Presupuesto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NotificacionEmailAdapter implements NotificacionPort {

    private static final Logger log = LoggerFactory.getLogger(NotificacionEmailAdapter.class);

    @Value("${smartwallet.notificaciones.destinatario:alertas@smartwallet.com}")
    private String emailDestinatario;

    @Override
    public void enviarAlertaPresupuesto(String mensaje, Presupuesto presupuesto) {
        log.warn("=== [NOTIFICACIÓN ALERTA PRESUPUESTO] ===");
        log.warn("Destinatario: {}", emailDestinatario);
        log.warn("Categoría: {}", presupuesto.getCategoria() != null ? presupuesto.getCategoria().getNombre() : "N/A");
        log.warn("Mensaje: {}", mensaje);
        log.warn("Límite: ${} | Gastado: ${} | Disponible: ${}",
                presupuesto.getMontoLimite(), presupuesto.getMontoActual(), presupuesto.getDisponible());
        log.warn("=========================================");
    }

    @Override
    public void enviarAlertaPresupuestoExcedido(Presupuesto presupuesto, BigDecimal montoIntento) {
        log.error("=== [NOTIFICACIÓN ALERTA CRÍTICA: PRESUPUESTO EXCEDIDO] ===");
        log.error("Destinatario: {}", emailDestinatario);
        log.error("Categoría: {}", presupuesto.getCategoria() != null ? presupuesto.getCategoria().getNombre() : "N/A");
        log.error("Monto intentado: ${}", montoIntento);
        log.error("Límite del presupuesto: ${} | Acumulado actual: ${}",
                presupuesto.getMontoLimite(), presupuesto.getMontoActual());
        log.error("==========================================================");
    }

    @Override
    public void enviarConfirmacionGasto(Gasto gasto) {
        log.info("=== [NOTIFICACIÓN CONFIRMACIÓN DE GASTO] ===");
        log.info("Destinatario: {}", emailDestinatario);
        log.info("Descripción: '{}' | Monto: ${} {}",
                gasto.getDescripcion(), gasto.getMonto(), gasto.getMoneda());
        log.info("Categoría: {}", gasto.getCategoria() != null ? gasto.getCategoria().getNombre() : "N/A");
        log.info("Fecha: {}", gasto.getFecha());
        log.info("============================================");
    }
}
