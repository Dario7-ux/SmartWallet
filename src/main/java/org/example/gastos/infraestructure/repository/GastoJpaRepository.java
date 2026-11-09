package org.example.gastos.infraestructure.repository;

import org.example.gastos.infraestructure.entities.GastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GastoJpaRepository extends JpaRepository<GastoEntity, Long> {

    @Query("SELECT g FROM GastoEntity g JOIN FETCH g.categoria WHERE g.categoria.id = :categoriaId ORDER BY g.fecha DESC")
    java.util.List<GastoEntity> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    @Query("SELECT g FROM GastoEntity g JOIN FETCH g.categoria ORDER BY g.fecha DESC")
    java.util.List<GastoEntity> findAllWithCategoria();
}

