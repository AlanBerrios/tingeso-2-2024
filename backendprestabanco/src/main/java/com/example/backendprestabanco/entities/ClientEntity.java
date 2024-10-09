package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String rut; // Identificador único de cliente (RUT en vez de ID)

    private String firstName; // Primer nombre del cliente
    private String lastName; // Apellido del cliente
    private String email; // Correo electrónico
    private String phone; // Teléfono del cliente
    private Double income; // Ingresos del cliente
    private String creditHistory; // Historial crediticio
    private Integer age; // Edad del cliente
    private String employmentType; // Tipo de empleo (Ej. "Empleado", "Independiente")
    private Integer employmentSeniority; // Antigüedad en el empleo (años)
    private String historyStatus; // Estado de historial crediticio
    private Integer pendingDebts; // Cantidad de deudas pendientes
}
