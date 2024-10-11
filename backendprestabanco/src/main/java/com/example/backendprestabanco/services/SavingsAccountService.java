package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.SavingsAccountEntity;
import com.example.backendprestabanco.repositories.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsAccountService {

    @Autowired
    private SavingsAccountRepository savingsAccountRepository;

    // Obtener todas las cuentas de ahorros
    public List<SavingsAccountEntity> getAllSavingsAccounts() {
        return savingsAccountRepository.findAll();
    }

    // Obtener cuenta de ahorros por RUT
    public SavingsAccountEntity getSavingsAccountByRut(String rut) {
        return savingsAccountRepository.findByRut(rut);
    }

    // Guardar nueva cuenta de ahorros
    public SavingsAccountEntity saveSavingsAccount(SavingsAccountEntity account) {
        return savingsAccountRepository.save(account);
    }

    // Actualizar cuenta de ahorros existente
    public SavingsAccountEntity updateSavingsAccount(SavingsAccountEntity account) {
        return savingsAccountRepository.save(account);
    }

    // Eliminar cuenta de ahorros por ID
    public boolean deleteSavingsAccount(Long id) {
        try {
            savingsAccountRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false; // O manejar la excepción según sea necesario
        }
    }
}
