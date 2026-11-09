package org.example.gastos.infraestructure.repository;

import org.example.gastos.application.ports.out.PresupuestoRepositoryPort;
import org.example.gastos.domain.exception.CategoriaNoEncontradaException;
import org.example.gastos.domain.exception.DatoInvalidoException;
import org.example.gastos.domain.models.Presupuesto;
import org.example.gastos.infraestructure.entities.CategoriaEntity;
import org.example.gastos.infraestructure.entities.PresupuestoEntity;
import org.example.gastos.infraestructure.mappers.PresupuestoMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class PresupuestoRepositoryAdapter implements PresupuestoRepositoryPort {

    private final PresupuestoJpaRepository presupuestoJpaRepository;
    private final CategoriaJpaRepository categoriaJpaRepository;

    public PresupuestoRepositoryAdapter(PresupuestoJpaRepository presupuestoJpaRepository, CategoriaJpaRepository categoriaJpaRepository) {
        this.presupuestoJpaRepository = presupuestoJpaRepository;
        this.categoriaJpaRepository = categoriaJpaRepository;
    }

    @Override
    public Presupuesto guardar(Presupuesto presupuesto) {
        if (presupuesto == null) {
            throw new DatoInvalidoException("El presupuesto no puede ser nulo");
        }

        PresupuestoEntity entity;
        if (presupuesto.getId() != null) {
            entity = presupuestoJpaRepository.findById(presupuesto.getId())
                    .orElse(new PresupuestoEntity());
        } else {
            entity = new PresupuestoEntity();
        }

        Long categoriaId = presupuesto.getCategoria() != null ? presupuesto.getCategoria().getId() : null;
        if (categoriaId == null) {
            throw new DatoInvalidoException("El presupuesto debe estar asociado a una categoría con id");
        }

        CategoriaEntity categoriaEntity = categoriaJpaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con id " + categoriaId + " no encontrada"));

        PresupuestoMapper.toEntity(presupuesto, entity, categoriaEntity);
        PresupuestoEntity guardado = presupuestoJpaRepository.save(entity);
        return PresupuestoMapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Presupuesto> buscarPorId(Long id) {
        return presupuestoJpaRepository.findByIdWithCategoria(id)
                .or(() -> presupuestoJpaRepository.findById(id))
                .map(PresupuestoMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Presupuesto> buscarPorCategoriaId(Long categoriaId) {
        return presupuestoJpaRepository.findByCategoriaId(categoriaId).map(PresupuestoMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Presupuesto> listar() {
        return presupuestoJpaRepository.findAllWithCategoria().stream()
                .map(PresupuestoMapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        presupuestoJpaRepository.deleteById(id);
    }
}

