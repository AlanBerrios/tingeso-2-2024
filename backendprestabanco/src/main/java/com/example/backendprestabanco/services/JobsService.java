package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.JobsEntity;
import com.example.backendprestabanco.repositories.JobsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobsService {

    @Autowired
    private JobsRepository jobsRepository;

    // Obtener todos los trabajos
    public List<JobsEntity> getAllJobs() {
        return jobsRepository.findAll();
    }

    // Obtener trabajos por RUT
    public List<JobsEntity> getJobsByRut(String rut) {
        return jobsRepository.findByRut(rut);
    }

    // Guardar nuevo trabajo
    public JobsEntity saveJob(JobsEntity job) {
        return jobsRepository.save(job);
    }

    // Actualizar trabajo existente
    public JobsEntity updateJob(JobsEntity job) {
        return jobsRepository.save(job);
    }

    // Eliminar trabajo por RUT
    public boolean deleteJob(String rut) {
        try {
            jobsRepository.deleteByRut(rut);
            return true;
        } catch (Exception e) {
            return false; // O manejar la excepción según sea necesario
        }
    }
}
