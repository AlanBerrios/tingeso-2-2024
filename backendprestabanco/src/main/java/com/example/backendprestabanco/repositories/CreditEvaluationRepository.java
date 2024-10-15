package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditEvaluationRepository extends JpaRepository<CreditEvaluationEntity, String> {

    CreditEvaluationEntity findByRut(String rut);

    void deleteByRut(String rut);

    // Query nativa para encontrar una evaluación de crédito por el RUT del cliente
    @Query(value = "SELECT * FROM credit_evaluations WHERE credit_evaluations.rut = :rut", nativeQuery = true)
    CreditEvaluationEntity findByRutNativeQuery(@Param("rut") String rut);

    // Relación cuota/ingreso mayor a cierto porcentaje
    @Query("SELECT c FROM CreditEvaluationEntity c WHERE c.paymentToIncomeRatio > :ratio")
    List<CreditEvaluationEntity> findByPaymentToIncomeRatioGreaterThan(@Param("ratio") double ratio);

}
