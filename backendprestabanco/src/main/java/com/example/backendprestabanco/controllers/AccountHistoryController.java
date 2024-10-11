package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import com.example.backendprestabanco.services.AccountHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-history")
@CrossOrigin("*")
public class AccountHistoryController {

    @Autowired
    private AccountHistoryService accountHistoryService;

    // Obtener el historial de cuentas por RUT
    @GetMapping("/{rut}")
    public ResponseEntity<List<AccountHistoryEntity>> getAccountHistoryByRut(@PathVariable String rut) {
        List<AccountHistoryEntity> history = accountHistoryService.getAccountHistoryByRut(rut);
        return ResponseEntity.ok(history);
    }

    // Guardar nueva transacción en el historial de cuentas
    @PostMapping("/")
    public ResponseEntity<AccountHistoryEntity> saveAccountHistory(@RequestBody AccountHistoryEntity history) {
        AccountHistoryEntity newHistory = accountHistoryService.saveAccountHistory(history);
        return ResponseEntity.ok(newHistory);
    }
}
