package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.AccountHistoryEntity;
import com.example.backendprestabanco.repositories.AccountHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHistoryService {

    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    // Obtener el historial de cuentas por RUT
    public List<AccountHistoryEntity> getAccountHistoryByRut(String rut) {
        return accountHistoryRepository.findByRut(rut);
    }

    // Guardar nueva transacción en el historial de cuentas
    public AccountHistoryEntity saveAccountHistory(AccountHistoryEntity history) {
        return accountHistoryRepository.save(history);
    }
}
