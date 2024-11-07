package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import com.example.backendprestabanco.repositories.AccountHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.backendprestabanco.controllers.AccountHistoryController.logger;

@Service
public class AccountHistoryService {

    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountHistoryService.class);


    // Obtener el historial de cuentas por RUT
    public List<AccountHistoryEntity> getAccountHistoryByRut(String rut) {
        return accountHistoryRepository.findByRut(rut);
    }

    // Guardar nueva transacción en el historial de cuentas
    public AccountHistoryEntity saveAccountHistory(AccountHistoryEntity history) {
        try {
            logger.info("Guardando historial de transacción: {}", history);
            return accountHistoryRepository.save(history);
        } catch (Exception e) {
            logger.error("Error al guardar el historial de transacción: ", e);
            throw e; // Propagar la excepción para manejarla en el controlador
        }
    }
}
