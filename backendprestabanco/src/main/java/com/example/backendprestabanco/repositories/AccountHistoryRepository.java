package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Long> {

    // Buscar historial de cuentas por RUT
    List<AccountHistoryEntity> findByRut(String rut);

    List<AccountHistoryEntity> findByRutAndTransactionDateAfter(String rut, LocalDate date);

    // Buscar depósitos por RUT, tipo de transacción y fecha posterior a una fecha dada
    List<AccountHistoryEntity> findByRutAndTransactionTypeAndTransactionDateAfter(String rut, String transactionType, LocalDate date);

    // Guardar transacción usando una native query
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO account_history (rut, account_type, transaction_type, transaction_amount, balance_after_transaction, transaction_date, transaction_time) " +
            "VALUES (:rut, :accountType, :transactionType, :transactionAmount, :balanceAfterTransaction, :transactionDate, :transactionTime)", nativeQuery = true)
    void saveTransactionNativeQuery(@Param("rut") String rut,
                                    @Param("accountType") String accountType,
                                    @Param("transactionType") String transactionType,
                                    @Param("transactionAmount") Double transactionAmount,
                                    @Param("balanceAfterTransaction") Double balanceAfterTransaction,
                                    @Param("transactionDate") LocalDate transactionDate,
                                    @Param("transactionTime") LocalTime transactionTime);

}

