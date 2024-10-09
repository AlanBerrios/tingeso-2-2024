package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.ClientEntity;
import com.example.backendprestabanco.entities.DebtEntity;
import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import com.example.backendprestabanco.repositories.ClientRepository;
import com.example.backendprestabanco.repositories.DebtRepository;
import com.example.backendprestabanco.repositories.CreditEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreditEvaluationService {
    @Autowired
    CreditEvaluationRepository creditEvaluationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DebtRepository debtRepository;

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

    // P1 Simulacion del credito hipotecario ------------------
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

    // P3. Registro de Credito ------------------
    // Metodo para registrar un credito


    // P4. R1 Relacion cuota/ingreso ------------------
    // Calcula el porcetentaje de la relacion cuota/ingreso
    public double feeIncomeRelation(double clientMonthlyIncome, double loanMonthlyPayment) {
        return (clientMonthlyIncome / loanMonthlyPayment) * 100;
    }

    // Decide si la relacion cuota ingreso es aceptable o no, comparandolo con 35
    public boolean feeIncomeRelationCondition(double clientMonthlyIncome, double loanMonthlyPayment) {
        return feeIncomeRelation(clientMonthlyIncome, loanMonthlyPayment) < 35;
    }

    // P4. R2 Historial crediticio del cliente ------------------
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

    // P4. R3 Antiguedad laboral y estabilidad financiera ------------------
    // Antiguedad laboral del cliente y estabilidad financiera (creo que se tiene que mostrar esto en el frontend)

    // P4. R4 Relacion deuda/ingreso ------------------
    // Calcula la relación de deuda/ingreso y decide si se acepta o no
    public boolean debtIncomeRelation(String rut) {
        // Obtener al cliente por RUT
        ClientEntity client = clientRepository.findByRut(rut);
        if (client == null) {
            throw new RuntimeException("Cliente no encontrado");
        }
        // Obtener los ingresos mensuales del cliente
        double clientMonthlyIncome = client.getIncome();
        // Obtener todas las deudas actuales del cliente
        List<DebtEntity> clientDebts = debtRepository.findByRut(rut);
        double totalDebt = clientDebts.stream().mapToDouble(DebtEntity::getTotalAmount).sum();
        // Calcular la relación deuda/ingreso
        double debtIncomeRatio = totalDebt / clientMonthlyIncome;
        // Si la relación deuda/ingreso es mayor al 50%, se rechaza la solicitud
        return debtIncomeRatio < 0.5;
    }

    // P4. R5 Monto máximo del financiamiento ------------------
    // Monto maximo del financiamiento (creo que se tiene que mostrar esto en el frontend)

    // P4. R6 Edad del solicitante ------------------
    // Metodo que verifica que el cliente no sea mayor a 70 anios
    public boolean clientAgeCondition(String rut) {
        ClientEntity client = clientRepository.findByRut(rut);
        if (client.getAge() > 70) {
            return false;
        } else {
            return true;
        }
    }

    // P4. R7 Capacidad de ahorro ---------------------------------------------------------------------

    //R7.1 Saldo minimo requerido

}
