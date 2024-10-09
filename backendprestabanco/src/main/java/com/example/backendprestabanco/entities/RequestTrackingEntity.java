package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestTrackingEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String rut; // Identificador único de cliente (RUT en vez de ID)

    @ManyToOne
    @JoinColumn(name = "request_rut")
    private CreditRequestEntity creditRequest; // Relación con entidad solicitud de crédito

    private String currentStatus; // Estado actual de la solicitud (Ej. "En Evaluación", "Aprobada")
    private String comments; // Comentarios adicionales
    private LocalDateTime lastUpdated; // Fecha y hora de la última actualización
}
