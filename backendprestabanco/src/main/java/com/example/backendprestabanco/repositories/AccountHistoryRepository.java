package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Long> {

    // Buscar historial de cuentas por RUT
    List<AccountHistoryEntity> findByRut(String rut);

    List<AccountHistoryEntity> findByRutAndTransactionDateAfter(String rut, LocalDate date);

    // Buscar depósitos por RUT, tipo de transacción y fecha posterior a una fecha dada
    List<AccountHistoryEntity> findByRutAndTransactionTypeAndTransactionDateAfter(String rut, String transactionType, LocalDate date);

    // query nativo para guardar una transacción en la base de datos
    @Query(value = "INSERT INTO account_history (rut, transaction_type, transaction_date, amount) VALUES (:rut, :transactionType, :transactionDate, :amount)", nativeQuery = true)
    void saveTransactionNativeQuery(@Param("rut") String rut, @Param("transactionType") String transactionType, @Param("transactionDate") LocalDate transactionDate, @Param("amount") double amount);

}
