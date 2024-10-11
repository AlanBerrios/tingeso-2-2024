package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.SavingsAccountEntity;
import com.example.backendprestabanco.services.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/savings-accounts")
@CrossOrigin("*")
public class SavingsAccountController {

    @Autowired
    private SavingsAccountService savingsAccountService;

    // Obtener todas las cuentas de ahorros
    @GetMapping("/")
    public ResponseEntity<List<SavingsAccountEntity>> listSavingsAccounts() {
        List<SavingsAccountEntity> accounts = savingsAccountService.getAllSavingsAccounts();
        return ResponseEntity.ok(accounts);
    }

    // Obtener cuenta de ahorros por RUT
    @GetMapping("/{rut}")
    public ResponseEntity<SavingsAccountEntity> getSavingsAccountByRut(@PathVariable String rut) {
        SavingsAccountEntity account = savingsAccountService.getSavingsAccountByRut(rut);
        return ResponseEntity.ok(account);
    }

    // Guardar nueva cuenta de ahorros
    @PostMapping("/")
    public ResponseEntity<SavingsAccountEntity> saveSavingsAccount(@RequestBody SavingsAccountEntity account) {
        SavingsAccountEntity newAccount = savingsAccountService.saveSavingsAccount(account);
        return ResponseEntity.ok(newAccount);
    }

    // Actualizar cuenta de ahorros existente
    @PutMapping("/")
    public ResponseEntity<SavingsAccountEntity> updateSavingsAccount(@RequestBody SavingsAccountEntity account) {
        SavingsAccountEntity updatedAccount = savingsAccountService.updateSavingsAccount(account);
        return ResponseEntity.ok(updatedAccount);
    }

    // Eliminar cuenta de ahorros por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteSavingsAccount(@PathVariable Long id) {
        boolean isDeleted = savingsAccountService.deleteSavingsAccount(id);
        return ResponseEntity.noContent().build();
    }
}
