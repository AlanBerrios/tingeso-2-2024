package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.DebtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<DebtEntity, Long> {

    // Buscar deudas por RUT
    List<DebtEntity> findByRut(String rut);

    void deleteByRut(String rut);

    // Eliminar deuda por ID
    void deleteById(Long id);
}
