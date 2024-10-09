package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.DebtEntity;
import com.example.backendprestabanco.services.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/debts")
@CrossOrigin("*")
public class DebtController {

    @Autowired
    private DebtService debtService;

    // Obtener todas las deudas
    @GetMapping("/")
    public ResponseEntity<List<DebtEntity>> listDebts() {
        List<DebtEntity> debts = debtService.getAllDebts();
        return ResponseEntity.ok(debts);
    }

    // Obtener deuda por RUT
    @GetMapping("/{rut}")
    public ResponseEntity<List<DebtEntity>> getDebtsByRut(@PathVariable String rut) {
        List<DebtEntity> debts = debtService.getDebtsByRut(rut);
        return ResponseEntity.ok(debts);
    }

    // Guardar nueva deuda
    @PostMapping("/")
    public ResponseEntity<DebtEntity> saveDebt(@RequestBody DebtEntity debt) {
        DebtEntity newDebt = debtService.saveDebt(debt);
        return ResponseEntity.ok(newDebt);
    }

    // Actualizar deuda existente
    @PutMapping("/")
    public ResponseEntity<DebtEntity> updateDebt(@RequestBody DebtEntity debt) {
        DebtEntity updatedDebt = debtService.updateDebt(debt);
        return ResponseEntity.ok(updatedDebt);
    }

    // Eliminar deuda por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDebt(@PathVariable Long id) {
        boolean isDeleted = debtService.deleteDebt(id);
        return ResponseEntity.noContent().build();
    }
}
