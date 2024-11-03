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
    @Column(name = "account_id") // Asegúrate de que el nombre coincida con el nombre de la columna en la BD
    private Long accountId;

    @Column(nullable = false)
    private String rut; // Identificador único del cliente (RUT)

    private Double balance; // Saldo actual de la cuenta de ahorros
    private LocalDate openingDate; // Fecha de apertura de la cuenta de ahorros
}
