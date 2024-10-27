package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rut; // Identificador único de cliente (RUT en vez de ID)

    private String firstName; // Primer nombre del cliente
    private String lastName; // Apellido del cliente
    private String companyName; // Nombre de la empresa donde trabaja
    private String jobType; // Tipo de trabajo (Ej. "Independiente", "Dependiente")
    private Integer yearsInJob; // Años en el trabajo actual
    private Boolean isActive; // Estado del trabajo (true = activo, false = inactivo)
    private Integer startYear; // Año en que comenzó el trabajo
    private Integer endYear; // Año en que terminó el trabajo (nulo si sigue activo)
}
