package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
