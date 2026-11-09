package org.example.gastos.infraestructure.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "creado", updatable = false)
    private LocalDateTime creado;

    @Column(name = "modificado")
    private LocalDateTime modificado;

    public CategoriaEntity() {
    }

    public CategoriaEntity(Long id, String nombre, String descripcion, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creado = creado;
        this.modificado = modificado;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        this.creado = ahora;
        this.modificado = ahora;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
