package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.MortgageLoanEntity;
import com.example.backendprestabanco.repositories.MortgageLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MortgageLoanService {
    @Autowired
    MortgageLoanRepository mortgageLoanRepository;

    public ArrayList<MortgageLoanEntity> getMortgageLoans() {
        return (ArrayList<MortgageLoanEntity>) mortgageLoanRepository.findAll();
    }


    public MortgageLoanEntity saveMortgageLoan(MortgageLoanEntity loan) {
        return mortgageLoanRepository.save(loan);
    }

    public MortgageLoanEntity updateMortgageLoan(MortgageLoanEntity loan) {
        return mortgageLoanRepository.save(loan);
    }

    public boolean deleteMortgageLoan(String rut) throws Exception {
        try {
            mortgageLoanRepository.deleteByRut(rut);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public MortgageLoanEntity getMortgageLoanById(Long id) {
        return mortgageLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mortgage loan not found"));
    }

    public List<MortgageLoanEntity> getMortgageLoansByRut(String rut) {
        return mortgageLoanRepository.findAllByRut(rut);
    }

    public MortgageLoanEntity getMortgageLoanByRut(String rut) {
        return mortgageLoanRepository.findByRut(rut);
    }

    public void updateMortgageLoanStatus(Long id, String status) {
        mortgageLoanRepository.updateMortgageLoanStatus(id, status);
    }
}
