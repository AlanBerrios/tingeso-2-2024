package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.CreditRequestEntity;
import com.example.backendprestabanco.services.CreditRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit-requests")
@CrossOrigin("*")
public class CreditRequestController {
    @Autowired
    CreditRequestService creditRequestService;

    @GetMapping("/")
    public ResponseEntity<List<CreditRequestEntity>> listCreditRequests() {
        List<CreditRequestEntity> requests = creditRequestService.getCreditRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<CreditRequestEntity> getCreditRequestByRut(@PathVariable String rut) {
        CreditRequestEntity request = creditRequestService.getCreditRequestByRut(rut);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/")
    public ResponseEntity<CreditRequestEntity> saveCreditRequest(@RequestBody CreditRequestEntity request) {
        CreditRequestEntity newRequest = creditRequestService.saveCreditRequest(request);
        return ResponseEntity.ok(newRequest);
    }

    @PutMapping("/")
    public ResponseEntity<CreditRequestEntity> updateCreditRequest(@RequestBody CreditRequestEntity request) {
        CreditRequestEntity updatedRequest = creditRequestService.updateCreditRequest(request);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Boolean> deleteCreditRequestByRut(@PathVariable String rut) throws Exception {
        var isDeleted = creditRequestService.deleteCreditRequest(rut);
        return ResponseEntity.noContent().build();
    }
}
