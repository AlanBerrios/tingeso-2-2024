package com.example.backendprestabanco.repositories;

import com.example.backendprestabanco.entities.RequestTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTrackingRepository extends JpaRepository<RequestTrackingEntity, String> {
    public RequestTrackingEntity findByRut(String rut);
    public void deleteByRut(String rut);

    // Query nativo para encontrar el seguimiento de una solicitud por el RUT del cliente
    @Query(value = "SELECT * FROM request_tracking WHERE request_tracking.rut = :rut", nativeQuery = true)
    RequestTrackingEntity findByRutNativeQuery(@Param("rut") String rut);
}
