package org.example.gastos.domain.models;

import org.example.gastos.domain.exception.DatoInvalidoException;
import org.example.gastos.domain.exception.PresupuestoExcedidoException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Presupuesto {

    private Long id;
    private BigDecimal montoLimite;
    private BigDecimal montoActual;
    private Categoria categoria;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Presupuesto(Long id, BigDecimal montoLimite, BigDecimal montoActual, Categoria categoria,
                       LocalDate fechaInicio, LocalDate fechaFin, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.montoLimite = montoLimite != null ? montoLimite.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2);
        this.montoActual = montoActual != null ? montoActual.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2);
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Presupuesto nuevo(BigDecimal montoLimite, Categoria categoria, LocalDate fechaInicio, LocalDate fechaFin) {
        if (montoLimite == null || montoLimite.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El límite del presupuesto debe ser mayor a cero");
        }
        if (categoria == null) {
            throw new DatoInvalidoException("El presupuesto debe asignarse a una categoría válida");
        }
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new DatoInvalidoException("La fecha final no puede ser anterior a la fecha inicial");
        }

        LocalDateTime ahora = LocalDateTime.now();
        LocalDate inicio = fechaInicio != null ? fechaInicio : LocalDate.now().withDayOfMonth(1);
        LocalDate fin = fechaFin != null ? fechaFin : inicio.plusMonths(1).minusDays(1);

        return new Presupuesto(
                null,
                montoLimite.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                categoria,
                inicio,
                fin,
                ahora,
                ahora
        );
    }

    public static Presupuesto nuevo(BigDecimal montoLimite, Categoria categoria) {
        return nuevo(montoLimite, categoria, null, null);
    }

    /**
     * Regla de negocio central del dominio:
     * Evalúa si un nuevo gasto provocaría que se supere el límite del presupuesto asignado.
     * Si se supera, lanza PresupuestoExcedidoException.
     */
    public void evaluarGasto(BigDecimal montoNuevoGasto) {
        if (montoNuevoGasto == null || montoNuevoGasto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El monto del gasto debe ser mayor a cero");
        }

        BigDecimal proyectado = this.montoActual.add(montoNuevoGasto).setScale(2, RoundingMode.HALF_UP);
        if (proyectado.compareTo(this.montoLimite) > 0) {
            BigDecimal excedente = proyectado.subtract(this.montoLimite).setScale(2, RoundingMode.HALF_UP);
            String nombreCat = (this.categoria != null && this.categoria.getNombre() != null)
                    ? this.categoria.getNombre()
                    : "No especificada";

            throw new PresupuestoExcedidoException(
                    String.format(
                            "Presupuesto excedido para la categoría '%s'. Límite: %s, Gastado actual: %s, Nuevo gasto: %s. Excedente proyectado: %s",
                            nombreCat,
                            this.montoLimite,
                            this.montoActual,
                            montoNuevoGasto,
                            excedente
                    )
            );
        }
    }

    /**
     * Evalúa el gasto asociando la entidad Gasto y verificando correspondencia de categoría.
     */
    public void evaluarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new DatoInvalidoException("El gasto a evaluar no puede ser nulo");
        }
        if (this.categoria != null && gasto.getCategoria() != null
                && this.categoria.getId() != null && gasto.getCategoria().getId() != null
                && !this.categoria.getId().equals(gasto.getCategoria().getId())) {
            throw new DatoInvalidoException("La categoría del gasto no coincide con la categoría del presupuesto");
        }
        evaluarGasto(gasto.getMonto());
    }

    /**
     * Acumula el gasto en el presupuesto tras evaluar que no exceda el límite.
     */
    public void registrarGasto(Gasto gasto) {
        evaluarGasto(gasto);
        this.montoActual = this.montoActual.add(gasto.getMonto()).setScale(2, RoundingMode.HALF_UP);
        this.modificado = LocalDateTime.now();
    }

    public void actualizarLimite(BigDecimal nuevoLimite) {
        if (nuevoLimite == null || nuevoLimite.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El nuevo límite debe ser mayor a cero");
        }
        this.montoLimite = nuevoLimite.setScale(2, RoundingMode.HALF_UP);
        this.modificado = LocalDateTime.now();
    }

    public BigDecimal getDisponible() {
        return this.montoLimite.subtract(this.montoActual).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean tienePresupuestoPara(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return this.montoActual.add(monto).compareTo(this.montoLimite) <= 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontoLimite() {
        return montoLimite;
    }

    public void setMontoLimite(BigDecimal montoLimite) {
        this.montoLimite = montoLimite;
    }

    public BigDecimal getMontoActual() {
        return montoActual;
    }

    public void setMontoActual(BigDecimal montoActual) {
        this.montoActual = montoActual;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    public void setCreado(LocalDateTime creado) {
        this.creado = creado;
    }

    public LocalDateTime getModificado() {
        return modificado;
    }

    public void setModificado(LocalDateTime modificado) {
        this.modificado = modificado;
    }
}

