package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {
    public ClientEntity findByRut(String rut);
    public void deleteByRut(String rut);
    public boolean existsByRut(String rut);

    // Query nativa para eliminar un cliente por su RUT
    @Modifying
    @Query(value = "DELETE FROM clients WHERE clients.rut = :rut", nativeQuery = true)
    void deleteByRutNativeQuery(@Param("rut") String rut);

    // Query nativo para encontrar un cliente por su RUT
    @Query(value = "SELECT * FROM clients WHERE clients.rut = :rut", nativeQuery = true)
    ClientEntity findByRutNativeQuery(@Param("rut") String rut);

    List<ClientEntity> findByIncomeGreaterThan(int i);

    List<ClientEntity> findByEmploymentType(String s);
}
