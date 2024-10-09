package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.DocumentationEntity;
import com.example.backendprestabanco.services.DocumentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentation")
@CrossOrigin("*")
public class DocumentationController {

    @Autowired
    DocumentationService documentationService;

    // Obtener todos los documentos asociados a los clientes
    @GetMapping("/")
    public ResponseEntity<List<DocumentationEntity>> listDocumentation() {
        List<DocumentationEntity> documentationList = documentationService.getAllDocumentation();
        return ResponseEntity.ok(documentationList);
    }

    // Obtener los documentos de un cliente por su RUT
    @GetMapping("/{rut}")
    public ResponseEntity<DocumentationEntity> getDocumentationByRut(@PathVariable String rut) {
        DocumentationEntity documentation = documentationService.getDocumentationByRut(rut);
        return ResponseEntity.ok(documentation);
    }

    // Guardar la información de documentación de un cliente
    @PostMapping("/")
    public ResponseEntity<DocumentationEntity> saveDocumentation(@RequestBody DocumentationEntity documentation) {
        DocumentationEntity newDocumentation = documentationService.saveDocumentation(documentation);
        return ResponseEntity.ok(newDocumentation);
    }

    // Actualizar la información de documentación de un cliente
    @PutMapping("/")
    public ResponseEntity<DocumentationEntity> updateDocumentation(@RequestBody DocumentationEntity documentation) {
        DocumentationEntity updatedDocumentation = documentationService.updateDocumentation(documentation);
        return ResponseEntity.ok(updatedDocumentation);
    }

    // Eliminar la información de documentación de un cliente por su RUT
    @DeleteMapping("/{rut}")
    public ResponseEntity<Boolean> deleteDocumentationByRut(@PathVariable String rut) {
        boolean isDeleted = documentationService.deleteDocumentationByRut(rut);
        return ResponseEntity.noContent().build();
    }
}
