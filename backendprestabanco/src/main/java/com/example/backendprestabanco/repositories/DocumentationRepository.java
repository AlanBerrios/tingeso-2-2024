package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.DocumentationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentationRepository extends JpaRepository<DocumentationEntity, String> {

    // Buscar documentación por RUT
    DocumentationEntity findByRut(String rut);

    // Eliminar documentación por RUT
    void deleteByRut(String rut);
}
