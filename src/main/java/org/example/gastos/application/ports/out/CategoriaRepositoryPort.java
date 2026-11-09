package org.example.gastos.application.ports.out;

import org.example.gastos.domain.models.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepositoryPort {

    Categoria guardar(Categoria categoria);

    Optional<Categoria> buscarPorId(Long id);

    Optional<Categoria> buscarPorNombre(String nombre);

    List<Categoria> listar();

    void eliminar(Long id);
}

