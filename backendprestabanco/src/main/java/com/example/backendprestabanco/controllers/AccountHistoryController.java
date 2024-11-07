package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import com.example.backendprestabanco.services.AccountHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account-history")
@CrossOrigin("*")
public class AccountHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(AccountHistoryController.class);


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
    public ResponseEntity<?> saveAccountHistory(@RequestBody AccountHistoryEntity history) {
        try {
            // Validación de campos obligatorios
            if (history.getRut() == null || history.getTransactionType() == null || history.getTransactionAmount() == null) {
                return ResponseEntity.badRequest().body("Todos los campos requeridos deben estar presentes");
            }

            AccountHistoryEntity newHistory = accountHistoryService.saveAccountHistory(history);
            return ResponseEntity.ok(newHistory);

        } catch (Exception e) {
            logger.error("Error al guardar el historial de la cuenta: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el historial de la cuenta: " + e.getMessage());
        }
    }

}
