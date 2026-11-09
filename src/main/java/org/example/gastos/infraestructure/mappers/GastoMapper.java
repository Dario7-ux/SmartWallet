package org.example.gastos.infraestructure.mappers;

import org.example.gastos.domain.models.Gasto;
import org.example.gastos.infraestructure.controllers.dtos.GastoResponse;
import org.example.gastos.infraestructure.entities.CategoriaEntity;
import org.example.gastos.infraestructure.entities.GastoEntity;

public final class GastoMapper {

    private GastoMapper() {
    }

    public static Gasto toDomain(GastoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Gasto(
                entity.getId(),
                entity.getDescripcion(),
                entity.getMonto(),
                entity.getMoneda(),
                entity.getFecha(),
                CategoriaMapper.toDomain(entity.getCategoria()),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static void toEntity(Gasto domain, GastoEntity entity, CategoriaEntity categoriaEntity) {
        if (domain == null || entity == null) {
            return;
        }
        entity.setDescripcion(domain.getDescripcion());
        entity.setMonto(domain.getMonto());
        entity.setMoneda(domain.getMoneda());
        entity.setFecha(domain.getFecha());
        entity.setCategoria(categoriaEntity);
    }

    public static GastoResponse toResponse(Gasto domain) {
        if (domain == null) {
            return null;
        }
        return new GastoResponse(
                domain.getId(),
                domain.getDescripcion(),
                domain.getMonto(),
                domain.getMoneda(),
                domain.getFecha(),
                CategoriaMapper.toResponse(domain.getCategoria()),
                domain.getCreado(),
                domain.getModificado()
        );
    }
}

