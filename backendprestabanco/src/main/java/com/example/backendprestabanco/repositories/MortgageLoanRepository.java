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
    public void deleteById(Long id);
    Optional<MortgageLoanEntity> findById(Long id);

    @Query(value = "SELECT * FROM mortgage_loans WHERE mortgage_loans.rut = :rut", nativeQuery = true)
    MortgageLoanEntity findByRutNativeQuery(@Param("rut") String rut);

    // Query nativa para eliminar una solicitud de credito por el ID de la solicitud
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM mortgage_loans WHERE id = :id", nativeQuery = true)
    void deleteByIdNativeQuery(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE MortgageLoanEntity m SET m.status = :status WHERE m.id = :id")
    void updateMortgageLoanStatus(@Param("id") Long id, @Param("status") String status);
}
