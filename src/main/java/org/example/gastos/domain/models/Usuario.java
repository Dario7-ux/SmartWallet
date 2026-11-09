package org.example.gastos.domain.models;

import java.time.LocalDateTime;

public class Usuario {

    private Long id;
    private String username;
    private String password;
    private Rol rol;
    private boolean activo;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    public Usuario() {
    }

    public Usuario(Long id, String username, String password, Rol rol, boolean activo, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.creado = creado;
        this.modificado = modificado;
    }

    public static Usuario nuevo(String username, String password, Rol rol) {
        LocalDateTime ahora = LocalDateTime.now();
        return new Usuario(null, username, password, rol != null ? rol : Rol.USER, true, ahora, ahora);
    }

    public void actualizar(String username, String password, Rol rol, boolean activo) {
        this.username = username;
        if (password != null && !password.isBlank()) {
            this.password = password;
        }
        this.rol = rol;
        this.activo = activo;
        this.modificado = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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

