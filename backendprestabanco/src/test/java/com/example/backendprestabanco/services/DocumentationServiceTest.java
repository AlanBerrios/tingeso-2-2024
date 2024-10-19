package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.DocumentationEntity;
import com.example.backendprestabanco.repositories.DocumentationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class DocumentationServiceTest {

    @MockBean
    private DocumentationRepository documentationRepository;

    @Autowired
    private DocumentationService documentationService;

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

        assertThat(result).hasSize(1);
    }
}
