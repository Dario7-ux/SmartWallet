package org.example.gastos.application.services;

import org.example.gastos.application.ports.in.RegistrarGastoUseCase;
import org.example.gastos.application.ports.out.CategoriaRepositoryPort;
import org.example.gastos.application.ports.out.GastoRepositoryPort;
import org.example.gastos.application.ports.out.NotificacionPort;
import org.example.gastos.application.ports.out.PresupuestoRepositoryPort;
import org.example.gastos.application.ports.out.TipoCambioPort;
import org.example.gastos.domain.exception.CategoriaNoEncontradaException;
import org.example.gastos.domain.exception.DatoInvalidoException;
import org.example.gastos.domain.exception.PresupuestoExcedidoException;
import org.example.gastos.domain.models.Categoria;
import org.example.gastos.domain.models.Gasto;
import org.example.gastos.domain.models.Presupuesto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class GastoService implements RegistrarGastoUseCase {

    private final GastoRepositoryPort gastoRepositoryPort;
    private final PresupuestoRepositoryPort presupuestoRepositoryPort;
    private final CategoriaRepositoryPort categoriaRepositoryPort;
    private final NotificacionPort notificacionPort;
    private final TipoCambioPort tipoCambioPort;

    public GastoService(
            GastoRepositoryPort gastoRepositoryPort,
            PresupuestoRepositoryPort presupuestoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort,
            TipoCambioPort tipoCambioPort) {
        this.gastoRepositoryPort = gastoRepositoryPort;
        this.presupuestoRepositoryPort = presupuestoRepositoryPort;
        this.categoriaRepositoryPort = categoriaRepositoryPort;
        this.notificacionPort = notificacionPort;
        this.tipoCambioPort = tipoCambioPort;
    }

    public GastoService(
            GastoRepositoryPort gastoRepositoryPort,
            PresupuestoRepositoryPort presupuestoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort) {
        this(gastoRepositoryPort, presupuestoRepositoryPort, categoriaRepositoryPort, notificacionPort, null);
    }

    @Override
    public Gasto registrar(String descripcion, BigDecimal monto, String moneda, LocalDateTime fecha, Long categoriaId) {
        if (categoriaId == null) {
            throw new DatoInvalidoException("El id de la categoría es obligatorio");
        }

        Categoria categoria = categoriaRepositoryPort.buscarPorId(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con id " + categoriaId + " no encontrada"));

        String monedaFinal = (moneda != null && !moneda.isBlank()) ? moneda.trim().toUpperCase() : "USD";
        BigDecimal montoEnMonedaBase = monto;

        // Si la moneda es distinta de USD y tenemos servicio de tipo de cambio, convertimos a USD para control presupuestario
        if (tipoCambioPort != null && !"USD".equalsIgnoreCase(monedaFinal)) {
            montoEnMonedaBase = tipoCambioPort.convertir(monto, monedaFinal, "USD");
        }

        Gasto nuevoGasto = Gasto.nuevo(descripcion, montoEnMonedaBase, monedaFinal, fecha, categoria);
        return registrar(nuevoGasto);
    }

    @Override
    public Gasto registrar(Gasto gasto) {
        if (gasto == null) {
            throw new DatoInvalidoException("El gasto a registrar no puede ser nulo");
        }

        if (gasto.getCategoria() == null || gasto.getCategoria().getId() == null) {
            throw new DatoInvalidoException("El gasto debe estar asociado a una categoría válida con id");
        }

        Long categoriaId = gasto.getCategoria().getId();
        Categoria categoriaExistente = categoriaRepositoryPort.buscarPorId(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con id " + categoriaId + " no encontrada"));
        gasto.setCategoria(categoriaExistente);

        // Evaluar presupuesto si existe uno asignado para la categoría
        Optional<Presupuesto> presupuestoOpt = presupuestoRepositoryPort.buscarPorCategoriaId(categoriaId);

        if (presupuestoOpt.isPresent()) {
            Presupuesto presupuesto = presupuestoOpt.get();
            try {
                // Validación del dominio
                presupuesto.evaluarGasto(gasto);
            } catch (PresupuestoExcedidoException e) {
                if (notificacionPort != null) {
                    notificacionPort.enviarAlertaPresupuestoExcedido(presupuesto, gasto.getMonto());
                }
                throw e;
            }

            // Registrar gasto y actualizar saldo del presupuesto
            presupuesto.registrarGasto(gasto);
            presupuestoRepositoryPort.guardar(presupuesto);

            // Alerta preventiva si se consumió >= 80% del presupuesto
            if (notificacionPort != null && debeNotificarAlerta(presupuesto)) {
                notificacionPort.enviarAlertaPresupuesto(
                        "Atención: El presupuesto de la categoría '" + categoriaExistente.getNombre() +
                                "' ha superado el 80% de su límite.",
                        presupuesto
                );
            }
        }

        Gasto guardado = gastoRepositoryPort.guardar(gasto);

        if (notificacionPort != null) {
            notificacionPort.enviarConfirmacionGasto(guardado);
        }

        return guardado;
    }

    private boolean debeNotificarAlerta(Presupuesto presupuesto) {
        if (presupuesto.getMontoLimite().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal porcentajeConsumido = presupuesto.getMontoActual()
                .divide(presupuesto.getMontoLimite(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        return porcentajeConsumido.compareTo(new BigDecimal("80.00")) >= 0;
    }

    @Override
    public Gasto buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException("El id del gasto no puede ser nulo");
        }
        return gastoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new DatoInvalidoException("Gasto con id " + id + " no encontrado"));
    }

    @Override
    public List<Gasto> listarTodos() {
        return gastoRepositoryPort.listar();
    }

    @Override
    public List<Gasto> listarPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            throw new DatoInvalidoException("El id de la categoría no puede ser nulo");
        }
        return gastoRepositoryPort.listarPorCategoria(categoriaId);
    }
}

