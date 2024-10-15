package com.example.backendprestabanco.enums;

public enum RequestStatusEnum {
    IN_INITIAL_REVIEW,           // En Revisión Inicial
    PENDING_DOCUMENTATION,       // Pendiente de Documentación
    UNDER_REVIEW,                // En Evaluación
    PRE_APPROVED,                // Pre-Aprobada
    FINAL_APPROVAL,              // En Aprobación Final
    APPROVED,                    // Aprobada
    REJECTED,                    // Rechazada
    CANCELED_BY_CLIENT,          // Cancelada por el Cliente
    DISBURSEMENT_IN_PROGRESS     // En Desembolso
}
