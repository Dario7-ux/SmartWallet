package org.example.gastos.infraestructure.repository;

import org.example.gastos.infraestructure.entities.PresupuestoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresupuestoJpaRepository extends JpaRepository<PresupuestoEntity, Long> {

    @Query("SELECT p FROM PresupuestoEntity p JOIN FETCH p.categoria WHERE p.categoria.id = :categoriaId")
    Optional<PresupuestoEntity> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    @Query("SELECT p FROM PresupuestoEntity p JOIN FETCH p.categoria WHERE p.id = :id")
    Optional<PresupuestoEntity> findByIdWithCategoria(@Param("id") Long id);

    @Query("SELECT p FROM PresupuestoEntity p JOIN FETCH p.categoria")
    java.util.List<PresupuestoEntity> findAllWithCategoria();

    boolean existsByCategoriaId(Long categoriaId);
}

