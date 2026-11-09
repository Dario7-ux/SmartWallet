package org.example.gastos.infraestructure.mappers;

import org.example.gastos.domain.models.Rol;
import org.example.gastos.domain.models.Usuario;
import org.example.gastos.infraestructure.controllers.dtos.LoginResponse;
import org.example.gastos.infraestructure.controllers.dtos.UsuarioRequest;
import org.example.gastos.infraestructure.controllers.dtos.UsuarioResponse;
import org.example.gastos.infraestructure.entities.UsuarioEntity;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toDomain(UsuarioEntity e) {
        if (e == null) {
            return null;
        }
        Rol rol = "ADMIN".equalsIgnoreCase(e.getRol()) ? Rol.ADMIN : Rol.USER;
        return new Usuario(
                e.getId(),
                e.getUsername(),
                e.getPassword(),
                rol,
                true,
                e.getCreado(),
                e.getModificado()
        );
    }

    public static void toEntity(Usuario u, UsuarioEntity e) {
        if (u == null || e == null) {
            return;
        }
        e.setUsername(u.getUsername());
        e.setPassword(u.getPassword());
        e.setRol(u.getRol() != null ? u.getRol().name() : "USER");
    }

    public static Usuario toDomain(UsuarioRequest req) {
        if (req == null) {
            return null;
        }
        return Usuario.nuevo(req.username(), req.password(), req.rol() != null ? req.rol() : Rol.USER);
    }

    public static UsuarioResponse toResponse(Usuario u) {
        if (u == null) {
            return null;
        }
        return new UsuarioResponse(
                u.getId(),
                u.getUsername(),
                u.getRol() != null ? u.getRol().name() : "USER",
                u.isActivo(),
                u.getCreado(),
                u.getModificado()
        );
    }

    public static LoginResponse toLoginResponse(UsuarioEntity entity, String token) {
        if (entity == null) {
            return null;
        }
        return new LoginResponse(token, "Bearer", entity.getUsername(), entity.getRol());
    }
}
