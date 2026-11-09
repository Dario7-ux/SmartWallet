package org.example.gastos.infraestructure.repository;

import org.example.gastos.application.ports.out.GastoRepositoryPort;
import org.example.gastos.domain.exception.CategoriaNoEncontradaException;
import org.example.gastos.domain.exception.DatoInvalidoException;
import org.example.gastos.domain.models.Gasto;
import org.example.gastos.infraestructure.entities.CategoriaEntity;
import org.example.gastos.infraestructure.entities.GastoEntity;
import org.example.gastos.infraestructure.mappers.GastoMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class GastoRepositoryAdapter implements GastoRepositoryPort {

    private final GastoJpaRepository gastoJpaRepository;
    private final CategoriaJpaRepository categoriaJpaRepository;

    public GastoRepositoryAdapter(GastoJpaRepository gastoJpaRepository, CategoriaJpaRepository categoriaJpaRepository) {
        this.gastoJpaRepository = gastoJpaRepository;
        this.categoriaJpaRepository = categoriaJpaRepository;
    }

    @Override
    public Gasto guardar(Gasto gasto) {
        if (gasto == null) {
            throw new DatoInvalidoException("El gasto no puede ser nulo");
        }

        GastoEntity entity;
        if (gasto.getId() != null) {
            entity = gastoJpaRepository.findById(gasto.getId())
                    .orElse(new GastoEntity());
        } else {
            entity = new GastoEntity();
        }

        Long categoriaId = gasto.getCategoria() != null ? gasto.getCategoria().getId() : null;
        if (categoriaId == null) {
            throw new DatoInvalidoException("El gasto debe tener una categoría válida");
        }

        CategoriaEntity categoriaEntity = categoriaJpaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con id " + categoriaId + " no encontrada"));

        GastoMapper.toEntity(gasto, entity, categoriaEntity);
        GastoEntity guardado = gastoJpaRepository.save(entity);
        return GastoMapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Gasto> buscarPorId(Long id) {
        return gastoJpaRepository.findById(id).map(GastoMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Gasto> listar() {
        return gastoJpaRepository.findAllWithCategoria().stream()
                .map(GastoMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Gasto> listarPorCategoria(Long categoriaId) {
        return gastoJpaRepository.findByCategoriaId(categoriaId).stream()
                .map(GastoMapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        gastoJpaRepository.deleteById(id);
    }
}

