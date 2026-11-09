package org.example.gastos.domain.models;

import org.example.gastos.domain.exception.DatoInvalidoException;

import java.time.LocalDateTime;

public class Categoria {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Categoria(Long id, String nombre, String descripcion, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Categoria nueva(String nombre, String descripcion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre de la categoría es obligatorio");
        }
        return new Categoria(null, nombre.trim(), descripcion != null ? descripcion.trim() : null, LocalDateTime.now(), LocalDateTime.now());
    }

    public void actualizar(String nombre, String descripcion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre de la categoría no puede estar vacío");
        }
        this.nombre = nombre.trim();
        this.descripcion = descripcion != null ? descripcion.trim() : null;
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

