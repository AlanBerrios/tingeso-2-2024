package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientDocumentEntity;
import com.example.backendprestabanco.entities.DocumentationEntity;
import com.example.backendprestabanco.repositories.ClientDocumentRepository;
import com.example.backendprestabanco.repositories.DocumentationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ClientDocumentServiceTest {

    @InjectMocks
    private ClientDocumentService clientDocumentService;

    @Mock
    private ClientDocumentRepository clientDocumentRepository;

    @Mock
    private DocumentationRepository documentationRepository;

    private static final String CLIENT_RUT = "12345678-9";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        DocumentationEntity existingDocumentation = new DocumentationEntity();
        existingDocumentation.setRut(CLIENT_RUT);
        existingDocumentation.setIncomeProof(false);
        when(documentationRepository.findById(CLIENT_RUT)).thenReturn(Optional.of(existingDocumentation));
    }

    @Test
    void whenSaveDocument_thenDocumentIsSaved() throws IOException {
        String documentType = "incomeProof";
        MockMultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", "dummy content".getBytes());
        ClientDocumentEntity document = new ClientDocumentEntity();
        document.setClientRut(CLIENT_RUT);
        document.setDocumentType(documentType);
        document.setDocumentName(file.getOriginalFilename());
        document.setDocumentData(file.getBytes());
        document.setUploadDate(new Date());

        when(clientDocumentRepository.save(any(ClientDocumentEntity.class))).thenReturn(document);

        ClientDocumentEntity savedDocument = clientDocumentService.saveDocument(CLIENT_RUT, documentType, file);

        assertThat(savedDocument).isNotNull();
        assertThat(savedDocument.getClientRut()).isEqualTo(CLIENT_RUT);
        assertThat(savedDocument.getDocumentType()).isEqualTo(documentType);
        verify(clientDocumentRepository, times(1)).save(any(ClientDocumentEntity.class));
        verify(documentationRepository, times(1)).save(any(DocumentationEntity.class));
    }

    @Test
    void whenSaveDocumentWithNonexistentDocumentation_thenThrowException() {
        String nonExistentRut = "00000000-0";
        MockMultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", "dummy content".getBytes());
        when(documentationRepository.findById(nonExistentRut)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientDocumentService.saveDocument(nonExistentRut, "incomeProof", file))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No DocumentationEntity found for RUT: " + nonExistentRut);
    }

    @Test
    void whenSaveDocumentWithEmptyFile_thenThrowException() {
        String documentType = "ID";
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.pdf", "application/pdf", new byte[0]);

        assertThatThrownBy(() -> clientDocumentService.saveDocument(CLIENT_RUT, documentType, emptyFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El archivo está vacío o no es válido.");
    }

    @Test
    void whenGetDocumentsByClientRut_thenReturnDocumentsList() {
        ClientDocumentEntity document1 = new ClientDocumentEntity();
        document1.setClientRut(CLIENT_RUT);
        document1.setDocumentType("ID");
        ClientDocumentEntity document2 = new ClientDocumentEntity();
        document2.setClientRut(CLIENT_RUT);
        document2.setDocumentType("Passport");
        List<ClientDocumentEntity> documents = Arrays.asList(document1, document2);
        when(clientDocumentRepository.findByClientRut(CLIENT_RUT)).thenReturn(documents);

        List<ClientDocumentEntity> result = clientDocumentService.getDocumentsByClientRut(CLIENT_RUT);

        assertThat(result).hasSize(2);
        verify(clientDocumentRepository, times(1)).findByClientRut(CLIENT_RUT);
    }

    @Test
    void whenGetDocumentById_thenReturnDocument() {
        Long documentId = 1L;
        ClientDocumentEntity document = new ClientDocumentEntity();
        document.setId(documentId);
        document.setClientRut(CLIENT_RUT);
        when(clientDocumentRepository.findById(documentId)).thenReturn(Optional.of(document));

        Optional<ClientDocumentEntity> result = clientDocumentService.getDocumentById(documentId);

        assertThat(result).isPresent();
        verify(clientDocumentRepository, times(1)).findById(documentId);
    }

    @Test
    void whenDeleteDocument_thenDocumentIsDeletedAndStatusUpdated() {
        // Arrange
        Long documentId = 1L;
        String documentType = "incomeProof";

        // Mockear el documento a eliminar
        ClientDocumentEntity document = new ClientDocumentEntity();
        document.setId(documentId);
        document.setClientRut(CLIENT_RUT);
        document.setDocumentType(documentType);

        // Mockear el comportamiento del repositorio para el documento y el estado de documentación
        DocumentationEntity documentation = new DocumentationEntity();
        documentation.setRut(CLIENT_RUT);
        documentation.setIncomeProof(true);  // El estado inicial es `true`

        when(clientDocumentRepository.findById(documentId)).thenReturn(Optional.of(document));
        when(documentationRepository.findById(CLIENT_RUT)).thenReturn(Optional.of(documentation));

        // Act
        clientDocumentService.deleteDocument(documentId);

        // Assert
        verify(clientDocumentRepository, times(1)).deleteById(documentId);
        verify(documentationRepository, times(1)).save(documentation);
        assertThat(documentation.getIncomeProof()).isFalse();
    }


    @Test
    void whenCheckIfAllDocumentsCompleted_thenReturnCorrectStatus() {
        DocumentationEntity documentation = new DocumentationEntity();
        documentation.setRut(CLIENT_RUT);
        documentation.setIncomeProof(true);
        documentation.setAppraisalCertificate(true);
        documentation.setCreditHistory(true);
        documentation.setFirstPropertyDeed(true);
        documentation.setBusinessFinancialStatement(true);
        documentation.setBusinessPlan(true);
        documentation.setRemodelingBudget(true);
        documentation.setUpdatedAppraisalCertificate(true);

        boolean allCompleted = clientDocumentService.checkIfAllDocumentsCompleted(documentation);

        assertThat(allCompleted).isTrue();

        documentation.setUpdatedAppraisalCertificate(false);
        allCompleted = clientDocumentService.checkIfAllDocumentsCompleted(documentation);

        assertThat(allCompleted).isFalse();
    }
}
