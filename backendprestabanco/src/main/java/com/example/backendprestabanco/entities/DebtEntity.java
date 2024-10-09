package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "debts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único de la deuda

    @Column(nullable = false)
    private String rut; // Identificador único del cliente (RUT en vez de relación @ManyToOne)

    private Double totalAmount; // Monto total de la deuda
    private Double monthlyPayment; // Pago mensual de la deuda
    private String description; // Descripción de la deuda
    private Boolean isActive; // Indica si la deuda está activa o si ya fue pagada completamente
}
