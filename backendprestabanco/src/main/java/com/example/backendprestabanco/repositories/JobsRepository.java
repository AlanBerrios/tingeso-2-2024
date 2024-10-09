package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.JobsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsRepository extends JpaRepository<JobsEntity, String> {

    // Buscar trabajos por RUT
    List<JobsEntity> findByRut(String rut);

    // Eliminar trabajo por RUT
    void deleteByRut(String rut);
}
