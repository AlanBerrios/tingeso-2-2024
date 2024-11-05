package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientDocumentEntity;
import com.example.backendprestabanco.entities.DocumentationEntity;
import com.example.backendprestabanco.repositories.ClientDocumentRepository;
import com.example.backendprestabanco.repositories.DocumentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ClientDocumentService {

    private static final Logger LOGGER = Logger.getLogger(ClientDocumentService.class.getName());

    @Autowired
    private ClientDocumentRepository clientDocumentRepository;

    @Autowired
    private DocumentationRepository documentationRepository;

    public ClientDocumentEntity saveDocument(String clientRut, String documentType, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o no es válido.");
        }

        // Save the document
        ClientDocumentEntity document = new ClientDocumentEntity();
        document.setClientRut(clientRut);
        document.setDocumentType(documentType);
        document.setDocumentName(file.getOriginalFilename());
        document.setDocumentData(file.getBytes());
        document.setUploadDate(new Date());
        clientDocumentRepository.save(document);

        LOGGER.info("Document uploaded successfully, attempting to update DocumentationEntity.");

        // Update the corresponding field in DocumentationEntity
        Optional<DocumentationEntity> documentationOpt = documentationRepository.findById(clientRut);
        if (documentationOpt.isPresent()) {
            DocumentationEntity documentation = documentationOpt.get();
            LOGGER.info("DocumentationEntity found for RUT: " + clientRut);

            // Update the field based on document type
            setDocumentStatus(documentation, documentType, true);

            // Save updated DocumentationEntity
            documentationRepository.save(documentation);
            LOGGER.info("Updated DocumentationEntity saved for documentType: " + documentType);
        } else {
            LOGGER.warning("No DocumentationEntity found for RUT: " + clientRut);
            throw new RuntimeException("No DocumentationEntity found for RUT: " + clientRut);
        }
        return document;
    }

    private void setDocumentStatus(DocumentationEntity documentation, String documentType, boolean status) {
        switch (documentType) {
            case "incomeProof":
                documentation.setIncomeProof(status);
                break;
            case "appraisalCertificate":
                documentation.setAppraisalCertificate(status);
                break;
            case "creditHistory":
                documentation.setCreditHistory(status);
                break;
            case "firstPropertyDeed":
                documentation.setFirstPropertyDeed(status);
                break;
            case "businessFinancialStatement":
                documentation.setBusinessFinancialStatement(status);
                break;
            case "businessPlan":
                documentation.setBusinessPlan(status);
                break;
            case "remodelingBudget":
                documentation.setRemodelingBudget(status);
                break;
            case "updatedAppraisalCertificate":
                documentation.setUpdatedAppraisalCertificate(status);
                break;
            default:
                throw new IllegalArgumentException("Tipo de documento no reconocido: " + documentType);
        }
        documentation.setAllDocumentsCompleted(checkIfAllDocumentsCompleted(documentation));
        LOGGER.info("Set documentType " + documentType + " to " + status + " in DocumentationEntity.");
    }

    private boolean checkIfAllDocumentsCompleted(DocumentationEntity documentation) {
        return Boolean.TRUE.equals(documentation.getIncomeProof()) &&
                Boolean.TRUE.equals(documentation.getAppraisalCertificate()) &&
                Boolean.TRUE.equals(documentation.getCreditHistory()) &&
                Boolean.TRUE.equals(documentation.getFirstPropertyDeed()) &&
                Boolean.TRUE.equals(documentation.getBusinessFinancialStatement()) &&
                Boolean.TRUE.equals(documentation.getBusinessPlan()) &&
                Boolean.TRUE.equals(documentation.getRemodelingBudget()) &&
                Boolean.TRUE.equals(documentation.getUpdatedAppraisalCertificate());
    }

    public Optional<ClientDocumentEntity> getDocumentById(Long id) {
        return clientDocumentRepository.findById(id);
    }

    public List<ClientDocumentEntity> getDocumentsByClientRut(String clientRut) {
        return clientDocumentRepository.findByClientRut(clientRut);
    }

    public void deleteDocument(Long id) {
        clientDocumentRepository.deleteById(id);
    }
}
