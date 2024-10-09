package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "documentation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentationEntity {

    @Id
    @Column(unique = true, nullable = false)
    private String rut; // Identificador único de cliente (RUT en vez de ID)

    // Booleanos que indican si el cliente ha proporcionado cada documento necesario

    private Boolean incomeProof; // Comprobante de ingresos
    private Boolean appraisalCertificate; // Certificado de avalúo
    private Boolean creditHistory; // Historial crediticio

    // Documentación adicional para préstamos específicos
    private Boolean firstPropertyDeed; // Escritura de la primera vivienda (solo para segunda vivienda)
    private Boolean businessFinancialStatement; // Estado financiero del negocio (solo para propiedades comerciales)
    private Boolean businessPlan; // Plan de negocios (solo para propiedades comerciales)
    private Boolean remodelingBudget; // Presupuesto de la remodelación (solo para remodelación)
    private Boolean updatedAppraisalCertificate; // Certificado de avalúo actualizado (solo para remodelación)

    // Indica si todos los documentos requeridos están completos
    private Boolean allDocumentsCompleted;
}
