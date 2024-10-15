package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.RequestTrackingEntity;
import com.example.backendprestabanco.enums.RequestStatusEnum;
import com.example.backendprestabanco.repositories.RequestTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class RequestTrackingService {

   @Autowired
   RequestTrackingRepository requestTrackingRepository;

   public ArrayList<RequestTrackingEntity> getRequestTracking() {
      return (ArrayList<RequestTrackingEntity>) requestTrackingRepository.findAll();
   }

   public RequestTrackingEntity saveRequestTracking(RequestTrackingEntity tracking) {
      tracking.setLastUpdated(LocalDateTime.now()); // Establecer la fecha de actualización
      return requestTrackingRepository.save(tracking);
   }

   public RequestTrackingEntity getRequestTrackingByRut(String rut) {
      return requestTrackingRepository.findByRut(rut);
   }

   // Actualizar el estado de la solicitud
   public RequestTrackingEntity updateRequestStatus(String rut, RequestStatusEnum status, String comments) {
      RequestTrackingEntity tracking = getRequestTrackingByRut(rut);
      tracking.setStatus(status); // Actualizar el estado
      tracking.setComments(comments); // Actualizar los comentarios si es necesario
      tracking.setLastUpdated(LocalDateTime.now()); // Actualizar la fecha de modificación
      return requestTrackingRepository.save(tracking);
   }

   public boolean deleteRequestTracking(String rut) throws Exception {
      try {
         requestTrackingRepository.deleteByRut(rut);
         return true;
      } catch (Exception e) {
         throw new Exception(e.getMessage());
      }
   }
}
