package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import com.example.backendprestabanco.enums.RequestStatusEnum;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestTrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rut; // Identificador único de cliente (RUT en vez de ID)

    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status; // Estado actual de la solicitud

    private String comments; // Comentarios adicionales
    private LocalDateTime lastUpdated; // Fecha y hora de la última actualización
}
