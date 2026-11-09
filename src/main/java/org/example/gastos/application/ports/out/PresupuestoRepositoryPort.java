package org.example.gastos.application.ports.out;

import org.example.gastos.domain.models.Presupuesto;

import java.util.List;
import java.util.Optional;

public interface PresupuestoRepositoryPort {

    Presupuesto guardar(Presupuesto presupuesto);

    Optional<Presupuesto> buscarPorId(Long id);

    Optional<Presupuesto> buscarPorCategoriaId(Long categoriaId);

    List<Presupuesto> listar();

    void eliminar(Long id);
}

