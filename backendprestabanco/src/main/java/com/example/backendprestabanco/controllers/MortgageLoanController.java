package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.MortgageLoanEntity;
import com.example.backendprestabanco.services.MortgageLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mortgage-loans")
@CrossOrigin("*")
public class MortgageLoanController {
    @Autowired
    MortgageLoanService mortgageLoanService;

    @GetMapping("/")
    public ResponseEntity<List<MortgageLoanEntity>> listMortgageLoans() {
        List<MortgageLoanEntity> loans = mortgageLoanService.getMortgageLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MortgageLoanEntity> getMortgageLoanById(@PathVariable Long id) {
        MortgageLoanEntity loan = mortgageLoanService.getMortgageLoanById(id);
        if (loan != null) {
            return ResponseEntity.ok(loan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<List<MortgageLoanEntity>> getMortgageLoansByRut(@PathVariable String rut) {
        List<MortgageLoanEntity> loans = mortgageLoanService.getMortgageLoansByRut(rut);
        if (loans != null && !loans.isEmpty()) {
            return ResponseEntity.ok(loans);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateMortgageLoanStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        mortgageLoanService.updateMortgageLoanStatus(id, status);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/")
    public ResponseEntity<MortgageLoanEntity> saveMortgageLoan(@RequestBody MortgageLoanEntity loan) {
        MortgageLoanEntity newLoan = mortgageLoanService.saveMortgageLoan(loan);
        return ResponseEntity.ok(newLoan);
    }

    @PutMapping("/")
    public ResponseEntity<MortgageLoanEntity> updateMortgageLoan(@RequestBody MortgageLoanEntity loan) {
        MortgageLoanEntity updatedLoan = mortgageLoanService.updateMortgageLoan(loan);
        return ResponseEntity.ok(updatedLoan);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Boolean> deleteMortgageLoanByRut(@PathVariable String rut) throws Exception {
        var isDeleted = mortgageLoanService.deleteMortgageLoan(rut);
        return ResponseEntity.noContent().build();
    }
}
