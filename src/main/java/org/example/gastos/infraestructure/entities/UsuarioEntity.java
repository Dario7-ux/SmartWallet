package org.example.gastos.infraestructure.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String rol = "USER";

    @Column(name = "creado", updatable = false)
    private LocalDateTime creado;

    @Column(name = "modificado")
    private LocalDateTime modificado;

    public UsuarioEntity() {
    }

    public UsuarioEntity(Long id, String username, String password, String rol, LocalDateTime creado, LocalDateTime modificado) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.creado = creado;
        this.modificado = modificado;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        this.creado = ahora;
        this.modificado = ahora;
        if (this.rol == null) {
            this.rol = "USER";
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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
