package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.*;
import com.example.backendprestabanco.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
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
    @Autowired
    private SavingsAccountRepository savingsAccountRepository;
    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

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
        return (loanMonthlyPayment / clientMonthlyIncome) * 100;
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

    // R7.1 Saldo minimo requerido
    // Funcion para verificar si el cliente cumple con el saldo mínimo requerido
    public boolean hasMinimumRequiredBalance(String rut, double loanAmount) {
        // Obtener la cuenta de ahorros del cliente por su RUT
        SavingsAccountEntity savingsAccount = savingsAccountRepository.findByRut(rut);
        if (savingsAccount == null) {
            throw new RuntimeException("Cuenta de ahorros no encontrada para el cliente con RUT: " + rut);
        }

        // Verificar si el saldo es al menos el 10% del monto del préstamo
        double requiredMinimumBalance = loanAmount * 0.10; // 10% del monto del préstamo
        double currentBalance = savingsAccount.getBalance(); // Saldo actual de la cuenta de ahorros

        // Retornar verdadero si el saldo cumple con el mínimo requerido, falso si no
        return currentBalance >= requiredMinimumBalance;
    }

    // R7.2 Historial de Ahorro Consistente
    // Función para verificar el historial de ahorro consistente durante los últimos 12 meses
    public boolean hasConsistentSavingsHistory(String rut) {
        // Obtener la cuenta de ahorros del cliente por su RUT
        SavingsAccountEntity savingsAccount = savingsAccountRepository.findByRut(rut);
        if (savingsAccount == null) {
            throw new RuntimeException("Cuenta de ahorros no encontrada para el cliente con RUT: " + rut);
        }

        double currentBalance = savingsAccount.getBalance(); // Saldo actual de la cuenta de ahorros
        LocalDate twelveMonthsAgo = LocalDate.now().minusMonths(12); // Fecha de hace 12 meses

        // Obtener todas las transacciones de los últimos 12 meses
        List<AccountHistoryEntity> recentTransactions = accountHistoryRepository.findByRutAndTransactionDateAfter(rut, twelveMonthsAgo);

        // Verificar si ha mantenido un saldo positivo durante los últimos 12 meses
        for (AccountHistoryEntity transaction : recentTransactions) {
            if (transaction.getTransactionType().equals("Retiro") && transaction.getBalanceAfterTransaction() <= 0) {
                // Si en algún momento el saldo fue cero o negativo, retornar falso
                return false;
            }

            // Verificar si hubo retiros significativos (> 50% del saldo actual)
            if (transaction.getTransactionType().equals("Retiro") && transaction.getTransactionAmount() > currentBalance * 0.50) {
                return false; // Si se detecta un retiro significativo, retornar falso
            }
        }

        // Si cumple con todas las condiciones, retornar verdadero
        return true;
    }

    // R7.3 Depósitos Periódicos
    // Función para verificar si el cliente realiza depósitos periódicos y cumplen con el monto mínimo
    public boolean hasRegularDeposits(String rut) {
        // Obtener el cliente por su RUT
        ClientEntity client = clientRepository.findByRut(rut);
        if (client == null) {
            throw new RuntimeException("Cliente no encontrado para el RUT: " + rut);
        }

        double monthlyIncome = client.getIncome(); // Ingresos mensuales del cliente
        double requiredDepositAmount = monthlyIncome * 0.05; // 5% de los ingresos mensuales

        LocalDate twelveMonthsAgo = LocalDate.now().minusMonths(12); // Fecha de hace 12 meses

        // Obtener todos los depósitos de los últimos 12 meses
        List<AccountHistoryEntity> deposits = accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter(
                rut, "Depósito", twelveMonthsAgo);

        double totalDeposits = 0.0;
        int monthlyDepositCount = 0; // Contador de depósitos mensuales
        int quarterlyDepositCount = 0; // Contador de depósitos trimestrales

        // Calcular el total de depósitos y verificar su regularidad
        for (AccountHistoryEntity deposit : deposits) {
            totalDeposits += deposit.getTransactionAmount(); // Sumar el monto del depósito

            LocalDate transactionDate = deposit.getTransactionDate();

            // Contar depósitos mensuales (deben estar dentro del último mes respecto a la transacción)
            if (transactionDate.isAfter(twelveMonthsAgo)) {
                monthlyDepositCount++;
            }

            // Contar depósitos trimestrales (dentro de los últimos 3 meses)
            if (transactionDate.isAfter(twelveMonthsAgo.plusMonths(3))) {
                quarterlyDepositCount++;
            }
        }

        // Verificar si el cliente ha realizado al menos 12 depósitos mensuales o 4 trimestrales
        boolean hasRegularDeposits = (monthlyDepositCount >= 12 || quarterlyDepositCount >= 4);

        // Verificar si el total de depósitos cumple con el mínimo requerido (5% de los ingresos mensuales por 12 meses)
        boolean meetsMinimumAmount = totalDeposits >= requiredDepositAmount * 12;

        // Imprimir los valores verificados para hasRegularDeposits y meetsMinimumAmount
        System.out.println("Contador de depósitos mensuales: " + monthlyDepositCount + " debe ser mayor o igual a: 12");
        System.out.println("Contador de depósitos trimestrales: " + quarterlyDepositCount + " debe ser mayor o igual a: 4");
        System.out.println("Total de depósitos: " + totalDeposits + " debe ser mayor o igual a: " + requiredDepositAmount * 12);

        // Verificar si cumple ambas reglas
        System.out.println("Regla 3 (Depósitos periódicos): " + hasRegularDeposits + " | " + meetsMinimumAmount);

        // Retornar verdadero solo si cumple ambas condiciones
        return hasRegularDeposits && meetsMinimumAmount;
    }





    // R7.4 Relación Saldo/Anios de Antiguedad
    // Función para verificar la relación saldo/años de antigüedad
    public boolean hasRequiredBalanceForTenure(String rut, double loanAmount) {
        // Obtener la cuenta de ahorros del cliente por su RUT
        SavingsAccountEntity savingsAccount = savingsAccountRepository.findByRut(rut);
        if (savingsAccount == null) {
            throw new RuntimeException("Cuenta de ahorros no encontrada para el cliente con RUT: " + rut);
        }

        double currentBalance = savingsAccount.getBalance(); // Saldo actual de la cuenta de ahorros
        LocalDate openingDate = savingsAccount.getOpeningDate(); // Fecha de apertura de la cuenta
        LocalDate currentDate = LocalDate.now(); // Fecha actual

        // Calcular la antigüedad de la cuenta en años
        int yearsOfTenure = Period.between(openingDate, currentDate).getYears();

        // Determinar el porcentaje requerido en función de la antigüedad
        double requiredBalancePercentage = (yearsOfTenure < 2) ? 0.20 : 0.10; // 20% si menos de 2 años, 10% si 2 años o más

        // Calcular el saldo mínimo requerido
        double requiredBalance = loanAmount * requiredBalancePercentage;

        // Retornar verdadero si el saldo actual cumple con el mínimo requerido según la antigüedad
        return currentBalance >= requiredBalance;
    }

    // R7.5  Retiros Recientes
    // Función para verificar si ha habido retiros recientes significativos
    public boolean hasSignificantRecentWithdrawals(String rut) {
        // Obtener la cuenta de ahorros del cliente por su RUT
        SavingsAccountEntity savingsAccount = savingsAccountRepository.findByRut(rut);
        if (savingsAccount == null) {
            throw new RuntimeException("Cuenta de ahorros no encontrada para el cliente con RUT: " + rut);
        }

        double currentBalance = savingsAccount.getBalance(); // Saldo actual de la cuenta de ahorros
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6); // Fecha de hace 6 meses

        // Obtener todos los retiros realizados en los últimos 6 meses
        List<AccountHistoryEntity> recentWithdrawals = accountHistoryRepository.findByRutAndTransactionTypeAndTransactionDateAfter(
                rut, "Retiro", sixMonthsAgo);

        // Verificar si alguno de los retiros supera el 30% del saldo actual
        for (AccountHistoryEntity withdrawal : recentWithdrawals) {
            if (withdrawal.getTransactionAmount() > currentBalance * 0.30) {
                return false; // Si se detecta un retiro mayor al 30% del saldo, retornar falso
            }
        }
        // Si no se detectaron retiros significativos, retornar verdadero
        return true;
    }

    // R7.6 Resultado de la evaluación
    // Función para evaluar la capacidad de ahorro del cliente según las 5 reglas
    public String evaluateSavingsCapacity(String rut, double loanAmount) {
        int rulesMet = 0;

        // Regla 1: Saldo mínimo requerido
        boolean rule1 = hasMinimumRequiredBalance(rut, loanAmount);
        System.out.println("Regla 1 (Saldo mínimo requerido): " + rule1);
        if (rule1) {
            rulesMet++;
        }

        // Regla 2: Historial de ahorro consistente
        boolean rule2 = hasConsistentSavingsHistory(rut);
        System.out.println("Regla 2 (Historial de ahorro consistente): " + rule2);
        if (rule2) {
            rulesMet++;
        }

        // Regla 3: Depósitos periódicos
        boolean rule3 = hasRegularDeposits(rut);
        System.out.println("Regla 3 (Depósitos periódicos): " + rule3);
        if (rule3) {
            rulesMet++;
        }

        // Regla 4: Relación saldo/años de antigüedad
        boolean rule4 = hasRequiredBalanceForTenure(rut, loanAmount);
        System.out.println("Regla 4 (Relación saldo/años de antigüedad): " + rule4);
        if (rule4) {
            rulesMet++;
        }

        // Regla 5: Retiros recientes
        boolean rule5 = hasSignificantRecentWithdrawals(rut);
        System.out.println("Regla 5 (Retiros recientes): " + rule5);
        if (rule5) {
            rulesMet++;
        }

        // Evaluar la capacidad de ahorro según la cantidad de reglas cumplidas
        System.out.println("Reglas cumplidas: " + rulesMet + "/5");

        if (rulesMet == 5) {
            return "Solid"; // Cumple con las 5 reglas
        } else if (rulesMet >= 3) {
            return "Moderate"; // Cumple con 3 o 4 reglas
        } else {
            return "Weak"; // Cumple con menos de 3 reglas
        }
    }


}
