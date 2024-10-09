package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.MortgageLoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MortgageLoanRepository extends JpaRepository<MortgageLoanEntity, String> {
    public MortgageLoanEntity findByRut(String rut);
    public void deleteByRut(String rut);

    // Query nativo para encontrar un préstamo por el RUT del cliente
    @Query(value = "SELECT * FROM mortgage_loans WHERE mortgage_loans.rut = :rut", nativeQuery = true)
    MortgageLoanEntity findByRutNativeQuery(@Param("rut") String rut);
}
