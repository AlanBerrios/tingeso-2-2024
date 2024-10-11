package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "account_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId; // Identificador único del historial de cuentas

    @Column(nullable = false)
    private String rut; // Identificador único del cliente (RUT)

    @Column(nullable = false)
    private Long accountId; // Identificador de la cuenta asociada

    private String accountType; // Tipo de cuenta (Ej. "Ahorros", "Inversiones")
    private String transactionType; // Tipo de movimiento (Ej. "Retiro", "Depósito")
    private Double transactionAmount; // Monto del movimiento
    private Double balanceAfterTransaction; // Saldo despues del movimiento
    private LocalDate transactionDate; // Fecha del movimiento
    private LocalTime transactionTime; // Hora del movimiento
}
