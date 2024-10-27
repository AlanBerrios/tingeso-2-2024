package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "savings_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId; // Identificador único de la cuenta de ahorros

    @Column(nullable = false)
    private String rut; // Identificador único del cliente (RUT)

    private Double balance; // Saldo actual de la cuenta de ahorros
    private LocalDate openingDate; // Fecha de apertura de la cuenta de ahorros
}
