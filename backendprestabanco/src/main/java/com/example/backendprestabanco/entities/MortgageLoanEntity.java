package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "mortgage_loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MortgageLoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rut;

    private String loanType; // Tipo de préstamo (Ej. "Primera Vivienda", "Segunda Vivienda")
    private Double amount; // Monto del préstamo
    private Integer term; // Plazo en meses
    private Double interestRate; // Tasa de interés en porcentaje
    private Double maxFinancingAmount; // Monto máximo de financiamiento
    private String status; // Estado del préstamo (Ej. "En Evaluación", "Aprobado")
    private LocalDate creationDate; // Fecha de creación del préstamo
    private LocalDate updateDate; // Fecha de última actualización del préstamo
    private Boolean documentRequirementsCompleted; // Indica si los requisitos documentales están completos
    private LocalDate documentRequirementsCompletionDate; // Fecha en que se completaron los requisitos documentales
}
