package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "credit_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestEntity {

    // CreditRequestEntity es la entidad que representa una solicitud de crédito

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rut; // Identificador único de cliente (RUT en vez de ID)

    private Double requestedAmount; // Monto solicitado por el cliente
    private String status; // Estado de la solicitud (Ej. "En Revisión", "Aprobada")
    private String attachedDocuments; // Documentos adjuntos (ej. URLs o rutas)
    private LocalDate requestDate; // Fecha en la que se realizó la solicitud
}
