package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.ClientDocumentEntity;
import com.example.backendprestabanco.services.ClientDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/client-documents")
@CrossOrigin("*")
public class ClientDocumentController {

    @Autowired
    private ClientDocumentService clientDocumentService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("clientRut") String clientRut,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        try {
            clientDocumentService.saveDocument(clientRut, documentType, file);
            return ResponseEntity.ok("Document uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload document.");
        }
    }

    @GetMapping("/by-client/{clientRut}")
    public ResponseEntity<List<ClientDocumentEntity>> getDocumentsByClientRut(@PathVariable String clientRut) {
        List<ClientDocumentEntity> documents = clientDocumentService.getDocumentsByClientRut(clientRut);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Optional<ClientDocumentEntity> documentOptional = clientDocumentService.getDocumentById(id);

        if (documentOptional.isPresent()) {
            ClientDocumentEntity document = documentOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getDocumentName() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(document.getDocumentData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        System.out.println("Solicitud para eliminar documento con ID: " + id);
        try {
            clientDocumentService.deleteDocument(id);
            System.out.println("Documento eliminado correctamente.");
            return ResponseEntity.ok("Documento eliminado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar el documento: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar el documento.");
        }
    }


}
