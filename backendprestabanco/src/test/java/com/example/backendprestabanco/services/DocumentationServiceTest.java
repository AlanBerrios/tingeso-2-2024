package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.DocumentationEntity;
import com.example.backendprestabanco.repositories.DocumentationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class DocumentationServiceTest {

    @InjectMocks
    private DocumentationService documentationService;

    @MockBean
    private DocumentationRepository documentationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllDocumentation_thenReturnDocumentationList() {
        List<DocumentationEntity> docs = new ArrayList<>();
        docs.add(new DocumentationEntity());

        given(documentationRepository.findAll()).willReturn(docs);

        List<DocumentationEntity> result = documentationService.getAllDocumentation();

        assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void whenGetDocumentationByRut_thenReturnDocumentation() {
        String rut = "12345678-9";
        DocumentationEntity doc = new DocumentationEntity();
        doc.setRut(rut);

        given(documentationRepository.findByRut(rut)).willReturn(doc);

        DocumentationEntity result = documentationService.getDocumentationByRut(rut);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(rut);
    }

    @Test
    void whenSaveDocumentation_thenReturnSavedDocumentation() {
        DocumentationEntity doc = new DocumentationEntity();
        doc.setRut("12345678-9");

        given(documentationRepository.save(doc)).willReturn(doc);

        DocumentationEntity result = documentationService.saveDocumentation(doc);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void whenUpdateDocumentation_thenReturnUpdatedDocumentation() {
        DocumentationEntity doc = new DocumentationEntity();
        doc.setRut("12345678-9");

        given(documentationRepository.save(doc)).willReturn(doc);

        DocumentationEntity result = documentationService.updateDocumentation(doc);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void whenDeleteDocumentationByRut_thenReturnTrueIfSuccessful() {
        String rut = "12345678-9";
        doNothing().when(documentationRepository).deleteByRut(rut);

        boolean result = documentationService.deleteDocumentationByRut(rut);

        assertThat(result).isTrue();
        verify(documentationRepository, times(1)).deleteByRut(rut);
    }

    @Test
    void whenDeleteDocumentationByRut_thenReturnFalseIfExceptionOccurs() {
        String rut = "12345678-9";
        doThrow(new RuntimeException("Error deleting documentation")).when(documentationRepository).deleteByRut(rut);

        boolean result = documentationService.deleteDocumentationByRut(rut);

        assertThat(result).isFalse();
        verify(documentationRepository, times(1)).deleteByRut(rut);
    }
}
