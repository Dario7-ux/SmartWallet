package org.example.gastos.application.ports.out;

import org.example.gastos.domain.models.Gasto;

import java.util.List;
import java.util.Optional;

public interface GastoRepositoryPort {

    Gasto guardar(Gasto gasto);

    Optional<Gasto> buscarPorId(Long id);

    List<Gasto> listar();

    List<Gasto> listarPorCategoria(Long categoriaId);

    void eliminar(Long id);
}

