package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.RequestTrackingEntity;
import com.example.backendprestabanco.repositories.RequestTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RequestTrackingService {
   @Autowired
   RequestTrackingRepository requestTrackingRepository;

   public ArrayList<RequestTrackingEntity> getRequestTracking() {
      return (ArrayList<RequestTrackingEntity>) requestTrackingRepository.findAll();
   }

   public RequestTrackingEntity saveRequestTracking(RequestTrackingEntity tracking) {
      return requestTrackingRepository.save(tracking);
   }

   public RequestTrackingEntity getRequestTrackingByRut(String rut) {
      return requestTrackingRepository.findByRut(rut);
   }

   public RequestTrackingEntity updateRequestTracking(RequestTrackingEntity tracking) {
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
