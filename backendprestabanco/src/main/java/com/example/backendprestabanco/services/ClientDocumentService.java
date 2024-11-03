package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientDocumentEntity;
import com.example.backendprestabanco.repositories.ClientDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClientDocumentService {

    @Autowired
    private ClientDocumentRepository clientDocumentRepository;

    public ClientDocumentEntity saveDocument(String clientRut, String documentType, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o no es válido.");
        }

        ClientDocumentEntity document = new ClientDocumentEntity();
        document.setClientRut(clientRut);
        document.setDocumentType(documentType);  // Incluye el tipo de documento
        document.setDocumentName(file.getOriginalFilename());
        document.setDocumentData(file.getBytes());
        document.setUploadDate(new Date());

        return clientDocumentRepository.save(document);
    }

    public List<ClientDocumentEntity> getDocumentsByClientRut(String clientRut) {
        return clientDocumentRepository.findByClientRut(clientRut);
    }

    public Optional<ClientDocumentEntity> getDocumentById(Long id) {
        return clientDocumentRepository.findById(id);
    }

    public void deleteDocument(Long id) {
        System.out.println("Eliminando documento con ID: " + id);
        clientDocumentRepository.deleteById(id);
        System.out.println("Documento eliminado de la base de datos.");
    }


}
