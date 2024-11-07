package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import com.example.backendprestabanco.repositories.AccountHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(AccountHistoryService.class);

    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    public List<AccountHistoryEntity> getAccountHistoryByRut(String rut) {
        return accountHistoryRepository.findByRut(rut);
    }

    public AccountHistoryEntity saveAccountHistory(AccountHistoryEntity history) {
        try {
            logger.info("Intentando guardar historial de transacción: {}", history);
            return accountHistoryRepository.save(history);
        } catch (Exception e) {
            logger.error("Error al guardar el historial de transacción: ", e);
            throw e;
        }
    }
}
