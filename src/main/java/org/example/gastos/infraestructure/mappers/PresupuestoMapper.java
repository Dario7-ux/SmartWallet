package org.example.gastos.infraestructure.mappers;

import org.example.gastos.domain.models.Presupuesto;
import org.example.gastos.infraestructure.controllers.dtos.PresupuestoResponse;
import org.example.gastos.infraestructure.entities.CategoriaEntity;
import org.example.gastos.infraestructure.entities.PresupuestoEntity;

public final class PresupuestoMapper {

    private PresupuestoMapper() {
    }

    public static Presupuesto toDomain(PresupuestoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Presupuesto(
                entity.getId(),
                entity.getMontoLimite(),
                entity.getMontoActual(),
                CategoriaMapper.toDomain(entity.getCategoria()),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getCreado(),
                entity.getModificado()
        );
    }

    public static void toEntity(Presupuesto domain, PresupuestoEntity entity, CategoriaEntity categoriaEntity) {
        if (domain == null || entity == null) {
            return;
        }
        entity.setMontoLimite(domain.getMontoLimite());
        entity.setMontoActual(domain.getMontoActual());
        entity.setCategoria(categoriaEntity);
        entity.setFechaInicio(domain.getFechaInicio());
        entity.setFechaFin(domain.getFechaFin());
    }

    public static PresupuestoResponse toResponse(Presupuesto domain) {
        if (domain == null) {
            return null;
        }
        return new PresupuestoResponse(
                domain.getId(),
                domain.getMontoLimite(),
                domain.getMontoActual(),
                domain.getDisponible(),
                CategoriaMapper.toResponse(domain.getCategoria()),
                domain.getFechaInicio(),
                domain.getFechaFin(),
                domain.getCreado(),
                domain.getModificado()
        );
    }
}

