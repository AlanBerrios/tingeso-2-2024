package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import com.example.backendprestabanco.services.CreditEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit-evaluations")
@CrossOrigin("*")
public class CreditEvaluationController {
    @Autowired
    CreditEvaluationService creditEvaluationService;

    @GetMapping("/")
    public ResponseEntity<List<CreditEvaluationEntity>> listCreditEvaluations() {
        List<CreditEvaluationEntity> evaluations = creditEvaluationService.getCreditEvaluations();
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<CreditEvaluationEntity> getCreditEvaluationByRut(@PathVariable String rut) {
        CreditEvaluationEntity evaluation = creditEvaluationService.getCreditEvaluationByRut(rut);
        return ResponseEntity.ok(evaluation);
    }

    @PostMapping("/")
    public ResponseEntity<CreditEvaluationEntity> saveCreditEvaluation(@RequestBody CreditEvaluationEntity evaluation) {
        CreditEvaluationEntity newEvaluation = creditEvaluationService.saveCreditEvaluation(evaluation);
        return ResponseEntity.ok(newEvaluation);
    }

    @PutMapping("/")
    public ResponseEntity<CreditEvaluationEntity> updateCreditEvaluation(@RequestBody CreditEvaluationEntity evaluation) {
        CreditEvaluationEntity updatedEvaluation = creditEvaluationService.updateCreditEvaluation(evaluation);
        return ResponseEntity.ok(updatedEvaluation);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Boolean> deleteCreditEvaluationByRut(@PathVariable String rut) throws Exception {
        var isDeleted = creditEvaluationService.deleteCreditEvaluation(rut);
        return ResponseEntity.noContent().build();
    }
}
