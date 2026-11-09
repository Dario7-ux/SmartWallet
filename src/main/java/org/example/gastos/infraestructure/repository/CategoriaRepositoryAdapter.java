package org.example.gastos.infraestructure.repository;

import org.example.gastos.application.ports.out.CategoriaRepositoryPort;
import org.example.gastos.domain.exception.DatoInvalidoException;
import org.example.gastos.domain.models.Categoria;
import org.example.gastos.infraestructure.entities.CategoriaEntity;
import org.example.gastos.infraestructure.mappers.CategoriaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaRepositoryAdapter implements CategoriaRepositoryPort {

    private final CategoriaJpaRepository categoriaJpaRepository;

    public CategoriaRepositoryAdapter(CategoriaJpaRepository categoriaJpaRepository) {
        this.categoriaJpaRepository = categoriaJpaRepository;
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        if (categoria == null) {
            throw new DatoInvalidoException("La categoría no puede ser nula");
        }

        CategoriaEntity entity;
        if (categoria.getId() != null) {
            entity = categoriaJpaRepository.findById(categoria.getId())
                    .orElse(new CategoriaEntity());
        } else {
            entity = new CategoriaEntity();
        }

        CategoriaMapper.toEntity(categoria, entity);
        CategoriaEntity guardado = categoriaJpaRepository.save(entity);
        return CategoriaMapper.toDomain(guardado);
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaJpaRepository.findById(id).map(CategoriaMapper::toDomain);
    }

    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaJpaRepository.findByNombreIgnoreCase(nombre).map(CategoriaMapper::toDomain);
    }

    @Override
    public List<Categoria> listar() {
        return categoriaJpaRepository.findAll().stream()
                .map(CategoriaMapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        categoriaJpaRepository.deleteById(id);
    }
}

