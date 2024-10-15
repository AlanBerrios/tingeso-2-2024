package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.services.LoanCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-cost")
@CrossOrigin("*")
public class LoanCostController {

    @Autowired
    private LoanCostService loanCostService;

    // Endpoint para calcular el costo total del préstamo
    @GetMapping("/calculate")
    public ResponseEntity<String> calculateLoanCost(
            @RequestParam double principal,
            @RequestParam double annualInterestRate,
            @RequestParam int termInYears,
            @RequestParam double lifeInsuranceRate,
            @RequestParam double fireInsuranceMont,
            @RequestParam double adminFeeRate) {

        // Calcular el costo mensual (incluyendo seguros)
        double monthlyPaymentWithInsurance = loanCostService.calculateMonthlyPayment(principal, annualInterestRate, termInYears, lifeInsuranceRate, fireInsuranceMont);

        // Calcular el costo total del préstamo (incluyendo la comisión de administración)
        double totalLoanCost = loanCostService.calculateTotalLoanPayment(principal, termInYears, adminFeeRate, monthlyPaymentWithInsurance);

        // Generar una respuesta detallada con los cálculos
        String result = String.format("Cuota Mensual con Seguros: $%.2f\nCosto Total del Préstamo (incluyendo comisión): $%.2f",
                monthlyPaymentWithInsurance, totalLoanCost);

        return ResponseEntity.ok(result);
    }
}
