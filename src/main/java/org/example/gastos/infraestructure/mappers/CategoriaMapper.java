package org.example.gastos.infraestructure.mappers;

import org.example.gastos.domain.models.Categoria;
import org.example.gastos.infraestructure.controllers.dtos.CategoriaRequest;
import org.example.gastos.infraestructure.controllers.dtos.CategoriaResponse;
import org.example.gastos.infraestructure.entities.CategoriaEntity;

public final class CategoriaMapper {

    private CategoriaMapper() {
    }

    public static Categoria toDomain(CategoriaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Categoria(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static void toEntity(Categoria domain, CategoriaEntity entity) {
        if (domain == null || entity == null) {
            return;
        }
        entity.setNombre(domain.getNombre());
        entity.setDescripcion(domain.getDescripcion());
    }

    public static CategoriaEntity toEntity(Categoria domain) {
        if (domain == null) {
            return null;
        }
        CategoriaEntity entity = new CategoriaEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setDescripcion(domain.getDescripcion());
        entity.setCreado(domain.getCreado());
        entity.setModificado(domain.getModificado());
        return entity;
    }

    public static Categoria toDomain(CategoriaRequest request) {
        if (request == null) {
            return null;
        }
        return Categoria.nueva(request.nombre(), request.descripcion());
    }

    public static CategoriaResponse toResponse(Categoria domain) {
        if (domain == null) {
            return null;
        }
        return new CategoriaResponse(
                domain.getId(),
                domain.getNombre(),
                domain.getDescripcion(),
                domain.getCreado(),
                domain.getModificado()
        );
    }
}

