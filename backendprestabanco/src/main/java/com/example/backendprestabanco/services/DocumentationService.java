package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.DocumentationEntity;
import com.example.backendprestabanco.repositories.DocumentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentationService {

    @Autowired
    DocumentationRepository documentationRepository;

    // Obtener toda la documentación de los clientes
    public List<DocumentationEntity> getAllDocumentation() {
        return (ArrayList<DocumentationEntity>) documentationRepository.findAll();
    }

    // Obtener documentación por RUT
    public DocumentationEntity getDocumentationByRut(String rut) {
        return documentationRepository.findByRut(rut);
    }

    // Guardar nueva documentación
    public DocumentationEntity saveDocumentation(DocumentationEntity documentation) {
        return documentationRepository.save(documentation);
    }

    // Actualizar documentación existente
    public DocumentationEntity updateDocumentation(DocumentationEntity documentation) {
        return documentationRepository.save(documentation);
    }

    // Eliminar la documentación de un cliente por su RUT
    public boolean deleteDocumentationByRut(String rut) {
        try {
            documentationRepository.deleteByRut(rut);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
