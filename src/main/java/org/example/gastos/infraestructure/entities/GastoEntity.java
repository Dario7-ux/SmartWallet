package org.example.gastos.infraestructure.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gastos")
public class GastoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 10)
    private String moneda = "USD";

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaEntity categoria;

    @Column(name = "creado", updatable = false)
    private LocalDateTime creado;

    @Column(name = "modificado")
    private LocalDateTime modificado;

    public GastoEntity() {
    }

    public GastoEntity(Long id, String descripcion, BigDecimal monto, String moneda, LocalDateTime fecha, CategoriaEntity categoria, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
        this.moneda = moneda;
        this.fecha = fecha;
        this.categoria = categoria;
        this.creado = creado;
        this.modificado = modificado;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        this.creado = ahora;
        this.modificado = ahora;
        if (this.fecha == null) {
            this.fecha = ahora;
        }
        if (this.moneda == null || this.moneda.isBlank()) {
            this.moneda = "USD";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modificado = LocalDateTime.now();
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

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
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
