package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientDocumentEntity;
import com.example.backendprestabanco.repositories.ClientDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ClientDocumentServiceTest {

    @InjectMocks
    private ClientDocumentService clientDocumentService;

    @Mock
    private ClientDocumentRepository clientDocumentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenSaveDocument_thenDocumentIsSaved() throws IOException {
        // Given
        String clientRut = "12345678-9";
        String documentType = "ID";
        MockMultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", "dummy content".getBytes());

        ClientDocumentEntity document = new ClientDocumentEntity();
        document.setClientRut(clientRut);
        document.setDocumentType(documentType);
        document.setDocumentName(file.getOriginalFilename());
        document.setDocumentData(file.getBytes());
        document.setUploadDate(new Date());

        given(clientDocumentRepository.save(any(ClientDocumentEntity.class))).willReturn(document);

        // When
        ClientDocumentEntity savedDocument = clientDocumentService.saveDocument(clientRut, documentType, file);

        // Then
        assertThat(savedDocument).isNotNull();
        assertThat(savedDocument.getClientRut()).isEqualTo(clientRut);
        assertThat(savedDocument.getDocumentType()).isEqualTo(documentType);
        assertThat(savedDocument.getDocumentName()).isEqualTo("document.pdf");
        verify(clientDocumentRepository, times(1)).save(any(ClientDocumentEntity.class));
    }

    @Test
    void whenSaveDocumentWithEmptyFile_thenThrowException() {
        // Given
        String clientRut = "12345678-9";
        String documentType = "ID";
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.pdf", "application/pdf", new byte[0]);

        // Then
        assertThatThrownBy(() -> clientDocumentService.saveDocument(clientRut, documentType, emptyFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El archivo está vacío o no es válido.");
    }

    @Test
    void whenGetDocumentsByClientRut_thenReturnDocumentsList() {
        // Given
        String clientRut = "12345678-9";
        ClientDocumentEntity document1 = new ClientDocumentEntity();
        document1.setClientRut(clientRut);
        document1.setDocumentType("ID");

        ClientDocumentEntity document2 = new ClientDocumentEntity();
        document2.setClientRut(clientRut);
        document2.setDocumentType("Passport");

        List<ClientDocumentEntity> documents = Arrays.asList(document1, document2);
        given(clientDocumentRepository.findByClientRut(clientRut)).willReturn(documents);

        // When
        List<ClientDocumentEntity> result = clientDocumentService.getDocumentsByClientRut(clientRut);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(document1, document2);
        verify(clientDocumentRepository, times(1)).findByClientRut(clientRut);
    }

    @Test
    void whenGetDocumentById_thenReturnDocument() {
        // Given
        Long documentId = 1L;
        ClientDocumentEntity document = new ClientDocumentEntity();
        document.setId(documentId);
        document.setClientRut("12345678-9");
        document.setDocumentType("ID");

        given(clientDocumentRepository.findById(documentId)).willReturn(Optional.of(document));

        // When
        Optional<ClientDocumentEntity> result = clientDocumentService.getDocumentById(documentId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(documentId);
        verify(clientDocumentRepository, times(1)).findById(documentId);
    }

    @Test
    void whenGetDocumentByIdAndDocumentNotFound_thenReturnEmpty() {
        // Given
        Long documentId = 1L;
        given(clientDocumentRepository.findById(documentId)).willReturn(Optional.empty());

        // When
        Optional<ClientDocumentEntity> result = clientDocumentService.getDocumentById(documentId);

        // Then
        assertThat(result).isEmpty();
        verify(clientDocumentRepository, times(1)).findById(documentId);
    }

    @Test
    void whenDeleteDocument_thenDocumentIsDeleted() {
        // Given
        Long documentId = 1L;

        // When
        clientDocumentService.deleteDocument(documentId);

        // Then
        verify(clientDocumentRepository, times(1)).deleteById(documentId);
    }
}
