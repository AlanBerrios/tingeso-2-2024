// ClientDocument.java
package com.example.backendprestabanco.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "client_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_rut", nullable = false)
    private String clientRut;

    @Column(name = "document_name", nullable = false)
    private String documentName;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] documentData;

    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    @Column(name = "document_type", nullable = false)
    private String documentType;
}