package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.JobsEntity;
import com.example.backendprestabanco.services.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@CrossOrigin("*")
public class JobsController {

    @Autowired
    private JobsService jobsService;

    // Obtener todos los trabajos
    @GetMapping("/")
    public ResponseEntity<List<JobsEntity>> listJobs() {
        List<JobsEntity> jobs = jobsService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    // Obtener trabajo por RUT
    @GetMapping("/{rut}")
    public ResponseEntity<List<JobsEntity>> getJobsByRut(@PathVariable String rut) {
        List<JobsEntity> jobs = jobsService.getJobsByRut(rut);
        return ResponseEntity.ok(jobs);
    }

    // Guardar nuevo trabajo
    @PostMapping("/")
    public ResponseEntity<JobsEntity> saveJob(@RequestBody JobsEntity job) {
        JobsEntity newJob = jobsService.saveJob(job);
        return ResponseEntity.ok(newJob);
    }

    // Actualizar trabajo existente
    @PutMapping("/")
    public ResponseEntity<JobsEntity> updateJob(@RequestBody JobsEntity job) {
        JobsEntity updatedJob = jobsService.updateJob(job);
        return ResponseEntity.ok(updatedJob);
    }

    // Eliminar trabajo por RUT
    @DeleteMapping("/{rut}")
    public ResponseEntity<Boolean> deleteJob(@PathVariable String rut) {
        boolean isDeleted = jobsService.deleteJob(rut);
        return ResponseEntity.noContent().build();
    }
}
