package org.example.gastos.domain.models;

import org.example.gastos.domain.exception.DatoInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Gasto {

    private Long id;
    private String descripcion;
    private BigDecimal monto;
    private String moneda;
    private LocalDateTime fecha;
    private Categoria categoria;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Gasto(Long id, String descripcion, BigDecimal monto, String moneda, LocalDateTime fecha, Categoria categoria, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
        this.moneda = moneda != null ? moneda.toUpperCase() : "USD";
        this.fecha = fecha != null ? fecha : LocalDateTime.now();
        this.categoria = categoria;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Gasto nuevo(String descripcion, BigDecimal monto, String moneda, LocalDateTime fecha, Categoria categoria) {
        validarCampos(descripcion, monto, categoria);
        LocalDateTime ahora = LocalDateTime.now();
        return new Gasto(
                null,
                descripcion.trim(),
                monto,
                moneda != null && !moneda.isBlank() ? moneda.trim().toUpperCase() : "USD",
                fecha != null ? fecha : ahora,
                categoria,
                ahora,
                ahora
        );
    }

    public static Gasto nuevo(String descripcion, BigDecimal monto, Categoria categoria) {
        return nuevo(descripcion, monto, "USD", LocalDateTime.now(), categoria);
    }

    public void actualizar(String descripcion, BigDecimal monto, String moneda, LocalDateTime fecha, Categoria categoria) {
        validarCampos(descripcion, monto, categoria);
        this.descripcion = descripcion.trim();
        this.monto = monto;
        if (moneda != null && !moneda.isBlank()) {
            this.moneda = moneda.trim().toUpperCase();
        }
        if (fecha != null) {
            this.fecha = fecha;
        }
        this.categoria = categoria;
        this.modificado = LocalDateTime.now();
    }

    private static void validarCampos(String descripcion, BigDecimal monto, Categoria categoria) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new DatoInvalidoException("La descripción del gasto es obligatoria");
        }
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El monto del gasto debe ser mayor a cero");
        }
        if (categoria == null) {
            throw new DatoInvalidoException("El gasto debe estar asociado a una categoría");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

