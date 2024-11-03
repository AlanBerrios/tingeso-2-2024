// ClientDocumentRepository.java
package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.ClientDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClientDocumentRepository extends JpaRepository<ClientDocumentEntity, Long> {
    List<ClientDocumentEntity> findByClientRut(String clientRut);
}
