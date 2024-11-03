package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.MortgageLoanEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MortgageLoanRepository extends JpaRepository<MortgageLoanEntity, String> {
    public MortgageLoanEntity findByRut(String rut);
    public List<MortgageLoanEntity> findAllByRut(String rut);
    public void deleteByRut(String rut);
    Optional<MortgageLoanEntity> findById(Long id);

    @Query(value = "SELECT * FROM mortgage_loans WHERE mortgage_loans.rut = :rut", nativeQuery = true)
    MortgageLoanEntity findByRutNativeQuery(@Param("rut") String rut);

    @Modifying
    @Transactional
    @Query("UPDATE MortgageLoanEntity m SET m.status = :status WHERE m.id = :id")
    void updateMortgageLoanStatus(@Param("id") Long id, @Param("status") String status);
}
