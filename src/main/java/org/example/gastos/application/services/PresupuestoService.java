package org.example.gastos.application.services;

import org.example.gastos.application.ports.in.EvaluarPresupuestoUseCase;
import org.example.gastos.application.ports.out.CategoriaRepositoryPort;
import org.example.gastos.application.ports.out.NotificacionPort;
import org.example.gastos.application.ports.out.PresupuestoRepositoryPort;
import org.example.gastos.domain.exception.CategoriaNoEncontradaException;
import org.example.gastos.domain.exception.DatoInvalidoException;
import org.example.gastos.domain.exception.PresupuestoExcedidoException;
import org.example.gastos.domain.models.Categoria;
import org.example.gastos.domain.models.Presupuesto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PresupuestoService implements EvaluarPresupuestoUseCase {

    private final PresupuestoRepositoryPort presupuestoRepositoryPort;
    private final CategoriaRepositoryPort categoriaRepositoryPort;
    private final NotificacionPort notificacionPort;

    public PresupuestoService(
            PresupuestoRepositoryPort presupuestoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort,
            NotificacionPort notificacionPort) {
        this.presupuestoRepositoryPort = presupuestoRepositoryPort;
        this.categoriaRepositoryPort = categoriaRepositoryPort;
        this.notificacionPort = notificacionPort;
    }

    public PresupuestoService(
            PresupuestoRepositoryPort presupuestoRepositoryPort,
            CategoriaRepositoryPort categoriaRepositoryPort) {
        this(presupuestoRepositoryPort, categoriaRepositoryPort, null);
    }

    @Override
    public void evaluarPresupuesto(Long categoriaId, BigDecimal montoGasto) {
        if (categoriaId == null) {
            throw new DatoInvalidoException("El id de la categoría es obligatorio para evaluar el presupuesto");
        }
        if (montoGasto == null || montoGasto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El monto a evaluar debe ser mayor a cero");
        }

        Presupuesto presupuesto = presupuestoRepositoryPort.buscarPorCategoriaId(categoriaId)
                .orElse(null);

        if (presupuesto != null) {
            try {
                presupuesto.evaluarGasto(montoGasto);
            } catch (PresupuestoExcedidoException e) {
                if (notificacionPort != null) {
                    notificacionPort.enviarAlertaPresupuestoExcedido(presupuesto, montoGasto);
                }
                throw e;
            }
        }
    }

    @Override
    public Presupuesto crearPresupuesto(Long categoriaId, BigDecimal montoLimite, LocalDate fechaInicio, LocalDate fechaFin) {
        if (categoriaId == null) {
            throw new DatoInvalidoException("La categoría es requerida para definir el presupuesto");
        }

        Categoria categoria = categoriaRepositoryPort.buscarPorId(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con id " + categoriaId + " no encontrada"));

        // Verificar si ya existe presupuesto para esta categoría
        presupuestoRepositoryPort.buscarPorCategoriaId(categoriaId).ifPresent(p -> {
            throw new DatoInvalidoException("Ya existe un presupuesto activo para la categoría '" + categoria.getNombre() + "'");
        });

        Presupuesto nuevo = Presupuesto.nuevo(montoLimite, categoria, fechaInicio, fechaFin);
        return presupuestoRepositoryPort.guardar(nuevo);
    }

    @Override
    public Presupuesto actualizarLimite(Long presupuestoId, BigDecimal nuevoLimite) {
        if (presupuestoId == null) {
            throw new DatoInvalidoException("El id del presupuesto no puede ser nulo");
        }

        Presupuesto presupuesto = buscarPorId(presupuestoId);
        presupuesto.actualizarLimite(nuevoLimite);
        return presupuestoRepositoryPort.guardar(presupuesto);
    }

    @Override
    public Presupuesto buscarPorId(Long id) {
        if (id == null) {
            throw new DatoInvalidoException("El id del presupuesto no puede ser nulo");
        }
        return presupuestoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new DatoInvalidoException("Presupuesto con id " + id + " no encontrado"));
    }

    @Override
    public Presupuesto obtenerPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            throw new DatoInvalidoException("El id de la categoría no puede ser nulo");
        }
        return presupuestoRepositoryPort.buscarPorCategoriaId(categoriaId)
                .orElseThrow(() -> new DatoInvalidoException("No existe presupuesto para la categoría " + categoriaId));
    }

    @Override
    public List<Presupuesto> listarTodos() {
        return presupuestoRepositoryPort.listar();
    }
}

