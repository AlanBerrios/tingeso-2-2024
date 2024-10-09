package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditEvaluationRepository extends JpaRepository<CreditEvaluationEntity, String> {
    public CreditEvaluationEntity findByRut(String rut);
    public void deleteByRut(String rut);

    // Query nativo para encontrar una evaluación de crédito por el RUT del cliente
    @Query(value = "SELECT * FROM credit_evaluations WHERE credit_evaluations.rut = :rut", nativeQuery = true)
    CreditEvaluationEntity findByRutNativeQuery(@Param("rut") String rut);
}
