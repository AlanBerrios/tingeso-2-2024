package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientEntity;
import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import com.example.backendprestabanco.repositories.ClientRepository;
import com.example.backendprestabanco.repositories.CreditEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CreditEvaluationService {
    @Autowired
    CreditEvaluationRepository creditEvaluationRepository;
    @Autowired
    private ClientRepository clientRepository;

    public ArrayList<CreditEvaluationEntity> getCreditEvaluations() {
        return (ArrayList<CreditEvaluationEntity>) creditEvaluationRepository.findAll();
    }

    public CreditEvaluationEntity saveCreditEvaluation(CreditEvaluationEntity evaluation) {
        return creditEvaluationRepository.save(evaluation);
    }

    public CreditEvaluationEntity getCreditEvaluationByRut(String rut) {
        return creditEvaluationRepository.findByRut(rut);
    }

    public CreditEvaluationEntity updateCreditEvaluation(CreditEvaluationEntity evaluation) {
        return creditEvaluationRepository.save(evaluation);
    }

    public boolean deleteCreditEvaluation(String rut) throws Exception {
        try {
            creditEvaluationRepository.deleteByRut(rut);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Método para simular el crédito
    public double simulateCredit(double principal, double annualInterestRate, int termInYears) {
        // Convertir la tasa de interés anual a mensual y a porcentaje
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        // Calcular el número total de pagos (plazo en años * 12)
        int totalPayments = termInYears * 12;

        // Calcular la cuota mensual usando la fórmula
        // Principal es el monto del prestamo
        double monthlyPayment = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalPayments)) /
                (Math.pow(1 + monthlyInterestRate, totalPayments) - 1);

        return monthlyPayment; // Retornar la cuota mensual calculada
    }

    // Calcula el porcetentaje de la relacion cuota/ingreso
    public double feeIncomeRelation(double clientMonthlyIncome, double loanMonthlyPayment) {
        return (clientMonthlyIncome / loanMonthlyPayment) * 100;
    }

    // P4 R1
    // Decide si la relacion cuota ingreso es aceptable o no, comparandolo con 35
    public boolean feeIncomeRelationCondition(double clientMonthlyIncome, double loanMonthlyPayment) {
        return feeIncomeRelation(clientMonthlyIncome, loanMonthlyPayment) < 35;
    }

    // P4 R2
    // Decide si el historial crediticio del cliente es bueno o no
    public boolean clientCreditHistoryIsGood(String rut) {
        ClientEntity client = clientRepository.findByRut(rut);
        String creditHistoryStatus = client.getHistoryStatus();
        Integer pendingDebts = client.getPendingDebts();
        if (creditHistoryStatus.equals("Good") && pendingDebts == 0) {
            return true;
        } else {
            return false;
        }
    }

    // P4 R3
    // Antiguedad laboral del cliente y estabilidad financiera (creo que se tiene que mostrar esto en el frontend)

    // P4 R4
    




}
