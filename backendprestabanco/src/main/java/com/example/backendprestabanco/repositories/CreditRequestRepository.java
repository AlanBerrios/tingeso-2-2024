package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.CreditRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRequestRepository extends JpaRepository<CreditRequestEntity, String> {
    public CreditRequestEntity findByRut(String rut);
    public void deleteByRut(String rut);

    // Query nativo para encontrar una solicitud de crédito por el RUT del cliente
    @Query(value = "SELECT * FROM credit_requests WHERE credit_requests.rut = :rut", nativeQuery = true)
    CreditRequestEntity findByRutNativeQuery(@Param("rut") String rut);
}
